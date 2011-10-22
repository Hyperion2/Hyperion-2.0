package org.hyperion.rs2.content;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hyperion.util.PersistenceManager;

/**
 * Start of a NPC Attack system. Easier to use, and and manage.
 * @author phil
 * - Load VIA XML until private development starts.
 */
public class NPCStyle {
	
	/**
	 * Holds the NPCAttack Style.
	 */
	public static final Map<Short, NPCStyle> attacks = new HashMap<Short, NPCStyle>();
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(NPCStyle.class.getName());

	private short npcId;
	
	private Style[] atts;
	
	public static class Style {
		
		private byte styleType;//0 Melee, 1 RANGE, 2 MAGE, 3 DragonFire, 4+ Custom
		
		private short max;//short as if on x10, goes ovver Byte size
		
		private short anim;
		
		private short[] gfx;
		
		private byte speed;// 0 - no delay, 1 - 600ms etc
		
		private double blockPercentile = 1.00;
		
		private boolean multi = false;

		/**
		 * @return the style
		 */
		public byte getStyle() {
			return styleType;
		}

		/**
		 * @return the max
		 */
		public short getMax() {
			return max;
		}

		/**
		 * @return the anim
		 */
		public short getAnim() {
			return anim;
		}

		/**
		 * @return the gfx
		 */
		public short[] getGfx() {
			return gfx;
		}

		/**
		 * @return the speed
		 */
		public byte getSpeed() {
			return speed;
		}

		/**
		 * @return the multi
		 */
		public boolean isMulti() {
			return multi;
		}

		/**
		 * @return the blockPercentile
		 */
		public double getBlockPercentile() {
			return blockPercentile;
		}
	}
	
	private short defAnim;

	/**
	 * @return the npcId
	 */
	public short getNpcId() {
		return npcId;
	}

	/**
	 * @return the atts
	 */
	public Style[] getAtts() {
		return atts;
	}

	/**
	 * @return the defAnim
	 */
	public short getDefAnim() {
		return defAnim;
	}
	
	@SuppressWarnings("unchecked")
	public static void init() {
		try {
			List<NPCStyle> loadedData = (List<NPCStyle>) PersistenceManager.load(new FileInputStream("./data/attack.xml"));
			for (NPCStyle data : loadedData)
			{
				//System.out.println(data.getAtts().length);
				attacks.put(data.npcId, data);
			}
			logger.info("Successfully loaded "+loadedData.size() +" npc Attacks.");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
