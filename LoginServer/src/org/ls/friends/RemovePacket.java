package org.ls.friends;

import org.apache.mina.core.buffer.IoBuffer;
import org.ls.login.PlayerData;
import org.ls.net.PacketHandler;
import org.ls.node.LoginPacket;
import org.ls.node.Node;
import org.ls.node.NodeManager;
import org.ls.utils.IoBufferUtils;
import org.ls.utils.NameUtils;

public class RemovePacket implements PacketHandler {

	@Override
	public void handle(Node n, LoginPacket packet) {
		// TODO Auto-generated method stub
		final IoBuffer buf = packet.getPayload();
		String user = IoBufferUtils.getRS2String(buf);//User
		String remove = IoBufferUtils.getRS2String(buf);//RemoveUser
		boolean friends = buf.get() == 1;
		int chat = n.getPlayer(user).getChatSetting();
		PlayerData d = n.getPlayer(user);
		if(d == null) return;
		if(friends)
			d.getFriendsList().remove(NameUtils.nameToLong(remove));
		else
			d.getIgnoreList().remove(NameUtils.nameToLong(remove));
		if(chat == 2 || chat == 0 || !friends) return;//Doesnt matter as everyone can see him as he has chat on. or off.
		Node sendTo = NodeManager.getNodeManager().getPlayersNode(remove);
		if(sendTo == null) return;//If not online who cares.
		IoBuffer resp = IoBuffer.allocate(16);
		resp.setAutoExpand(true);
		IoBufferUtils.putRS2String(resp, remove);
		IoBufferUtils.putRS2String(resp, user);
		sendTo.getSession().write(new LoginPacket(LoginPacket.REMOVE_FRIEND_OR_IGNORE_RESPONSE, resp));
	}

}
