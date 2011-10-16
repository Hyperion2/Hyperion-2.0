package org.ls.friends;

import org.apache.mina.core.buffer.IoBuffer;
import org.ls.net.PacketHandler;
import org.ls.node.LoginPacket;
import org.ls.node.Node;
import org.ls.node.NodeManager;
import org.ls.utils.NameUtils;

public class Message implements PacketHandler{

	@Override
	public void handle(Node n, LoginPacket packet) {
		// TODO Auto-generated method stub
		final IoBuffer buf = packet.getPayload();
		long to = buf.getLong();
		long from = buf.getLong();
		byte rights = buf.get();
		int size = buf.getInt();
		byte[] message = new byte[size];
		buf.get(message);
		
		IoBuffer res = IoBuffer.allocate(16);
		res.setAutoExpand(true);
		res.putLong(to);
		res.putLong(from);
		res.put(rights);
		res.putInt(size);
		res.put(message);
		res.flip();
		Node node = NodeManager.getNodeManager().getPlayersNode(NameUtils.formatNameForProtocol(NameUtils.longToName(to)));
		if(node == null) {
			System.out.println("User doesnt exist");
			return;
		}
		node.getSession().write(new LoginPacket(LoginPacket.PRIVATE_MESSAGE_COMMUNICATION_RESPONSE, res));
	}

}
