package org.hyperion.rs2.content;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hyperion.rs2.model.Entity;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.region.RegionManager;
import org.hyperion.util.PersistenceManager;


/**
 * Projectile Manager.
 * @author phil
 *
 */
public class ProjectileManager {

	/**
	 * Holds Projectiles. NPC NOT SUPPORTED - NEW ATTACK FORMAT NEEDED.
	 * We are using a String to represent the Projectile NPC<Projectile GFX>.
	 */
	private static Map<Short, Projectile> projectiles;
	
	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(ProjectileManager.class.getName());
	
	@SuppressWarnings("unchecked")
	public static void init() {
		try {
			List<Projectile> loadedData = (List<Projectile>) PersistenceManager.load(new FileInputStream("./data/projectiles.xml"));
			projectiles = new HashMap<Short, Projectile>(loadedData.size());
			for (Projectile data : loadedData)
			{
				projectiles.put(data.getGfx(), data);
			}
			logger.info("Successfully loaded "+loadedData.size() +" projectile definitions.");
		} catch(Exception e) {
			
		}
	}
	
	/**
	 * Gets the Projectile based on name.
	 * @param projName The name of projectile data.
	 * @return
	 */
	public static Projectile getProjectile(short proj) {
		return projectiles.get(proj);
	}
	
	/**
	 * Creates a Projectile in the region of the caster.
	 * @param caster The caster.
	 * @param target The target.
	 * @param proj The distinct projectile gfx.
	 */
	public static void createProjectile(Entity caster, Entity target, Projectile projectile) {
		if(projectile == null) return;
		for(Player player : RegionManager.getLocalPlayers(target.getLocation()))
			player.getActionSender().sendProjectile(caster.getLocation().applySizeDistoration(caster.getSize()), target.getLocation(), target, projectile.getGfx(), projectile.getHeight()[0], projectile.getHeight()[1], projectile.getDelay(), projectile.getSlowness(), projectile.getCurve(), projectile.getRadius());
	}
}
