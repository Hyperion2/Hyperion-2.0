package org.hyperion.rs2.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.hyperion.rs2.Constants;
import org.hyperion.rs2.GameEngine;
import org.hyperion.rs2.GenericWorldLoader;
import org.hyperion.rs2.WorldLoader;
import org.hyperion.rs2.WorldLoader.LoginResult;
import org.hyperion.rs2.content.DegradeSystem;
import org.hyperion.rs2.content.NPCStyle;
import org.hyperion.rs2.content.ProjectileManager;
import org.hyperion.rs2.event.Event;
import org.hyperion.rs2.event.EventManager;
import org.hyperion.rs2.event.impl.CleanupEvent;
import org.hyperion.rs2.event.impl.CycleEvent;
import org.hyperion.rs2.event.impl.FloorItemEvent;
import org.hyperion.rs2.login.LoginServerConnector;
import org.hyperion.rs2.login.LoginServerWorldLoader;
import org.hyperion.rs2.model.container.Shops;
import org.hyperion.rs2.model.container.Trade;
import org.hyperion.rs2.net.PacketBuilder;
import org.hyperion.rs2.net.PacketManager;
import org.hyperion.rs2.packet.PacketHandler;
import org.hyperion.rs2.task.Task;
import org.hyperion.rs2.task.impl.SessionLoginTask;
import org.hyperion.rs2.tick.Tick;
import org.hyperion.rs2.tick.TickManager;
import org.hyperion.rs2.util.ConfigurationParser;
import org.hyperion.rs2.util.EntityList;
import org.hyperion.rs2.util.NameUtils;
import org.hyperion.util.BlockingExecutorService;

/**
 * Holds data global to the game world.
 * @author Graham Edgecombe
 *
 */
public class World {
	
	/**
	 * Logging class.
	 */
	private static final Logger logger = Logger.getLogger(World.class.getName());
	
	/**
	 * World instance.
	 */
	private static final World world = new World();
	
	/**
	 * Gets the world instance.
	 * @return The world instance.
	 */
	public static World getWorld() {
		return world;
	}
	
	/**
	 * An executor service which handles background loading tasks.
	 */
	private BlockingExecutorService backgroundLoader = new BlockingExecutorService(Executors.newSingleThreadExecutor());

	/**
	 * The game engine.
	 */
	private GameEngine engine;
	
	/**
	 * Holds this worlds information.
	 */
	private int worldId, port;
	
	/**
	 * The tickable manager.
	 */
	private TickManager tickManager;
	
	/**
	 * The event manager.
	 */
	private EventManager eventManager;
	
	/**
	 * The current loader implementation.
	 */
	private WorldLoader loader;
	
	/**
	 * INCOMPLETE!
	 * A list of lobby players.
	 */
	private EntityList<Player> lobby = new EntityList<Player>(Constants.MAX_PLAYERS);
	
	/**
	 * A list of connected players.
	 */
	private EntityList<Player> players = new EntityList<Player>(Constants.MAX_PLAYERS);
	
	/**
	 * A list of active NPCs.
	 */
	private EntityList<NPC> npcs = new EntityList<NPC>(Constants.MAX_NPCS);
	
	/**
	 * The login server connector.
	 */
	private LoginServerConnector connector;
	
