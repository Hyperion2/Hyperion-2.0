package org.hyperion.rs2.packet;

import java.util.logging.Logger;

import org.hyperion.rs2.content.combat.WeaponInfo;
import org.hyperion.rs2.content.skills.Prayer;
import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.container.Trade;
import org.hyperion.rs2.net.Packet;

/**
 * Handles clicking on most buttons in the interface.
 * @author Graham Edgecombe
 *
 */
public class ActionButtonPacketHandler implements PacketHandler {

	/**
	 * The logger instance.
	 */
	private static final Logger logger = Logger.getLogger(ActionButtonPacketHandler.class.getName());
	
	@Override
	public void handle(Player player, Packet packet) {
		final int button = packet.getShort();
		//prayer buttons
		if(button >= 683 && button <= 685 || button >= 5609 && button <= 5623) {
			Prayer.prayerClick(player, button);
			return;
		}
		switch(button) {
		
		
		case 12298:
		case 2282:
		case 5860:
		case 336:
		case 8466:
		case 2429:
		case 1704:
		case 1772:
			player.setFightStyle((byte) 0);
			WeaponInfo.setAttackEmote(player);
			break;
			
		case 12297:
		case 2285:
		case 5862:
		case 335:
		case 2432:
		case 1707:
		case 8468:
		case 1771:
			player.setFightStyle((byte) 1);
			WeaponInfo.setAttackEmote(player);
			break;
			
		case 12296:
		case 2284:
		case 5861:
		case 334:
		case 8467:
		case 2431:
		case 1706:
		case 1770:
			player.setFightStyle((byte) 2);
			WeaponInfo.setAttackEmote(player);
			break;
			
		case 2283:
		case 2430:
		case 1705:
			player.setFightStyle((byte) 3);
			WeaponInfo.setAttackEmote(player);
			break;
		
		case 161:
			player.playAnimation(Animation.CRY);
			break;
			
		case 162:
			player.playAnimation(Animation.THINKING);
			break;
			
		case 163:
			player.playAnimation(Animation.WAVE);
			break;
			
		case 164:
			player.playAnimation(Animation.BOW);
			break;
			
		case 165:
			player.playAnimation(Animation.ANGRY);
			break;
			
		case 166:
			player.playAnimation(Animation.DANCE);
			break;
			
		case 167:
			player.playAnimation(Animation.BECKON);
			break;
			
		case 168:
			player.playAnimation(Animation.YES_EMOTE);
			break;
			
		case 169:
			player.playAnimation(Animation.NO_EMOTE);
			break;
			
		case 170:
			player.playAnimation(Animation.LAUGH);
			break;
			
		case 171:
			player.playAnimation(Animation.CHEER);
			break;
			
		case 172:
			player.playAnimation(Animation.CLAP);
			break;
			
		case 13362:
			player.playAnimation(Animation.PANIC);
			break;
			
		case 13363:
			player.playAnimation(Animation.JIG);
			break;
			
		case 13364:
			player.playAnimation(Animation.SPIN);
			break;
			
		case 13365:
			player.playAnimation(Animation.HEADBANG);
			break;
			
		case 13366:
			player.playAnimation(Animation.JOYJUMP);
			break;
			
		case 13367:
			player.playAnimation(Animation.RASPBERRY);
			break;
			
		case 13368:
			player.playAnimation(Animation.YAWN);
			break;
			
		case 13383:
			player.playAnimation(Animation.GOBLIN_BOW);
			break;
			
		case 13384:
			player.playAnimation(Animation.GOBLIN_DANCE);
			break;
			
		case 13369:
			player.playAnimation(Animation.SALUTE);
			break;
			
		case 13370:
			player.playAnimation(Animation.SHRUG);
			break;
			
		case 11100:
			player.playAnimation(Animation.BLOW_KISS);
			break;
			
		case 667:
			player.playAnimation(Animation.GLASS_BOX);
			break;
		case 6503:
			
			player.playAnimation(Animation.CLIMB_ROPE);
			break;
			
		case 6506:
			player.playAnimation(Animation.LEAN);
			break;
			
		case 666:
			player.playAnimation(Animation.GLASS_WALL);
			break;
			
		case 2458:
			player.getActionSender().sendLogout();
			break;
			
		case 5387:
			player.getSettings().setWithdrawAsNotes(false);
			break;
			
		case 5386:
			player.getSettings().setWithdrawAsNotes(true);
			break;
			
		case 8130:
			player.getSettings().setSwapping(true);
			break;
			
		case 8131:
			player.getSettings().setSwapping(false);
			break;
			
		case 3420:
		case 3546:
			Trade.setReadyMessages(player);
			break;
			
		case 152:
			player.getWalkingQueue().setRunningToggled(false);
			player.getActionSender().sendConfig(173, 0);
			break;
			
		case 153:
			player.getWalkingQueue().setRunningToggled(true);
			player.getActionSender().sendConfig(173, 1);
			break;
			
		default:
			logger.info("Unhandled action button : " + button);
			break;
		}
	}

}
