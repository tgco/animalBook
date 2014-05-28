package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

//Instance of the market drawn at the end of the lane for each level
public class Market extends ABDrawable {
	
	protected static final String texturePath = "objectTextures/market.png";
	
	/**
	 * Constructs a new Market.
	 * <p>
	 * Sets the width and height to be equal to a decimal multiple of the width and height
	 * (respectively) of the application window. Sets the bounds as a Rectangle centered in
	 * the middle of the object with the same width and height of the object.
	 */
	public Market() {
		super(texturePath);
		
		width = .185f*Gdx.graphics.getWidth();
		height = .294f*Gdx.graphics.getHeight();
		bounds = new Rectangle(position.x - width/2,position.y - height/2,width,height);
	}
	
	/**
	 * Returns <code>true</code>
	 * <p>
	 * Overrides the default isMarket method of ABDrawable to return <code>true</code>
	 */
	@Override
	public boolean isMarket() {
		return true;
	}

}
