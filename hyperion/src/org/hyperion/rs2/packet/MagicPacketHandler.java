package org.hyperion.rs2.packet;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.content.combat.impl.Magic;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.net.Packet;

public class MagicPacketHandler implements PacketHandler {

	final int MAGIC_ON_NPC = 131, MAGIC_ON_PLAYER = 249;
	
	@Override
	public void handle(Player player, Packet packet) {
		// TODO Auto-generated method stub
		int id = -1, spell = -1;
		switch(packet.getOpcode()) {
			
		case MAGIC_ON_NPC:
			id = packet.getLEShortA();
			if(id < 0 || id >= Constants.MAX_NPCS) {
				return;
			}
            spell = packet.getShortA();
            
            player.setInteractingEntity(World.getWorld().getNPCs().get(id));
            
            if(player.getMagicSpells().size() >= 3)
				player.getMagicSpells().poll();
            
            player.getMagicSpells().add(spell);
            
            if(player.getMagicSpells().size() > 1) return;
            
			Magic.execute(player, World.getWorld().getNPCs().get(id));
			break;
			
		case MAGIC_ON_PLAYER:
			id = packet.getShortA();
			if(id < 0 || id >= Constants.MAX_PLAYERS) {
				return;
			}
            spell = packet.getLEShort();
            
            player.setInteractingEntity(World.getWorld().getPlayers().get(id));
            
            if(player.getMagicSpells().size() >= 3) {
				player.getMagicSpells().poll();
            }
            
            player.getMagicSpells().add(spell);
            
            if(player.getMagicSpells().size() > 1) return;
            
            System.out.println("execute: "+spell);
            Magic.execute(player, World.getWorld().getPlayers().get(id));
			break;
		
		}
	}

}
