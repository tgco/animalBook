package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.gameObjects.Consumable.DropType;

public class Pig extends Animal {

	private static final DropType dropType = Consumable.DropType.BACON;

	private static final Texture texture = new Texture(Gdx.files.internal("objectTextures/pig.png"));

	public Pig() {
		super(texture);
	}

	

}
