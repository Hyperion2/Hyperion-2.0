package org.hyperion.rs2.packet;

import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Graphic;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Skills;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.model.container.Bank;
import org.hyperion.rs2.model.npcs.npc;
import org.hyperion.rs2.model.region.Region;
import org.hyperion.rs2.net.Packet;

/**
 * Handles player commands (the ::words).
 * @author Graham Edgecombe
 *
 */
public class CommandPacketHandler implements PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		String commandString = packet.getRS2String();
		String[] args = commandString.split(" ");
		String command = args[0].toLowerCase();
	//	player.getUpdateFlags().flag(UpdateFlag.CHAT);
		try {
			if(command.equals("tele")) {
				if(args.length == 3 || args.length == 4) {
					int x = Integer.parseInt(args[1]);
					int y = Integer.parseInt(args[2]);
					int z = player.getLocation().getZ();
					if(args.length == 4) {
						z = Integer.parseInt(args[3]);
					}
					player.setTeleportTarget(Location.create(x, y, z));
				} else {
					player.getActionSender().sendMessage("Syntax is ::tele [x] [y] [z].");
				}
			} else if(command.equals("pos")) {
				player.getActionSender().sendMessage("You are at: " + player.getLocation() + ".");
			} else if(command.equals("npc")) {
				World.getWorld().register(new npc(Integer.valueOf(args[1])));
			} else if(command.contains("region")) {
				Region r = player.getRegion();
				player.getActionSender().sendMessage("Region: "+r);
				player.getActionSender().sendMessage("X: "+r.getCoordinates().getX()+" Y: "+r.getCoordinates().getY());
				player.getActionSender().sendMessage("ItemCount: "+r.getRegionItems().size());
				player.getActionSender().sendMessage("Entity Count: "+r.getPlayers().size()+" NPC: "+r.getNpcs().size());
			} else if(command.equals("chatinfo")) {
				String[] s = new String[Integer.valueOf(args[1])];
				for(int i = 0; i < s.length; i++)
					s[i] = ""+i;
				player.getActionSender().sendChatInfoInterface(s);
			} else if(command.equals("config")) {
				player.getActionSender().sendConfig(Integer.valueOf(args[1]), Integer.valueOf(args[2]));
			} else if(command.equals("item")) {
				if(args.length >= 2 && args.length <= 4) {
					int id = Integer.parseInt(args[1]);
					int count = 1;
					int hit = 0;
					if(args.length >= 3)
						count = Integer.parseInt(args[2]);
					if(args.length == 4)
						hit = Integer.parseInt(args[3]);
					player.getInventory().add(new Item(id, count, hit));
					player.getActionSender().sendMessage("Item: "+id+" Amount: "+count+" Degrade: "+hit+" spawned.");
				} else {
					player.getActionSender().sendMessage("Syntax is ::item [id] [count] [degrade].");
				}
			} else if(command.equals("anim")) {
				if(args.length == 2 || args.length == 3) {
					int id = Integer.parseInt(args[1]);
					int delay = 0;
					if(args.length == 3) {
						delay = Integer.parseInt(args[2]);
					}
					player.playAnimation(Animation.create(id, delay));
				}
			} else if(command.equals("gfx")) {
				if(args.length == 2 || args.length == 3) {
					int id = Integer.parseInt(args[1]);
					int delay = 0;
					if(args.length == 3) {
						delay = Integer.parseInt(args[2]);
					}
					player.playGraphics(Graphic.create(id, delay));
				}
			} else if(command.equals("bank")) {
				Bank.open(player);
			} else if(command.equals("max")) {
				for(int i = 0; i <= Skills.SKILL_COUNT; i++) {
					player.getSkills().setLevel(i, 99);
					player.getSkills().setExperience(i, 13034431);
				}
			} else if(command.startsWith("empty")) {
				player.getInventory().clear();
				player.getActionSender().sendMessage("Your inventory has been emptied.");
			} else if(command.startsWith("lvl")) {
				try {
					player.getSkills().setLevel(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
					player.getSkills().setExperience(Integer.parseInt(args[1]), player.getSkills().getXPForLevel(Integer.parseInt(args[2])) + 1);
					player.getActionSender().sendMessage(Skills.SKILL_NAME[Integer.parseInt(args[1])] + " level is now " + Integer.parseInt(args[2]) + ".");	
				} catch(Exception e) {
					e.printStackTrace();
					player.getActionSender().sendMessage("Syntax is ::lvl [skill] [lvl].");				
				}
			} 
		} catch(Exception ex) {
			ex.printStackTrace();
			player.getActionSender().sendMessage("Error while processing command.");
		}
	}

}
