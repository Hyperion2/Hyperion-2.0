/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package npcdef.resources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author phil
 */
public class NPCDefinition {

    public static Map<Integer, NPCDefinition> npcs = new HashMap<Integer, NPCDefinition>();

    /**
	 * Gets a Definition from the Map.
	 * @param i The NPC id.
	 * @return The NPC definition.
	 */
	public static NPCDefinition forID(int i) {
		return npcs.get(i);
	}

	/**
	 * Adds a Definition to the index.
	 * @param i The index.
	 * @param def The value.
	 */
	public static void addDefinition(int i, NPCDefinition def) {
		npcs.put(i, def);
	}


	/**
	 * Constructor for setting the NPCDefinition.
	 * @param name The NPC name.
	 * @param level The combatLevel.
	 * @param size The NPC size.
	 */
	public NPCDefinition(String name, int id, int level, int size) {
		this.name = name;
		this.npcId = id;
		this.combat = level;
		this.size = size;
	}

	/**
	 * The NPC name.
	 */
	private String name;

	/**
	 * Gets the NPC name.
	 * @return The name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * The NPC id.
	 */
	private int npcId;

	/**
	 * Gets the NPC id.
	 * @return The id.
	 */
	public int getId()
	{
		return npcId;
	}

	/**
	 * The NPC combatLevel.
	 */
	private int combat;

	/**
	 * Gets the NPC combatLevel.
	 * @return The combatLevel.
	 */
	public int getCombatLevel()
	{
		return combat;
	}

	/**
	 * The NPC size.
	 */
	private int size;

	/**
	 * Gets the NPC size.
	 * @return The size.
	 */
	public int getSize()
	{
		return size;
	}

        /**
	 * This will write the .Txt gui to a .DAT.
	 * @param index NPC index.
	 */
	public static void loadNPCDefinition() {
            BufferedReader list;
		try {
			list = new BufferedReader(new FileReader("./data.txt"));
			String line;
			int i = 0;
			while(((line = list.readLine()) != null)) {
				String[] info = line.split(":");
				npcs.put(i = Integer.parseInt(info[0]),
                                        new NPCDefinition(info[1],
                                           i,
                                            Integer.parseInt(info[2]),
                                            Integer.parseInt(info[3])));
			}
			//maxRarity = i;
			list.close();
                        System.out.println("Npc Data Loaded.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			/*try {
				File f = new File("./Data.dat.gz");
				InputStream is = new GZIPInputStream(new FileInputStream(f));
				IoBuffer buf = IoBuffer.allocate(1024);
				buf.setAutoExpand(true);
				while(true) {
					byte[] temp = new byte[1024];
					int read = is.read(temp, 0, temp.length);
					if(read == -1) {
						break;
					} else {
						buf.put(temp, 0, read);
					}
				}
				buf.flip();
				while(buf.hasRemaining()) {
					int i = 0;
					npcs.put(i = buf.getInt(), new NPCDefinition(NPCXML.readRS2String(buf), i, buf.getInt(), buf.getInt()));
				}
			} catch (IOException ex) {
				System.out.println("Couldn't load Cache Definitions.");
			}*/
		}
}
