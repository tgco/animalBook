package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.gameObjects.Consumable.DropType;

public class Dog extends Animal {

	public Dog(String texturePath, Vector2 position) {
		super(texturePath, position);
		
		width = .093f*Gdx.graphics.getWidth();
		height = .147f*Gdx.graphics.getHeight();
		
		bounds = new Rectangle(position.x - width/2,position.y - height/2,width,height);
	}
	
	@Override
	public DropType getDropType() {
		return null;
	}

}
