package org.hyperion.rs2.model;

public class Shop {
	
	private String name;
	
	private int[] items, prices, amount;
	
	private boolean isGeneral;
	
	public Shop(String name, int[] items, int[] prices, boolean general) {
		this.name = name;
		this.items = general ? new int[40] : items;
		this.prices = general ? new int[40] : prices;
		this.isGeneral = general;
		amount = new int[!general ? prices.length : 40];
		if(!isGeneral)
			for(int i = 0; i < amount.length; i++)
				amount[i] = 1;//Not Needed, as most Shops nowadays are unlimited stock.
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the items
	 */
	public int[] getItems() {
		return items;
	}

	/**
	 * @return the prices
	 */
	public int[] getPrices() {
		return prices;
	}

	/**
	 * @return the isGeneral
	 */
	public boolean isGeneral() {
		return isGeneral;
	}

	/**
	 * @return the amount
	 */
	public int[] getAmount() {
		return amount;
	}
}
