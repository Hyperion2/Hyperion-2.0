package org.hyperion.rs2.model;

/**
 * Represents a single item.
 * @author Graham Edgecombe
 *
 */
public class Item {
	
	/**
	 * The id.
	 */
	private short id;
	
	/**
	 * The number of items.
	 */
	private int count;
	
	/**
	 * The degrading hits left.
	 */
	private short degrade;
	
	/**
	 * Creates a single item.
	 * @param id The id.
	 */
	public Item(int id) {
		this(id, 1);
	}
	
	/**
	 * Creates a stacked item.
	 * @param id The id.
	 * @param count The number of items.
	 * @throws IllegalArgumentException if count is negative.
	 */
	public Item(int id, int count) {
		if(count < 0) {
			throw new IllegalArgumentException("Count cannot be negative.");
		}
		this.id = (short) id;
		this.count = count;
		this.degrade = 0;//default is 0
	}
	
	/**
	 * Creates a stacked item.
	 * @param id The id.
	 * @param count The number of items.
	 * @throws IllegalArgumentException if count is negative.
	 */
	public Item(int id, int count, int degrade) {
		if(count < 0) {
			throw new IllegalArgumentException("Count cannot be negative.");
		}
		this.id = (short) id;
		this.count = count;
		this.degrade = (short) degrade;//default is 0
	}
	
	/**
	 * Gets the definition of this item.
	 * @return The definition.
	 */
	public ItemDefinition getDefinition() {
		return Definitions.forId(id);
	}
	
	/**
	 * Gets the item id.
	 * @return The item id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the count.
	 * @return The count.
	 */
	public int getCount() {
		return count;
	}
	
	@Override
	public String toString() {
		return Item.class.getName() + " [id=" + id + ", count=" + count + "]";
	}

	/**
	 * @param i the degrade to set
	 */
	public void setDegrade(int i) {
		this.degrade = (short) i;
	}
	
	/**
	 * Changes the items id.
	 * @param i
	 */
	public void transform(short i) {
		this.id = i;
	}
	
	/**
	 * 
	 * @param i
	 */
	public void setCount(int i) {
		this.count = i;
	}

	/**
	 * @return the degrade
	 */
	public int getDegrade() {
		return degrade;
	}

}
