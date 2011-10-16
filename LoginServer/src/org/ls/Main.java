package org.ls;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.ls.clan.Clan;

public class Main {

	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(Main.class.getName());
	
	/**
	 * Holds information on the current Clans.
	 */
	public static Map<String, Clan> clans = new HashMap<String, Clan>();
	
	/**
	 * The entry point of the program.
	 * @param args The command line arguments.
	 * @throws IOException if an I/O error occurs.
	 */
	public static void main(String[] args) throws IOException {
		logger.info("Starting Master Server...");
		LoginServer.loadSettings();
		new LoginServer().bind().start();
		System.gc();
	}
}
