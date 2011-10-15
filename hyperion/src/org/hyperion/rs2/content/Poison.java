package org.hyperion.rs2.content;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.model.Damage.Hit;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.Damage.HitType;

/**
 * This can be expanded with various other things.
 * @author phil
 *
 */
public class Poison {

	/**
	 * Poisons the entity if can.
	 * @param e The entity,
	 * @param poisonDmg The poison inital dmg.
	 */
	public static void poison(final Entity e, int poisonDmg) {
		if(e.getPoisDmg() > 0 || poisonDmg <= 0 || poisonDmg >= 255) return;//should also include immunity to poison.
		//18 seconds/30 ticks - Constitution 2, w/o - 90 ticks? - 54 seconds?
		e.setPoisDmg(Constants.CONSTITUTION_ENABLE ? (poisonDmg * 10)+8 : poisonDmg);
		if(e instanceof Player)
			((Player)e).getActionSender().sendMessage("You have been poisoned.");
		World.getWorld().submit(new Event(17450) {//Allowing execution on next Tick.

			@Override
			public void execute() {
				// TODO Auto-generated method stub
				if(e.getPoisDmg() <= (Constants.CONSTITUTION_ENABLE ? 10 : 1) || e.getHealth() <= 0
						|| e instanceof Player && !((Player)e).getSession().isConnected()) {
					this.stop();
					return;
				}
				if(e instanceof Player && (e.getPoisDmg() - (Constants.CONSTITUTION_ENABLE ? 2 : 1)) < (Constants.CONSTITUTION_ENABLE ? 10 : 1)) {
					((Player)e).getActionSender().sendMessage("Your poison begins to ware off...");
					e.setPoisDmg(0);
					this.stop();
				}
				e.inflictDamage(new Hit((e.getPoisDmg() - (Constants.CONSTITUTION_ENABLE ? 2 : 1)), HitType.POISON_DAMAGE), null);
			}
			
		});
	}
	
	/**
     * Gets the poison damage based on the items name.
     * @param item
     * @return
     */
    public static int getPoisonDamg(String item) {
    	item = item.toLowerCase();
        if(item.contains("arrow") || item.contains("knive") || item.contains("bolt") || item.contains("knife")) {
            if(item.contains("(p)"))
                return 2;
            if(item.contains("(p+)"))
                return 3;
            if(item.contains("(p++)"))
                return 4;
        } else if(item.contains("dagger") || item.contains("spear")) {
            if(item.contains("(p)"))
                return 3;
            if(item.contains("(p+)"))
                return 4;
            if(item.contains("(p++)"))
                return 5;
        }
        return 0;
    }
}
