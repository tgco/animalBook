package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.gameObjects.Consumable.DropType;

public class Goose extends Animal {

	private static final DropType dropType = Consumable.DropType.EGG;
	
	private static final String texturePath = "objectTextures/goose.png";
	
	public Goose() {
		super(texturePath);
		super.position = new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		super.currentTarget = position;
		previousTarget = position;
	}
	

}
