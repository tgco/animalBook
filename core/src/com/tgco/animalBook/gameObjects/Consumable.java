package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.graphics.Texture;

//Dropped by animals for collection by the player
public class Consumable extends Drawable {
	
	//Drop type enumeration
	public enum DropType {
		MILK, EGG, BACON, CHEESE, WOOL;
	}

	public Consumable(Texture texture) {
		super(texture);
	}

}
