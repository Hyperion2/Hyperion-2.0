package org.hyperion.rs2.task.impl;

import org.hyperion.rs2.GameEngine;
import org.hyperion.rs2.content.combat.Combat;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.UpdateFlags.UpdateFlag;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.net.PacketManager;
import org.hyperion.rs2.task.Task;

/**
 * A task which is executed before an <code>UpdateTask</code>. It is similar to
 * the call to <code>process()</code> but you should use <code>Event</code>s
 * instead of putting timers in this class.
 * @author Graham Edgecombe
 *
 */
public class PlayerTickTask implements Task {
	
	/**
	 * The player.
	 */
	private Player player;
	
	/**
	 * Creates a tick task for a player.
	 * @param player The player to create the tick task for.
	 */
	public PlayerTickTask(Player player) {
		this.player = player;
	}

	@Override
	public void execute(GameEngine context) {
		
		Packet packet = null;
		
		/*
		 * Queue Packet - Sync Execution.
		 */
		while ((packet = player.getQueuedPackets().poll()) != null) {
			PacketManager.getPacketManager().handle(player.getSession(), packet);
		}
		
		/*
		 * Message Queue
		 */
		if(player.getChatMessageQueue().size() > 0) {
			player.getUpdateFlags().flag(UpdateFlag.CHAT);
			player.setCurrentChatMessage(player.getChatMessageQueue().poll());
		} else {
			player.setCurrentChatMessage(null);
		}
		
		/*
		 * Magic Cast Delay.
		 */
		if(player.getAbstractMagicDelay() > 0)
			player.setAbstractMagicDelay(player.getAbstractMagicDelay() - 1);
		
		/*
		 * Food Delay.
		 */
		if(player.getFoodTimer() > 0)
			player.setFoodTimer((byte) (player.getFoodTimer() - 1));
		
		/*
		 * Potion Delay.
		 */
		if(player.getPotionTimer() > 0)
			player.setPotionTimer((byte) (player.getPotionTimer() - 1));
		
		/*
		 * Combat Delay.
		 */
		if(player.getLastAttack() > 0)
			player.setLastAttack(player.getLastAttack() - 1);
		
		/*
		 * Combat Timer.
		 */
		if(player.getCombatTimer() > 0)
			player.setCombatTimer(player.getCombatTimer() - 1);
		
		/*
		 * Combat.
		 */
		if(player.getCurrentTarget() != null)
			Combat.execute(player);
		
		/*
		 * Running
		 * Make sure player is not running.
		 */
		if(player.getSprites().getSecondarySprite() == -1 && player.getWalkingQueue().getRunEnergy() < 10000) {
			int regain = (int) (25 + player.getSkills().getLevel(Skills.AGILITY) * 1.81);
			player.getWalkingQueue().setRunEnergy(player.getWalkingQueue().getRunEnergy()+regain);
			player.getActionSender().sendEnergy(player.getWalkingQueue().getRunEnergy());
		}
		
		/*
		 * Process the next movement in the Player's walking queue.
		 */
		player.getWalkingQueue().processNextMovement();
	}

}
