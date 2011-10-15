package org.hyperion.rs2.content.combat.impl;

import org.hyperion.Server;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.content.Bonus;
import org.hyperion.rs2.content.NPCStyle;
import org.hyperion.rs2.content.Projectile;
import org.hyperion.rs2.content.ProjectileManager;
import org.hyperion.rs2.content.NPCStyle.Style;
import org.hyperion.rs2.content.combat.Combat;
import org.hyperion.rs2.content.combat.CombatCheck;
import org.hyperion.rs2.content.combat.CombatEffect;
import org.hyperion.rs2.content.magic.Spells;
import org.hyperion.rs2.content.magic.Spells.Spell;
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
import org.hyperion.rs2.tick.Tick;

/**
 * Magic Casting
 * @author phil
 *
 */
public class Magic {

	/**
	 * Executes magic combat.
	 * <NPC> Support not valid.
	 * @param caster The opponent.
	 * @param opp The 'proposed' target.
	 */
	public static void execute(final Entity caster, final Entity opp) {
		//Check to see if we should continue.
		if(!CombatCheck.correctAttributes(caster, opp) || caster.isDead()) {
			//Declare out of combat.
			return;
		}
		
		//Distance checking - Basic - Should check lineOfSite also.
		if(!caster.getLocation().withinRange(opp.getLocation(), 8)) {
			//Follow to open spot
			return;
		} else {
			caster.getWalkingQueue().reset();//Dont walk if within range.
		}
		
		//Check timer.
		if(caster instanceof Player && ((Player)caster).getAbstractMagicDelay() > 0
				|| caster instanceof NPC && caster.getLastAttack() > 0)//Magic delay is indirectly related to CombatTimer.
			return;
		
		//Check the magic spells.
		if(caster instanceof Player && ((Player)caster).getMagicSpells().isEmpty()) {
			//end combat.
			return;
		}
		
		caster.setInteractingEntity(opp);
		caster.setCurrentTarget(opp);
		
		//Spell, no support for NPC or autocasting.
		final Spell sp = caster instanceof Player ? Spells.getSpell(((Player)caster).getMagicSpells().poll()) : null;
		final NPCStyle s = caster instanceof Player ? null : NPCStyle.attacks.get(((NPC)caster).getId());
		final Style st = s == null ? null : s.getAtts()[caster.getFightIndex()];
		if(sp == null && caster instanceof Player
				|| st == null && caster instanceof NPC) return;//No Support for NPCs atm.
		
		int max = (caster instanceof NPC) ? st.getMax() : sp.getBaseMax();//
		
		//check player requirements.
		if(caster instanceof Player) {
			Player p = (Player) caster;
			//level check
			if(p.getSkills().getLevel(Skills.MAGIC) < sp.getLv()) {
				p.getActionSender().sendMessage("You do not have the required magic level to cast this spell.");
				return;
			}
			//Staff check.
			if(sp.getStaff() != 65535 && sp.getStaff() > 0) {
				if(!p.getEquipment().contains(sp.getStaff())) {
					p.getActionSender().sendMessage("You do not have the required staff to cast this spell.");
					return;
				}
			}
			//Runes check
			for(Item rune : sp.getRunes()) {
				//no support for staffs.
				if(!p.getInventory().contains(rune.getId()) || p.getInventory().getCount(rune) < rune.getCount()) {
					p.getActionSender().sendMessage("You do not have enough "+rune.getDefinition().getName()+"s to cast this spell.");
					return;
				}
			}
			//Remove Items.
			for(Item rune : sp.getRunes())
				p.getInventory().remove(rune);
		}
		
		//Animation
		final int animId = caster instanceof Player ? sp.getAnim() : st.getAnim();
		caster.playAnimation(Animation.create(animId));
		
		//graphic
		if(caster instanceof Player)
			caster.playGraphics(sp.getGfx()[0]);//Delay, height etc set from magic.
		if(caster instanceof NPC && st.getGfx()[0] != 65535)
			caster.playGraphics(Graphic.create(st.getGfx()[0], 100 << 16));
		
		//Skulling not supported.
		
		//Get hit.
		final boolean success = successfull(caster, opp);
		final int hit = success ? altarDamage(caster, opp, CombatCheck.random((Constants.CONSTITUTION_ENABLE ? (max*10)-1 : max))) : 0;//Should check for Effects
		
		//addxp
		if(caster instanceof Player) {
			((Player)caster).getSkills().addExperience(Skills.MAGIC, (sp.getCastXp() * Constants.SKILL_EXPERIENCE));//Change the 4 to different if using new combat system.
			CombatEffect.giveExperience(((Player)caster), hit, Combat.MAGIC);
		}
		//set delay
		if(caster instanceof Player)
			//Autocast not supported.
			((Player)caster).setAbstractMagicDelay(5);//Should also set combat delay to about 4/3
		caster.setLastAttack(caster instanceof Player ? 5 : st.getSpeed());
		
		Projectile proj = null;
		
		//Set projectile data.
		proj = ProjectileManager.getProjectile(caster instanceof Player ? sp.getProj() : st.getGfx()[1]);
		
		//Delays *Thanks to Maxi*
		double splatDelay = proj == null ? 0 : (proj.getDelay() + proj.getSlowness()) + caster.getLocation().getDistanceFromLocation(opp.getLocation()) * 5;
		int delay = (int) Math.ceil((splatDelay * 12d) / 600d);
		
		//reduce delay by 1 if using barrage?.
		if(proj == null || proj != null && proj.getGfx() == 65535)//Check by anim.
			delay++;
		
		final Graphic gfx = caster instanceof Player ? sp.getGfx()[1] : Graphic.create(st.getGfx()[2], 100 << 16);
		
		//setting graphic timer
		/*if(opp.getCurrentGraphic() == null)
			if(caster instanceof Player && sp.getBaseMax() != 0 && !success || hit <= 0)
				opp.playGraphics(Graphic.create(85, 100 << 16, (int) splatDelay));
			else if(caster instanceof Player || st != null && st.getGfx().length >= 3)
				opp.playGraphics(Graphic.create(gfx.getId(), gfx.getDelay(), (int) splatDelay));*/
		
		ProjectileManager.createProjectile(caster, opp, proj);
		
	//	opp.playAnimation(Animation.create(st == null ? ((Player)caster).getDefEmote()	:	s.getDefAnim(), (int) splatDelay-20));//Supposed to do it before the hit.
		
		//Finalize hit delay.
		final int hitDelay = delay;
		
		//setDelay
		World.getWorld().submit(new Tick(1) {//Next tick send effects.
			
			int tick = 0;
			
			@Override
			public void execute() {
				// TODO Auto-generated method stub
				//check if dead.
				if(opp.isDead() || opp.getHealth() <= 0) {
					this.stop();
					return;
				}
				
				//Effects 1 tick after.
				if(tick++ == 0) {
					//Apply effects if was successful.
					if(success)
						CombatEffect.spellEffect(caster, opp, sp);
					setDelay((int) hitDelay);
					return;
				}

				//GFX before hit. Not too sure about this.
				if(animId == 1979) {
					if(tick++ == 1) {//and spell is ancients.
						setDelay(1);
						//setting graphic timer
						if(caster instanceof Player && sp.getBaseMax() != 0 && !success || hit <= 0)
							opp.playGraphics(Graphic.create(85, 100 << 16));
						else if(caster instanceof Player || st != null && st.getGfx().length >= 3)
							opp.playGraphics(Graphic.create(gfx.getId(), gfx.getDelay()));
						return;
					}
				} else {//Apply gfx now if not on ancients.
					if(caster instanceof Player && sp.getBaseMax() != 0 && !success || hit <= 0)
						opp.playGraphics(Graphic.create(85, 100 << 16));
					else if(caster instanceof Player || st != null && st.getGfx().length >= 3)
						opp.playGraphics(Graphic.create(gfx.getId(), gfx.getDelay()));
				}
				
				//apply
					if(success && caster instanceof NPC ||
							success && sp.getBaseMax() != 0)
						opp.inflictDamage(new Hit(hit > opp.getHealth() ? opp.getHealth() : hit <= 0 ? -1 : hit, HitType.NORMAL_DAMAGE), caster);
				
				//Add damage to the Map.
				if(hit > 0)
					opp.addDmgRecieved(caster, hit);
				
				/* Handle Agressive NPCs */
				if(caster instanceof NPC && CombatCheck.isInMultiZone(caster.getLocation())
						&& caster.getAggressorState())
					caster.setCurrentTarget(null);//Change opponents.
				this.stop();
			}
			
		});
	}
	
