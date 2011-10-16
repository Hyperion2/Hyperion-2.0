package org.ls.net;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.ls.friends.ChangeSetting;
import org.ls.friends.Message;
import org.ls.friends.RemovePacket;
import org.ls.friends.UpdateFriendsList;
import org.ls.login.impl.ConfirmUser;
import org.ls.login.impl.Disconnect;
import org.ls.login.impl.SaveUserData;
import org.ls.login.impl.SendUserData;
import org.ls.node.LoginPacket;
import org.ls.node.Node;


/**
 * Managers <code>PacketHandler</code>s.
 * @author Graham Edgecombe
 *
 */
public class PacketManager {
	
	/**
	 * The logger class.
	 */
	private static final Logger logger = Logger.getLogger(PacketManager.class.getName());
	
	/**
	 * The instance.
	 */
	private static final PacketManager INSTANCE = new PacketManager();
	
	/**
	 * Gets the packet manager instance.
	 * @return The packet manager instance.
	 */
	public static PacketManager getPacketManager() {
		return INSTANCE;
	}
	
	/**
	 * The packet handler array.
	 */
	private PacketHandler[] packetHandlers = new PacketHandler[10];
	
	/**
	 * Creates the packet manager.
	 */
	public PacketManager() {
		bind(LoginPacket.CHECK_LOGIN, new ConfirmUser());
		bind(LoginPacket.LOAD, new SendUserData());
		bind(LoginPacket.SAVE, new SaveUserData());
		bind(LoginPacket.DISCONNECT, new Disconnect());
		bind(LoginPacket.PRIVATE_MESSAGE_REGISTER, new UpdateFriendsList());
		bind(LoginPacket.PRIVATE_MESSAGE_COMMUNICATION, new Message());
		bind(LoginPacket.CHANGE_CHAT_SETTINGS, new ChangeSetting());
		bind(LoginPacket.REMOVE_FRIEND_OR_IGNORE, new RemovePacket());
	}
	
	/**
	 * Binds an opcode to a handler.
	 * @param id The opcode.
	 * @param handler The handler.
	 */
	public void bind(int id, PacketHandler handler) {
		packetHandlers[id] = handler;
	}

	/**
	 * Handles a packet.
	 * @param session The session.
	 * @param packet The packet.
	 */
	public void handle(Node session, LoginPacket packet) {
		try {
			packetHandlers[packet.getOpcode()].handle(session, packet);
		} catch(Exception ex) {
			logger.log(Level.SEVERE, "Exception handling packet.", ex);
		}
	}

}
