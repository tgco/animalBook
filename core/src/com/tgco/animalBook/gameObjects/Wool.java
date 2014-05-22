package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.math.Vector2;

public class Wool extends Consumable {
	private static final String texturePath = "objectTextures/wool.png";

	public Wool(Vector2 position) {
		super(texturePath, position);
	}

	@Override
	public DropType getType() {
		return DropType.WOOL;
	}
}
