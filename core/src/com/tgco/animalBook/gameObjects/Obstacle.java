package com.tgco.animalBook.gameObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

//Instance of the obstacle drawn at random locations
public class Obstacle extends ABDrawable {
	
	protected static String texturePath = "objectTextures/pond.png";
	Random rand = new Random();
	
	/**
	 * Constructs a new Obstacle.
	 * <p>
	 * Sets the width and height to be equal to a decimal multiple of the width and height
	 * (respectively) of the application window. Sets the bounds as a Rectangle centered in
	 * the middle of the object with the same width and height of the object.
	 */
	public Obstacle() {
		super(texturePath);
		
		int randIndex = rand.nextInt(101);
		if (randIndex <= 50) {
			texturePath = "objectTextures/boulder.png";
		}
		resetText();
		
		width = .123f*Gdx.graphics.getWidth();
		height = .196f*Gdx.graphics.getHeight();
		bounds = new Rectangle(position.x - width/2,position.y - height/2,width,height);
	}
	
	/**
	 * resets the texture when switching screens
	 */
	public void resetText(){
		super.resetTexture(texturePath);
	}
}
