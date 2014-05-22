package com.tgco.animalBook.gameObjects;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public class Egg extends Consumable {
	private static final Random rand = new Random();

	public Egg(String texturePath) {
		super(texturePath);
		// TODO Auto-generated constructor stub
	}

	public Egg(String texturePath, Vector2 position) {
		super(texturePath, position);
		// TODO Auto-generated constructor stub
	}
	
	public Boolean attemptHatching(){	
		if (rand.nextInt(10000) == 0)
			return true;
		return false;
	}

}
