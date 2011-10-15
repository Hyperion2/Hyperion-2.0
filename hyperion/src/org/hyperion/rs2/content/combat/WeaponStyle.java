package org.hyperion.rs2.content.combat;

import java.util.HashMap;
import java.util.Map;

import org.hyperion.rs2.content.Bonus;
import org.hyperion.rs2.model.Player;

public class WeaponStyle {
	
	/**
	 * STYLES
	 */
	public static final int STYLE_ACCURATE = 0;
	public static final int STYLE_AGGRESSIVE = 1;
	public static final int STYLE_CONTROLLED = 2;
	public static final int STYLE_DEFENSIVE = 3;
	public static final int STYLE_RAPID = 4;
	public static final int STYLE_LONG_RANGE = 5;
	
	/**
	 * TYPES
	 */
	public static final int TYPE_STAB = 0;
	public static final int TYPE_SLASH = 1;
	public static final int TYPE_CRUSH = 2;
	public static final int TYPE_RANGED = 3;
	public static final int TYPE_MAGIC = 4;
	
	private static final Map<Byte, Style> styles = new HashMap<Byte, Style>();

	public static int getType(Player p) {
		if (styles.get(p.getFightIndex()).config.length < p.getFightStyle())
			p.setFightStyle((byte) (p.getFightStyle()-1));
		return styles.get(p.getFightIndex()).config[p.getFightStyle()][1];
	}
	
	public static int getStyle(Player p) {
		if (styles.get(p.getFightIndex()).config.length < p.getFightStyle())
			p.setFightStyle((byte) (p.getFightStyle()-1));
		return styles.get(p.getFightIndex()).config[p.getFightStyle()][0];
	}
	
	public static int getAttackBonusForType(Player player) {
		switch (getType(player)) {
		case TYPE_STAB:
			return (int) player.getBonus()[Bonus.STAB_ATTACK];
		case TYPE_SLASH:
			return (int) player.getBonus()[Bonus.SLASH_ATTACK];
		case TYPE_CRUSH:
			return (int) player.getBonus()[Bonus.CRUSH_ATTACK];
		case TYPE_RANGED:
			return (int) player.getBonus()[Bonus.RANGE_ATTACK];
		case TYPE_MAGIC:
			return (int) player.getBonus()[Bonus.MAGIC_ATTACK];
			default:
				return 0;
		}
	}
	
	public static int getDefenceBonusForType(Player player) {
		switch (getType(player)) {
		case TYPE_STAB:
			return (int) player.getBonus()[Bonus.STAB_DEF];
		case TYPE_SLASH:
			return (int) player.getBonus()[Bonus.SLASH_DEF];
		case TYPE_CRUSH:
			return (int) player.getBonus()[Bonus.CRUSH_DEF];
		case TYPE_RANGED:
			return (int) player.getBonus()[Bonus.RANGE_DEF];
		case TYPE_MAGIC:
			return (int) player.getBonus()[Bonus.MAGIC_DEF];
			default:
				return 0;
		}
	}
	
	public static class Style {
		int[][] config;
		
		private Style(int[][] config) {
			this.config = config;
		}
	}
	
