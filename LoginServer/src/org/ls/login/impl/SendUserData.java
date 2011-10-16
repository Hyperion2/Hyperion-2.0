package org.ls.login.impl;

import java.io.BufferedReader;

import org.apache.mina.core.buffer.IoBuffer;
import org.ls.Constants;
import org.ls.login.Account;
import org.ls.net.PacketHandler;
import org.ls.node.LoginPacket;
import org.ls.node.Node;
import org.ls.utils.IoBufferUtils;
import org.ls.utils.NameUtils;

public class SendUserData implements PacketHandler {

	@Override
	public void handle(Node n, LoginPacket packet) {
		// TODO Auto-generated method stub
		final IoBuffer buf = packet.getPayload();
		String name = NameUtils.formatNameForProtocol(IoBufferUtils.getRS2String(buf));
		int code = 1;
		IoBuffer resp = IoBuffer.allocate(1024);
		resp.setAutoExpand(true);
		IoBufferUtils.putRS2String(resp, name);
		resp.put((byte) code);
		if(code == 1 && Constants.CHAR_FILE_EXTENTION.equals(".dat.gz")) {
			IoBuffer data = Account.loadPlayerData(name, n.getId());
			resp.putShort((short) data.remaining());
			resp.put(data);
		} else if(code == 1 && Constants.CHAR_FILE_EXTENTION.equals(".txt")) {
			BufferedReader read = Account.readPlayerfile(name, n.getId());
			resp.putObject(read);
		}
		resp.flip();
		n.getSession().write(new LoginPacket(LoginPacket.LOAD_RESPONSE, resp));
	}
}
