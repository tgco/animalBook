package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.math.Vector2;

public class Bacon extends Consumable {
	private static final String texturePath = "gameObject/bacon.png";

	public Bacon(Vector2 position) {
		super(texturePath, position);
	}

}