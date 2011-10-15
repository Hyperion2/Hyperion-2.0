package org.hyperion.rs2.packet;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.action.impl.PlayerOptionAction;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.net.Packet;

public class PlayerOptionPacketHandler implements PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		int id = -1;
		Player victim = null;
		System.out.println("Opcode: "+packet.getOpcode());
		switch(packet.getOpcode()) {
		case 139:
			/*
			 * Option 1.
			 */
			id = packet.getLEShort() & 0xFFFF;
			player.getActionQueue().cancelQueuedActions();
			if(id < 0 || id >= Constants.MAX_PLAYERS) {
				return;
			}
			victim = (Player) World.getWorld().getPlayers().get(id);
			if(victim == null) return;
			player.getActionQueue().addAction(new PlayerOptionAction(player, victim, 1));
			break;
			
		case 73://Follow
			/*
			 * Option 2.
			 */
			id = packet.getLEShort() & 0xFFFF;
			player.getActionQueue().cancelQueuedActions();
			if(id < 0 || id >= Constants.MAX_PLAYERS) {
				return;
			}
			victim = (Player) World.getWorld().getPlayers().get(id);
			if(victim == null) return;
			player.getActionQueue().addAction(new PlayerOptionAction(player, victim, 2));
			break;
		}
	}

}

