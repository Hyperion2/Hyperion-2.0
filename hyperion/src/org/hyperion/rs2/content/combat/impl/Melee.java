package org.hyperion.rs2.content.combat.impl;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.content.Bonus;
import org.hyperion.rs2.content.NPCStyle;
import org.hyperion.rs2.content.Poison;
import org.hyperion.rs2.content.combat.Combat;
import org.hyperion.rs2.content.combat.CombatCheck;
import org.hyperion.rs2.content.combat.CombatEffect;
import org.hyperion.rs2.content.combat.WeaponStyle;
import org.hyperion.rs2.content.skills.Prayer;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Damage.Hit;
import org.hyperion.rs2.model.Damage.HitType;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Graphic;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.tick.Tick;

public class Melee {

	/**
	 * Executes melee combat.
	 * @param atker The attacking entity,
	 * @param victim The victim of the attack.
	 */
	public static void execute(final Entity atker, final Entity victim) {
		//Check to see if we should continue.
		if(!CombatCheck.correctAttributes(atker, victim) || atker.isDead()) {
			//Declare out of combat.
			return;
		}
		
		//diagnal check.
		//TODO ^^
		
		//distance check
		if(!atker.getLocation().withinRange(victim.getLocation(), 1)) {
			//follow
			return;
		}
		
		//delay
		if(atker.getLastAttack() > 0) return;
		
		//face Opponent
		if(atker.getInteractingEntity() == null)
			atker.setInteractingEntity(victim);
		
		//Set the target.
		atker.setCurrentTarget(victim);
		
		//Special
		boolean specOn = false;
		
		//Amount of hits.
		int hitCount = 1;
		
		//NPC Info.
		//NPC Attack info
		final NPCStyle s = atker instanceof NPC ? NPCStyle.attacks.get(((NPC)atker).getId()) : null;
		
		//Get max hit of entity.
		int max = s == null ? maxDamage(atker, victim, specOn) : s.getAtts()[atker.getFightIndex()].getMax();

		//animation
		//TODO special emotes.
		int emote = s == null ? ((Player)atker).getAttackEmote() : s.getAtts()[atker.getFightIndex()].getAnim();
		atker.playAnimation(Animation.create(emote));
		
		//defend emote.
		if(victim.getCurrentAnimation() == null)
			victim.playAnimation(Animation.create(victim instanceof Player ? ((Player)victim).getDefEmote() : NPCStyle.attacks.get(((NPC)victim).getId()).getDefAnim()));
		
		//Weapon being used.
		final Item weapon = atker instanceof Player ? ((Player)atker).getEquipment().get(Equipment.SLOT_WEAPON) : null;
		
		//Graphics - Mostly NPCs
		if(atker instanceof NPC)
			atker.playGraphics(Graphic.create(s.getAtts()[atker.getFightIndex()].getGfx()[0]));
		
		//Tick timers
		//TODO redo this part.
		Item wep = atker instanceof NPC ? null : ((Player)atker).getEquipment().get(Equipment.SLOT_WEAPON);
		atker.setLastAttack(s == null ? wep == null ? 6 : wep.getDefinition().getSpeed() : s.getAtts()[atker.getFightIndex()].getSpeed());
		if(atker instanceof Player)
			((Player)atker).setAbstractMagicDelay(3);
		
		//hit Loop.
		for(int i = 0; i < hitCount; i++) {
			
			//Get base hit.
			int hit = successfull(atker, victim, specOn) ? CombatCheck.random(max) : 0;
			
			//the final determined hit.
			final int dHit = hit;
			
			//Hit Tick.
			World.getWorld().submit(new Tick(1) {
	
				
				@Override
				protected void execute() {
					// TODO Auto-generated method stub
					if(victim.getHealth() <= 0) {
						this.stop();
						return;
					}
					
					//poison
					if(CombatCheck.random(4) == 1 && weapon != null)
						if(weapon.getDefinition().getPoisonDmg() > 0 && weapon.getDefinition().getPoisonDmg() != 65535)
							Poison.poison(victim, weapon.getDefinition().getPoisonDmg());
					
					//inflict the damage done.
					victim.inflictDamage(new Hit(dHit > victim.getHealth() ? victim.getHealth() : dHit, dHit <= 0 ? HitType.NO_DAMAGE : HitType.NORMAL_DAMAGE), atker);
					
					/* Handle Agressive NPCs */
					if(atker instanceof NPC && CombatCheck.isInMultiZone(atker.getLocation())
							&& atker.getAggressorState())
						atker.setCurrentTarget(null);//Change opponents.
					this.stop();
				}
				
			});
		
			//apply experience
			if(atker instanceof Player)
				CombatEffect.giveExperience(((Player)atker), dHit, Combat.MELEE);
		}
	}
	
