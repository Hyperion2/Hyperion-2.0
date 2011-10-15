package org.hyperion.rs2.tick.impl;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.content.combat.EntityDeath;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.tick.Tick;

/**
 * The death event handles player and npc deaths. Drops loot, does animation, teleportation, etc.
 * @author Graham
 *
 */
public class DeathTick extends Tick {
	
	private Entity entity;

	/**
	 * Creates the death event for the specified entity.
	 * @param entity The player or npc whose death has just happened.
	 */
	public DeathTick(Entity entity) {
		super(5);//This shall be changed to accordance with NPCs
		this.entity = entity;
	}

	@Override
	public void execute() {
		entity.playAnimation(Animation.create(65535));//reset animation.
		if(entity instanceof Player) {
			Player player = (Player) entity;
			for(int i = 0; i < Skills.SKILL_COUNT; i++) {
				player.getSkills().setLevel(i, player.getSkills().getLevelForExperience(i));
			}
			player.applyHealthChange(player.getSkills().getLevel(Skills.HITPOINTS) * (Constants.CONSTITUTION_ENABLE ? 10 : 1), true);
			EntityDeath.playerDied(player);
			entity.setLocation(Entity.DEFAULT_LOCATION);
			entity.setTeleportTarget(Entity.DEFAULT_LOCATION);
			entity.setDead(false);
		} else {
			NPC n = (NPC) entity;
			n.sendDeathEffects(entity.inflictedMostDamage());
			n.applyHealthChange(((NPC)entity).getDefinition().getMaxHealth(), true);
			n.setInvisible(true);
		}
		entity.setLastAttacked(null);
		entity.setCurrentTarget(null);
		entity.setPoisDmg(-1);
		entity.getDmgRecieved().clear();
		this.stop();
	}

}