package org.hyperion.util;

import java.io.InputStream;
import java.io.OutputStream;

import org.hyperion.rs2.content.DegradeSystem;
import org.hyperion.rs2.content.NPCStyle;
import org.hyperion.rs2.content.Projectile;
import org.hyperion.rs2.content.NPCStyle.Style;

import com.thoughtworks.xstream.XStream;

/**
 * Has the xstream object.
 * @author Graham
 *
 */
public class PersistenceManager {
	private PersistenceManager() {}
	
	private static XStream xstream;
	
	static {
		xstream = new XStream();
		xstream.alias("degrade", DegradeSystem.class);
		xstream.alias("proj", Projectile.class);
		xstream.alias("attack", NPCStyle.class);
		xstream.alias("style", Style.class);
	}
	
	public static void save(Object object, OutputStream out) {
		xstream.toXML(object, out);
	}
	
	public static Object load(InputStream in) {
		return xstream.fromXML(in);
	}

}
