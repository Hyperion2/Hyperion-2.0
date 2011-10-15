package org.hyperion.rs2.content.skills;

import org.hyperion.rs2.model.Player;

public class Prayer {

	/**
	 * Info of the Prayers.
	 */
	public static final int[][][] PRAYER = {
		{
			
		},
		{//Leave This blank! Unless using Ancient Curses
			
		}
	};
	
	/**
	 * Resets the prayers.
	 * @param p The player.
	 */
	public static void resetPrayers(Player p, boolean disable) {
		for(int i = 0; i < p.getPrayers().length; i++) {
			if(p.getPrayers()[i]) {
				p.getPrayers()[i] = false;
				p.getActionSender().sendConfig(PRAYER[p.getPrayBook()][i][0], 0);
			}
		}
		if(disable)
			p.getActionSender().sendMessage("Your prayer's have been disabled.");
	}
}
