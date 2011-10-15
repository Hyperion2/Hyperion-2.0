package org.hyperion.rs2.action.impl;

import org.hyperion.rs2.action.Action;
import org.hyperion.rs2.model.NPC;
import org.hyperion.rs2.model.Player;

public class NPCInteractAction extends Action {

	public NPCInteractAction(Player p, NPC n, int opcode) {
		super(p, (long) ((p.getLocation().getDistanceFromLocation(n.getLocation())-1) * 300));
		// TODO Auto-generated constructor stub
		this.n = n;
		this.interactOpcode = opcode;
	}
	
	private NPC n;
	
	private int interactOpcode;

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
		if(!n.getLocation().withinRange(getPlayer().getLocation(), 1)) return;//This should be changed to allow large NPCs.
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
