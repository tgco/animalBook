package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.gameObjects.Consumable.DropType;

public class Goose extends Animal {

	private static final DropType dropType = Consumable.DropType.EGG;
	
	private static final String texturePath = "objectTextures/goose.png";
	
	public Goose(Vector2 pos) {
		super(texturePath, pos);
		
		
		if (AnimalBookGame.tapControls)
			speed = 1/2f;
		else
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
