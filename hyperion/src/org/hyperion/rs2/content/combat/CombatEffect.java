package org.hyperion.rs2.content.combat;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.content.magic.Spells.Spell;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;

public class CombatEffect {

	/**
	 * Spell Effect of the spell, or Entity.
	 * @param e The caster.
	 * @param o The target.
	 * @param sp The spell/Null if Player.
	 */
	public static void spellEffect(Entity e, Entity o, Spell sp) {
		if(sp != null) {
			if(sp.getProj() == 178) {
				if(o.getFreezeDelay()+4000 > System.currentTimeMillis()) return;
				if(o instanceof Player)
					((Player)o).getActionSender().sendMessage("You feel as if magic has strucken you immobile.");
				//Prayer reduce by 1/2
				o.setFreezeDelay(System.currentTimeMillis() + (sp == Spell.BIND ? 5000 : 10000));
				return;
			}
		} else {
			((NPC)e).distributeAttackEffects(e, 0);
		}
	}
	
	/**
	 * Combat Experience for the selected combat style.
	 * @param player The player.
	 * @param combatType The combatType.
	 */
	public static void giveExperience(Player player, int dmg, int combatType) {
		int style = WeaponStyle.getStyle(player);
		player.getSkills().addExperience(Skills.HITPOINTS, Constants.COMBAT_EXPERIENCE * (Constants.CONSTITUTION_ENABLE ? dmg * .133 : dmg * 1.33));
		switch(combatType) {
		
			case Combat.MAGIC:
				//This has to change if higher revision.
				player.getSkills().addExperience(Skills.MAGIC, Constants.COMBAT_EXPERIENCE * (Constants.CONSTITUTION_ENABLE ? .4 * dmg : 4 * dmg));
				break;
				
			case Combat.MELEE:
				if(style == WeaponStyle.STYLE_ACCURATE)
					player.getSkills().addExperience(Skills.ATTACK, Constants.COMBAT_EXPERIENCE * (Constants.CONSTITUTION_ENABLE ? .4 * dmg : 4 * dmg));
				else if(style == WeaponStyle.STYLE_AGGRESSIVE)
					player.getSkills().addExperience(Skills.STRENGTH, Constants.COMBAT_EXPERIENCE * (Constants.CONSTITUTION_ENABLE ? .4 * dmg : 4 * dmg));
				else if(style == WeaponStyle.STYLE_DEFENSIVE)
					player.getSkills().addExperience(Skills.DEFENCE, Constants.COMBAT_EXPERIENCE * (Constants.CONSTITUTION_ENABLE ? .4 * dmg : 4 * dmg));
				else
					for(int i = 0; i < Skills.STRENGTH; i++)
						player.getSkills().addExperience(i, (Constants.COMBAT_EXPERIENCE * (Constants.CONSTITUTION_ENABLE ? .4 * dmg : 4 * dmg)) / 3);
				break;
				
			case Combat.RANGE:
				if(style == WeaponStyle.STYLE_LONG_RANGE) {
					player.getSkills().addExperience(Skills.DEFENCE, (Constants.COMBAT_EXPERIENCE * (Constants.CONSTITUTION_ENABLE ? .4 * dmg : 4 * dmg)) / 2);
					player.getSkills().addExperience(Skills.RANGE, (Constants.COMBAT_EXPERIENCE * (Constants.CONSTITUTION_ENABLE ? .4 * dmg : 4 * dmg)) / 2);
				} else {
					player.getSkills().addExperience(Skills.RANGE, Constants.COMBAT_EXPERIENCE * (Constants.CONSTITUTION_ENABLE ? .4 * dmg : 4 * dmg));
				}
				break;
		}
	}
}
