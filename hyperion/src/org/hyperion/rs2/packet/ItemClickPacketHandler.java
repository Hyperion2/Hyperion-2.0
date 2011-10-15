package org.hyperion.rs2.packet;

import org.hyperion.rs2.content.Consumable;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;

public class ItemClickPacketHandler implements PacketHandler {

	private static final int OPTION_1 = 122;
	private static final int OPTION_2 = 75;

	@Override
	public void handle(Player player, Packet packet) {
		switch (packet.getOpcode()) {
		case OPTION_1:
			packet.getLEShortA();
			int itemSlot = (packet.getShort() - 128);
			int itemId = packet.getLEShort();
			player.getActionSender().sendMessage("Item: "+itemId+" Slot: "+itemSlot);
			if(player.getInventory().get(itemSlot) == null
					|| player.getInventory().get(itemSlot).getId() != itemId) return;
			Consumable.eatFood(player, itemSlot);
			Consumable.drinkLiquid(player, itemSlot);
			break;
		case OPTION_2:
			
			break;
		}
	}
}
