package org.ls.login;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.mina.core.buffer.IoBuffer;
import org.ls.Constants;
import org.ls.utils.NameUtils;
import org.ls.utils.Streams;

public class Account {

	/**
	 * Checks if the file has the same name, and password.
	 * @param s The name.
	 * @param p The password.
	 * @return The returnCode.
	 */
	public static int[] checkFile(String s, String p, int nodeId) {
		int code = 0;
		int rights = 0;
		File f = new File("data/savedGames/"+(Constants.SHARE_NODE_FILES ? "" : "node_"+nodeId+"/") + NameUtils.formatNameForProtocol(s) + Constants.CHAR_FILE_EXTENTION);
		if(f.exists()) {
			if(Constants.CHAR_FILE_EXTENTION.equals(".dat.gz")) {
				try {
					InputStream is = new GZIPInputStream(new FileInputStream(f));
					String name = Streams.readRS2String(is);
					String pass = Streams.readRS2String(is);
					rights = is.read();
					code = 2;
					if(!name.equals(NameUtils.formatName(s))) {
						code = 3;
					}
					if(!pass.equals(p)) {
						code = 3;
					}
				} catch(IOException ex) {
					code = 11;
				}
			}
		}
		return new int[] {code, rights};
	}
	
	/**
	 * Loads the players file into a buffer to be sent.
	 * @param name The users name.
	 * @return account buffer.
	 */
	public static IoBuffer loadPlayerData(String name, int nodeId) {
		try {
			File f = new File("data/savedGames/" + (Constants.SHARE_NODE_FILES ? "" : "node_"+nodeId+"/") + NameUtils.formatNameForProtocol(name) + Constants.CHAR_FILE_EXTENTION);
			if(Constants.CHAR_FILE_EXTENTION.equals(".dat.gz")) {
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
				is.close();
				return buf;
			}
			//buf.flip();
		} catch(Exception e) {
			//error, underflow etc.
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Reads the file then sends it.
	 * @param name The player.
	 * @param nodeId NodeId.
	 * @return if read correctly.
	 */
	public static BufferedReader readPlayerfile(String name, int nodeId) {
		try {
			BufferedReader read = new BufferedReader(new FileReader("data/savedGames/" + (Constants.SHARE_NODE_FILES ? "" : "node_"+nodeId+"/") + NameUtils.formatNameForProtocol(name) + Constants.CHAR_FILE_EXTENTION));
			read.close();
			return read;
		} catch(Exception e) {
			
		}
		return null;
	}
	
	/**
	 * Writer the Character file, i doubt this is a good way.
	 * @param name The name of the player.
	 * @param nodeId NodeId.
	 * @param r Data.
	 */
	public static void writeCharacterFile(String name, int nodeId, BufferedWriter r) {
		try {
			r.close();//Wil simply write the file.
		} catch(Exception e) {
			
		}
	}
	
	/**
	 * Saves the players data.
	 * @param save User to be saved.
	 * @param buf User's data to be saved.
	 * @return If saved went smoothly.
	 */
	public static boolean savePlayer(String save, IoBuffer buf, int nodeId) {
		try {
			if(Constants.CHAR_FILE_EXTENTION.equals(".dat.gz")) {
				OutputStream os = new GZIPOutputStream(new FileOutputStream("data/savedGames/" + (Constants.SHARE_NODE_FILES ? "" : "node_"+nodeId+"/") + NameUtils.formatNameForProtocol(save) + Constants.CHAR_FILE_EXTENTION));
				byte[] data = new byte[buf.limit()];
				buf.get(data);
				os.write(data);
				os.flush();
				os.close();
				return true;
			}
		} catch(IOException ex) {
			//error, underflow/cannot save
		}
		return false;
	}
}
