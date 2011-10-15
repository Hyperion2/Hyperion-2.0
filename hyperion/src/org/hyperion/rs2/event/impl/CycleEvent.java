package org.hyperion.rs2.event.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.task.ConsecutiveTask;
import org.hyperion.rs2.task.ParallelTask;
import org.hyperion.rs2.task.Task;

/**
 * An event which starts player update tasks.
 * @author Graham Edgecombe
 *
 */
public class CycleEvent extends Event {

	/**
	 * The cycle time, in milliseconds.
	 */
	public static final int CYCLE_TIME = 600;
	
	/**
	 * Creates the update event to cycle every 600 milliseconds.
	 */
	public CycleEvent() {
		super(CYCLE_TIME);
	}

	@Override
	public void execute() {
		if(World.getWorld().getPlayers().size() == 0) return;
		List<Task> tickTasks = new ArrayList<Task>();
		List<Task> updateTasks = new ArrayList<Task>();
		List<Task> resetTasks = new ArrayList<Task>();
		
		for(NPC npc : World.getWorld().getNPCs()) {
			if(npc.isInvisible()) continue;
			tickTasks.add(npc.getTickTask());
			resetTasks.add(npc.getResetTask());
		}
		
		Iterator<Player> it$ = World.getWorld().getPlayers().iterator();
		while(it$.hasNext()) {
			Player player = it$.next();
			if(!player.getSession().isConnected()) {
				it$.remove();
			} else {
				tickTasks.add(player.getTickTask());
				updateTasks.add(player.getUpdateTask());
				resetTasks.add(player.getResetTask());
			}
		}
		
		// ticks can no longer be parallel due to region code
		Task tickTask = new ConsecutiveTask(tickTasks.toArray(new Task[0]));
		Task updateTask = new ParallelTask(updateTasks.toArray(new Task[0]));
		Task resetTask = new ParallelTask(resetTasks.toArray(new Task[0]));
		
		World.getWorld().submit(new ConsecutiveTask(tickTask, updateTask, resetTask));
	}

}
