package org.ls.login.impl;

import org.apache.mina.core.buffer.IoBuffer;
import org.ls.login.Account;
import org.ls.login.PlayerData;
import org.ls.net.PacketHandler;
import org.ls.node.LoginPacket;
import org.ls.node.Node;
import org.ls.node.NodeManager;
import org.ls.utils.IoBufferUtils;
import org.ls.utils.NameUtils;

public class ConfirmUser implements PacketHandler {

	@Override
	public void handle(Node n, LoginPacket packet) {
		// TODO Auto-generated method stub
		final IoBuffer buf = packet.getPayload();
		String name = NameUtils.formatNameForProtocol(IoBufferUtils.getRS2String(buf));
		String password = IoBufferUtils.getRS2String(buf);
		int[] data = Account.checkFile(name, password, n.getId());//This part will change.
		boolean playerIsAlreadyOnline = NodeManager.getNodeManager().getPlayer(name) != null;
		if(data[0] == 2 && !playerIsAlreadyOnline
				|| data[0] == 0 && !playerIsAlreadyOnline) {
			PlayerData pd = new PlayerData(name, data[1]);
			NodeManager.getNodeManager().register(pd, n);
		}
		IoBuffer resp = IoBuffer.allocate(16);
		resp.setAutoExpand(true);
		IoBufferUtils.putRS2String(resp, name);
		resp.put((byte) (playerIsAlreadyOnline ? 5 : data[0]));
		resp.flip();
		n.getSession().write(new LoginPacket(LoginPacket.CHECK_LOGIN_RESPONSE, resp));
	}
}
