package org.hyperion.rs2.packet;

import org.hyperion.rs2.content.combat.WeaponInfo;
import org.hyperion.rs2.model.Definitions;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.container.Equipment;
import org.hyperion.rs2.model.container.Equipment.EquipmentType;
import org.hyperion.rs2.model.container.Inventory;
import org.hyperion.rs2.net.Packet;

/**
 * Handles the 'wield' option on items.
 * @author Graham Edgecombe
 *
 */
public class WieldPacketHandler implements PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		int itemId = packet.getShort() & 0xFFFF;
		int slot = packet.getShortA() & 0xFFFF;
		int interfaceId = packet.getShortA() & 0xFFFF;
		if(player.isTeleporting()) return;
		switch(interfaceId) {
		
		case Inventory.INTERFACE:
			if(slot >= 0 && slot < Inventory.SIZE) {
				Item item = player.getInventory().get(slot);
				if(item != null && item.getId() == itemId) {
					EquipmentType type = Definitions.forId(itemId).getEquipmentType();
					if(player.getInventory().get(slot) == null || player.getInventory().get(slot).getId() != itemId) return;
					for(int[] req : Definitions.forId(itemId).getSkillReq()) {
						if(player.getSkills().getLevelForExperience(req[0]) < req[1]) {
							player.getActionSender().sendMessage("You need a "+Skills.SKILL_NAME[req[0]]+" level of at least "+req[1]+" to "+(Definitions.forId(itemId).getEquipmentType().getSlot() == Equipment.SLOT_WEAPON ? "wield this." : "wear this."));
							return;
						}
					}
					player.getMagicSpells().clear();
					Item equip = player.getInventory().get(slot);
					Item oldEquip = player.getEquipment().get(type.getSlot());
					boolean twoHanded = WeaponInfo.isItemTwoHanded(equip) 
					|| type.getSlot() == Equipment.SLOT_SHIELD && WeaponInfo.isItemTwoHanded(player.getEquipment().get(Equipment.SLOT_WEAPON));
					if(twoHanded) {
						int slotToRemove = type.getSlot() == Equipment.SLOT_WEAPON ? Equipment.SLOT_SHIELD : Equipment.SLOT_WEAPON;
						if(player.getEquipment().get(slotToRemove) != null) {
							if(player.getInventory().freeSlots() > 1) {
								player.getEquipment().set(slotToRemove, null);
								player.getInventory().add(player.getEquipment().get(slotToRemove));
							} else {
								player.getActionSender().sendMessage("You do not have enough room in your inventory.");
								return;
							}
						}
					}
					if(oldEquip != null && oldEquip.getId() == itemId && equip.getDefinition().isStackable()) {
						//If used swap equipment.
						//equip = new Item(itemId, equip.getCount() + oldEquip.getCount());
						equip.setCount(equip.getCount() + oldEquip.getCount());
						oldEquip = null;
					}
					//Else just add.
					player.getInventory().set(slot, oldEquip);
					player.getEquipment().set(type.getSlot(), equip);
				}
			}
			break;
		}
	}

}
