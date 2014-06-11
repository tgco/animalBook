package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;


public class Wind extends Movable{
	private final static String TEXTURE_PATH = "objectTextures/swipe.png";
	private final float SPEED = 1f;

	public Wind(String texturePath) {
		super(TEXTURE_PATH);
		
		//default speed
		speed = SPEED;
		moveBias = 1f;
		rotation = 0;
		rotationSpeed = 50f;
	}
}
