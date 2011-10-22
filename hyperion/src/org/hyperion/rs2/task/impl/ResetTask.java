package org.hyperion.rs2.task.impl;

import org.hyperion.rs2.GameEngine;
import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.task.Task;

/**
 * A task which resets an NPC after an update cycle.
 * @author Graham Edgecombe
 *
 */
public class ResetTask implements Task {
	
	/**
	 * The npc to reset.
	 */
	private Entity r;
	
	/**
	 * Creates the reset task.
	 * @param npc The npc to reset.
	 */
	public ResetTask(Entity o) {
		this.r = o;
	}

	@Override
	public void execute(GameEngine context) {
		r.getUpdateFlags().reset();
		r.setTeleporting(false);
		r.setMapRegionChanging(false);
		if(r instanceof Player) {
			Player player = (Player)r;
			player.resetTeleportTarget();
			//player.resetCachedUpdateBlock();
		}
		r.reset();
	}

}
