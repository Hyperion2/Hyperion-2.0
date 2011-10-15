package org.hyperion.rs2.model.container;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Shop;
import org.hyperion.rs2.model.container.impl.InterfaceContainerListener;

public class Shops {

	/**
	 * The shop size.
	 */
	public static final int SIZE = 40;
	
	/**
	 * The player inventory interface.
	 */
	public static final int PLAYER_INVENTORY_INTERFACE = 3823;

	/**
	 * The shop inventory interface.
	 */
	public static final int SHOP_INTERFACE = 3900;
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(Shops.class.getName());
	
	/**
	 * A List of Shops.
	 */
	private static List<Shop> shops = new LinkedList<Shop>();
	
	/**
	 * Opens the shop for the specified player.
	 * @param player The player to open the shop for.
	 */
	public static void open(Player player,int id) {
		player.getActionSender().sendInterfaceInventory(3824, 3822);
		Shop shop = shops.get(id);
		player.getInterfaceState().addListener(player.getInventory(), new InterfaceContainerListener(player, PLAYER_INVENTORY_INTERFACE));
        player.getActionSender().sendString(3901, shop.getName());
		player.getActionSender().sendUpdateItems(SHOP_INTERFACE, shop.getItems(), shop.getAmount());
		player.setViewingShop(id);
	}
	
	/**
	 * Buys the item from the shop.
	 * @param player The player.
	 * @param interSlot The slot of the item.
	 * @param amount The amount.
	 */
	public static void buyFromShop(Player player, int interSlot, int amount) {
		Shop shop = shops.get(player.getViewingShop());
		if(shop == null) {
			player.getActionSender().sendMessage("Shop is null.");
			return;
		}
		if(shop.getItems()[interSlot] <= 0) {
			player.getActionSender().sendMessage("ItemId doesnt exist.");
			return;
		}
		int currencyForShop = 995;
		int amountOfCurrency = player.getInventory().getCount(new Item(currencyForShop));
		int item = shop.getItems()[interSlot];
		int amountPrItem = shop.getPrices()[interSlot];
		if(amountPrItem <= 0) {
			player.getActionSender().sendMessage("Item price is below 0.");
			return;
		}
		if(!player.getInventory().hasRoomFor(new Item(item, amount)))
			amount = player.getInventory().freeSlots();
		amount = (int) Math.floor(amountOfCurrency / amountPrItem);
		if(shop.isGeneral())
			amount = shop.getAmount()[interSlot] <= amount ? shop.getAmount()[interSlot] : amount;
		if(amountOfCurrency < (amount * amountPrItem) || amount <= 0 || amountOfCurrency <= 0) {
			player.getActionSender().sendMessage("You do not have enough coins.");
			return;
		}
		if(shop.isGeneral()) {
			shop.getAmount()[interSlot] -= amount;
			if(shop.getAmount()[interSlot] <= 0) {
				shop.getItems()[interSlot] = -1;
				shop.getPrices()[interSlot] = 0;
				//shop.shift();
			}
			for(Player p : player.getRegion().getPlayers()) {//Update players viewing same shop.
				if(p.getViewingShop() == player.getViewingShop()) {
					player.getActionSender().sendUpdateItems(3900, shop.getItems(), shop.getAmount());
				}
			}
		}
		System.out.println(amount);
		player.getInventory().remove(new Item(currencyForShop, amount * amountPrItem));
		player.getInventory().add(new Item(item, amount));
	}
	
	/**
	 * 
	 * @param player
	 * @param interSlot
	 * @param shop
	 */
	public static void sendPrice(Player player, int interSlot, boolean shop2) {
		Shop shop = shops.get(player.getViewingShop());
		if(shop == null) return;
		boolean canBeSold = shop.isGeneral() || shop2;
		if(!shop2)
			canBeSold = player.getInventory().get(interSlot).getDefinition().isTradable();
		int price = 0;
		if(!shop.isGeneral() && !shop2) {
			for(int i = 0; i < 40; i++) {
				if(shop.getItems()[i] == player.getInventory().get(interSlot).getId()) {
					canBeSold = true;
					price = (int) (shop.getPrices()[i] * 0.33);
					break;
				}
			}
		}
		if(!canBeSold) {
			player.getActionSender().sendMessage("This item cannot be sold to this shop.");
			return;
		}
		player.getActionSender().sendMessage("This item currently costs "+ amountToString((int) (shop2 ? shop.getPrices()[interSlot] : price != 0 ? price : player.getInventory().get(interSlot).getDefinition().getHighAlch()*0.33 + player.getInventory().get(interSlot).getDefinition().getLowAlch()))+".");
	}
	