	static {
		styles.put((byte) 0, new Style(new int[][]{{STYLE_ACCURATE, TYPE_CRUSH}, {STYLE_AGGRESSIVE, TYPE_CRUSH}, {STYLE_DEFENSIVE, TYPE_CRUSH}}));		
		styles.put((byte) 1, new Style(new int[][]{{STYLE_ACCURATE, TYPE_CRUSH}, {STYLE_AGGRESSIVE, TYPE_CRUSH}, {STYLE_DEFENSIVE, TYPE_CRUSH}}));
		styles.put((byte) 2, new Style(new int[][]{{STYLE_ACCURATE, TYPE_SLASH}, {STYLE_AGGRESSIVE, TYPE_SLASH}, {STYLE_AGGRESSIVE, TYPE_CRUSH}, {STYLE_DEFENSIVE, TYPE_SLASH}}));
		styles.put((byte) 3, new Style(new int[][]{{STYLE_ACCURATE, TYPE_CRUSH}, {STYLE_AGGRESSIVE, TYPE_CRUSH}, {STYLE_DEFENSIVE, TYPE_CRUSH}}));
		styles.put((byte) 4, new Style(new int[][]{{STYLE_ACCURATE, TYPE_STAB}, {STYLE_AGGRESSIVE, TYPE_STAB}, {STYLE_AGGRESSIVE, TYPE_CRUSH}, {STYLE_DEFENSIVE, TYPE_STAB}}));
		styles.put((byte) 5, new Style(new int[][]{{STYLE_ACCURATE, TYPE_STAB}, {STYLE_AGGRESSIVE, TYPE_STAB}, {STYLE_AGGRESSIVE, TYPE_SLASH}, {STYLE_DEFENSIVE, TYPE_STAB}}));
		styles.put((byte) 6, new Style(new int[][]{{STYLE_ACCURATE, TYPE_SLASH}, {STYLE_AGGRESSIVE, TYPE_SLASH}, {STYLE_CONTROLLED, TYPE_STAB}, {STYLE_DEFENSIVE, TYPE_SLASH}}));
		styles.put((byte) 7, new Style(new int[][]{{STYLE_ACCURATE, TYPE_SLASH}, {STYLE_AGGRESSIVE, TYPE_SLASH}, {STYLE_AGGRESSIVE, TYPE_CRUSH}, {STYLE_DEFENSIVE, TYPE_SLASH}}));
		styles.put((byte) 8, new Style(new int[][]{{STYLE_ACCURATE, TYPE_CRUSH}, {STYLE_AGGRESSIVE, TYPE_CRUSH}, {STYLE_CONTROLLED, TYPE_STAB}, {STYLE_DEFENSIVE, TYPE_CRUSH}}));
		styles.put((byte) 9, new Style(new int[][]{{STYLE_ACCURATE, TYPE_SLASH}, {STYLE_AGGRESSIVE, TYPE_SLASH}, {STYLE_CONTROLLED, TYPE_STAB}, {STYLE_DEFENSIVE, TYPE_SLASH}}));
		styles.put((byte) 10, new Style(new int[][]{{STYLE_ACCURATE, TYPE_CRUSH}, {STYLE_AGGRESSIVE, TYPE_CRUSH}, {STYLE_DEFENSIVE, TYPE_CRUSH}}));
		styles.put((byte) 11, new Style(new int[][]{{STYLE_ACCURATE, TYPE_SLASH}, {STYLE_CONTROLLED, TYPE_SLASH}, {STYLE_DEFENSIVE, TYPE_SLASH}}));
		styles.put((byte) 14, new Style(new int[][]{{STYLE_CONTROLLED, TYPE_STAB}, {STYLE_CONTROLLED, TYPE_SLASH}, {STYLE_CONTROLLED, TYPE_CRUSH}, {STYLE_DEFENSIVE, TYPE_STAB}}));
		styles.put((byte) 15, new Style(new int[][]{{STYLE_CONTROLLED, TYPE_STAB}, {STYLE_AGGRESSIVE, TYPE_SLASH}, {STYLE_DEFENSIVE, TYPE_STAB}}));
		styles.put((byte) 16, new Style(new int[][]{{STYLE_ACCURATE, TYPE_RANGED}, {STYLE_RAPID, TYPE_RANGED}, {STYLE_LONG_RANGE, TYPE_RANGED}}));
		styles.put((byte) 17, new Style(new int[][]{{STYLE_ACCURATE, TYPE_RANGED}, {STYLE_RAPID, TYPE_RANGED}, {STYLE_LONG_RANGE, TYPE_RANGED}}));
		styles.put((byte) 18, new Style(new int[][]{{STYLE_ACCURATE, TYPE_RANGED}, {STYLE_RAPID, TYPE_RANGED}, {STYLE_LONG_RANGE, TYPE_RANGED}}));
		styles.put((byte) 22, new Style(new int[][]{{STYLE_ACCURATE, TYPE_SLASH}, {STYLE_AGGRESSIVE, TYPE_STAB}, {STYLE_AGGRESSIVE, TYPE_CRUSH}, {STYLE_DEFENSIVE, TYPE_SLASH}}));
	}
}