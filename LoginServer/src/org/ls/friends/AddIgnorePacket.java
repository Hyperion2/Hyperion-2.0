package org.ls.friends;

import org.ls.net.PacketHandler;
import org.ls.node.LoginPacket;
import org.ls.node.Node;

public class AddIgnorePacket implements PacketHandler {

	@Override
	public void handle(Node n, LoginPacket packet) {
		// TODO Auto-generated method stub
		//final IoBuffer buf = packet.getPayload();
	}
}
