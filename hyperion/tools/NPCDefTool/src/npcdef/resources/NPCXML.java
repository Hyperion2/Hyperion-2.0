/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package npcdef.resources;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;

/**
 *
 * @author phil
 */
public class NPCXML {

    public static List<NPC> defs = new ArrayList<NPC>();

    public static class NPC {
        private String name;
        private int combatLevel;
        private int tileSize;
        private int id;
        private int spawnTimer;
        private int maxHealth;
        private boolean immune;

        public NPC(String n, int cl, int ts, int a, boolean c, int d, int e) {
            this.name = n;
            this.combatLevel = cl;
            this.tileSize = ts;
            this.id = a;
            this.immune = c;
            this.maxHealth = d;
            this.spawnTimer = e;
        }

        public NPC(int a, boolean c, int d, int e) {
            this.id = a;
            this.name = NPCDefinition.npcs.get(a).getName();
            this.combatLevel = NPCDefinition.npcs.get(a).getCombatLevel();
            this.tileSize = NPCDefinition.npcs.get(a).getSize();
            this.immune = c;
            this.maxHealth = d;
            this.spawnTimer = e;
        }

        public int getID() {
            return id;
        }

		/**
		 * Gets the NPC maxHealth.
		 * @return The maxHealth.
		 */
		public int getMaxHealth()
		{
			return maxHealth;
		}

		/**
		 * Gets the NPC spawnTimer.
		 * @return The spawnTimer.
		 */
		public int getSpawnTimer()
		{
			return spawnTimer;
		}

                /**
		 * Gets the NPC spawnTimer.
		 * @return The spawnTimer.
		 */
		public int getTileSize()
		{
			return tileSize;
		}

                /**
		 * Gets the NPC spawnTimer.
		 * @return The spawnTimer.
		 */
		public int getCombatLevel()
		{
			return combatLevel;
		}

		/**
		 * Gets the Immune.
		 * @return The immune.
		 */
		public boolean isImmuneToPoison() {
			return immune;
		}

                /**
		 * Gets the NPC examine.
		 * @return The examine.
		 */
		public String getName()
		{
			return name;
		}
    }

    public static void createBinary() {
        try {
        	final DataOutputStream out = new DataOutputStream(new FileOutputStream("NPC.dat"));
			int i = 0;
                        //buf.putInt(defs.size());
                        for(NPC n : defs) {
                            out.writeShort(n.getID());
                           out.writeUTF(NPCDefinition.npcs.get(n.getID()).getName());
                            //putRS2String(buf, n.getExamine());
                            out.writeByte((n.isImmuneToPoison() ? 1 : 0));
                           out.writeShort(NPCDefinition.npcs.get(n.getID()).getCombatLevel());
                            out.writeByte(NPCDefinition.npcs.get(n.getID()).getSize());
                            out.writeShort(n.getMaxHealth());
                            out.writeByte(n.getSpawnTimer());
                            i++;
                        }
			out.close();
			System.out.println("Defintions created Successfully! Saved: "+i);
		} catch (IOException ex) {
			System.out.println("Couldn't save Definition File.");
		}
    }
    public static void loadBinary() {
        try {
			File f = new File("NPC.dat");
			DataInputStream is = new DataInputStream(new FileInputStream(f));
			while(is.available() != 0) {
                            int i = is.readShort();
                            String a = is.readUTF();
                            //String b = readRS2String(buf);
                            boolean c = is.readByte() == 1;
                            int d = is.readShort();
                            int e = is.readByte();
                            int g = is.readShort();
                            int h =  is.readByte();
                            defs.add(new NPC(a, d, e, i, c, g, h));
                        }
			System.out.println("Loaded: "+defs.size()+" NPC definitions.");
		} catch (IOException ex) {
			System.out.println("Couldn't load Definition File.");
		}
    }

    /**
	 * Reads a RuneScape string from the specified <code>InputStream</code>.
	 *
	 * @param in
	 *            The input stream.
	 * @return The string.
	 * @throws IOException
	 *             if an I/O error occurs, such as the stream closing.
	 */
    public static String readRS2String(IoBuffer buf) {
		StringBuilder bldr = new StringBuilder();
		byte b;
		while(buf.hasRemaining() && (b = buf.get()) != 10) {
			bldr.append((char) b);
		}
		return bldr.toString();
	}

	/**
	 * Writes a RuneScape string to a buffer.
	 * @param buf The buffer.
	 * @param string The string.
	 */
	public static void putRS2String(IoBuffer buf, String string) {
		buf.put(string.getBytes());
		buf.put((byte) 10);
	}
}
