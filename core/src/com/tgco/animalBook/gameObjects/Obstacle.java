package com.tgco.animalBook.gameObjects;

import java.util.Random;

import com.badlogic.gdx.math.Rectangle;

public class Obstacle extends ABDrawable {
	
	private static final String texturePath = "objectTextures/player.png";
	private static final Random rand = new Random();

	public Obstacle() {
		super(texturePath);
		
		
	}

}
