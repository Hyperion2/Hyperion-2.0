package org.ls.login;

import java.util.LinkedList;
import java.util.List;

import org.ls.utils.NameUtils;

/**
 * Represents a single player in the login server.
 * @author Graham Edgecombe
 *
 */
public class PlayerData {
	
	/**
	 * The player name.
	 */
	private String name;
	
	/**
	 * The player rights.
	 */
	private int rights;
	
	/**
	 * Friends list of the player.
	 */
	List<String> friends = new LinkedList<String>();
	
	/**
	 * Ignore list of the player.
	 */
	List<String> ignores = new LinkedList<String>();
	
	/**
	 * The chat Setting.
	 */
	private int chatSetting;
	
	/**
	 * Creates the player.
	 * @param name The name.
	 * @param rights The rights.
	 */
	public PlayerData(String name, int rights) {
		this.name = NameUtils.formatNameForProtocol(name);
		this.rights = rights;
	}
	
	/**
	 * Gets the player name.
	 * @return The player name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the player rights.
	 * @return The player rights.
	 */
	public int getRights() {
		return rights;
	}
	
	/**
	 * Gets the friends of the player.
	 * @return The friends list.
	 */
	public List<String> getFriendsList() {
		return friends;
	}
	
	/**
	 * Gets the friends of the player.
	 * @return The friends list.
	 */
	public List<String> getIgnoreList() {
		return ignores;
	}

	/**
	 * @param chatSetting the chatSetting to set
	 */
	public void setChatSetting(int chatSetting) {
		this.chatSetting = chatSetting;
	}

	/**
	 * @return the chatSetting
	 */
	public int getChatSetting() {
		return chatSetting;
	}

}
