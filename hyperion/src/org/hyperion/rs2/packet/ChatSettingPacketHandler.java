package org.hyperion.rs2.packet;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.World;
import org.hyperion.rs2.net.Packet;

public class ChatSettingPacketHandler implements PacketHandler {

	@Override
	public void handle(Player player, Packet packet) {
		// TODO Auto-generated method stub
		int pub = packet.get();
		int priv = packet.get();
		int trade = packet.get();
		int oldSetting = player.getSettings().getPrivateChatSetting()[1];
		player.getSettings().getPrivateChatSetting()[0] = pub;
		player.getSettings().getPrivateChatSetting()[1] = priv;
		player.getSettings().getPrivateChatSetting()[2] = trade;
		if(oldSetting != priv) {
			if(Constants.CONNNECT_TO_LOGIN_SERVER) {
				World.getWorld().getLoginServerConnector().sendChangePrivateSettings(player);
			} else {
				for(Player p : World.getWorld().getPlayers()) {
					if(priv == 2 && p.getFriends().contains(player.getNameAsLong()))
						p.getActionSender().sendPrivateMessageStatus(p.getNameAsLong(), -9);
					else if(priv == 1 && p.getFriends().contains(player.getNameAsLong())
							&& !player.getFriends().contains(p.getNameAsLong()))
						p.getActionSender().sendPrivateMessageStatus(player.getNameAsLong(), -9);
					else if(p.getFriends().contains(player.getNameAsLong()))
						p.getActionSender().sendPrivateMessageStatus(player.getNameAsLong(), World.getWorld().getWorldId());
				}
			}
		}
	}

}
