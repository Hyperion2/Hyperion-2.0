package org.ls.node;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.ls.Constants;
import org.ls.friends.Deregister;
import org.ls.login.PlayerData;
import org.ls.utils.NameUtils;

/**
 * Manages all of the nodes in the login server.
 * @author Graham Edgecombe
 *
 */
public class NodeManager {
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(NodeManager.class.getName());
	
	/**
	 * The node manager instance.
	 */
	private static final NodeManager INSTANCE = new NodeManager();

	/**
	 * A map of nodes.
	 */
	private Map<Integer, Node> nodes = new HashMap<Integer, Node>();
	
	/**
	 * A map of player names to nodes.
	 */
	private Map<String, Node> players = new HashMap<String, Node>();
	
	/**
	 * Gets the node manager instance.
	 * @return The node manager instance.
	 */
	public static NodeManager getNodeManager() {
		return INSTANCE;
	}
	
	/**
	 * Gets a player.
	 * @param name The player name.
	 * @return The player object.
	 */
	public PlayerData getPlayer(String name) {
		name = NameUtils.formatNameForProtocol(name);
		Node n = getPlayersNode(name);
		if(n == null) {
			return null;
		}
		return n.getPlayer(name);
	}
	
	/**
	 * Registers a node.
	 * @param node The node to add.
	 */
	public void register(Node node) {
		logger.info("Registering node : World-" + node.getId() + ".");
		nodes.put(node.getId(), node);
		if(!Constants.SHARE_NODE_FILES) {
			File f = new File("data/savedGames/node_"+node.getId());
			if(!f.isDirectory()) {
				f.mkdir();
			}
		}
		//System.gc();
	}
	
	/**
	 * Unregisters a node.
	 * @param node The node to remove.
	 */
	public void unregister(Node node) {
		logger.info("Unregistering node : World-" + node.getId() + ".");
		for(PlayerData d : node.getPlayers())
			Deregister.deregister(d.getName());
		nodes.remove(node.getId());
		for(PlayerData p : node.getPlayers()) {
			players.remove(p.getName());
		}
	//	System.gc();
	}
	
	/**
	 * Gets a node by its id.
	 * @param id The id.
	 * @return The node.
	 */
	public Node getNode(int id) {
		return nodes.get(id);
	}
	
	/**
	 * Registers a player.
	 * @param player The player.
	 * @param node The node.
	 */
	public void register(PlayerData player, Node node) {
		logger.info("Registering player : " + player.getName() + "...");
		players.put(player.getName(), node);
		node.register(player);
	}
	
	/**
	 * Unregisters a player.
	 * @param player The player.
	 */
	public void unregister(PlayerData player) {
		logger.info("Unregistering player : " + player.getName() + "...");
		Deregister.deregister(player.getName());
		if(players.containsKey(player.getName())) {
			players.remove(player.getName()).unregister(player);
		}
	}
	
	/**
	 * Gets the node a player is on.
	 * @param player The player.
	 * @return The node.
	 */
	public Node getPlayersNode(String player) {
		return players.get(player);
	}
	
	/**
	 * Gets the collection of all the connected nodes.
	 * @return The collection of connected nodes.
	 */
	public Collection<Node> getNodes() {
		return nodes.values();
	}

	/**
	 * Checks if a login is valid.
	 * @param node The node id.
	 * @param password The password.
	 * @return Valid flag.
	 */
	public boolean isNodeAuthenticationValid(int node, String password) {
		// TODO password check
		return !nodes.containsKey(node) && password.equals(Constants.PASSWORD);
	}

}
