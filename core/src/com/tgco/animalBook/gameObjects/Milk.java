package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.math.Vector2;

public class Milk extends Consumable {
	private static final String texturePath = "objectTextures/milk.png";

	public Milk(Vector2 position) {
		super(texturePath, position);
	}

	@Override
	public DropType getType() {
		return DropType.MILK;
	}
}
