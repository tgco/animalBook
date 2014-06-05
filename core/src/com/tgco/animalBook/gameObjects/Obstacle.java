package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

//Instance of the obstacle drawn at random locations
public class Obstacle extends ABDrawable {
	
	protected static final String texturePath = "objectTextures/obstacle.png";
	
	/**
	 * Constructs a new Obstacle.
	 * <p>
	 * Sets the width and height to be equal to a decimal multiple of the width and height
	 * (respectively) of the application window. Sets the bounds as a Rectangle centered in
	 * the middle of the object with the same width and height of the object.
	 */
	public Obstacle() {
		super(texturePath);
		
		width = .123f*Gdx.graphics.getWidth();
		height = .196f*Gdx.graphics.getHeight();
		bounds = new Rectangle(position.x - width/2,position.y - height/2,width,height);
	}
	
	public void resetText(){
		super.resetTexture(texturePath);
	}
}
