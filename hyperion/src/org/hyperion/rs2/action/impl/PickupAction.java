package org.hyperion.rs2.action.impl;

import org.hyperion.rs2.action.Action;
import org.hyperion.rs2.event.impl.FloorItemEvent;
import org.hyperion.rs2.model.FloorItem;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Player;

public class PickupAction extends Action{

	public PickupAction(Player player, FloorItem fl) {
		super(player, 450);
		// TODO Auto-generated constructor stub
		after = fl;
	}
	
	private FloorItem after;

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
		//Check location.
		Player player = getPlayer();
		Location loc = player.getLocation();
		if(loc.getX() != after.getLoc().getX() && loc.getY() != after.getLoc().getY()) return;
		if(player.getInventory().hasRoomFor(new Item(after.getItem(), after.getAmount()))) {
			if(FloorItemEvent.takeFloorItem(after))
				player.getInventory().add(new Item(after.getItem(), after.getAmount()));
		} else {
			player.getActionSender().sendMessage("You do not have enough inventory space.");
		}
		this.stop();
	}

}
