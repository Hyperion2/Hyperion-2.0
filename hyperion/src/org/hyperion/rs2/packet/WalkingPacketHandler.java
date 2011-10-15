package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;

/**
 * A packet which handles walking requests.
 * @author Graham Edgecombe
 *
 */
public class WalkingPacketHandler implements PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		int size = packet.getLength();
		if(packet.getOpcode() == 248) {
		    size -= 14;
		}

		player.getWalkingQueue().reset();
		if(player.getActionQueue().hasAction())
			player.getActionQueue().cancelQueuedActions();
		if(player.getInterfaceState().getCurrentInterface() > 0)
			player.getActionSender().sendRemoveAllInterfaces();
		if(player.getInteractingEntity() != null)
			player.resetInteractingEntity();
		if(!player.getMagicSpells().isEmpty())
			player.getMagicSpells().clear();
		player.setCurrentTarget(null);
		final int steps = (size - 5) / 2;
		final int[][] path = new int[steps][2];

		final int firstX = packet.getLEShortA();
		for (int i = 0; i < steps; i++) {
		    path[i][0] = packet.getByte();
		    path[i][1] = packet.getByte();
		}
		final int firstY = packet.getLEShort();
		final boolean runSteps = packet.getByteC() == 1;
		
		player.getWalkingQueue().setRunningQueue(runSteps);
		player.getWalkingQueue().addStep(firstX, firstY );
		
		for (int i = 0; i < steps; i++) {
		    path[i][0] += firstX;
		    path[i][1] += firstY;
		    player.getWalkingQueue().addStep(path[i][0], path[i][1]);
		}
		player.getWalkingQueue().finish();
	}

}
