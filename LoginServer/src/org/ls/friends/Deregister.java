package org.ls.friends;

import org.apache.mina.core.buffer.IoBuffer;
import org.ls.node.LoginPacket;
import org.ls.node.Node;
import org.ls.node.NodeManager;
import org.ls.utils.IoBufferUtils;

public class Deregister {
	
	/**
	 * Deregisters the user.
	 * @param user The deregisterer.
	 */
	public static void deregister(String user) {
		for(Node n2 : NodeManager.getNodeManager().getNodes()) {
			IoBuffer resp3 = IoBuffer.allocate(16);
			resp3.setAutoExpand(true);
			IoBufferUtils.putRS2String(resp3, user);
			resp3.flip();
			n2.getSession().write(new LoginPacket(LoginPacket.DISCONNECT_RESPONSE, resp3));
		}
	}

}
