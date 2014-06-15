package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.gameObjects.Consumable.DropType;


public class Dog extends Animal {
	private static final String texturePath = "objectTextures/dog.png";

	public Dog(Vector2 position, int animalX, int animalY) {
		super(texturePath, position, animalY, animalY);
		width = .04f*Gdx.graphics.getWidth();
		height = .147f*Gdx.graphics.getHeight();
	}
	
//	@Override
//	public void move(float cameraSpeed, float delta) {
//		if (position.x > (Gdx.graphics.getWidth() - width)) {
//			currentTarget = new Vector2(0,0);
//		}
//		if (position.x < width) {
//			currentTarget = new Vector2(Gdx.graphics.getWidth(),0);
//		}
//		
//		position.lerp(currentTarget, delta);
//		
//	}

	@Override
	public DropType getDropType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetTexture() {
		// TODO Auto-generated method stub
		
	}

}
