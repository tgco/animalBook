package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.gameObjects.Consumable.DropType;

public class Sheep extends Animal {

	private static final DropType dropType = Consumable.DropType.WOOL;

	private static final String texturePath = "objectTextures/sheep.png";

	public Sheep(Vector2 pos) {
		super(texturePath, pos);
		
		speed = 1/14f;
		width = 100;
		height = 100;
		bounds = new Rectangle(position.x - width/2,position.y - height/2,width,height);
	}

	@Override
	public DropType getDropType() {
		return dropType;
	}
	

}
