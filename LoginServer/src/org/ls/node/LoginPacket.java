package org.ls.node;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * Represents a packet between the login server and world server.
 * @author Graham Edgecombe
 *
 */
public class LoginPacket {
	
	/**
	 * Authentication request opcode.
	 */
	public static final int AUTH = 0;
	
	/**
	 * Authentication response opcode.
	 */
	public static final int AUTH_RESPONSE = 0;
	
	/**
	 * Check login request opcode.
	 */
	public static final int CHECK_LOGIN = 1;
	
	/**
	 * Check login response opcode.
	 */
	public static final int CHECK_LOGIN_RESPONSE = 1;
	
	/**
	 * Load request opcode.
	 */
	public static final int LOAD = 2;
	
	/**
	 * Load response opcode.
	 */
	public static final int LOAD_RESPONSE = 2;
	
	/**
	 * Save request opcode.
	 */
	public static final int SAVE = 3;
	
	/**
	 * Save response opcode.
	 */
	public static final int SAVE_RESPONSE = 3;

	/**
	 * Player disconnected opcode.
	 */
	public static final int DISCONNECT = 4;
	
	/**
	 * Disconnects response.
	 */
	public static final int DISCONNECT_RESPONSE = 4;
	
	/**
	 * Friends request opcode.
	 */
	public static final int PRIVATE_MESSAGE_REGISTER = 5;
	
	/**
	 * Private message register child response opcode.
	 */
	public static final int PRIVATE_MESSAGE_REGISTER_RESPONSE = 5;
	
	/**
	 * Message being sent.
	 */
	public static final int PRIVATE_MESSAGE_COMMUNICATION = 6;
	
	/**
	 * Message sent and recieved.
	 */
	public static final int PRIVATE_MESSAGE_COMMUNICATION_RESPONSE = 6;
	
	/**
	 * Sends the remove Friend packet.
	 */
	public static final int REMOVE_FRIEND_OR_IGNORE = 7;
	
	/**
	 * Remove Friend or Ignore response packet.
	 */
	public static final int REMOVE_FRIEND_OR_IGNORE_RESPONSE = 7;
	
	/**
	 * Change the chat settings opcode.
	 */
	public static final int CHANGE_CHAT_SETTINGS = 8;
	
	/**
	 * Change the chat setting response opcode.
	 */
	public static final int CHANGE_CHAT_SETTINGS_RESPONSE = 8;
	
	/**
	 * The opcode.
	 */
	private int opcode;
	
	/**
	 * The length.
	 */
	private int length;
	
	/**
	 * The payload.
	 */
	private IoBuffer payload;
	
	/**
	 * Creates the login packet.
	 * @param opcode The opcode.
	 * @param payload The payload.
	 */
	public LoginPacket(int opcode, IoBuffer payload) {
		this.opcode = opcode;
		this.length = payload.remaining();
		this.payload = payload;
	}
	
	/**
	 * Gets the opcode.
	 * @return The opcode.
	 */
	public int getOpcode() {
		return opcode;
	}
	
	/**
	 * Gets the length.
	 * @return The length.
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * Gets the payload.
	 * @return The payload.
	 */
	public IoBuffer getPayload() {
		return payload;
	}

}