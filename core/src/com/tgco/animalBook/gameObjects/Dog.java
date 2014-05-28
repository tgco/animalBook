package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.gameObjects.Consumable.DropType;

public class Dog extends Animal {

	/**
	 * Constructor that takes the texture path and position
	 * 
	 * @param texturePath path to the texture for this animal
	 * @param position	  the desired position of this animal in world coordinates
	 */
	public Dog(String texturePath, Vector2 position) {
		super(texturePath, position);
		
		width = .093f*Gdx.graphics.getWidth();
		height = .147f*Gdx.graphics.getHeight();
		
		bounds = new Rectangle(position.x - width/2,position.y - height/2,width,height);
	}
	
	/**
	 * Returns the type of item this animal drops
	 * 
	 * @return		null since Dog will not drop items
	 */
	@Override
	public DropType getDropType() {
		return null;
	}

}
