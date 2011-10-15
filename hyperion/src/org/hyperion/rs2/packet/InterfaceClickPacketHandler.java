package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Bank;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.model.container.Shops;
import org.hyperion.rs2.model.container.Trade;
import org.hyperion.rs2.net.Packet;

/**
 * Remove item options.
 * @author Graham Edgecombe
 *
 */
public class InterfaceClickPacketHandler implements PacketHandler {
	
	/**
	 * Option 1 opcode.
	 */
	private static final int FIRST_CLICK = 145;
	
	/**
	 * Option 2 opcode.
	 */
	private static final int SECOND_CLICK = 117;
	
	/**
	 * Option 3 opcode.
	 */
	private static final int THIRD_CLICK = 43;
	
	/**
	 * Option 4 opcode.
	 */
	private static final int FOURTH_CLICK = 129;
	
	/**
	 * Option 5 opcode.
	 */
	private static final int FIFTH_CLICK = 135;

	@Override
	public void handle(Player player, Packet packet) {
		switch(packet.getOpcode()) {
		case FIRST_CLICK:
			firstClickOption(player, packet);
			break;
		case SECOND_CLICK:
			secondClickOption(player, packet);
			break;
		case THIRD_CLICK:
			thirdClickOption(player, packet);
			break;
		case FOURTH_CLICK:
			fourthClickOption(player, packet);
			break;
		case FIFTH_CLICK:
			fifthClickOption(player, packet);
			break;
		}
	}

	/**
	 * Handles item option 1.
	 * @param player The player.
	 * @param packet The packet.
	 */
	private void firstClickOption(Player player, Packet packet) {
		int interfaceId = packet.getShortA() & 0xFFFF;
		int slot = packet.getShortA() & 0xFFFF;
		int id = packet.getShortA() & 0xFFFF;
		
		switch(interfaceId) {
		case Equipment.INTERFACE:
			if(slot >= 0 && slot < Equipment.SIZE) { 
				if(!Container.transfer(player.getEquipment(), player.getInventory(), slot, id)) {
					// indicate it failed
				}
			}
			break;
		case Bank.PLAYER_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Bank.deposit(player, slot, id, 1);
			}
			break;
		case Bank.BANK_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Bank.SIZE) {
				Bank.withdraw(player, slot, id, 1);
			}
			break;
		case Trade.PLAYER_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Trade.addItemToOffer(player, slot, 1);
			}
			break;
		case Trade.TRADE_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Trade.removeItemFromOffer(player, slot, 1);
			}
			break;
		case Shops.PLAYER_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Shops.sendPrice(player, slot, false);
			}
			break;
		case Shops.SHOP_INTERFACE:
			if(slot >= 0 && slot < Shops.SIZE) {
				Shops.sendPrice(player, slot, true);
			}
			break;
		}
		System.out.println("Interface: "+interfaceId+" Slot: "+slot+" id: "+id);
	}
	
	/**
	 * Handles item option 2.
	 * @param player The player.
	 * @param packet The packet.
	 */
	private void secondClickOption(Player player, Packet packet) {
		int interfaceId = packet.getLEShortA() & 0xFFFF;
		int id = packet.getLEShortA() & 0xFFFF;
		int slot = packet.getLEShort() & 0xFFFF;
		switch(interfaceId) {
		case Bank.PLAYER_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Bank.deposit(player, slot, id, 5);
			}
			break;
		case Bank.BANK_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Bank.SIZE) {
				Bank.withdraw(player, slot, id, 5);
			}
			break;
		case Shops.PLAYER_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Shops.sellToShop(player, slot, 1);
			}
			break;
		case Shops.SHOP_INTERFACE:
			if(slot >= 0 && slot < Shops.SIZE) {
				Shops.buyFromShop(player, slot, 1);
			}
			break;
		case Trade.PLAYER_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Trade.addItemToOffer(player, slot, 5);
			}
			break;
		case Trade.TRADE_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Trade.removeItemFromOffer(player, slot, 5);
			}
			break;
		}
		System.out.println("Interface: "+interfaceId+" Slot: "+slot+" id: "+id);
	}
	
	/**
	 * Handles item option 3.
	 * @param player The player.
	 * @param packet The packet.
	 */
	private void thirdClickOption(Player player, Packet packet) {
		int interfaceId = packet.getLEShort() & 0xFFFF;
		int id = packet.getShortA() & 0xFFFF;
		int slot = packet.getShortA() & 0xFFFF;
		
		switch(interfaceId) {
		case Bank.PLAYER_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Bank.deposit(player, slot, id, 10);
			}
			break;
		case Bank.BANK_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Bank.SIZE) {
				Bank.withdraw(player, slot, id, 10);
			}
			break;
		case Shops.PLAYER_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Shops.sellToShop(player, slot, 5);
			}
			break;
		case Shops.SHOP_INTERFACE:
			if(slot >= 0 && slot < Shops.SIZE) {
				Shops.buyFromShop(player, slot, 5);
			}
			break;
		case Trade.PLAYER_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Trade.addItemToOffer(player, slot, 10);
			}
			break;
		case Trade.TRADE_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Trade.removeItemFromOffer(player, slot, 10);
			}
			break;
		}
	}
	
	/**
	 * Handles item option 4.
	 * @param player The player.
	 * @param packet The packet.
	 */
	private void fourthClickOption(Player player, Packet packet) {
		int slot = packet.getShortA() & 0xFFFF;
		int interfaceId = packet.getShort() & 0xFFFF;
		int id = packet.getShortA() & 0xFFFF;
		
		switch(interfaceId) {
		case Bank.PLAYER_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Bank.deposit(player, slot, id, player.getInventory().getCount(player.getInventory().get(slot)));
			}
			break;
		case Bank.BANK_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Bank.SIZE) {
				Bank.withdraw(player, slot, id, player.getBank().getCount(player.getBank().get(slot)));
			}
			break;
		case Shops.PLAYER_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Shops.sellToShop(player, slot, 10);
			}
			break;
		case Shops.SHOP_INTERFACE:
			if(slot >= 0 && slot < Shops.SIZE) {
				Shops.buyFromShop(player, slot, 10);
			}
			break;
		case Trade.PLAYER_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Trade.addItemToOffer(player, slot, player.getInventory().getCount(player.getInventory().get(slot)));
			}
			break;
		case Trade.TRADE_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Trade.removeItemFromOffer(player, slot, player.getOther().getCount(player.getOther().get(slot)));
			}
			break;
		}
	}
	
	/**
	 * Handles item option 5.
	 * @param player The player.
	 * @param packet The packet.
	 */
	private void fifthClickOption(Player player, Packet packet) {
		int slot = packet.getLEShort() & 0xFFFF;
		int interfaceId = packet.getShortA() & 0xFFFF;
		int id = packet.getLEShort() & 0xFFFF;
		
		switch(interfaceId) {
		case Bank.PLAYER_INVENTORY_INTERFACE:
		case Trade.PLAYER_INVENTORY_INTERFACE:
		case Trade.TRADE_INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				player.getInterfaceState().openEnterAmountInterface(interfaceId, slot, id);
			}
			break;
		case Bank.BANK_INVENTORY_INTERFACE:
			if(slot >= 0 && slot < Bank.SIZE) {
				player.getInterfaceState().openEnterAmountInterface(interfaceId, slot, id);
			}
			break;
		}
	}

}
