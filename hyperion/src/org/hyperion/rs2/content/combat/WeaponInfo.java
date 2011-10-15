package org.hyperion.rs2.content.combat;

import org.hyperion.rs2.model.Definitions;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;

public class WeaponInfo {
	
	/**
	 * Contains 2hs.
	 */
	public static final String[] TWO_HANDED = {
		"2h", "flail", "maul", "shortbow", "longbow", "torag", "ahrim", "guthan", "darkbow",
		"karil", "godsword", "zaryte", "dharok", "spear"
	};

	/**
	 * Checks to see if weapon is two handed.
	 * @param i The weapon.
	 * @return TwoHanded.
	 */
	public static boolean isItemTwoHanded(Item i) {
		if(i == null) return false;
		String name = i.getDefinition().getName().toLowerCase();
		for(String s : TWO_HANDED)
			if(name.contains(s))
				return true;
		return false;
	}
	
	/**
	 * Set the player's attack emote based on the weapon currently Equip and it's attackStyle.
	 * @param player
	 */
	public static void setAttackEmote(Player player) {
		int emote = 426;
		player.setAttackEmote(emote);
	}
	
	/**
	 * Set the player's defend emote based on the shield/Weapon. currently equipted.
	 * @param player
	 */
	public static void setDefendEmote(Player player) {
		int emote = 424;
		player.setDefEmote(emote);
	}
	
	/**
	 * Weapon Timer.
	 * @param i The weapon.
	 * @return WeaponTImer.
	 */
	public static int timer(int i) {
		//TODO pack into Item file
		int timer = 4;
		if(Definitions.forId(i) == null) return 4;
		String item = Definitions.forId(i).getName().toLowerCase();
		if(item.contains("knive") || item.contains("knife") || item.contains("dart"))
			timer = 3;
		else if(item.contains("longsword") || item.contains("mace") || item.contains("hatchet")
				|| item.contains("spear") && !item.contains("zamor") || item.contains("pickaxe")
				|| item.contains("tzhaar-ket-em") || item.contains("torag") || item.contains("guthan")
				|| item.contains("flail") || item.contains("staff") || item.contains("composite")
				|| item.contains("seercull") || item.contains("crystal") || item.contains("throwing"))
			timer = 5;
		else if(item.contains("battleaxe") || item.contains("warhammer") || item.contains("godsword")
				|| item.contains("toktz-mej-tal") || item.contains("longbow") || item.contains("crossbow")
				&& !item.contains("karil") || item.contains("cannon") || item.contains("jav"))
			timer = 6;
		else if(item.contains("two handed") || item.contains("2h") || item.contains("halberd")
				|| item.contains("granite maul") || item.contains("tzhaar-ket-om") || item.contains("great axe"))
			timer = 7;
		else if(item.contains("dark bow"))
			timer = 9;
		return timer;//default is usually 2400ms.
	}
}
