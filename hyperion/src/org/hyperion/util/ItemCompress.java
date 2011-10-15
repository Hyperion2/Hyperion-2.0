package org.hyperion.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.hyperion.Server;
import org.hyperion.rs2.content.Poison;
import org.hyperion.rs2.content.combat.WeaponInfo;
import org.hyperion.rs2.model.ItemDefinition;

public class ItemCompress {
	
	public ItemCompress() {
		System.out.println("Loading item bonuses...");
		ItemBonuses.init();
		init();
	}

	public static BufferedReader in = null;//new BufferedReader(new FileReader("./data/Item.txt"));
	
	public static Map<Integer, Byte> equipIds = new HashMap<Integer, Byte>();
	
	private static Map<Integer, Double> weight = new HashMap<Integer, Double>();
	
	public static void init() {
		System.out.println("Loading item definitions...");
		try {
			in = new BufferedReader(new FileReader("data/EquipSlots.txt"));
            String line2;
            int id2 = 0;
            while((line2 = in.readLine()) != null) {
                equipIds.put(id2++, Byte.parseByte(line2));
            }
            in.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		try {
			Scanner s = new Scanner(new File("data/weight.txt"));
			while (s.hasNextLine()) {
				String[] line = s.nextLine().split(" ");
				if(line[0].isEmpty()) continue;
				weight.put(Integer.parseInt(line[0]), Double.parseDouble(line[1]));
			}
		} catch (IOException e) {
			e.printStackTrace();	
		}
		try {
			in = new BufferedReader(new FileReader("./data/Item.txt"));
			final DataOutputStream out = new DataOutputStream(new FileOutputStream("data/Item.dat"));
			int id = -1, equipId = -1, renderId = -1, lentId = -1, notedCounterPart = -1;
			boolean isNoted = false, isMember = false, stackable = false;
            String line, name = "";
            while((line = in.readLine()) != null) {
            	if(line.startsWith("Name: ")) {
                    name = line.replaceAll("Name: ", "");
                    if(name.equals("null")) name = "";
                } else if(line.startsWith("isNoteable: ")) {
                    isNoted = Boolean.parseBoolean(line.replaceAll("isNoteable: ", ""));
                } else if(line.startsWith("isMember: ")) {
                	isMember = Boolean.parseBoolean(line.replaceAll("isMember: ", ""));
                } else if(line.startsWith("isStackable: ")) {
                	stackable = Boolean.parseBoolean(line.replaceAll("isStackable: ", ""));
                } else if(line.startsWith("EquipId: ")) {
                    equipId = Integer.parseInt(line.replaceAll("EquipId: ", ""));
                } else if(line.startsWith("NotedId: ")) {
                    notedCounterPart = Integer.parseInt(line.replaceAll("NotedId: ", ""));
                } else if(line.startsWith("lentId: ")) {
                    lentId = Integer.parseInt(line.replaceAll("lentId: ", ""));
                } else if(line.startsWith("RenderId: ")) {
                    renderId = Integer.parseInt(line.replaceAll("RenderId: ", ""));
                } else if(line.startsWith("SkillReq: ")) {
                	id++;
                	//out.writeShort(id++);
                    out.writeUTF(name);
                    if(name.isEmpty()) continue;
                    out.writeByte((byte) (isNoted ? 1 : 0));
                    out.writeByte((byte) (isMember ? 1 : 0));
                    out.writeByte((byte) (stackable ? 1 : 0));
                    out.writeByte(Poison.getPoisonDamg(name));
                    out.writeShort((short) notedCounterPart);
                    out.writeDouble(weight.get(id) == null ? 0.0 : weight.get(id));
                    if(Server.VERSION >= 508) {
                    	out.writeShort((short) lentId);
	                    out.writeShort((short) equipId);
	                    out.writeShort((short) renderId);
	                    out.writeUTF(ItemBonuses.examine.get(id) == null ? "Its a "+name+"." : ItemBonuses.examine.get(id));
                    }
                    out.writeByte(equipIds.get(id) == null ? 0 : equipIds.get(id));
                    int timer = WeaponInfo.timer(id);
                    out.writeByte((byte)timer);
                    int[] alch = ItemBonuses.alch.get(id) == null ? new int[2] : ItemBonuses.alch.get(id);
                    out.writeInt(alch[0]);
                    out.writeInt(alch[1]);
                    boolean tradable = (ItemBonuses.tradable.get(id) == null ? 1 : ItemBonuses.tradable.get(id) ? 1 : 0) == 1;
                    out.writeByte((byte) (tradable ? 1 : 0));
                    
                    int[] bonus = ItemBonuses.getItemBonuses(id);
                    
                    if(bonus == null)
                    	bonus = new int[18];
                    
                    for(int i = 0; i < bonus.length; i++) {
                    	//System.out.println(bonus[i]);
                    	out.writeByte(bonus[i]);
                    }
                    
                    String[] split = line.replaceAll("SkillReq: ", "").split(";");
                    for(int i = 0; i < split.length; i++) {
                    	String[] req = split[i].split(":");
                    	out.writeByte(Integer.parseInt(req[0]));
                    	out.writeByte(Integer.parseInt(req[1]));
                    }
                }
            if(id == ItemDefinition.MAX_ITEMS) break;
            }
			out.flush();
			out.close();
			in.close();
			System.out.println("Compressed: "+id+" Item Definitions.");
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("Compressing Item.dat");
	}
}
