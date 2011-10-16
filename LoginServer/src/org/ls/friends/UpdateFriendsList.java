package org.ls.friends;

import java.util.LinkedList;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.ls.login.PlayerData;
import org.ls.net.PacketHandler;
import org.ls.node.LoginPacket;
import org.ls.node.Node;
import org.ls.node.NodeManager;
import org.ls.utils.IoBufferUtils;

public class UpdateFriendsList implements PacketHandler {

	@Override
	public void handle(Node n, LoginPacket packet) {
		// TODO Auto-generated method stub
		final IoBuffer buf = packet.getPayload();
		final String userRegistered = IoBufferUtils.getRS2String(buf);
		PlayerData pD = n.getPlayer(userRegistered);
		byte nodeId = buf.get();
		byte chatSetting = buf.get();
		boolean login = buf.get() == 0;
		
		int friendsToCheck = buf.get();
		
		List<String> namesToCheck = new LinkedList<String>();
		
		for(int i = 0; i < friendsToCheck; i++)
			namesToCheck.add(IoBufferUtils.getRS2String(buf));
		
		if(pD == null) return;//Nothing we can do.
		
		pD.setChatSetting(chatSetting);
		
		for(String s : namesToCheck)
			pD.getFriendsList().add(s);
		
		IoBuffer resp = IoBuffer.allocate(16);
		resp.setAutoExpand(true);
		IoBufferUtils.putRS2String(resp, userRegistered);
		resp.put(nodeId);
		resp.put(chatSetting);
		resp.put((byte) 1);
		resp.put((byte) namesToCheck.size());
		for(String s : namesToCheck) {//Checks if players chat is off, if there on the friendslist etc.
			Node n2 = NodeManager.getNodeManager().getPlayersNode(s);
			PlayerData data = n2 == null ? null : n2.getPlayer(s);
			IoBufferUtils.putRS2String(resp, s);
			resp.put((byte) (n2 == null || data == null || !data.getFriendsList().contains(userRegistered) && data.getChatSetting() == 1 || data.getChatSetting() == 2 ? 0 : n2.getId()));
		}
		if(login) {
			for(Node n2 : NodeManager.getNodeManager().getNodes()) {
				IoBuffer resp2 = IoBuffer.allocate(16);
				resp2.setAutoExpand(true);
				String user = userRegistered;
				IoBufferUtils.putRS2String(resp2, user);
				resp2.put(nodeId);
				resp2.put(chatSetting);
				resp2.put((byte) 0);
				resp2.flip();
				n2.getSession().write(new LoginPacket(LoginPacket.PRIVATE_MESSAGE_REGISTER_RESPONSE, resp2));
			}
		}
		resp.flip();
		n.getSession().write(new LoginPacket(LoginPacket.PRIVATE_MESSAGE_REGISTER_RESPONSE, resp));
	}
}
