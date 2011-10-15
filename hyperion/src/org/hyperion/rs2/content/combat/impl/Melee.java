package org.hyperion.rs2.content.combat.impl;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.content.Bonus;
import org.hyperion.rs2.content.NPCStyle;
import org.hyperion.rs2.content.combat.Combat;
import org.hyperion.rs2.content.combat.CombatCheck;
import org.hyperion.rs2.content.combat.CombatEffect;
import org.hyperion.rs2.content.combat.WeaponStyle;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Damage.Hit;
import org.hyperion.rs2.model.Damage.HitType;
import org.hyperion.rs2.model.Entity;
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
		
		//Graphics - Mostly NPCs
		//TODO special graphics
		//int gfx1
		//atker.playGraphics(Graphic.create(id))
		
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
		return true;
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
		int strength = (int) p.getSkills().getLevel(Skills.STRENGTH);
			//if(Equipment.wearingDharock(this)) {
			//	double nb = bonus /= 100; //This is now 1.05 with nothing but DH.
			//	boost *= 0.60 + (nb - 1.05);
			//	return (int) (((skills.getLevelForExperience(Skills.HITPOINTS) + strength) / 2 - getHitpoints() / 2 ) * boost);
			//}

		double effectiveStrength = Math.round(strength * boost);
		int style = WeaponStyle.getStyle(p);
		
		if(style == WeaponStyle.STYLE_AGGRESSIVE)//Agressive
			effectiveStrength += 3;

		if(style == WeaponStyle.STYLE_CONTROLLED) //Controlled.
			effectiveStrength += 1;

		int max = (int) Math.ceil(1.3 + effectiveStrength/10 + bonus/80 + effectiveStrength*bonus/640);
		
		return Constants.CONSTITUTION_ENABLE ? max * 10 : max;
	}
}
