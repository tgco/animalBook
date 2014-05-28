package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.gameObjects.Consumable.DropType;

public class Goat extends Animal{
	
	/**
	 * The type of item this animal drops
	 */
	private static final DropType dropType = Consumable.DropType.CHEESE;

	/**
	 * Path to the texture for this animal
	 */
	private static final String texturePath = "objectTextures/goat.jpg";
	
	/**
	 * Constructor that takes a position
	 * 
	 * @param pos the desired position in world coordinates for this animal
	 */
	public Goat(Vector2 pos) {
		super(texturePath, pos);
		
		speed = 1/14f;
		width = .093f*Gdx.graphics.getWidth();
		height = .147f*Gdx.graphics.getHeight();
		bounds = new Rectangle(position.x - width/2,position.y - height/2,width,height);
	}
	
	/**
	 * Returns the type of item this animal drops
	 * 
	 * @return		the drop type for this animal
	 */
	@Override
	public DropType getDropType() {
		return dropType;
	}

	
	

}
