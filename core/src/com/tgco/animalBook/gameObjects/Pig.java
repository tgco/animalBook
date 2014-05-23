package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.gameObjects.Consumable.DropType;

public class Pig extends Animal {

	private static final DropType dropType = Consumable.DropType.BACON;

	private static final String texturePath = "objectTextures/pig.png";

	public Pig(Vector2 pos) {
		super(texturePath, pos);
		
		width = 100;
		height = 100;
		bounds = new Rectangle(position.x,position.y,width,height);
	}

	

}
