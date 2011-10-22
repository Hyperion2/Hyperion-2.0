package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.net.Packet;

public class DialoguePacketHandler implements PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		// TODO Auto-generated method stub
		player.getActionSender().sendRemoveAllInterfaces();
	}

}
