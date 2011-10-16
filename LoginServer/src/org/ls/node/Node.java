package org.ls.node;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.ls.LoginServer;
import org.ls.login.PlayerData;

/**
 * Manages a single node (world).
 * @author Graham Edgecombe
 *
 */
public class Node {
		/**
	 * The login server.
	 */
	private LoginServer server;
	
	/**
	 * The session.
	 */
	private IoSession session;
	
	/**
	 * The id.
	 */
	private int id;
	
	/**
	 * A map of players.
	 */
	private Map<String, PlayerData> players = new HashMap<String, PlayerData>();
	
	/**
	 * Creates a node.
	 * @param server The server.
	 * @param session The session.
	 * @param id The id.
	 */
	public Node(LoginServer server, IoSession session, int id) {
		this.server = server;
		this.session = session;
		this.id = id;
	}
	
	/**
	 * Registers a new player.
	 * @param player The player to add.
	 */
	public void register(PlayerData player) {
		players.put(player.getName(), player);
	}
	
	/**
	 * Removes an old player.
	 * @param player The player to remove.
	 */
	public void unregister(PlayerData player) {
		players.remove(player.getName());
	}
	
	/**
	 * Gets a player by their name.
	 * @param name The player name.
	 * @return The player.
	 */
	public PlayerData getPlayer(String name) {
		return players.get(name);
	}
	
	/**
	 * Gets the players in this node.
	 * @return The players in this node.
	 */
	public Collection<PlayerData> getPlayers() {
		return players.values();
	}
	
	/**
	 * Gets the main Server.
	 * @return The loginServer.
	 */
	public LoginServer getLoginServer() {
		return server;
	}

	/**
	 * Gets the session.
	 * @return The session.
	 */
	public IoSession getSession() {
		return session;
	}
	
	/**
	 * Gets the id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

}
