package org.hyperion.rs2.content.combat;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Player;

/**
 * Combat Checks that need to be initialized.
 * @author phil The creator duh!
 *
 */
public class CombatCheck {

	/**
	 * Checks the position of the Entity.
	 * @param e The checking entity.
	 * @return If valid to continue.
	 */
	public static boolean correctAttributes(Entity e, Entity ot) {
		if(ot.isDead()) {//spell will continue if the attack is dead.
			return false;
		}
		//Check your state.
		if(e.getLastAttacked() != null && e.getLastAttacked() != ot && !isInMultiZone(e.getLocation())
				|| e.getLastAttacked() != null && e.getLastAttacked() != ot && !isInMultiZone(ot.getLocation())) {
			if(e instanceof Player)
				((Player)e).getActionSender().sendMessage("You are already under attack.");
			return false;
		}
		//Check other entity state.
		if(ot.getLastAttacked() != null && ot.getLastAttacked() != e && !isInMultiZone(e.getLocation())
				|| ot.getLastAttacked() != null && ot.getLastAttacked() != e && !isInMultiZone(ot.getLocation())) {
			if(e instanceof Player)
				((Player)e).getActionSender().sendMessage("This "+(ot instanceof Player ? "player" : "monster") + " is already in combat.");
			return false;
		}
		if(e instanceof Player && ot instanceof Player) {
			if(isInWilderness(e.getLocation()) && !isInWilderness(ot.getLocation())//Wilderness Check.
					|| !isInWilderness(e.getLocation()) && isInWilderness(ot.getLocation())) {
				((Player)e).getActionSender().sendMessage("You can only attack other players in the wilderness.");
				return false;
			}
			if(isInWilderness(e.getLocation()) && isInWilderness(ot.getLocation())) {
				int difference = Math.abs(((Player)e).getSkills().getCombatLevel() -  ((Player)ot).getSkills().getCombatLevel());
				int lv1 = Math.abs(wildernessLevel(e.getLocation()));
				int lv2 = Math.abs(wildernessLevel(ot.getLocation()));
				if(lv1 < difference || lv2 < difference) {
					((Player)e).getActionSender().sendMessage("You need to move deeper into the wilderness.");
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Gets the wilderness level of the current position.
	 * @param position The level of position.
	 * @return Widlerness level.
	 */
	public static int wildernessLevel(Location position) {
		return ((position.getY() - 3520) / 8)+1;
	}
	
	/**
	 * Determines if the location is in the wilderness.
	 * @param loc The location.
	 * @return In Wilderness.
	 */
	public static boolean isInWilderness(Location loc) {
		return Constants.PVP_WORLD;
	}
	
	/**
	 * Determines if the location is in a MultiCombat zone.
	 * @param loc The location.
	 * @return In wilderness.
	 */
	public static boolean isInMultiZone(Location loc) {
		return false;
	}
	
	/**
	 * Random
	 * @param o The random Integer, long, or Double.
	 * @return The result.
	 */
	public static int random(Object o) {
		return (int) (Math.random() * ((Integer) o));
	}
}
