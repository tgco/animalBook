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
public class Cow extends Animal {

	/**
	 * The type of dropped item this animal produces
	 */
	private static final DropType dropType = Consumable.DropType.MILK;
	
	/**
	 * Path to this animals texture
	 */
	private static final String texturePath = "objectTextures/cow.png";

	/**
	 * Constructor that takes the desired position
	 * 
	 * @param pos the location in world coordinates that the animal is located at
	 */
	public Cow(Vector2 pos, int aX, int aY) {
		super(texturePath, pos, aX, aY);
		
		speed = 1/14f;
		
		//width = .3045*height
		width = .067f*Gdx.graphics.getWidth();
		height = .22f*Gdx.graphics.getHeight();
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
	
	/**
	 * resets the this animal texture with it's path
	 */
	@Override
	public void resetTexture() {
		super.resetTexture(texturePath);
	}
	

}
