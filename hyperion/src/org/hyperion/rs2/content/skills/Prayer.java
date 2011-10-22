package org.hyperion.rs2.content.skills;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;

/**
 * Prayer class, handles prayer. Could be done a better way. But this is sufficent.
 * @author phil
 *
 */
public class Prayer {
	
	/**
	 * Prayer Types.
	 */
	public static final int STRENGTH_BOOST = 1, ATTACK_BOOST = 0, DEFENCE_BOOST = 2, HEADICON_PRAYER = 3, OTHER = 4;

	/**
	 * Static spot of the prayers.
	 */
	public static final int 
		THICK_SKIN = 0,
		BURST_OF_STRENGTH = 1,
		CLARITY_OF_THOUGHT = 2,
		ROCK_SKIN = 3,
		SUPERHUMAN_STRENGTH = 4,
		IMPROVED_STRENGTH = 5,
		RAPID_RESTORE = 6,
		RAPID_HEAL = 7,
		PROTECT_ITEM = 8,
		STEEL_SKIN = 9,
		ULTIMATE_STRENGTH = 10,
		INCREDIBLE_REFLEXES = 11,
		PROTECT_FROM_MAGE = 12,
		PROTECT_FROM_MISSILES = 13,
		PROTECT_FROM_MELEE = 14,
		RETRIBUTION = 15,
		REDEMPTION = 16,
		SMITE = 17;
	
	/**
	 * Info of the Prayers.
	 */
	public static final Object[][][] PRAYER = {
		{
			{"Thick Skin", 1, 83, 0.0, DEFENCE_BOOST},
			{"Burst of Strength", 4, 84, 0.0,  STRENGTH_BOOST},
			{"Clarity of Thought", 7, 85, 0.0, ATTACK_BOOST},
			{"Rock Skin", 10, 86, 0.0, DEFENCE_BOOST},
			{"Superhuman Strength", 13, 87, 0.0, STRENGTH_BOOST},
			{"Improved Reflexes", 16, 88, 0.0, ATTACK_BOOST},
			{"Rapid Restore", 19, 89, 0.0, OTHER},
			{"Rapid Heal", 22, 90, 0.0, OTHER},
			{"Protect Items", 25, 91, 0.0, OTHER},
			{"Steel Skin", 28, 92, 0.0, DEFENCE_BOOST},
			{"Ultimate Strength", 31, 93, 0.0, STRENGTH_BOOST},
			{"Incredible Reflexes", 34, 94, 0.0, ATTACK_BOOST},
			{"Protect from Magic", 37, 95, 0.0, HEADICON_PRAYER, (byte) 2},
			{"Protect from Missiles", 40, 96, 0.0, HEADICON_PRAYER, (byte) 1},
			{"Protect from Melee", 43, 97, 0.0, HEADICON_PRAYER, (byte) 0},
			{"Retribution", 46, 98, 0.0, HEADICON_PRAYER, (byte) 3},
			{"Redemption", 49, 99, 0.0, HEADICON_PRAYER, (byte) 5},
			{"Smite", 52, 100, 0.0, HEADICON_PRAYER, (byte) 4},
		},
		{//Leave This blank! Unless using Ancient Curses
			
		}
	};
	
	/**
	 * Prayer Click. Activates/Deactivates selected Prayer.
	 * @param p The player.
	 * @param button the buttonId.
	 */
	public static void prayerClick(Player p, int button) {
		int index = button <= 685 ? button - 683 + 15 : button - 5609;
		Object[] prayer = PRAYER[p.getPrayBook()][index];
		//Level Check.
		if(p.getSkills().getLevelForExperience(Skills.PRAYER) < (Integer) prayer[1]) {
			p.getActionSender().sendConfig((Integer) prayer[2], 0);
			p.getActionSender().sendChatInfoInterface("You need a Prayer level of at least "+((Integer) prayer[1])+" to use @dbl@"+((String) prayer[0])+".");
			return;
		}
		boolean activated = p.getPrayers()[index];
		//Check Activation.
		if(activated) {
			p.getPrayers()[index] = false;
			if((Integer) prayer[4] == HEADICON_PRAYER)//Remove Headicon.
				p.setHeadIcon((byte) -1);
			p.getActionSender().sendConfig((Integer) prayer[2], 0);
			return;
		}
		//Now activate, apply headicons etc.
		if((Integer)prayer[2] != OTHER) {
			for(int i = 0; i < p.getPrayers().length; i++) {
				if(PRAYER[p.getPrayBook()][i][4] == prayer[4] &&
						PRAYER[p.getPrayBook()][i][1] != prayer[1]) {//Reset prayer of same type.
					p.getActionSender().sendConfig((Integer) PRAYER[p.getPrayBook()][i][2], 0);
					continue;
				}
			}
		}
		//headicon.
		if((Integer)prayer[4] == HEADICON_PRAYER)
			p.setHeadIcon((Byte) prayer[5]);
		//send config.
		p.getActionSender().sendConfig((Integer) prayer[2], 1);
	}
	
	/**
	 * Resets the prayers.
	 * @param p The player.
	 */
	public static void resetPrayers(Player p, boolean disable) {
		for(int i = 0; i < p.getPrayers().length; i++) {
			if(p.getPrayers()[i] || disable) {
				p.getPrayers()[i] = false;
				p.getActionSender().sendConfig((Integer) PRAYER[p.getPrayBook()][i][2], 0);
			}
		}
	}
}
