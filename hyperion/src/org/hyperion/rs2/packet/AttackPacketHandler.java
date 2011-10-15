package org.hyperion.rs2.packet;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.net.Packet;

public class AttackPacketHandler implements PacketHandler {

	final int NPC = 72;
	
	@Override
	public void handle(Player player, Packet packet) {
		// TODO Auto-generated method stub
		int index = -1;
		
		switch(packet.getOpcode()) {
		
		case NPC:
			index = packet.getShortA();
			if(index < 0 || index > Constants.MAX_NPCS) return;
			NPC n = (NPC) World.getWorld().getNPCs().get(index);
			player.setCurrentTarget(n);
			break;
			
		}
	}

}
