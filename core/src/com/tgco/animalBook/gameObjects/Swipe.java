package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;


/**
 * 
 * @author
 *
 * Container class that stores begin and end points of a line and
 * provides drawing functionality for that line
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
		
		ShapeRenderer render = new ShapeRenderer();
		render.setProjectionMatrix(cam.combined);
		render.begin(ShapeType.Line);
		render.line(begin.x, begin.y, end.x, end.y, Color.ORANGE, Color.PINK);
		render.end();
		render.dispose();
		
		lifeTime -= 1;
	}
	
	/**
	 * Returns the lifetime left for this swipe
	 * @return		the value remaining for the lifetime of this swipe
	 */
	public int getLifeTime() {
		return lifeTime;
	}

}
