package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.gameObjects.Consumable.DropType;


public class Dog extends Animal {
	private static final String texturePath = "objectTextures/cow.png";

	public Dog(Vector2 position, int animalX, int animalY) {
		super(texturePath, position, animalX, animalY);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void move(float cameraSpeed, float delta) {
		if (position.x > (Gdx.graphics.getWidth() - width)) {
			currentTarget = new Vector2(0,0);
		}
		
	}

	@Override
	public void resetTexture() {
		// TODO Auto-generated method stub

	}

	@Override
	public DropType getDropType() {
		// TODO Auto-generated method stub
		return null;
	}

}
