package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.tgco.animalBook.gameObjects.Consumable.DropType;

public class Inventory {
	
	private static ArrayMap<Consumable.DropType, Array<Consumable>> inventory;
	private static final int MAX_CAPACITY = 99;
	
	/**
	 * Constructs a new Inventory
	 * <p>
	 * Creates a new inventory ArrayMap. Then stores Arrays of all consumable types
	 * within the inventory.
	 */
	
	public Inventory() {
		inventory = new ArrayMap<Consumable.DropType, Array<Consumable>>(false, 5);
		Consumable.DropType[] dropTypes = Consumable.DropType.values();
		for (int i = 0; i < dropTypes.length; i++){
			inventory.put(dropTypes[i], new Array<Consumable>(false, MAX_CAPACITY));
			/*for (int j = 0; j < 2; j++)
				inventory.get(Consumable.DropType.values()[i]).add(new Consumable(Consumable.DropType.values()[i]));
			*/ //for debugging purposes
		}
	}
	
	/**
	 * Adds an item to the inventory.
	 * <p>
	 * If the inventory has 99 items in it, it is considered full, and the adding of the item
	 * will fail.
	 * 
	 * @param consumable the item to be added to the inventory
	 * @return <code>true</code> if the item is successfully added, <code>false</code> if the inventory is full
	 */
	public boolean addItem(Consumable consumable){
		if (inventory.get(consumable.getType()).size == MAX_CAPACITY)
			return false;
		else{
			inventory.get(consumable.getType()).add(consumable);
			return true;
		}
	}
	
	/**
	 * Removes an item from the inventory.
	 * <p>
	 * If the inventory has 0 items in it, it is considered empty, and the removing of the item
	 * will fail.
	 * 
	 * @param consumable the item to be removed from the inventory
	 * @return <code>true</code> if the item is successfully removed, <code>false</code> if the inventory is already empty
	 */
	public boolean removeItem(Consumable.DropType dropType){
		if (inventory.get(dropType).size == 0)
			return false;
		else{
			inventory.get(dropType).removeIndex(0);
			return true;
		}
	}
	
	/**
	 * Returns the inventory ArrayMap
	 * 
	 * @return the inventory object
	 */
	public ArrayMap<Consumable.DropType, Array<Consumable>> getInventory(){
		return inventory;
	}
}