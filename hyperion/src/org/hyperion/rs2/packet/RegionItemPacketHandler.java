package org.hyperion.rs2.packet;

import org.hyperion.rs2.action.impl.PickupAction;
import org.hyperion.rs2.event.impl.FloorItemEvent;
import org.hyperion.rs2.model.Definitions;
import org.hyperion.rs2.model.FloorItem;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.net.Packet;

public class RegionItemPacketHandler implements PacketHandler {

	public static final int DROP = 87, PICKUP = 236;
	
	@Override
	public void handle(Player player, Packet packet) {
		// TODO Auto-generated method stub
		switch(packet.getOpcode()) {
		
			case DROP:
				int item = packet.getShortA();
				int inter = packet.getShort();
				int slot = packet.getShortA();
				if(inter != Inventory.INTERFACE) return;
				if(player.getInventory().get(slot) == null || player.getInventory().get(slot).getId() != item) return;
				if(!Definitions.forId(item).isTradable()) {
					player.getActionSender().sendMessage("This item is nontradable. Therefore you cannot drop it, use ::empty to remove it.");
					return;
				}
				FloorItemEvent.addFloorItem(new FloorItem(item, player.getInventory().get(slot).getCount(), player.getLocation(), player, player, false));
				player.getInventory().set(slot, null);
				break;
				
			case PICKUP:
				int y = packet.getLEShort();
				int id = packet.getShort();
				int x = packet.getLEShort();
				int z = player.getLocation().getZ();
				FloorItem fl = null;
				player.getActionQueue().clearNonWalkableActions();
				for(FloorItem f : FloorItemEvent.getFloorItem()) {
					if(f.getItem() == id && !f.isTaken() && f.getLoc().getX() == x && f.getLoc().getY() == y
							&& f.getLoc().getZ() == z) {
						fl = f;
						break;
					}
				}
				if(fl == null) return;
				player.getActionQueue().addAction(new PickupAction(player, fl));
				break;
				
		}
	}

}
