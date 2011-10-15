package org.hyperion.rs2.model;


public class FloorItem {

	/**
	 * ItemId.
	 */
	private int item;
	
	/**
	 * Amount of item.
	 */
	private int amount;
	
	/**
	 * Location spawned.
	 */
	private Location loc;
	
	/**
	 * Owner of drop.
	 */
	private Entity owner;
	
	/**
	 * One it was droppedFor.
	 */
	private Entity to;
	
	/**
	 * Global object.
	 */
	private boolean global;
	
	/**
	 * Timer.
	 */
	private int timer;
	
	/**
	 * Allows the user to take it before the update.
	 */
	private boolean beenTaken;
	
	/**
	 * 
	 * @param item
	 * @param loc
	 * @param owner
	 * @param global
	 */
	public FloorItem(int item, int amount, Location loc, Entity owner, Entity givenTo, boolean global) {
		this.item = item;
		this.setAmount(amount);
		this.loc = loc;
		this.owner = owner;
		this.global = false;//Global in 1 second after dropping.
		this.to = givenTo;
		this.timer = global ? 1 : 90;//1.5minutes per session.
	}

	/**
	 * @return the item
	 */
	public int getItem() {
		return item;
	}

	/**
	 * @return the loc
	 */
	public Location getLoc() {
		return loc;
	}

	/**
	 * @return the owner
	 */
	public Entity getOwner() {
		return owner;
	}

	/**
	 * @param global the global to set
	 */
	public void setGlobal(boolean global) {
		this.global = global;
	}

	/**
	 * @return the global
	 */
	public boolean isGlobal() {
		return global;
	}

	/**
	 * @param timer the timer to set
	 */
	public void setTimer(int timer) {
		this.timer = timer;
	}

	/**
	 * @return the timer
	 */
	public int getTimer() {
		return timer;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param beenTaken the beenTaken to set
	 */
	public void setBeenTaken(boolean beenTaken) {
		this.beenTaken = beenTaken;
	}

	/**
	 * @return the beenTaken
	 */
	public boolean isTaken() {
		return beenTaken;
	}

	/**
	 * @return the to
	 */
	public Player getDroppedFor() {
		return ((Player)to);
	}
}
