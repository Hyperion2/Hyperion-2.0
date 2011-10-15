package org.hyperion.rs2.event.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.model.FloorItem;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.region.Region;
import org.hyperion.rs2.model.region.RegionManager;

/**
 * As this class is looked at every second, synchronization needs to be added.
 * @author phil
 *
 */
public class FloorItemEvent extends Event {
	
	private static List<FloorItem> global = new ArrayList<FloorItem>();

	public FloorItemEvent() {
		super(1000);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Adds to the array without modificiation exceptions.
	 * @param floor
	 */
	public static void addFloorItem(FloorItem floor) {
		///synchronized(global) {
		Player droppedFor = floor.getDroppedFor();
			droppedFor.getActionSender().sendRegionalItem(floor);
			if(droppedFor != floor.getOwner())
				((Player)floor.getOwner()).getActionSender().sendRegionalItem(floor);
			global.add(floor);//Check here for items of the same.
			droppedFor.getRegion().addFloorItem(floor);
		//}
	}
	
	/**
	 * Remove a floorItem.
	 * @param fl
	 * @return
	 */
	public static boolean takeFloorItem(FloorItem fl) {
	//	synchronized(global) {
			for(FloorItem f : global) {
				if(f.getItem() == fl.getItem() && fl.getAmount() == f.getAmount()
						&& f.getLoc().equals(fl.getLoc()) && !f.isTaken()) {
					f.setBeenTaken(true);
					for(Player p : RegionManager.getLocalPlayers(f.getLoc())) {
						p.getActionSender().sendRemoveRegionalItem(f);
					}
					return true;
				}
			}
	//	}
		return false;
	}

	/**
	 * Gets the list of Globalitems.
	 * @return The list of GlobalItems.
	 */
	public static Collection<FloorItem> getFloorItem() {
		synchronized(global) {
			return Collections.unmodifiableCollection(new LinkedList<FloorItem>(global));
		}
	}
	
	@Override
	public void execute() {//Cannot allow modifications while examining objects.
		// TODO Auto-generated method stub
			for(Iterator<FloorItem> it$ = global.iterator(); it$.hasNext();) {
				FloorItem item = it$.next();
				
				if(item.isTaken()) {//Should check before continueing.
					it$.remove();
					continue;
				}
				item.setTimer(item.getTimer()-1);
				
				if(item.getTimer() <= 0) {
					Region r = RegionManager.getRegionByLocation(item.getLoc());
					if(item.isGlobal()) {//Remove from list.
						/**
						 * Send RemoveFloor item.
						 */
						for(Player p : RegionManager.getLocalPlayers(item.getLoc()))
							p.getActionSender().sendRemoveRegionalItem(item);
						//blah
						item.setBeenTaken(true);
						r.removeFloorItem(item);
						it$.remove();
						continue;
					} else {
						r.removeFloorItem(item);
						item.setGlobal(true);
						r.addFloorItem(item);
						item.setTimer(90);
						/**
						 * Send Item to all Players.
						 */
						for(Player p : RegionManager.getLocalPlayers(item.getLoc()))
							if(p != item.getOwner() && p != item.getDroppedFor())//Only check if there in the same region.
								p.getActionSender().sendRegionalItem(item);
					}
				}
			}
	}

}