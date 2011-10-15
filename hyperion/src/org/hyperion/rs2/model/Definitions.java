package org.hyperion.rs2.model;


/**
 * Holds all Definition info to make it easier.
 * @author phil
 *
 */
public class Definitions {

	/**
	 * Holds information on NPCs.
	 */
	private static final NPCDefinition[] npcs = new NPCDefinition[NPCDefinition.MAX_DEFINITIONS+1];
	
	/**
	 * Gets a Definition from the Map.
	 * @param i The NPC id.
	 * @return The NPC definition.
	 */
	public static NPCDefinition forID(int i) {
		return npcs[i];
	}
	
	/**
	 * Adds a Definition to the index.
	 * @param i The index.
	 * @param def The value.
	 */
	public static void addDefinition(int i, NPCDefinition def) {
		npcs[i] = def;;
	}
	
	/**
	 * The definition array.
	 */
	private static final ItemDefinition[] items = new ItemDefinition[ItemDefinition.MAX_ITEMS+1];
	
	/**
	 * Gets a definition for the specified id.
	 * 
	 * @param id
	 *            The id.
	 * @return The definition.
	 */
	public static ItemDefinition forId(int id) {
		return items[id];
	}
	
	/**
	 * Adds a definition to the index.
	 * @param id The index.
	 * @param def The Item Definition.
	 */
	public static void addDefinition(int id, ItemDefinition def) {
		items[id] = def;
	}

}
