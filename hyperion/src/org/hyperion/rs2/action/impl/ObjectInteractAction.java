package org.hyperion.rs2.action.impl;

import org.hyperion.rs2.action.Action;
import org.hyperion.rs2.model.Location;
import org.hyperion.rs2.model.Player;

public class ObjectInteractAction extends Action {

	public ObjectInteractAction(Player p, int id, Location obj, int opcode) {
		super(p, 600);
		// TODO Auto-generated constructor stub
		this.objectId = id;
		this.obj = obj;
		this.interactOpcode = opcode;
	}
	
	private Location obj;
	
	private int interactOpcode;
	
	@SuppressWarnings("unused")
	private int objectId;

	@Override
	public QueuePolicy getQueuePolicy() {
		// TODO Auto-generated method stub
		return QueuePolicy.NEVER;//This should be first.
	}

	@Override
	public WalkablePolicy getWalkablePolicy() {
		// TODO Auto-generated method stub
		return WalkablePolicy.NON_WALKABLE;//If walking clicked exit.
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		if(!obj.withinRange(getPlayer().getLocation(), 1)) return;//This should be changed to allow large Objs.
		switch(interactOpcode) {
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		}
		this.stop();
	}

}
