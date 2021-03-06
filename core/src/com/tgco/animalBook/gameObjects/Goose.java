package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.gameObjects.Consumable.DropType;

/**
 * Implements correct drop type, texture path, speed and size
 * for this type of animal.
 * 
 * @author
 *
 * 
 */
public class Goose extends Animal {

	/**
	 * The type of item this animal drops
	 */
	private static final DropType dropType = Consumable.DropType.EGG;

	/**
	 * Path to the texture for this animal
	 */
	private static final String texturePath = "objectTextures/goose.png";

	/**
	 * Constructor that takes a position
	 * 
	 * @param pos the desired position in world coordinates for this animal
	 */
	public Goose(Vector2 pos,  int aX, int aY) {
		super(texturePath, pos, aX, aY);
		speed = 1/10f;

		if (AnimalBookGame.SCREEN_HEIGHT < 3f) {
			width = .037f*Gdx.graphics.getWidth() * 1.45f;
			height = .110f*Gdx.graphics.getHeight() * 1.45f;
		} else {
			width = .037f*Gdx.graphics.getWidth();
			height = .110f*Gdx.graphics.getHeight();
		}
		
		
		
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