	/**
	 * Selling item to the shop.
	 * @param player The player.
	 * @param interSlot The slot of the item.
	 * @param amount The amount of the item.
	 */
	public static void sellToShop(Player player, int interSlot, int amount) {
		Shop shop = shops.get(player.getViewingShop());
		if(shop == null || player.getInventory().get(interSlot) == null) return;
		Item take = player.getInventory().get(interSlot);
		boolean canSell = shop.getName().contains("General");
		int price = (int) (take.getDefinition().getHighAlch()*0.33 + take.getDefinition().getLowAlch());
		for(int i = 0; i < shop.getItems().length; i++) {
			if(shop.getItems()[i] == take.getId()) {
				canSell = true;
				price = (int) (shop.getPrices()[i] * 0.33);
				break;
			}
		}
		amount = (amount > take.getCount() && shop.isGeneral() ? take.getCount() : amount);
		if(!canSell || take.getId() == 995 || !take.getDefinition().isTradable()
				|| take.getDegrade() != 0) {
			player.getActionSender().sendMessage("You cannot sell this item to the store.");
			return;
		}
		
		if(shop.isGeneral()) {
			int spot = -1;
			for(int i = 0; i < shop.getItems().length; i++) {
				if(shop.getItems()[i] <= 0) {
					spot = i;
					break;
				}
			}
			if(spot == -1) {
				player.getActionSender().sendMessage("The shop does not have enough room.");
				return;
			}
			
			shop.getItems()[spot] = take.getId();
			shop.getPrices()[spot] = take.getDefinition().getHighAlch() + take.getDefinition().getLowAlch();
			shop.getAmount()[spot] = amount;
			//shop.shift();
			for(Player p : player.getRegion().getPlayers()) {//Update players viewing same shop.
				if(p.getViewingShop() == player.getViewingShop()) {		
					player.getActionSender().sendUpdateItems(3900, shop.getItems(), shop.getAmount());
				}
			}
		}
		player.getInventory().remove(new Item(take.getId(), amount));
		player.getInventory().add(new Item(995, price));
	}
	
	/**
	 * 
	 * @param price
	 * @return
	 */
	public static String amountToString(int price) {
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setGroupingSeparator(',');
		df.setDecimalFormatSymbols(dfs);
		return df.format(price);
	}
	
	/**
	 * Loads Shop Files.
	 */
	public static void loadShopFile() {
		String line = "";
		String[] token3 = null;
		BufferedReader characterfile;
		int loaded = 0;
		try {
			characterfile = new BufferedReader(new FileReader("./data/shop.cfg"));
			while ((line = characterfile.readLine()) != null) {
				line = line.trim();
				if(line.contains("//")) continue;
				int end = line.indexOf("=");
				if(end <= -1) {
					characterfile.close();
					return;
				}
				String name = line.substring(0, end);
				line = line.substring(end+1, line.length());
				token3 = line.split(" : ");
				int[] items = new int[token3.length];
				int[] prices = new int[token3.length];
				boolean general = name.toLowerCase().contains("general");
				if(token3.length > 0) {
					for(int i = 0; i < token3.length; i++) {
						String[] data = token3[i].split(" ");
						if(data[0].isEmpty()) continue;
						items[i] = Integer.parseInt(data[0]);
						prices[i] = Integer.parseInt(data[1]);
					}
				} 
				shops.add(new Shop(name, general ? null : items, general ? null : prices, general));
				loaded++;
			}
			characterfile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Successfully loaded "+loaded+" shops.");
	}
}
