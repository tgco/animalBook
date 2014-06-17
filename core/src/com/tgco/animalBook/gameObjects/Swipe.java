package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.AnimalBookGame;


/**
 * Container class that stores begin and end points of a line and
 * provides drawing functionality for that line
 * 
 * @author
 *
 * 
 */
public class Swipe {

	/**
	 * The amount of time the swipe is drawn on screen
	 */
	private float lifeTime;

	/**
	 * Texture that swipes are drawn in
	 */
	private final Texture TEXTURE = new Texture(Gdx.files.internal("objectTextures/swipe.png"));

	/**
	 * Sprite to provide fading functionality
	 */
	private Sprite sprite = new Sprite(TEXTURE);

	/**
	 * Constructor that takes beginning and end point of the swipe line
	 * 
	 * @param begin the beginning point of the line
	 * @param end	the ending point of the line
	 */
	public Swipe(Vector2 begin, Vector2 end) {
		lifeTime = 100f;

		//Find length of swipe and rotation setup sprite
		float length = end.cpy().sub(begin).len();
		float rotation = end.cpy().sub(begin).angle();
		sprite.setBounds(begin.x, begin.y, length, 35);
		sprite.setOrigin(0, 0);
		sprite.rotate(rotation);
		
	}

	/**
	 * Draws the swipes to the screen using world coordinates
	 * 
	 * @param unprojectedBatch 		the sprite batch that draws in world coordinates
	 * @param delta					the time between frames
	 */
	public void draw(SpriteBatch unprojectedBatch,float delta) {
		sprite.draw(unprojectedBatch,(lifeTime/100f));
		lifeTime -= (AnimalBookGame.TARGET_FRAME_RATE*delta);
	}

	/**
	 * Returns the lifetime left for this swipe
	 * @return		the value remaining for the lifetime of this swipe
	 */
	public float getLifeTime() {
		return lifeTime;
	}

	/** 
	 * disposing of the swipe texture
	 */
	public void dispose() {
		TEXTURE.dispose();
	}

}
