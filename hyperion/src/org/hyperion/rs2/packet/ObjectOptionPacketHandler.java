package org.hyperion.rs2.packet;

import org.hyperion.rs2.action.impl.ObjectInteractAction;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;

/**
 * Object option packet handler.
 * @author Graham Edgecombe
 *
 */
public class ObjectOptionPacketHandler implements PacketHandler {
	
	/**
	 * Option 1 opcode.
	 */
	private static final int OPTION_1 = 132, OPTION_2 = 252;

	@Override
	public void handle(Player player, Packet packet) {
		switch(packet.getOpcode()) {
		case OPTION_1:
			handleOption1(player, packet);
			break;
		case OPTION_2:
			handleOption2(player, packet);
			break;
		}
	}

	/**
	 * Handles the option 1 packet.
	 * @param player The player.
	 * @param packet The packet.
	 */
	private void handleOption1(Player player, Packet packet) {
		int x = packet.getLEShortA() & 0xFFFF;
		int id = packet.getShort() & 0xFFFF;
		int y = packet.getShortA() & 0xFFFF;
		Location loc = Location.create(x, y, player.getLocation().getZ());
		// woodcutting
		player.getActionQueue().cancelQueuedActions();
		player.getActionQueue().addAction(new ObjectInteractAction(player, id, loc, 1));
	}
	
    /**
     * Handles the option 2 packet.
     * @param player The player.
     * @param packet The packet.
     */
    private void handleOption2(Player player, Packet packet) {        
        int id = packet.getLEShortA() & 0xFFFF;
        int y = packet.getLEShort() & 0xFFFF;
        int x = packet.getShortA() & 0xFFFF;
        Location loc = Location.create(x, y, player.getLocation().getZ());
        player.getActionQueue().addAction(new ObjectInteractAction(player, id, loc, 2));
    }


}
