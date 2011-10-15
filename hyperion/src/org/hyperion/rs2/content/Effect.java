package org.hyperion.rs2.content;

import org.hyperion.rs2.model.Entity;

/**
 * Effects/
 * @author phil
 *
 */
public enum Effect {
	//TODO implement this.
	
	Example {
		void execute(Entity e) {
			//body goes here.
		}
	};
	
	/**
	 * Void to be executed.
	 * @param e
	 */
	abstract void execute(Entity e);
}
