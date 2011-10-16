package org.ls.friends;

import org.apache.mina.core.buffer.IoBuffer;
import org.ls.net.PacketHandler;
import org.ls.node.LoginPacket;
import org.ls.node.Node;
import org.ls.node.NodeManager;
import org.ls.utils.IoBufferUtils;

public class ChangeSetting implements PacketHandler {

	@Override
	public void handle(Node n, LoginPacket packet) {
		// TODO Auto-generated method stub
		final IoBuffer buf = packet.getPayload();
		String user = IoBufferUtils.getRS2String(buf);
		int node = buf.get();//Needed if there going from offline to online.
		int newSetting = buf.get();
		n.getPlayer(user).setChatSetting(newSetting);
		//Now we need to check nodes and send appropriate packets.
		
		
		for(Node n2 : NodeManager.getNodeManager().getNodes()) {
			buf.clear();
			buf.setAutoExpand(true);
			IoBufferUtils.putRS2String(buf, user);
			buf.put((byte) node);
			buf.put((byte) newSetting);
			for(String s : n.getPlayer(user).getFriendsList())
				if(n2.getPlayer(s) != null)
					IoBufferUtils.putRS2String(buf, s);
			buf.flip();
			n2.getSession().write(new LoginPacket(LoginPacket.CHANGE_CHAT_SETTINGS_RESPONSE, buf));
		}
		
	}

}
