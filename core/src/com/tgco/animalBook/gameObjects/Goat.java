package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tgco.animalBook.gameObjects.Consumable.DropType;

public class Goat extends Animal{
	
	private static final DropType dropType = Consumable.DropType.CHEESE;

	private static final Texture texture = new Texture(Gdx.files.internal("objectTextures/goat.png"));
	
	public Goat() {
		super(texture);
	}

}