	/**
	 * Determines if the hit was successful or not.
	 * @param e The atker.
	 * @param k The defender.
	 * @return SuccessfullHit.
	 */
	public static boolean successfull(Entity e, Entity k) {
		//Not Supported
		int def = 1, atk = 1;
		//Atker distribution
		if(e instanceof Player) {
			Player a = (Player)e;
			int mageLv = a.getSkills().getLevel(Skills.MAGIC);
			int mageAtt = a.getBonus()[Bonus.MAGIC_ATTACK];
			//Prayer
			
			//Armour
			
			atk += (int) ((mageLv * 0.67) + (mageAtt * 0.63) + (mageAtt * mageLv / 1000));
		}
		//Opponent distribution
		if(k instanceof Player) {
			Player d = (Player)k;
			int defLv = d.getSkills().getLevel(Skills.DEFENCE);
			int mageLv = d.getSkills().getLevel(Skills.DEFENCE);
			int mageDef = d.getBonus()[Bonus.MAGIC_DEF];
			def += ((defLv + mageLv) / 1.67) + (mageDef * 0.77);
		}
		return CombatCheck.random(def) <= CombatCheck.random(atk);
	}
	
	/**
	 * Determines if hit has been altar'd
	 * @param e The caster.
	 * @param o The opponent.
	 * @param hit The initial Damage.
	 * @return The final hit.
	 */
	public static int altarDamage(Entity e, Entity o, int hit) {
		if(o instanceof NPC || e instanceof NPC) return hit;//No NPC Support.
		int base = hit;
		if(Server.VERSION >= 614)
			hit += (base * (((Player)e).getBonus()[Bonus.MAGIC_DAMAGE]) / 100);
		Player p = (Player)e;
		if(p.getSkills().getLevel(Skills.MAGIC) - p.getSkills().getLevelForExperience(Skills.MAGIC) > 0)
			hit = (int) (hit + (base * ((p.getSkills().getLevel(Skills.MAGIC) - p.getSkills().getLevelForExperience(Skills.MAGIC)) * 0.03)));
		return hit;
	}
 }
