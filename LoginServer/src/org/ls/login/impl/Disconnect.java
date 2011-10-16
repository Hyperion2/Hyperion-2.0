package org.ls.login.impl;

import org.apache.mina.core.buffer.IoBuffer;
import org.ls.login.PlayerData;
import org.ls.net.PacketHandler;
import org.ls.node.LoginPacket;
import org.ls.node.Node;
import org.ls.node.NodeManager;
import org.ls.utils.IoBufferUtils;
import org.ls.utils.NameUtils;

public class Disconnect implements PacketHandler {

	@Override
	public void handle(Node n, LoginPacket packet) {
		// TODO Auto-generated method stub
		final IoBuffer buf = packet.getPayload();
		String name = NameUtils.formatNameForProtocol(IoBufferUtils.getRS2String(buf));
		PlayerData p = NodeManager.getNodeManager().getPlayer(name);
		if(p != null) {
			NodeManager.getNodeManager().unregister(p);
		}
	}

}
