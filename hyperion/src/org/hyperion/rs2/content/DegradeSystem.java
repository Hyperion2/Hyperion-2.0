package org.hyperion.rs2.content;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.hyperion.rs2.Constants;
import org.hyperion.rs2.model.Definitions;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.util.PersistenceManager;

public class DegradeSystem {
	
	private short[] items;
	private short degradeValue;
	private String degradeMessage;
	
	/**
	 * Holds the degradable information.
	 */
	public static Map<Short, DegradeSystem> degradables;
	
	/**
	 * Logger instance.
	 */
	public static final Logger logger = Logger.getLogger(DegradeSystem.class.getName());
	
	@SuppressWarnings("unchecked")
	public static void init() {
		if(!Constants.DEGRADING_ENABLE) {
			logger.info("Degradable item's disabled. CAN BE ENABLED/DISABLED IN settings.ini.");
			return;
		}
		try {
			List<DegradeSystem> loadedData = (List<DegradeSystem>) PersistenceManager.load(new FileInputStream("./data/degradables.xml"));
			degradables = new HashMap<Short, DegradeSystem>(loadedData.size());
			for (DegradeSystem data : loadedData)
			{
				for(short s : data.items) {
					degradables.put(s, data);
				}
			}
			logger.info("Successfully loaded "+loadedData.size() +" degradable item's information.");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks degradeables.
	 * @param player The player.
	 */
	public static void checkDegrade(Player player) {
		if(!Constants.DEGRADING_ENABLE || degradables == null || degradables.size() == 0) return;
		for(Item i : player.getEquipment().toArray()) {
			if(i != null) {
				if(i.getDegrade() <= 0 || i.getDegrade() == 65535) continue;
				if(i.getDegrade() == 1) {
					DegradeSystem d = degradables.get((short)i.getId());
					if(d == null) continue;
					int nextStage = -1;
					boolean isContinue = false;;
					for(int i3 = 0; i3 < d.items.length; i3++) {
						if(d.items[i3] == (short) i.getId()) {
							if(d.items.length < i3+1) {
								nextStage = i3+1;
								isContinue = i.getDefinition().getEquipmentType() == Definitions.forId(d.items[nextStage]).getEquipmentType();
							}
							break;
						}
					}
					player.getActionSender().sendMessage(nextStage == -1 ? "Your "+i.getDefinition().getName().trim()+" has turned into dust." : !isContinue ? "Your "+i.getDefinition().getName().trim()+" changed to a "+Definitions.forId(d.items[nextStage]).getName().trim() : d.degradeMessage);
					if(nextStage == -1) {
						i = null;
					} else {
						i.transform(d.items[nextStage]);
						i.setDegrade(!isContinue ? 0 : d.degradeValue);
					}
					if(i == null) continue;
				}
				i.setDegrade(i.getDegrade()-1);
			}
		}
		player.getEquipment().fireItemsChanged();
	}
}
