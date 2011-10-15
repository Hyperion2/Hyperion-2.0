package org.hyperion.rs2.packet;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.net.Packet;
import org.hyperion.rs2.util.NameUtils;

public class PrivateMessagePacketHandler implements PacketHandler {

	public static final int ADD_FRIEND = 188, MESSAGE = 126;
	
	@Override
	public void handle(Player player, Packet packet) {
		// TODO Auto-generated method stub
		switch(packet.getOpcode()) {
		
		case ADD_FRIEND:
			long add = packet.getLong();
			String name = NameUtils.formatNameForProtocol(NameUtils.longToName(add));
			if(player.getFriends().contains(add)) {
				player.getActionSender().sendMessage("This user is already on your friends list.");
				return;
			}
			if(add == player.getNameAsLong()) {
				player.getActionSender().sendMessage("You cannot add yourself to the friendslist.");
				return;
			}
			player.getFriends().add(add);
			if(Constants.CONNNECT_TO_LOGIN_SERVER) {
				World.getWorld().getLoginServerConnector().sendAddFriend(player, name);
			} else {
				for(Player p : World.getWorld().getPlayers()) {
					if(p.getNameAsLong() == add) {
						if(p.getSettings().getPrivateChatSetting()[1] == 0 ||
								p.getSettings().getPrivateChatSetting()[1] == 1 && p.getFriends().contains(add)) {
							player.getActionSender().sendPrivateMessageStatus(add, (byte) World.getWorld().getWorldId());
							return;
						}
					}
				}
				player.getActionSender().sendPrivateMessageStatus(add, (byte) -9);
			}
			break;
			
		case MESSAGE:
			long to = packet.getLong();
			//System.out.println("Packet Length: "+packet.getLength());
			int chatTextSize = (packet.getLength() - 8);
			byte[] chat = new byte[chatTextSize];
			packet.get(chat);
			if(Constants.CONNNECT_TO_LOGIN_SERVER) {
				World.getWorld().getLoginServerConnector().sendPrivateMessage(to, player.getNameAsLong(), player.getRights().toInteger(), chat);
			} else {
				for(Player p : World.getWorld().getPlayers()) {
					if(p.getNameAsLong() == to)
						p.getActionSender().sendPrivateMessage(player.getNameAsLong(), player.getRights().toInteger(), chat);
						
				}
			}
			break;
			
		}
	}

}
