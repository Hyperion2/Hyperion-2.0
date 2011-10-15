package org.hyperion.rs2.content.combat;

import org.hyperion.rs2.content.combat.impl.Magic;
import org.hyperion.rs2.content.combat.impl.Melee;
import org.hyperion.rs2.content.combat.impl.Range;
import org.hyperion.rs2.model.Entity;

public class Combat {
	
	/**
	 * Combat Styles.
	 */
	public static final int MELEE = 0, RANGE = 1, MAGIC = 2;
	
	/**
	 * 
	 * @param o
	 */
	public static void execute(Entity o) {
		//Execute checks incase state has changed.
		Entity opponent = o.getCurrentTarget();
		
		if(opponent == null || !CombatCheck.correctAttributes(o, opponent)) {
			//End combat.
			return;
		}
		
		int combatType = o.determineNextCombatAttack(opponent);
		
		switch(combatType) {
		
		case RANGE:
			Range.execute(o, opponent);
			break;
		
		case MAGIC:
			Magic.execute(o, opponent);
			break;
			
		case MELEE:
			Melee.execute(o, opponent);
			break;
		
		default:
			System.out.println("Combat Style: "+combatType+" Not Supported.");
		}
	}
}
