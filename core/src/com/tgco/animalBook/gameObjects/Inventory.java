package com.tgco.animalBook.gameObjects;

import java.util.ArrayList;
import java.util.TreeMap;

public class Inventory {

	private static TreeMap<Consumable.DropType, ArrayList<Consumable>> inventory;
	public Inventory() {
		Consumable.DropType[] dropTypes = Consumable.DropType.values();
		for (int i = 0; i < dropTypes.length; i++){
			inventory.put(dropTypes[i], new ArrayList<Consumable>());
		}
	}


}
