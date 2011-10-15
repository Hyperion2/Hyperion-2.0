package org.hyperion.rs2.model.container;

import org.hyperion.rs2.model.Item;

/**
 * Contains equipment utility methods.
 * @author Graham Edgecombe
 * @author Lothy
 *
 */
public class Equipment {
	
	/**
	 * The size of the equipment container.
	 */
	public static final int SIZE = 14;
	
	/**
	 * The helmet slot.
	 */
	public static final int SLOT_HELM = 0;
	
	/**
	 * The cape slot.
	 */
	public static final int SLOT_CAPE = 1;
	
	/**
	 * The amulet slot.
	 */
	public static final int SLOT_AMULET = 2;
	
	/**
	 * The weapon slot.
	 */
	public static final int SLOT_WEAPON = 3;
	
	/**
	 * The chest slot.
	 */
	public static final int SLOT_CHEST = 4;
	
	/**
	 * The shield slot.
	 */
	public static final int SLOT_SHIELD = 5;
	
	/**
	 * The bottoms slot.
	 */
	public static final int SLOT_BOTTOMS = 7;
	
	/**
	 * The gloves slot.
	 */
	public static final int SLOT_GLOVES = 9;
	
	/**
	 * The boots slot.
	 */
	public static final int SLOT_BOOTS = 10;
	
	/**
	 * The rings slot.
	 */
	public static final int SLOT_RING = 12;
	
	/**
	 * The arrows slot.
	 */
	public static final int SLOT_ARROWS = 13;

	/**
	 * Equipment interface id.
	 */
	public static final int INTERFACE = 1688;
	
	/**
	 * Equipment type enum.
	 * @author Lothy
	 * @author Miss Silabsoft
	 *
	 */
	public enum EquipmentType {
		CAPE(0, "Cape", Equipment.SLOT_CAPE),
		BOOTS(1, "Boots", Equipment.SLOT_BOOTS),
		GLOVES(2, "Gloves", Equipment.SLOT_GLOVES),
		SHIELD(3, "Shield", Equipment.SLOT_SHIELD),
		HAT(4, "Hat", Equipment.SLOT_HELM),
		AMULET(5, "Amulet", Equipment.SLOT_AMULET),
		ARROWS(6, "Arrows", Equipment.SLOT_ARROWS),
		RING(7, "Ring", Equipment.SLOT_RING),
		BODY(8, "Body", Equipment.SLOT_CHEST),
		LEGS(9, "Legs", Equipment.SLOT_BOTTOMS),
		PLATEBODY(10, "Plate body", Equipment.SLOT_CHEST),
		FULL_HELM(11, "Full helm", Equipment.SLOT_HELM),
		FULL_MASK(12, "Full mask", Equipment.SLOT_HELM),
		WEAPON(13, "Weapon", Equipment.SLOT_WEAPON);
		
		/**
		 * The description.
		 */
		private String description;
		
		/**
		 * Index.
		 */
		private int index;
		
		/**
		 * The slot.
		 */
		private int slot;
		
		/**
		 * Creates the equipment type.
		 * @param description The description.
		 * @param slot The slot.
		 */
		private EquipmentType(int index, String description, int slot) {
			this.description = description;
			this.slot = slot;
			this.index = index;
		}
		
		/**
		 * Gets the description.
		 * @return The description.
		 */
		public String getDescription() {
			return description;
		}
		
		/**
		 * Gets the slot.
		 * @return The slot.
		 */
		public int getSlot() {
			return slot;
		}
		
		/**
		 * Gets the Type.
		 * @param i
		 * @return
		 */
		public static EquipmentType getType(int i) {
			for(EquipmentType t : EquipmentType.values())
				if(t.index == i)
					return t;
			return WEAPON;
		}
	}
	
	/**
	 * Static initializer block to populate the type map.
	 *
	static {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("data/EquipSlots.txt"));
			for(int i = 0; i < ItemDefinition.MAX_ITEMS; i++) {
				String name = ItemDefinition.forId(i).getName().toLowerCase();
				if(name.contains("full helm"))
					out.write(""+EquipmentType.FULL_HELM.index);
				else if(name.contains("hat") || name.contains("coif") || name.contains("hood"))
					out.write(""+EquipmentType.HAT.index);
				else if(name.contains("helm") || name.contains("mask"))
					out.write(""+EquipmentType.FULL_MASK.index);
				else if(name.contains("legs") || name.contains("skirt") || name.contains("bottom")
						|| name.contains("tasset") || name.contains("chap"))
					out.write(""+EquipmentType.LEGS.index);
				else if(name.contains("chainbody"))
					out.write(""+EquipmentType.BODY.index);
				else if(name.contains("body") || name.contains("torso") || name.contains("brassard") || name.contains("chest"))
						out.write(""+EquipmentType.PLATEBODY.index);
				else if(name.contains("bracelet") || name.contains("glove") || name.contains("vamb"))
					out.write(""+EquipmentType.GLOVES.index);
				else if(name.contains("arrow") || name.contains("bolt"))
					out.write(""+EquipmentType.ARROWS.index);
				else if(name.contains("cape") || name.contains("ava's"))
					out.write(""+EquipmentType.CAPE.index);
				else if(name.contains("boot"))
					out.write(""+EquipmentType.BOOTS.index);
				else if(name.contains("ring"))
					out.write(""+EquipmentType.RING.index);
				else if(name.contains("shield") || name.contains("defender"))
					out.write(""+EquipmentType.SHIELD.index);
				else
					out.write(""+EquipmentType.WEAPON.index);
				out.newLine();
			}
			out.flush();
			out.close();
			System.out.println("File Written");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if an item is of a specific type.
	 * @param type The type.
	 * @param item The item.
	 * @return <code>true</code> if the types are the same, <code>false</code>
	 * if not.
	 */
	public static boolean is(EquipmentType type, Item item) {
		return item.getDefinition().getEquipmentType().equals(type);
	}

}
