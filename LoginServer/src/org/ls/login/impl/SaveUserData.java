package org.ls.login.impl;

import java.io.BufferedWriter;

import org.apache.mina.core.buffer.IoBuffer;
import org.ls.Constants;
import org.ls.login.Account;
import org.ls.net.PacketHandler;
import org.ls.node.LoginPacket;
import org.ls.node.Node;
import org.ls.utils.IoBufferUtils;
import org.ls.utils.NameUtils;

public class SaveUserData implements PacketHandler {

	@Override
	public void handle(Node n, LoginPacket packet) {
		// TODO Auto-generated method stub
		final IoBuffer buf = packet.getPayload();
		String name = NameUtils.formatNameForProtocol(IoBufferUtils.getRS2String(buf));
		int code = 1;
		
		/** Seperates Here different Loading Types. */
		if(Constants.CHAR_FILE_EXTENTION.equals(".dat.gz")) {
			int dataLength = buf.getUnsignedShort();
			byte[] data = new byte[dataLength];
			buf.get(data);
			IoBuffer dataBuffer = IoBuffer.allocate(dataLength);
			dataBuffer.put(data);
			dataBuffer.flip();
			code = Account.savePlayer(name, dataBuffer, n.getId()) ? 1 : 0;
		} else if(Constants.CHAR_FILE_EXTENTION.equals(".txt")) {
			try {
				BufferedWriter write = (BufferedWriter) buf.getObject();
				Account.writeCharacterFile(name, n.getId(), write);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				code = 0;
			}
		}
		
		/** This is response to NODE */
		IoBuffer resp = IoBuffer.allocate(16);
		resp.setAutoExpand(true);
		IoBufferUtils.putRS2String(resp, name);
		resp.put((byte) code);
		resp.flip();
		n.getSession().write(new LoginPacket(LoginPacket.SAVE_RESPONSE, resp));
	}
}
