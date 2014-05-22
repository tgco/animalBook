package com.tgco.animalBook.gameObjects;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public class Egg extends Consumable {
	private static final Random rand = new Random();
	private static final String texturePath = "gameTexture/egg.png";

	public Egg(Vector2 position) {
		super(texturePath, position);
	}
	
	public Boolean attemptHatching(){	
		if (rand.nextInt(10000) == 0)
			return true;
		return false;
	}
}
