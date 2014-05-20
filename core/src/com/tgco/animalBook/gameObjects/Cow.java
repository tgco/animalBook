package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.tgco.animalBook.gameObjects.Consumable.DropType;

public class Cow extends Animal {

	private static final DropType dropType = Consumable.DropType.MILK;

	private static final Texture texture = new Texture(Gdx.files.internal("objectTextures/cow.png"));

	public Cow() {
		super(texture);
	}



}
