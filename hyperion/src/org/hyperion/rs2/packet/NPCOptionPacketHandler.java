package org.hyperion.rs2.packet;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.action.impl.FishingAction;
import org.hyperion.rs2.action.impl.FishingAction.SPOTS;
import org.hyperion.rs2.action.impl.NPCInteractAction;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.net.Packet;

public class NPCOptionPacketHandler implements PacketHandler {

	public final int FIRST_CLICK = 155, SECOND_CLICK = 17, THIRD_CLICK = 21;
	
	@Override
	public void handle(Player player, Packet packet) {
		// TODO Auto-generated method stub
		int slot = -1;
		NPC npc = null;
		switch(packet.getOpcode())
		{
			case FIRST_CLICK:
				slot = packet.getLEShort();
				if(slot < 0 || slot > Constants.MAX_NPCS)return;
				npc = (NPC) World.getWorld().getNPCs().get(slot);
				player.getActionQueue().cancelQueuedActions();
				if(SPOTS.getSpot(npc.getId()) != null)
					player.getActionQueue().addAction(new FishingAction(player, npc.getLocation(), SPOTS.getSpot(npc.getId()).options[0]));
				else
					player.getActionQueue().addAction(new NPCInteractAction(player, npc, 1));
			break;

            case SECOND_CLICK:
				slot = packet.getLEShortA();
				if(slot < 0 || slot > Constants.MAX_NPCS)return;
				npc = (NPC) World.getWorld().getNPCs().get(slot);
				if(SPOTS.getSpot(npc.getId()) != null && FishingAction.SPOTS.getSpot(npc.getId()).options[1] != null)
					player.getActionQueue().addAction(new FishingAction(player, npc.getLocation(), SPOTS.getSpot(npc.getId()).options[1]));
				else
					player.getActionQueue().addAction(new NPCInteractAction(player, npc, 2));
			break;

            case THIRD_CLICK:
				slot = packet.getShort();
				if(slot < 0 || slot > Constants.MAX_NPCS)return;
				npc = (NPC) World.getWorld().getNPCs().get(slot);
				player.getActionQueue().addAction(new NPCInteractAction(player, npc, 3));
			break;
		}
	}

}
