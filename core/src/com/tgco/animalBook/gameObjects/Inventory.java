package com.tgco.animalBook.gameObjects;

import java.util.TreeMap;

import com.badlogic.gdx.utils.Array;

public class Inventory {

	private static TreeMap<Consumable.DropType, Array<Consumable>> inventory;
	public Inventory() {
		Consumable.DropType[] dropTypes = Consumable.DropType.values();
		for (int i = 0; i < dropTypes.length; i++){
			inventory.put(dropTypes[i], new Array<Consumable>());
		}
	}


}
