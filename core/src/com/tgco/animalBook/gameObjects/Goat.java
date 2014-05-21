package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.gameObjects.Consumable.DropType;

public class Goat extends Animal{
	
	private static final DropType dropType = Consumable.DropType.CHEESE;

	private static final String texturePath = "objectTextures/goat.jpg";
	
	public Goat(Vector2 pos) {
		super(texturePath, pos);
	}

	
	

}
