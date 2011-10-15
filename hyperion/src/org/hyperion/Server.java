package org.hyperion;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.RS2Server;
import org.hyperion.rs2.model.World;

/**
 * A class to start both the file and game servers.
 * @author Graham Edgecombe
 *
 */
public class Server {
	
	/**
	 * The protocol version.
	 */
	public static int VERSION = 317;
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(Server.class.getName());
	
	/**
	 * The entry point of the application.
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		//new ItemCompress();
		logger.info("Starting Hyperion...");
		Properties pro = new Properties();
		logger.info("loading settings...");
		try {
			pro.load(new FileInputStream("settings.ini"));
			Constants.SERVER_NAME = pro.getProperty("name");
			Constants.WEBSITE = pro.getProperty("website");
			Constants.OWNER = pro.getProperty("owner");
			Constants.PVP_WORLD = Boolean.valueOf(pro.getProperty("pvp-world"));
			Constants.DEGRADING_ENABLE = Boolean.valueOf(pro.getProperty("degrade"));
			Constants.CONSTITUTION_ENABLE = Boolean.valueOf(pro.getProperty("constitution"));
			Constants.CONNNECT_TO_LOGIN_SERVER = Boolean.valueOf(pro.getProperty("loginserver"));
			Constants.UNLIMITED_RUNNING = Boolean.valueOf(pro.getProperty("unlimRun"));
			Constants.LOBBY_ENABLED = Boolean.valueOf(pro.getProperty("lobby"));
			Constants.SKILL_EXPERIENCE = Double.valueOf(pro.getProperty("skillxp"));
			Constants.COMBAT_EXPERIENCE = Double.valueOf(pro.getProperty("combatxp"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("loading background tasks...");
		World.getWorld(); // this starts off background loading
		try {
			World.getWorld().setPort(Integer.parseInt(args[1]));
			World.getWorld().setWorldId(Integer.parseInt(args[0]));
			//new LoginServer().bind().start();
			//new FileServer().bind().start();
			new RS2Server().start();
		} catch(Exception ex) {
			logger.log(Level.SEVERE, "Error starting Hyperion.", ex);
			System.exit(1);
		}
	}

}
