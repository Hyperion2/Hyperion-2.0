package org.hyperion.rs2.model.container;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.RequestManager;
import org.hyperion.rs2.model.RequestManager.RequestType;
import org.hyperion.rs2.model.container.impl.InterfaceContainerListener;

public class Trade
{

	/**
	 * The Size of Trade.
	 */
	public static final int SIZE = 28;

	/**
	 * The player inventory interface.
	 */
	public static final int PLAYER_INVENTORY_INTERFACE = 3322;
	/**
	 * The shop inventory interface.
	 */
	public static final int TRADE_INTERFACE = 3415;

	/**
	 * Exits out of trading interfaces upon calling.
	 * 
	 * @param player
	 */
	public static void exitOutOfTrade(Player player, boolean complete)
	{
		if (player == null || player.getRequestManager().getRequestType() != RequestManager.RequestType.TRADE
				|| player.getRequestManager().getState() != RequestManager.RequestState.PARTICIPATING)
			return;
		Player[] traders = { player, player.getRequestManager().getAcquaintance()};
		for (Player p : traders)
		{
			Container items = (complete) ? p.getRequestManager().getAcquaintance().getOther() : p.getOther();
			for(Item i : items.toArray()) {
				if(i != null)
					p.getInventory().add(i);
			}
			items.clear();
			p.getRequestManager().setAcquaintance(null);
			p.getRequestManager().setRequestStage(-1);
			p.getRequestManager().setRequestType(null);
			p.getRequestManager().setState(null);
			p.getActionSender().sendRemoveAllInterfaces();
			if(!complete)
				p.getActionSender().sendMessage(p == player ? "You declined the trade." : "The other person has declined the trade.");
		}
	}

	/**
	 * Opens the First Trading Interface.
	 * 
	 * @param trader
	 */
	public static void openFirstInterface(Player[] trader)
	{
		for (Player player : trader)
		{
			player.getRequestManager().setRequestType(RequestManager.RequestType.TRADE);
			player.getRequestManager().setState(RequestManager.RequestState.PARTICIPATING);
			player.getRequestManager().setRequestStage(1);
			player.getRequestManager().getAcquaintance().getActionSender().sendString(3431, "");
			player.getInterfaceState().addListener(player.getOther(), new InterfaceContainerListener(player, 3415));
			player.getInterfaceState().addListener(player.getInventory(), new InterfaceContainerListener(player, 3322));
			player.getInterfaceState().addListener(player.getRequestManager().getAcquaintance().getOther(), new InterfaceContainerListener(player, 3416));
			player.getActionSender().sendString(3417, "Trading With: "+ player.getRequestManager().getAcquaintance().getName());
			player.getActionSender().sendInterfaceInventory(PLAYER_INVENTORY_INTERFACE+1, TRADE_INTERFACE-94);
		}
	}
	
	/**
	 * The Message of how much is being transfered.
	 * @param player1 You.
	 * @param player2 Other player.
	 * @return
	 *
	public static String wealthString(Player player1, Player player2) {
		long wealthA = 0;
		for(Item i : player1.getOther().toArray())
			if(i != null)
				wealthA += i.getCount() * i.getDefinition().getValue();
		long wealthB = 0;
		for(Item i : player2.getOther().toArray())
			if(i != null)
				wealthB += i.getCount() * i.getDefinition().getValue();
		String name = "";
		if(wealthA > wealthB)
			name = player2.getName();
		else if(wealthA < wealthB)
			name = "Me";
		return Math.abs(wealthA - wealthB) >= Integer.MAX_VALUE ? "<col=FF0000>Unkown!" : wealthA == wealthB ? "No wealth transfer" : (wealthA > wealthB ? "<col=FF0000>" : "")+"Wealth Transfer: "+Shop.getPrice((int) Math.abs(wealthA-wealthB), "coins")+" to "+name+".";
	}

	/**
	 * Sets the Second Trading interface.
	 * @param player
	 * @return
	 *
	public static String getSecondString(Player player)
	{
		String a = "";
        if (player.getOther().size() > 0) {
            for (Item item : player.getOther().toArray()) {
            	if(item != null) {
	                a = a + "<col=FF9040>" + item.getDefinition().getName();
	                if (item.getCount() > 1) {
	                    a = a + "<col=FFFFFF> x ";
	                    a = a + "<col=FFFFFF>" +
	                            Integer.toString(item.getCount()) + "<br>";
	                } else {
	                    a = a + "<br>";
	                }
            	}
            }
        } else {
            a = "<col=FFFFFF>Absolutely nothing!";
        }
		return a;
	}
	
	/**
         * Author Martin.
         * @param items
         * @return
         */
        public static String itemString(Item[] items){
		String sendTrade = "Absolutely nothing!";
		String sendAmount = "";
		int count = 0;
		for (Item item : items) {
			if(item == null)
				continue;
			if (item.getId() > 0) {
				if ((item.getCount() >= 1000) && (item.getCount() < 1000000)) {
					sendAmount = "@cya@" + (item.getCount() / 1000) + "K @whi@("
							+ Shops.amountToString(item.getCount()) + ")";
				} else if (item.getCount() >= 1000000) {
					sendAmount = "@gre@" + (item.getCount() / 1000000)
							+ " million @whi@(" + Shops.amountToString(item.getCount())
							+ ")";
				} else {
					sendAmount = "" + Shops.amountToString(item.getCount());
				}
				if (count == 0) {
					sendTrade = "";
					count = 2;
				}
				if(count == 1){
					sendTrade = sendTrade + "\\n" + item.getDefinition().getName();
				} else if(count == 2){
					sendTrade = sendTrade + " " + item.getDefinition().getName();
					count = 0;
				}
				if (item.getDefinition().isStackable()) {
					sendTrade = sendTrade + " x " + sendAmount;
				}
				sendTrade = sendTrade + "     ";
				count++;
			}
		}
		return sendTrade;
	}
	
