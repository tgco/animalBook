package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;


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
	 * Where the swipe begins
	 */
	private Vector2 begin;
	
	/**
	 * Where the swipe ends
	 */
	private Vector2 end;
	
	/**
	 * The amount of time the swipe is drawn on screen
	 */
	private int lifeTime;
	
	/**
	 * Texture that swipes are drawn in
	 */
	private final Texture TEXTURE = new Texture(Gdx.files.internal("objectTextures/swipe.png"));

	/**
	 * Constructor that takes beginning and end point of the swipe line
	 * 
	 * @param begin the beginning point of the line
	 * @param end	the ending point of the line
	 */
	public Swipe(Vector2 begin, Vector2 end) {
		this.begin = begin;
		this.end = end;
		lifeTime = 100;
	}
	
	/**
	 * Draws the swipes to the screen using world coordinates
	 * 
	 * @param cam the camera used to view the world in order to draw in world coordinates
	 */
	public void draw(OrthographicCamera cam) {
		
		SpriteBatch batch = new SpriteBatch();
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		//Find length of swipe and rotation
		float length = end.cpy().sub(begin).len();
		float rotation = end.cpy().sub(begin).angle();
		batch.draw(TEXTURE, begin.x, begin.y, 0, 0, length, length/5, 1, 1, rotation, 0, 0, TEXTURE.getWidth(), TEXTURE.getHeight(), false, false);
		batch.end();
		batch.dispose();
		
		lifeTime -= 1;
	}
	
	/**
	 * Returns the lifetime left for this swipe
	 * @return		the value remaining for the lifetime of this swipe
	 */
	public int getLifeTime() {
		return lifeTime;
	}
	
	public void dispose() {
		TEXTURE.dispose();
	}

}
