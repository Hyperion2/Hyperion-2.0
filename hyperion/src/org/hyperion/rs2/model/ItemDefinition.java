package org.hyperion.rs2.model;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.hyperion.Server;
import org.hyperion.rs2.model.container.Equipment.EquipmentType;

public class ItemDefinition {
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(ItemDefinition.class
			.getName());
	/**
	 * Max amount of items.
	 */
	public static final int MAX_ITEMS = 8000;

	/**
	 * Loads the ItemDefintions.
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public static void load() throws IOException {
		logger.info("Loading item definitions...");
		int amt = 0;
		try {
			File f = new File("data/Item.dat");
			DataInputStream is = new DataInputStream(new FileInputStream(f));
			for(int i = 0; i < MAX_ITEMS; i++) {
				String name = is.readUTF();
				if(name.isEmpty()) {
					amt++;
					continue;
				}
				boolean noted = is.readByte() == 1;
				boolean member = is.readByte() == 1;
				boolean stackable = is.readByte() == 1;
				byte poison = is.readByte();
				short notedId = is.readShort();
				double weight = is.readDouble();
				short lentId = 0;
				short equipId = 0;
				short renderId = 0;
				String examine = null;
				if(Server.VERSION >= 508) {
					lentId = is.readShort();
					equipId = is.readShort();
					renderId = is.readShort();
					examine = is.readUTF();
				}
				EquipmentType slot = EquipmentType.getType(is.readByte());
				byte speed = is.readByte();
				int[][] req = new int[3][2];
				int[] alch = {0, 0};
				alch[0] = is.readInt();
				alch[1] = is.readInt();
				boolean tradable = is.readByte() == 1;
				byte[] bonus = new byte[18];
				for(int i2 = 0; i2 < 18; i2++) {
					bonus[i2] = is.readByte();
				}
				for(int i2 = 0; i2 < 3; i2++) {
					req[i2][0] = is.readByte();
					req[i2][1] = is.readByte();
				}
				Definitions.addDefinition(amt++, new ItemDefinition(name, noted, notedId, req, poison, stackable,
						//renderId, equipId, 
						slot, bonus, speed, alch, tradable, weight));
			}
			//buf.reset();
			//buf.clear();
			is.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		//Equipment.is(null, new Item(4151));
		logger.info("Loaded and defined " + amt + " item definitions.");
		
	}
	
	/**
	 * Name.
	 */
	private final String name;
		//examine;
	/**
	 * LentItem id.
	 */
	private final short 
		//lentId, 
		noteCounterPart;
	/**
	 * Skill Requirements.
	 */
	private int[][] skillReq;
	/**
	 * Noted flag.
	 */
	private final boolean isNoteable,
		stackable, 
		//isMember,
		tradable;
	/**
	 * The equip id.
	 *
	private final short equipId, itemAnimationIndex;
	
	/**
	 * Item Bonuses.
	 */
	private final byte[] bonus;
	
	private final int[] alchValues;
	
	private byte poisonDmg,
		speed;
	
	/**
	 * EquipmentSlot.
	 */
	private final EquipmentType type;
	
	/**
	 * Weight of items.
	 */
	private double weight;

	/**
	 * Creates the item definition.
	 * 
	 * @param id
	 *            The id.
	 * @param name
	 *            The name.
	 * @param examine
	 *            The description.
	 * @param noted
	 *            The noted flag.
	 * @param noteable
	 *            The noteable flag.
	 * @param stackable
	 *            The stackable flag.
	 * @param parentId
	 *            The non-noted id.
	 * @param notedId
	 *            The noted id.
	 * @param members
	 *            The members flag.
	 * @param shopValue
	 *            The shop price.
	 * @param highAlcValue
	 *            The high alc value.
	 * @param lowAlcValue
	 *            The low alc value.
	 */
	private ItemDefinition(String name, boolean noted, short notedId, int[][] skillReq, byte pois,
			boolean stackable,
			EquipmentType type, byte[] bonus, byte speed,
			int[] alch, boolean trade, double weight) {
		this.name = name;;
		this.isNoteable = noted;
		this.noteCounterPart = notedId;
		//this.isMember = isMember;
		//this.itemAnimationIndex = renderAnim;
		//this.lentId = lentId;
		this.skillReq = skillReq;
		this.stackable = stackable;
		this.speed = speed;
		//this.equipId = equipId;
		this.type = type;
		this.bonus = bonus;
		this.alchValues = alch;
		this.poisonDmg = pois;
		this.tradable = trade;
		this.weight = weight;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the lentId
	 *
	public int getLentId() {
		return lentId;
	}

	/**
	 * @return the noteCounterPart
	 */
	public int getNoteCounterPart() {
		return noteCounterPart;
	}

	/**
	 * @return the isMember
	 *
	public boolean isMember() {
		return isMember;
	}

	/**
	 * @return the skillReq
	 */
	public int[][] getSkillReq() {
		return skillReq;
	}

	/**
	 * @return the isNoteable
	 */
	public boolean isNoteable() {
		return isNoteable;
	}
	
	public boolean isNoted() {
		return !isNoteable && noteCounterPart != -1 && noteCounterPart != 65535;
	}

	/**
	 * @return the stackable
	 */
	public boolean isStackable() {
		return stackable || isNoted();
	}

	/**
	 * @return the equipId
	 *
	public int getEquipId() {
		return equipId;
	}
	
	/**
	 * 
	 * @return
	 */
	public EquipmentType getEquipmentType() {
		return type;
	}

	/**
	 * @return the itemAnimationIndex
	 *
	public int getItemAnimationIndex() {
		return itemAnimationIndex;
	}

	/**
	 * @return the bonus
	 */
	public byte[] getBonus() {
		return bonus;
	}

	/**
	 * @return the alchValues
	 */
	public int getHighAlch() {
		return alchValues[0];
	}
	
	public int getLowAlch() {
		return alchValues[1];
	}

	/**
	 * @return the tradable
	 */
	public boolean isTradable() {
		return tradable;
	}

	/**
	 * @return the examine
	 *
	public String getExamine() {
		return examine;
	}

	/**
	 * @return the poisonDmg
	 */
	public byte getPoisonDmg() {
		return poisonDmg;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(byte speed) {
		this.speed = speed;
	}

	/**
	 * @return the speed
	 */
	public byte getSpeed() {
		return speed;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}
}