	/**
	 * Sends the ready messages to each player.
	 * @param player
	 */
	public static void setReadyMessages(Player player) {
		if (player.getRequestManager().getRequestStage() == 1
				&& player.getRequestManager().getAcquaintance()
						.getRequestManager().getRequestStage() == 1)
		{
			player.getRequestManager().setRequestStage(2);
			player.getActionSender().sendString(3431, "Waiting for other player.");
			player.getRequestManager().getAcquaintance().getActionSender().sendString(3431, "Other player has accepted.");
		} else if (player.getRequestManager().getRequestStage() == 1 && player.getRequestManager().getAcquaintance().getRequestManager().getRequestStage() == 2){
			player.getRequestManager().setRequestStage(2);
			player.getActionSender().sendString(3431, "");
			player.getRequestManager().getAcquaintance().getActionSender().sendString(3431, "");
			openConfirmInterface(new Player[] {player, player.getRequestManager().getAcquaintance()});
		} else if (player.getRequestManager().getRequestStage() == 2 && player.getRequestManager().getAcquaintance().getRequestManager().getRequestStage() == 2){
			if(player.getInventory().freeSlots() < player.getRequestManager().getAcquaintance().getOther().size()) {
				player.getActionSender().sendMessage("You do not have enough inventory space.");
				return;
			}
			player.getRequestManager().setRequestStage(3);
			player.getActionSender().sendString(3431, "Waiting for other player.");
			player.getRequestManager().getAcquaintance().getActionSender().sendString(3431, "Other player has accepted.");
		} else if (player.getRequestManager().getRequestStage() == 2 && player.getRequestManager().getAcquaintance().getRequestManager().getRequestStage() == 3){
			if(player.getInventory().freeSlots() < player.getRequestManager().getAcquaintance().getOther().size()) {
				player.getActionSender().sendMessage("You do not have enough inventory space.");
				return;
			}
			player.getRequestManager().setRequestStage(3);
			player.getActionSender().sendString(3431, "");
			player.getRequestManager().getAcquaintance().getActionSender().sendString(3431, "");
			exitOutOfTrade(player, true);
		}
	}

	/**
	 * This sends the second interface during trading.
	 * @param trader
	 */
	public static void openConfirmInterface(Player[] trader)
	{
		for (Player player : trader)
		{
			player.getRequestManager().setRequestType(null);
			player.getActionSender().sendInterfaceInventory(3443, 3213);
			player.getRequestManager().setRequestType(RequestType.TRADE);
			player.getActionSender().sendString(3557, itemString(player.getOther().toArray()));
			player.getActionSender().sendString(3558, itemString(player.getRequestManager().getAcquaintance().getOther().toArray()));
			player.getActionSender().sendString(3535, "Are you sure you want to make this trade?");
			//player.getActionSender().sendInterfaceConfig(334, 46, true);
		}
	}
	
	/**
	 * This is for staking & Trading really.
	 * @param player
	 * @param item
	 */
	public static void addItemToOffer(Player player, int slot, int amount) {
		Item item = player.getInventory().get(slot);
		
		if(item == null) return;
		
		/////////UNTRADABLES\\\\\\\\\\\\\\\
		
		if(!item.getDefinition().isTradable() || item.getDegrade() != 0) {
			player.getActionSender().sendMessage("This item cannot be traded.");
			return;
		}
		/////////END\\\\\\\\\\\\\\\\\\\
		
		if(amount > player.getInventory().getCount(item))
			amount = player.getInventory().getCount(item);
		
		if(amount == -1) amount = player.getInventory().getCount(item);
			
		item.setCount(amount);
		
		player.getInventory().remove(item);
		player.getOther().add(item);
		player.getActionSender().sendString(3431, "");
		player.getRequestManager().getAcquaintance().getRequestManager().setRequestStage(1);
		player.getRequestManager().getAcquaintance().getActionSender().sendString(3431, "");
		player.getRequestManager().setRequestStage(1);
	}
	
	/**
	 * This is for Trading, removing items from interface.
	 * @param player
	 * @param item
	 * @param slot
	 */
	public static void removeItemFromOffer(Player player, int slot, int amount) {
		Item item = player.getOther().get(slot);
		
		if(item == null) return;
		
		if(amount > player.getOther().getCount(item))
			amount = player.getOther().getCount(item);
		
		if(amount == -1) amount = player.getOther().getCount(item);
			
		item.setCount(amount);
		
		player.getOther().remove(item);
		player.getOther().shift();
		player.getInventory().add(item);
		player.getActionSender().sendString(3431, "");
		player.getRequestManager().getAcquaintance().getRequestManager().setRequestStage(1);
		player.getRequestManager().getAcquaintance().getActionSender().sendString(3431, "");
		player.getRequestManager().setRequestStage(1);
	}
}