	/**
	 * Creates the world and begins background loading tasks.
	 */
	public World() {
		backgroundLoader.submit(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				ItemDefinition.load();
				return null;
			}
		});
		backgroundLoader.submit(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				NPCDefinition.load();
				NPCStyle.init();
				return null;
			}
		});
		backgroundLoader.submit(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				DegradeSystem.init();
				ProjectileManager.init();
				return null;
			}
		});
		backgroundLoader.submit(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				Shops.loadShopFile();
				return null;
			}
		});
	}
	
	/**
	 * Gets the login server connector.
	 * @return The login server connector.
	 */
	public LoginServerConnector getLoginServerConnector() {
		return connector;
	}
	
	/**
	 * Gets the background loader.
	 * @return The background loader.
	 */
	public BlockingExecutorService getBackgroundLoader() {
		return backgroundLoader;
	}
	
	/**
	 * Initialises the world: loading configuration and registering global
	 * events.
	 * @param engine The engine processing this world's tasks.
	 * @throws IOException if an I/O error occurs loading configuration.
	 * @throws ClassNotFoundException if a class loaded through reflection was not found.
	 * @throws IllegalAccessException if a class could not be accessed.
	 * @throws InstantiationException if a class could not be created.
	 * @throws InvalidCacheException 
	 * @throws IllegalStateException if the world is already initialised.
	 */
	public void init(GameEngine engine) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if(this.engine != null) {
			throw new IllegalStateException("The world has already been initialised.");
		} else {
			this.engine = engine;
			this.eventManager = new EventManager(engine);
			this.tickManager = new TickManager();
			this.registerGlobalEvents();
			this.loadConfiguration();
		}
	}
	
	/**
	 * Loads server configuration.
	 * @throws IOException if an I/O error occurs.
	 * @throws ClassNotFoundException if a class loaded through reflection was not found.
	 * @throws IllegalAccessException if a class could not be accessed.
	 * @throws InstantiationException if a class could not be created.
	 */
	private void loadConfiguration() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		FileInputStream fis = new FileInputStream("data/configuration.cfg");
		try {
			ConfigurationParser p = new ConfigurationParser(fis);
			Map<String, String> mappings = p.getMappings();
			/*if(mappings.containsKey("worldLoader")) {
				String worldLoaderClass = mappings.get("worldLoader");
				Class<?> loader = Class.forName(worldLoaderClass);
				this.loader = (WorldLoader) loader.newInstance();
				logger.fine("WorldLoader set to : " + worldLoaderClass);
			} else {
				this.loader = new GenericWorldLoader();
				logger.fine("WorldLoader set to default");
			}*/
			if(Constants.CONNNECT_TO_LOGIN_SERVER)
				this.loader = new LoginServerWorldLoader();
			else
				this.loader = new GenericWorldLoader();
			Map<String, Map<String, String>> complexMappings = p.getComplexMappings();
			if(complexMappings.containsKey("packetHandlers")) {
				Map<Class<?>, Object> loadedHandlers = new HashMap<Class<?>, Object>();
				for(Map.Entry<String, String> handler : complexMappings.get("packetHandlers").entrySet()) {
					int id = Integer.parseInt(handler.getKey());
					Class<?> handlerClass = Class.forName(handler.getValue());
					Object handlerInstance;
					if(loadedHandlers.containsKey(handlerClass)) {
						handlerInstance = loadedHandlers.get(loadedHandlers.get(handlerClass));
					} else {
						handlerInstance = handlerClass.newInstance();
					}
					PacketManager.getPacketManager().bind(id, (PacketHandler) handlerInstance);
					logger.fine("Bound " + handler.getValue() + " to opcode : " + id);
				}
			}
			if(loader instanceof LoginServerWorldLoader) {
				connector = new LoginServerConnector(mappings.get("loginServer"));
				connector.connect(mappings.get("nodePassword"), worldId);
			}
		} finally {
			fis.close();
		}
	}
	
	/**
	 * Registers global events such as updating.
	 */
	private void registerGlobalEvents() {
		submit(new CycleEvent());
		submit(new CleanupEvent());
		submit(new FloorItemEvent());
	}
	
	/**
	 * Submits a new event.
	 * @param event The event to submit.
	 */
	public void submit(Event event) {
		this.eventManager.submit(event);
	}
	
	/**
	 * Submits a new task.
	 * @param task The task to submit.
	 */
	public void submit(Task task) {
		this.engine.pushTask(task);
	}
	
	/**
	 * Submits a new tickable.
	 * @param tickable The tickable to submit.
	 */
	public void submit(final Tick tickable) {
		submit(new Task() {
			@Override
			public void execute(GameEngine context) {
				// DO NOT REMOVE THIS CODE, IT PREVENTS CONCURRENT MODIFICATION
				// PROBLEMS!
				World.getWorld().tickManager.submit(tickable);
			}
		});
	}
	
	/**
	 * Gets the tickable manager.
	 * @return The tickable manager.
	 */
	public TickManager getTickManager() {
		return tickManager;
	}
	
	/**
	 * Gets the world loader.
	 * @return The world loader.
	 */
	public WorldLoader getWorldLoader() {
		return loader;
	}
	
	/**
	 * Gets the game engine.
	 * @return The game engine.
	 */
	public GameEngine getEngine() {
		return engine;
	}
	
	/**
	 * Loads a player's game in the work service.
	 * @param pd The player's details.
	 */
	public void load(final PlayerDetails pd) {
		engine.submitWork(new Runnable() {
			public void run() {
				LoginResult lr = loader.checkLogin(pd);
				int code = lr.getReturnCode() == 0 ? 2 : lr.getReturnCode();
				if(!NameUtils.isValidName(pd.getName())) {
					code = 11;
				}
				if(code != 2) {
					PacketBuilder bldr = new PacketBuilder();
					bldr.put((byte) code);
					pd.getSession().write(bldr.toPacket()).addListener(new IoFutureListener<IoFuture>() {
						@Override
						public void operationComplete(IoFuture future) {
							future.getSession().close(false);
						}
					});
				} else {
					lr.getPlayer().getSession().setAttribute("player", lr.getPlayer());
					if(lr.getReturnCode() != 0)
						loader.loadPlayer(lr.getPlayer());
					engine.pushTask(new SessionLoginTask(lr.getPlayer()));
				}
			}
		});
	}
	
	/**
	 * Registers a new npc.
	 * @param npc The npc to register.
	 */
	public void register(NPC npc) {
		npcs.add(npc);
	}
	
	/**
	 * Unregisters an old npc.
	 * @param npc The npc to unregister.
	 */
	public void unregister(NPC npc) {
		npcs.remove(npc);
		npc.destroy();
	}

	/**
	 * Registers a new player.
	 * @param player The player to register.
	 */
	public void register(final Player player) {
		// do final checks e.g. is player online? is world full?
		int returnCode = 2;
		if(!players.add(player)) {
			returnCode = 7;
			logger.info("Could not register player : " + player + " [world full]");
		}
		final int fReturnCode = returnCode;
		PacketBuilder bldr = new PacketBuilder();
		bldr.put((byte) returnCode);
		bldr.put((byte) player.getRights().toInteger());
		bldr.put((byte) 0);
		player.getSession().write(bldr.toPacket()).addListener(new IoFutureListener<IoFuture>() {
			@Override
			public void operationComplete(IoFuture future) {
				if(fReturnCode != 2) {
					player.getSession().close(false);
				} else {
					if(Constants.CONNNECT_TO_LOGIN_SERVER) {
						connector.sendPrivateMessagingStatus(player);
					} else {
						int mySetting = player.getSettings().getPrivateChatSetting()[1];
						for(Player p : players) {
							int setting = p.getSettings().getPrivateChatSetting()[1];
							if(mySetting == 2 && p.getFriends().contains(player.getNameAsLong())
									|| mySetting == 1 && p.getFriends().contains(player.getNameAsLong())
									&& !player.getFriends().contains(p.getNameAsLong()))
								p.getActionSender().sendPrivateMessageStatus(player.getNameAsLong(), -9);
							else if(p.getFriends().contains(player.getNameAsLong()))
								p.getActionSender().sendPrivateMessageStatus(player.getNameAsLong(), getWorldId());

							if(setting == 2 && player.getFriends().contains(p.getNameAsLong())
									|| setting == 1 && player.getFriends().contains(p.getNameAsLong())
									&& !p.getFriends().contains(player.getNameAsLong()))
								player.getActionSender().sendPrivateMessageStatus(p.getNameAsLong(), -9);
							else if(player.getFriends().contains(p.getNameAsLong()))
								player.getActionSender().sendPrivateMessageStatus(p.getNameAsLong(), getWorldId());
						}
					}
					player.getActionSender().sendLogin();
				}
			}
		});
		if(returnCode == 2) {
			logger.info("Registered player : " + player + " [online=" + players.size() + "]");
		}
	}
	
	/**
	 * Gets the player list.
	 * @return The player list.
	 */
	public EntityList<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Gets the npc list.
	 * @return The npc list.
	 */
	public EntityList<NPC> getNPCs() {
		return npcs;
	}

	/**
	 * Unregisters a player, and saves their game.
	 * @param player The player to unregister.
	 */
	public void unregister(final Player player) {
		player.getActionQueue().cancelQueuedActions();
		//final Player unregister = player;
		Trade.exitOutOfTrade(player, false);
		player.destroy();
		player.getSession().close(false);
		players.remove(player);
		logger.info("Unregistered player : " + player + " [online=" + players.size() + "]");
		engine.submitWork(new Runnable() {
			public void run() {
				loader.savePlayer(player);
				if(World.getWorld().getLoginServerConnector() != null && Constants.CONNNECT_TO_LOGIN_SERVER) {
					World.getWorld().getLoginServerConnector().disconnected(player.getName());
				} else {
					//Remove user online
					for(Player p : World.getWorld().getPlayers())
						if(p.getFriends().contains(player.getNameAsLong()))
							p.getActionSender().sendPrivateMessageStatus(p.getNameAsLong(), -9);
				}
			}
		});
	}

	/**
	 * Handles an exception in any of the pools.
	 * @param t The exception.
	 */
	public void handleError(Throwable t) {
		logger.severe("An error occurred in an executor service! The server will be halted immediately.");
		t.printStackTrace();
		System.exit(1);
	}

	/**
	 * @param worldId the worldId to set
	 */
	public void setWorldId(int worldId) {
		this.worldId = worldId;
	}

	/**
	 * @return the worldId
	 */
	public int getWorldId() {
		return worldId;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the lobby
	 */
	public EntityList<Player> getLobby() {
		return lobby;
	}
	
}
