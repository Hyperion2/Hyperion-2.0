package org.ls.clan;

/**
 * Chatist Information.
 * @author phil
 *
 */
public class Member {

	/**
	 * Member name.
	 */
	private String name;
	
	/**
	 * Member rank.
	 */
	private short rank;
	
	/**
	 * Member world.
	 */
	private byte world;
	
	/**
	 * Member.
	 * @param name The name.
	 * @param rank The rank.
	 * @param world The worldId.
	 */
	public Member(String name, short rank, byte world) {
		this.name = name;
		this.rank = rank;
		this.world = world;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(short rank) {//kept as they will be edited.
		this.rank = rank;
	}

	/**
	 * @return the rank
	 */
	public short getRank() {
		return rank;
	}

	/**
	 * @return the world
	 */
	public byte getWorld() {
		return world;
	}
}
