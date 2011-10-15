package org.hyperion.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class ItemBonuses {

	private static HashMap<Integer, int[]> itemBonuses;
	
	public static HashMap<Integer, Boolean> tradable;
	
	public static HashMap<Integer, int[]> alch;
	
	public static HashMap<Integer, String> examine;
	
	public static final void init() {
		if(new File("data/bonuses.ib").exists())
			loadItemBonuses();
		else
			throw new RuntimeException("Missing item bonuses.");
	}
	
	public static final int[] getItemBonuses(int itemId) {
		return itemBonuses.get(itemId);
	}
	
	private static final void loadItemBonuses() {
		try {
			DataInputStream in = new DataInputStream(new FileInputStream("data/bonuses.ib"));
			itemBonuses = new HashMap<Integer, int[]>();
			tradable = new HashMap<Integer, Boolean>();
			alch = new HashMap<Integer, int[]>();
			examine = new HashMap<Integer, String>();
			while (in.available() != 0) {
				int itemId = in.readUnsignedShort();
				int[] bonuses = new int[18];
				for(int index = 0; index < bonuses.length; index++) {
					int bonus = in.readShort();
					bonuses[index] = bonus;
					//if(itemId == 1323) System.out.println(bonus);
				}
				alch.put(itemId, new int[] {in.readInt(), in.readInt()});
				tradable.put(itemId, in.readUnsignedByte() == 1);
				examine.put(itemId, in.readUTF());
				itemBonuses.put(itemId, bonuses);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private ItemBonuses() {
		
	}
	
}
