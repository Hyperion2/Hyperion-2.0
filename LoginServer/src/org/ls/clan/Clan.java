package org.ls.clan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author phil
 *
 */
public class Clan {
	
	/**
	 * Clan Owner
	 */
	private String channelOwner;
	
	/**
	 * Clan name.
	 */
	private String channelName;
	
	/**
	 * Ranks of clan Members.
	 */
	private Map<String, Short> ranks = new HashMap<String, Short>();
	
	/**
	 * Current Chatists in the channel.
	 */
	private ArrayList<Member> activeChatists = new ArrayList<Member>();
	
	/**
	 * Lootshare/Coinshare enabled.
	 */
	private boolean lootshareEnabled = true;
	
	/**
	 * Ranks.
	 */
	private int lootshareRank = -1, talkRank = -1, joinRank = -1, kickRank = 7;

	/**
	 * Creates a ClanChat.
	 * @param owner The owner of the Channel.
	 */
	public Clan(String owner) {
		this.channelName = this.channelOwner = owner;
	}

	/**
	 * @return the channelOwner
	 */
	public String getChannelOwner() {
		return channelOwner;
	}

	/**
	 * @param channelName the channelName to set
	 */
	public String setChannelName(String channelName) {
		this.channelName = channelName;
		return channelName;
	}

	/**
	 * @return the channelName
	 */
	public String getChannelName() {
		return channelName;
	}
	
	/**
	 * Gets the rank of the user.
	 * @param user The user to be assigned a rank.
	 * @return The user's rank.
	 */
	public short getRank(String user) {
		return ranks.get(user) != null ? ranks.get(user) : -1;
	}

	/**
	 * @return the activeChatists
	 */
	public ArrayList<Member> getActiveChatists() {
		return activeChatists;
	}

	/**
	 * @param lootshareEnabled the lootshareEnabled to set
	 */
	public void setLootshareEnabled(boolean lootshareEnabled) {
		this.lootshareEnabled = lootshareEnabled;
	}

	/**
	 * @return the lootshareEnabled
	 */
	public boolean isLootshareEnabled() {
		return lootshareEnabled;
	}

	/**
	 * @param lootshareRank the lootshareRank to set
	 */
	public void setLootshareRank(int lootshareRank) {
		this.lootshareRank = lootshareRank;
	}

	/**
	 * @return the lootshareRank
	 */
	public int getLootshareRank() {
		return lootshareRank;
	}

	/**
	 * @param talkRank the talkRank to set
	 */
	public void setTalkRank(int talkRank) {
		this.talkRank = talkRank;
	}

	/**
	 * @return the talkRank
	 */
	public int getTalkRank() {
		return talkRank;
	}

	/**
	 * @param joinRank the joinRank to set
	 */
	public void setJoinRank(int joinRank) {
		this.joinRank = joinRank;
	}

	/**
	 * @return the joinRank
	 */
	public int getJoinRank() {
		return joinRank;
	}

	/**
	 * @param kickRank the kickRank to set
	 */
	public void setKickRank(int kickRank) {
		this.kickRank = kickRank;
	}

	/**
	 * @return the kickRank
	 */
	public int getKickRank() {
		return kickRank;
	}
}