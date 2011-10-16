package org.ls;

/**
 * Holds global server constants.
 * @author Graham Edgecombe
 *
 */
public class Constants {
	
	/**
	 * Login Server name.
	 */
	public static String LOGIN_SERVER_NAME = "";
	
	/**
	 * Tells the Server how to load/save character files.
	 */
	public static String CHAR_FILE_EXTENTION = "";
	
	/**
	 * Whether or not Node files will be shared or seperated.
	 */
	public static boolean SHARE_NODE_FILES = false;
	
	/**
	 * Login Server Password.
	 */
	public static String PASSWORD = "";
	
	/**
	 * Max Aloud Players on 1 Node.
	 */
	public static final int MAX_PLAYERS = 2000;

	/**
	 * Port the login Server is connected On.
	 */
	public static int LOGIN_PORT = 0;
	
	/**
	 * An array of valid characters in a long username.
	 */
	public static final char VALID_CHARS[] = { '_', 'a', 'b', 'c', 'd',
		'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
		'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
		'4', '5', '6', '7', '8', '9', '!', '@', '#', '$', '%', '^', '&',
		'*', '(', ')', '-', '+', '=', ':', ';', '.', '>', '<', ',', '"',
		'[', ']', '|', '?', '/', '`' };
	
	/**
	 * Packed text translate table.
	 */
	public static final char XLATE_TABLE[] = { ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n',
		's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b',
		'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-',
		'&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"',
		'[', ']' };

}