	/**
	 * If the attack was successfull.
	 * @param e Attacking entity.
	 * @param o Defending entity.
	 * @param s Special attack enabled.
	 * @return successfull hit.
	 */
	public static boolean successfull(Entity e, Entity o, boolean s) {
		int atk = 80, def = 75;
		if(e instanceof Player) {
			Player p = (Player)e;
			int effectiveAccuracy = p.getSkills().getLevel(Skills.ATTACK);
			double boost = 1.00;
			if(p.getPrayers()[Prayer.CLARITY_OF_THOUGHT])
				boost *= 1.05;
			else if(p.getPrayers()[Prayer.IMPROVED_STRENGTH])
				boost *= 1.10;
			else if(p.getPrayers()[Prayer.INCREDIBLE_REFLEXES])
				boost *= 1.15;
			int bonus = WeaponStyle.getAttackBonusForType(p);
			effectiveAccuracy = (int) Math.floor(boost * effectiveAccuracy) + 8;
			if(p.getFightStyle() == WeaponStyle.STYLE_ACCURATE)
				effectiveAccuracy += 3;
			else if(p.getFightStyle() == WeaponStyle.STYLE_CONTROLLED)
				effectiveAccuracy += 1;
			atk = (int) (effectiveAccuracy * (1 + bonus/64)) * 10;
		}
		if(o instanceof Player) {
			Player p = (Player)e;
			int effectiveDefence = p.getSkills().getLevel(Skills.DEFENCE);
			double boost = 1.00;
			if(p.getPrayers()[Prayer.CLARITY_OF_THOUGHT])
				boost *= 1.05;
			else if(p.getPrayers()[Prayer.IMPROVED_STRENGTH])
				boost *= 1.10;
			else if(p.getPrayers()[Prayer.INCREDIBLE_REFLEXES])
				boost *= 1.15;
			int bonus = WeaponStyle.getDefenceBonusForType(p);
			effectiveDefence = (int) Math.floor(boost * effectiveDefence) + 8;
			if(p.getFightStyle() == WeaponStyle.STYLE_DEFENSIVE || p.getFightStyle() == WeaponStyle.STYLE_LONG_RANGE)
				effectiveDefence += 3;
			else if(p.getFightStyle() == WeaponStyle.STYLE_CONTROLLED)
				effectiveDefence += 1;
			def = (int) (effectiveDefence * (1 + bonus/64)) * 10;
		}
		int accuracy = atk > def ? 1 - (def+1) / (2 * atk) : (atk-1) / (2* def); 
		return CombatCheck.random(99)+1 <= (accuracy * 100);
	}
	
	/**
	 * Max damage the attacking Entity may hit.
	 * @param e Attacking entity.
	 * @param o Defending entity.
	 * @param s Special attack enabled.
	 * @return maxDamage.
	 */
	public static int maxDamage(Entity e, Entity o, boolean s) {
		Player p = (Player)e;
		int bonus = p.getBonus()[Bonus.STRENGTH_BONUS];
		double boost = 1.00;
			//}
		/*	+ Burst of Strength Prayer: 1.05
			+ Superhuman Strength Prayer: 1.10
			+ Ultimate Strength Prayer: 1.15
			+ Chivalry Prayer: 1.18
			+ Piety Prayer: 1.23
			+ Void Knight, Full Melee: 1.20
			+ Black Mask (Slayer Missions): 1.15
			+ Salve Amulet (Versus Undead): 1.15
			+ Salve Amulet(e) (Versus Undead): 1.20
			
			*/
		if(p.getPrayers()[Prayer.BURST_OF_STRENGTH])
			boost *= 1.05;
		else if(p.getPrayers()[Prayer.SUPERHUMAN_STRENGTH])
			boost *= 1.10;
		else if(p.getPrayers()[Prayer.ULTIMATE_STRENGTH])
			boost *= 1.10;
		int strength = (int) p.getSkills().getLevel(Skills.STRENGTH);
			//if(Equipment.wearingDharock(this)) {
			//	double nb = bonus /= 100; //This is now 1.05 with nothing but DH.
			//	boost *= 0.60 + (nb - 1.05);
			//	return (int) (((skills.getLevelForExperience(Skills.HITPOINTS) + strength) / 2 - getHitpoints() / 2 ) * boost);
			//}

		double effectiveStrength = Math.floor(strength * boost);
		int style = WeaponStyle.getStyle(p);
		
		if(style == WeaponStyle.STYLE_AGGRESSIVE)//Agressive
			effectiveStrength += 3;

		if(style == WeaponStyle.STYLE_CONTROLLED) //Controlled.
			effectiveStrength += 1;

		int max = (int) Math.ceil(1.3 + effectiveStrength/10 + bonus/80 + effectiveStrength*bonus/640);
		
		return Constants.CONSTITUTION_ENABLE ? max * 10 : max;
	}
}
