package org.ls;



import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.ls.net.LoginConnectionHandler;

/**
 * The login server.
 * @author Graham Edgecombe
 *
 */
public class LoginServer {
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(LoginServer.class.getName());
	
	/**
	 * The acceptor.
	 */
	private IoAcceptor acceptor = new NioSocketAcceptor();
	
	/**
	 * The task queue.
	 */
	private BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<Runnable>();
	
	/**
	 * Creates the login server.
	 */
	public LoginServer() {
		logger.info("Building "+Constants.LOGIN_SERVER_NAME+" Login Server...");
		acceptor.setHandler(new LoginConnectionHandler(this));
	}

	/**
	 * Binds the login server to the default port.
	 * @return The login server instance, for chaining.
	 * @throws IOException if an I/O error occurs.
	 */
	public LoginServer bind() throws IOException {
		logger.info("Binding to port : " + Constants.LOGIN_PORT + "...");
		acceptor.bind(new InetSocketAddress(Constants.LOGIN_PORT));
		return this;
	}
	
	/**
	 * Starts the login server.
	 */
	public void start() {
		logger.info("Ready.");
		while(true) {
			try {
				tasks.take().run();
			} catch(InterruptedException e) {
				continue;
			}
		}
	}

	/**
	 * Pushses a task onto the queue.
	 * @param runnable The runnable.
	 */
	public void pushTask(Runnable runnable) {
		tasks.add(runnable);
	}
	
	/**
	 * Loads the Settings for the loginServer
	 */
	public static void loadSettings() {	
		try {
			Properties pro = new Properties();
			pro.load(new FileInputStream("settings.ini"));
			Constants.LOGIN_SERVER_NAME = pro.getProperty("name");
			Constants.LOGIN_PORT = Integer.parseInt(pro.getProperty("port"));
			Constants.CHAR_FILE_EXTENTION = pro.getProperty("extension");
			Constants.SHARE_NODE_FILES = Boolean.valueOf(pro.getProperty("split"));
			Constants.PASSWORD = pro.getProperty("pass");
			pro.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
