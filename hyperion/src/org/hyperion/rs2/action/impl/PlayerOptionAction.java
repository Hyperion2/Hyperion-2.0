package org.hyperion.rs2.action.impl;

import org.hyperion.rs2.action.Action;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.RequestManager.RequestState;
import org.hyperion.rs2.model.container.Trade;

public class PlayerOptionAction extends Action {

	public PlayerOptionAction(Player player, Player other, int option) {
		super(player, (long) ((player.getLocation().getDistanceFromLocation(other.getLocation())-1) * 300));
		// TODO Auto-generated constructor stub
		interacted = other;
		this.option = option;
	}
	
	private Player interacted;
	
	private int option;

	@Override
	public QueuePolicy getQueuePolicy() {
		// TODO Auto-generated method stub
		return QueuePolicy.NEVER;
	}

	@Override
	public WalkablePolicy getWalkablePolicy() {
		// TODO Auto-generated method stub
		return WalkablePolicy.NON_WALKABLE;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		if(!getPlayer().getLocation().isWithinDistance(interacted.getLocation())) {
			this.stop();
			getPlayer().getActionSender().sendMessage("Player cannot be found.");
			return;
		}
		if(!getPlayer().getLocation().withinRange(interacted.getLocation(), 1)) return;
		Player player = getPlayer();
		switch(option) {
		
		case 1://Option1, Attacking needs to be handled differently.
			if(player.isInCombat()) {
				player.getActionSender().sendMessage("You cannot trade while in combat.");
				return;
			}
			if(interacted == player) return;
			/*if(player.getAddress().equals(interacted.getAddress())) {
				player.getActionSender().sendMessage("You cannot trade people of the same household.");
				return;
			}*/
			if(interacted.isInCombat()) {
				player.getActionSender().sendMessage("Other player is in combat.");
				return;
			}
			player.face(interacted.getLocation());
			if(interacted.getRequestManager().getAcquaintance() == player) {
				player.getRequestManager().setAcquaintance(interacted);
				interacted.getRequestManager().setAcquaintance(player);
	            Trade.openFirstInterface(new Player[] {player, interacted});
	        } else if(interacted.getRequestManager().getState() != RequestState.PARTICIPATING) {
	            player.getActionSender().sendMessage("Sending trade request...");
	            interacted.getActionSender().sendMessage(player.getName()+" :tradereq:");
	            player.getRequestManager().setState(RequestState.REQUESTED);
	            player.getRequestManager().setAcquaintance(interacted);
	        } else {
	            player.getActionSender().sendMessage("That player is currently buzy.");
	        }
			break;//Trading?
		case 2:
			break;//Follow
		case 3://IDK? Duel maybe?
			break;
		}
		this.stop();
	}

}
