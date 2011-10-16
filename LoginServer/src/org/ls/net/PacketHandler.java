package org.ls.net;

import org.ls.node.LoginPacket;
import org.ls.node.Node;


/**
 * An interface which describes a class that handles packets.
 * @author Graham Edgecombe
 *
 */
public interface PacketHandler {
	
	/**
	 * Handles a single packet.
	 * @param player The player.
	 * @param packet The packet.
	 */
	public void handle(Node n, LoginPacket packet);

}
