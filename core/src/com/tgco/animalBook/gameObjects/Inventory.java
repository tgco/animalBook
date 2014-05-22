package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class Inventory {
	
	private static ArrayMap<Consumable.DropType, Array<Consumable>> inventory;
	
	public Inventory() {
		inventory = new ArrayMap<Consumable.DropType, Array<Consumable>>(false, 5);
		Consumable.DropType[] dropTypes = Consumable.DropType.values();
		for (int i = 0; i < dropTypes.length; i++){
			inventory.put(dropTypes[i], new Array<Consumable>(false, 100));
		}
	}
	
	public boolean addItem(Consumable consumable){
		if (inventory.get(consumable.getType()).size == 100)
			return false;
		else{
			inventory.get(consumable.getType()).add(consumable);
			return true;
		}
	}
	
	public boolean removeItem(Consumable.DropType dropType){
		if (inventory.get(dropType).size == 0)
			return false;
		else{
			inventory.get(dropType).removeIndex(0);
			return true;
		}
	}
	
	public ArrayMap<Consumable.DropType, Array<Consumable>> getInventory(){
		return inventory;
	}
}