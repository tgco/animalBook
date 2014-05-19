package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.graphics.Texture;

public abstract class Animal extends Movable {
	
	//rate in percent that a child animal is spawned instead of a consumable
	private float fertilityRate;
	
	//interval between drop chances
	private float dropInterval;
	

	public Animal(Texture texture) {
		super(texture);
	}
	
	//Create a consumable or new animal
	public void drop() {
		
	}

}
