package org.hyperion.rs2.model.npcs;

import org.hyperion.rs2.content.NPCStyle;
import org.hyperion.rs2.content.combat.CombatCheck;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.NPC;

/**
 * Default NPC. NOT FINISHED YET!
 * @author phil
 *
 */
public class npc extends NPC {

	public npc(int id) {
		super(id);
		// TODO Auto-generated constructor stub
		health = getDefinition().getMaxHealth();
	}

	@Override
	public int determineNextCombatAttack(Entity Opp) {
		// TODO Auto-generated method stub
		if(getFightStyle() != -1) return getFightStyle();
		byte nextIndex = (byte) (NPCStyle.attacks.get(getId()) != null ? CombatCheck.random(NPCStyle.attacks.get(getId()).getAtts().length-1) : 0);
		setFightIndex(nextIndex);
		setFightStyle(NPCStyle.attacks.get(getId()).getAtts()[nextIndex].getStyle());
		return getFightStyle();
	}

	@Override
	public void sendDeathEffects(Entity Opp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void distributeAttackEffects(Entity Opp, int hit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean withinAllowedZone(Location loc) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void executeSpawnAffects() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activateHealthStageEffects(Entity Opp) {
		// TODO Auto-generated method stub
		
	}

}
