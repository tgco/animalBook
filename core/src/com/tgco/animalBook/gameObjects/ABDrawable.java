/**
 * File: ABDrawable.java
 * 
 * every drawable needs to be able to render itself on the screen. In  order to do so, 
 * it needs pic, position, bounds, and width and height
 */
package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class ABDrawable {
	
	/** texture is the picture of each item */
	protected Texture texture;
	
	/** width and height need to be float for batch.draw()  */
	protected float width, height;
	
	/** the collision detector bounds */
	protected Rectangle bounds;
	 /** it's current position */
	protected Vector2 position;

	/** 
	 * The consturctor sets up the pciture, how big it is, and the bounds.
	 * @param texturePath the string of where the picture is
	 */
	public ABDrawable(String texturePath) {
		this.texture = new Texture(Gdx.files.internal(texturePath));
		position = new Vector2();
		width = .093f*Gdx.graphics.getWidth();
		height = .147f*Gdx.graphics.getHeight();
		bounds = new Rectangle(position.x - width/2,position.y - height/2,width,height);
	}
	
	/**
	 * it draws the picture with the proper variables for the screen
	 * @param batch the batch comes from the screen.
	 */
	public void draw(SpriteBatch batch) {
		batch.draw(texture, position.x - width/2, position.y - height/2, width, height);
	}
	
	/**
	 * gets the current position
	 * @return Vector2 is the current position of the drawable
	 */
	public Vector2 getPosition() {
		return position;
	}
	
	/**
	 *  sets the position on the new  of the object
	 * @param position is the new position of it
	 */
	public void setPosition(Vector2 position) {
		this.position = position;
		bounds.setX(position.x - width/2);
		bounds.setY(position.y - height/2);
	}
	
	/**
	 * determines if this object can be moved
	 * @return boolean 
	 */
	public boolean isMovable() {
		return false;
	}
	
	/**
	 * returns if the drawable is the market object
	 * @return boolean
	 */
	public boolean isMarket() {
		return false;
	}
	
	/**
	 * returns if the drawable is a dropping object
	 * @return boolean
	 */
	public boolean isDropping() {
		return false;
	}
	
	/**
	 * dispose deletes the texture associated with this drawable object
	 */
	public void dispose() {
		texture.dispose();
	}

	/**
	 * This returns the objects bounds for collision detection in the world.
	 * @return bounds
	 */
	public Rectangle getBounds() {
		return bounds;
	}
	
	/**
	 * getter for the height of the object
	 * @return height
	 */
	public float getHeight() {
		return height;
	}
	
	/**
	 * getter for the width of the object
	 * @return width
	 */
	public float getWidth() {
		return width;
	}
	public void resetTexture(String texturePath) {
		this.texture = new Texture(Gdx.files.internal(texturePath));	
	}
}
