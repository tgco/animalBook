package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

//Dropped by animals for collection by the player
public abstract class Consumable extends Drawable {
	
	public Consumable(String texturePath){
		super(texturePath);
	}
	public Consumable(String texturePath, Vector2 position){
		super(texturePath);
		this.position = position;
	}
	
	public enum DropType {
		MILK, EGG, BACON, CHEESE, WOOL;
	}

}
