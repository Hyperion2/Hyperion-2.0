package org.hyperion.rs2.content;

import java.util.HashMap;
import java.util.Map;

import org.hyperion.rs2.model.Animation;
import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;

public class Consumable {

	/**
	 * Holds information on Food. - Fish, bread, pastry etc.
	 * @author phil
	 *
	 */
	enum Food {//A effect could be initiated.
		//TODO add effects.
			
			Shrimp(319, 1, false);
			
			short id;
			
			byte healAmt;
			
			boolean hpLimit;
			
			Food(int id, int heal, boolean limit) {
				this.id = (short) id;
				this.healAmt = (byte) heal;//highest healing food is 250 i believe
				this.hpLimit = limit;
			}
			
			/**
			 * Map of food info.
			 */
			private static Map<Short, Food> foods = new HashMap<Short, Food>();
			
			static {
				for(Food f : Food.values())
					foods.put(f.id, f);
			}
			
			/**
			 * Gets food data.
			 * @param id The foodId.
			 * @return The foodData.
			 */
			public static Food getInfo(int id) {
				return foods.get((short)id);
			}
	}
	
	/**
	 * Drinks as this handles all drinkable items.
	 * @author phil
	 *
	 */
	enum Drink {

		/*Potion Types
		 * 0 - Normal - Not a potion.
		 * 1 - Regular Combat Potion.
		 * 2 - Super Combat Potion.
		 * 3 - Prayer Potion.
		 * 4 - Skill Potion.
		 */
		Regular_Strength4(113, 115, new byte[] {2}, 1),
		Regular_Strength3(115, 117, new byte[] {2}, 1),
		Regular_Strength2(117, 119, new byte[] {2}, 1),
		Regular_Strength1(119, 229, new byte[] {2}, 1);
		
		short drinkId, consumeId;
		
		byte[] skills;
		
		byte potionType;
		
		Drink(int id, int nextId, byte[] skills, int potionType) {
			this.drinkId = (short) id;
			this.consumeId = (short) nextId;
			this.skills = skills;
			this.potionType = (byte) potionType;
		}
		
		/*
		 * Information on drinks.
		 */
		private static Map<Short, Drink> drinks = new HashMap<Short, Drink>();
		
		static {
			for(Drink d : Drink.values())
				drinks.put(d.drinkId, d);
		}
		
		/**
		 * Gets the info of the Drink.
		 * @param i Item id.
		 * @return Info.
		 */
		public static Drink getInfo(int i) {
			return drinks.get((short)i);
		}
	}
	
	/**
	 * Eats the food from the slot.
	 * @param player The player.
	 * @param itemSlot ItemSlot.
	 */
	public static void eatFood(Player player, int itemSlot) {
		Item clicked = player.getInventory().get(itemSlot);
		if(clicked == null || player.getFoodTimer() > 0) return;
		Food food = Food.getInfo(clicked.getId());
		if(food == null) return;
		player.applyHealthChange(food.healAmt, food.hpLimit);
		if(clicked.getCount() > 1)
			player.getInventory().remove(new Item(clicked.getId(), 1));
		else
			player.getInventory().set(itemSlot, null);
		player.playAnimation(Animation.create(829));
		player.setFoodTimer((byte)3);
		if(!player.isAutoRetaliating())
			player.setCurrentTarget(null);
		if(player.getLastAttack() < 3)
			player.setLastAttack(3);
		if(player.getAbstractMagicDelay() < 3)
			player.setAbstractMagicDelay(3);
		player.getActionSender().sendMessage("You eat the "+clicked.getDefinition().getName()+".");
	}
	
	/**
	 * Drinks the item.
	 * @param player The player.
	 * @param slot The item slot.
	 */
	public static void drinkLiquid(Player player, int slot) {
		Item clicked = player.getInventory().get(slot);
		if(clicked == null || player.getPotionTimer() > 0) return;
		Drink drink = Drink.getInfo(clicked.getId());
		if(drink == null) return;
		int drinkType = drink.potionType;
		player.playAnimation(Animation.create(829));
		for(int i = 0; i < drink.skills.length; i++) {
			int autoAdd = drinkType == 1 ? 3 : drinkType == 2 ? 5 : 7;
			int lv = player.getSkills().getLevel(drink.skills[i]);
			double generatedAmount = drinkType == 1 ? 0.10 : drinkType == 2 ? 0.15 : 0.25;
			int max = (int) (drinkType == 3 ? player.getSkills().getLevelForExperience(drink.skills[i]) : Math.floor(autoAdd + player.getSkills().getLevelForExperience(drink.skills[i]) + (player.getSkills().getLevelForExperience(drink.skills[i]) * generatedAmount)));
			if(max < Math.floor(autoAdd + (lv * generatedAmount) + lv))
				player.getSkills().setLevel(drink.skills[i], max);
			else
				player.getSkills().setLevel(drink.skills[i], (int) Math.floor(autoAdd + (lv * generatedAmount) + lv));
		}
		player.getInventory().set(slot, drink.consumeId != -1 && drink.consumeId != 65535 ? new Item(drink.consumeId) : null);
		player.getActionSender().sendMessage("You take a sip of your "+clicked.getDefinition().getName().replace("(4)", "").replace("(3)", "").replace("(2)", "").replace("(1)", "")+".");
	}
}
