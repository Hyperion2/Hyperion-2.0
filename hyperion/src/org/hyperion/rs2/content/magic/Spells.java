package org.hyperion.rs2.content.magic;

import java.util.HashMap;
import java.util.Map;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.Graphic;
import org.hyperion.rs2.model.Item;

public class Spells {

	/**
	 * The spell container.
	 */
	private static final Map<Integer, Spell> spells = new HashMap<Integer, Spell>();
	
	/**
	 * Gets the spell Definition,
	 * @param i The spellId.
	 * @return The spell Definition.
	 */
	public static Spell getSpell(int i) {
		return spells.get(i);
	}
	
	static {
		for(Spell sp : Spell.values()) {
			spells.put(sp.spell, sp);
		}
	}
	
	/**
	 * Spell definitions <Release on <link>www.rune-server.org</link>
	 * @author phil
	 *
	 * 727/728 - wave>?
	 *
	 */
	public enum Spell {
		WIND_STRIKE(1152, 2, 1, 711, 5.5, -1, new Item[] {new Item(558), new Item(556)}, new Graphic[] {Graphic.create(90, 100 << 16), Graphic.create(92, 100 << 16)}, 91),
		CONFUSE(1153, 0, 3, 716, 13, -1, new Item[] {new Item(555, 3), new Item(557, 2), new Item(559)}, new Graphic[] {Graphic.create(102, 100 << 16), Graphic.create(104, 200 << 16)}, 103),
		WATER_STRIKE(1154, 4, 5, 711, 7.5, -1, new Item[] {new Item(555), new Item(558), new Item(556)}, new Graphic[] {Graphic.create(93, 100 << 16), Graphic.create(95, 100 << 16)}, 94),
		EARTH_STRIKE(1156, 6, 9, 711, 9.5, -1, new Item[] {new Item(557), new Item(558), new Item(556)}, new Graphic[] {Graphic.create(96, 100 << 16), Graphic.create(98, 100 << 16)}, 97),
		WEAKEN(1157, 0, 11, 729, 21, -1, new Item[] {new Item(559), new Item(555, 3), new Item(558, 2)}, new Graphic[] {Graphic.create(105, 90 << 16), Graphic.create(107, 200 << 16)}, 106),
		FIRE_STRIKE(1158, 8, 13, 711, 11.5, -1, new Item[] {new Item(558), new Item(554), new Item(556, 2)}, new Graphic[] {Graphic.create(99, 100 << 16), Graphic.create(101, 100 << 16)}, 100),
		BIND(1572, 0, 20, 710, 30, -1, new Item[] {new Item(561, 2), new Item(557, 3), new Item(555, 3)}, new Graphic[] {Graphic.create(177, 100 << 16), Graphic.create(179, 100 << 16)}, 178);
		
		Spell(int sp, int baseMax, int lv, int anim, double castXp, int staff, Item[] req, Graphic[] gfx, int proj) {
			spell = sp;
			this.proj = (short) proj;
			this.baseMax = (short) baseMax;
			this.lv = (byte) lv;
			this.castXp = castXp;
			this.staff = (short) staff;
			this.runes = req;
			this.anim = (short) anim;
			this.gfx = gfx;
		}

		/**
		 * @return the baseMax
		 */
		public short getBaseMax() {
			return (short) (Constants.CONSTITUTION_ENABLE ? baseMax * 10 : baseMax);
		}
		
		/**
		 * @return the staff
		 */
		public short getStaff() {
			return staff;
		}
		
		/**
		 * @return the anim
		 */
		public short getAnim() {
			return anim;
		}
		
		/**
		 * @return the castXp
		 */
		public double getCastXp() {
			return castXp;
		}
		/**
		 * @return the runes
		 */
		public Item[] getRunes() {
			return runes;
		}

		/**
		 * @return the lv
		 */
		public byte getLv() {
			return lv;
		}

		/**
		 * @return the gfx
		 */
		public Graphic[] getGfx() {
			return gfx;
		}

		/**
		 * @return the proj
		 */
		public short getProj() {
			return proj;
		}
		private int spell;
		private short baseMax, staff, anim, proj;
		private double castXp;
		private Item[] runes;
		private byte lv;
		private Graphic[] gfx;
		
	}
	//More will be added further revisisions.
}
