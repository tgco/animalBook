package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.gameObjects.Consumable.DropType;

public class Pig extends Animal {

	private static final DropType dropType = Consumable.DropType.BACON;

	private static final String texturePath = "objectTextures/pig.png";

	public Pig(Vector2 pos) {
		super(texturePath, pos);
		
		speed = 1/10f;
		width = .093f*Gdx.graphics.getWidth();
		height = .147f*Gdx.graphics.getHeight();
		bounds = new Rectangle(position.x - width/2,position.y - height/2,width,height);
	}

	@Override
	public DropType getDropType() {
		return dropType;
	}

}
