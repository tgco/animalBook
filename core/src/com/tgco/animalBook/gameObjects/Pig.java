package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.gameObjects.Consumable.DropType;

/**
 * Implements correct drop type, texture path, speed and size
 * for this type of animal.
 * 
 * @author
 *
 * 
 */
public class Pig extends Animal {

	/**
	 * The type of item this animal drops
	 */
	private static final DropType dropType = Consumable.DropType.BACON;

	/**
	 * Path to the texture for this animal
	 */
	private static final String texturePath = "objectTextures/pig.png";

	/**
	 * Constructor that takes a position
	 * 
	 * @param pos the desired position in world coordinates of this animal
	 */
	public Pig(Vector2 pos, int aX, int aY) {
		super(texturePath, pos, aX, aY);
		
		speed = 1/10f;
		width = .093f*Gdx.graphics.getWidth();
		height = .147f*Gdx.graphics.getHeight();
		bounds = new Rectangle(position.x - width/2,position.y - height/2,width,height);
	}

	/**
	 * Returns the type of item this animal drops
	 * 
	 * @return		the type of item this animal drops
	 */
	@Override
	public DropType getDropType() {
		return dropType;
	}
	
	/**
	 * resets the this animal texture with it's path
	 */
	@Override
	public void resetTexture() {
		super.resetTexture(texturePath);
	}
}
