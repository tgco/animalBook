package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.math.Vector2;

public class Cheese extends Consumable {
	private static final String texturePath = "objectTextures/cheese.png";

	public Cheese(Vector2 position) {
		super(texturePath, position);
	}

}
