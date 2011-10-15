package org.hyperion.rs2.model.container.impl;

import org.hyperion.rs2.content.combat.WeaponInfo;
import org.hyperion.rs2.content.combat.WeaponStyle;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Container;
import org.hyperion.rs2.model.container.ContainerListener;
import org.hyperion.rs2.model.container.Equipment;

/**
 * A listener which updates the weapon tab.
 * @author Graham Edgecombe
 *
 */
public class WeaponContainerListener implements ContainerListener {
	
	/**
	 * The player.
	 */
	private Player player;

	/**
	 * Creates the listener.
	 * @param player The player.
	 */
	public WeaponContainerListener(Player player) {
		this.player = player;
	}

	@Override
	public void itemChanged(Container container, int slot) {
		Item item = player.getEquipment().get(slot);
		if(slot == Equipment.SLOT_WEAPON)
			sendWeapon();
		if(slot == Equipment.SLOT_SHIELD)
			WeaponInfo.setDefendEmote(player);
		//player.getActionSender().sendBonuses();
		if(slot == Equipment.SLOT_ARROWS)
			player.setAmmoUsable(hasUsableAmmo(item != null ? item.getId() : -1));
	}

	@Override
	public void itemsChanged(Container container, int[] slots) {
		for(int slot : slots) {
			Item item = player.getEquipment().get(slot);
			if(slot == Equipment.SLOT_WEAPON)
				sendWeapon();
			else if(slot == Equipment.SLOT_SHIELD)
				WeaponInfo.setDefendEmote(player);
			else if(slot == Equipment.SLOT_ARROWS)
				player.setAmmoUsable(hasUsableAmmo(item != null ? item.getId() : -1));
		}
		//player.getActionSender().sendBonuses();
	}

	@Override
	public void itemsChanged(Container container) {
		sendWeapon();
	//player.getActionSender().sendBonuses();
	}
	
	/**
	 * Sends weapon information.
	 */
	private void sendWeapon() {
		Item weapon = player.getEquipment().get(Equipment.SLOT_WEAPON);
		int id = -1;
		String name = null;
		if(weapon == null) {
			name = "unarmed";
		} else {
			name = weapon.getDefinition().getName();
			id = weapon.getId();
		}
		sendWeapon(id, name.toLowerCase());
		WeaponInfo.setAttackEmote(player);
		WeaponInfo.setDefendEmote(player);
		player.getActionSender().sendConfig(107, player.getFightStyle());
		player.setAmmoUsable(hasUsableAmmo(id));
	}
	
	/**
	 * Determines if usable ammo depending on the bow.
	 * @return
	 */
	public boolean hasUsableAmmo(int bowId) {
		int arrowId = player.getEquipment().get(Equipment.SLOT_ARROWS) != null ? player.getEquipment().get(Equipment.SLOT_ARROWS).getId() : -1;
		//magic bows
		if(bowId == 861 || bowId == 859)
			if(arrowId == -1 || arrowId == 11212)//empty or
				return false;
		//regular bows
		if(bowId == 839 || bowId == 841)
			if(arrowId != 882 && arrowId != 884)
				return false;
		//oak bows
		if(bowId == 843 || bowId == 845)
			if(!(arrowId >= 882 && arrowId <= 886))
				return false;
		//willow bow
		if(bowId == 847 || bowId == 849)
			if(!(arrowId >= 882 && arrowId <= 888))
				return false;
		//maple bows
		if(bowId == 851 || bowId == 853)
			if(!(arrowId >= 882 && arrowId <= 890))
				return false;
		//Yew bows
		if(bowId == 855 || bowId == 857)
			if(!(arrowId >= 882 && arrowId <= 892))
				return false;
		//Other bows
		if(bowId == 11235 || bowId == 19143 || bowId == 19146 || bowId == 19149)
			if(arrowId == -1)
				return false;
		return true;
	}

	/**
	 * Sends weapon information.
	 * @param id The id.
	 * @param name The name.
	 * @param genericName The filtered name.
	 */
	private void sendWeapon(int id, String name) {
		if(name.equals("unarmed")) {
			player.getActionSender().sendSidebarInterface(0, 5855);
			player.getActionSender().sendString(5857, name);
			player.setFightIndex((byte) 0);
		} else if(name.endsWith("whip")) {
			player.getActionSender().sendSidebarInterface(0, 12290);
			player.getActionSender().sendInterfaceModel(12291, 200, id);
			player.getActionSender().sendString(12293, name);
			player.setFightIndex((byte) 11);
		} else if(name.endsWith("scythe")) {
			player.getActionSender().sendSidebarInterface(0, 776);
			player.getActionSender().sendInterfaceModel(777, 200, id);
			player.getActionSender().sendString(779, name);
		} else if(name.endsWith("bow") || name.startsWith("toktz-xil-ul")) {
			player.getActionSender().sendSidebarInterface(0, 1764);
			player.getActionSender().sendInterfaceModel(1765, 200, id);
			player.getActionSender().sendString(1767, name);
			player.setFightIndex((byte) 16);
		} else if(name.startsWith("staff") || name.endsWith("staff")) {
			player.getActionSender().sendSidebarInterface(0, 328);
			player.getActionSender().sendInterfaceModel(329, 200, id);
			player.getActionSender().sendString(331, name);
			player.setFightIndex((byte) 1);
		} else if(name.contains("dart") || name.contains("knife")) {
			player.getActionSender().sendSidebarInterface(0, 4446);
			player.getActionSender().sendInterfaceModel(4447, 200, id);
			player.getActionSender().sendString(4449, name);
			player.setFightIndex((byte) 17);
		} else if(name.contains("dagger")) {
			player.getActionSender().sendSidebarInterface(0, 2276);
			player.getActionSender().sendInterfaceModel(2277, 200, id);
			player.getActionSender().sendString(2279, name);
			player.setFightIndex((byte) 5);
		} else if(name.contains("pickaxe")) {
			player.getActionSender().sendSidebarInterface(0, 5570);
			player.getActionSender().sendInterfaceModel(5571, 200, id);
			player.getActionSender().sendString(5573, name);
			player.setFightIndex((byte) 4);
		} else if(name.contains("hatchet") || name.contains("battleaxe")) {
			player.getActionSender().sendSidebarInterface(0, 1698);
			player.getActionSender().sendInterfaceModel(1699, 200, id);
			player.getActionSender().sendString(1701, name);
			player.setFightIndex((byte) 2);
		} else if(name.contains("halberd")) {
			player.getActionSender().sendSidebarInterface(0, 8460);
			player.getActionSender().sendInterfaceModel(8461, 200, id);
			player.getActionSender().sendString(8463, name);
			player.setFightIndex((byte) 15);
		} else {
			player.getActionSender().sendSidebarInterface(0, 2423);
			player.getActionSender().sendInterfaceModel(2424, 200, id);
			player.getActionSender().sendString(2426, name);
			player.setFightIndex((byte) 6);
		}
		WeaponStyle.getType(player);
	}

}
