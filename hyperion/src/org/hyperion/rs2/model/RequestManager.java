package org.hyperion.rs2.model;

/**
 * The request manager manages 
 * @author Graham Edgecombe
 *
 */
public class RequestManager {
	
	/**
	 * Represents the different types of request.
	 * @author Graham Edgecombe
	 *
	 */
	public enum RequestType {
		
		/**
		 * A trade request.
		 */
		TRADE("tradereq"),
		
		/**
		 * A duel request.
		 */
		DUEL("duelreq");
		
		/**
		 * The client-side name of the request.
		 */
		private String clientName;
		
		/**
		 * Creates a type of request.
		 * @param clientName The name of the request client-side.
		 */
		private RequestType(String clientName) {
			this.clientName = clientName;
		}
		
		/**
		 * Gets the client name.
		 * @return The client name.
		 */
		public String getClientName() {
			return clientName;
		}
		
	}
	
	/**
	 * Holds the different states the manager can be in.
	 * @author Graham Edgecombe
	 *
	 */
	public enum RequestState {
		
		/**
		 * Nobody has offered a request.
		 */
		NORMAL,
		
		/**
		 * Somebody has offered some kind of request.
		 */
		REQUESTED,
		
		/**
		 * The player is participating in an existing request of this type, so
		 * cannot accept new requests at all.
		 */
		PARTICIPATING;
		
	}
	
	/**
	 * The player.
	 */
	private Player player;
	
	/**
	 * The current state.
	 */
	private RequestState state = RequestState.NORMAL;
	
	/**
	 * The current request type.
	 */
	private RequestType requestType;
	
	/**
	 * The current 'acquaintance'.
	 */
	private Player acquaintance;
	
	/**
	 * The current 'requestStage'.
	 */
	private int requestStage;
	
	/**
	 * Creates the request manager.
	 * @param player The player whose requests the manager is managing.
	 */
	public RequestManager(Player player) {
		this.player = player;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(RequestState state) {
		this.state = state;
	}

	/**
	 * @return the state
	 */
	public RequestState getState() {
		return state;
	}

	/**
	 * @param acquaintance the acquaintance to set
	 */
	public void setAcquaintance(Player acquaintance) {
		this.acquaintance = acquaintance;
	}

	/**
	 * @return the acquaintance
	 */
	public Player getAcquaintance() {
		return acquaintance;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param requestType the requestType to set
	 */
	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	/**
	 * @return the requestType
	 */
	public RequestType getRequestType() {
		return requestType;
	}

	/**
	 * @param requestStage the requestStage to set
	 */
	public void setRequestStage(int requestStage) {
		this.requestStage = requestStage;
	}

	/**
	 * @return the requestStage
	 */
	public int getRequestStage() {
		return requestStage;
	}
	
}