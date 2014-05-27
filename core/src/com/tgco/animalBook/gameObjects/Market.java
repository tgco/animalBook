package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

//Instance of the market drawn at the end of the lane for each level
public class Market extends ABDrawable {
	
	protected static final String texturePath = "objectTextures/market.png";

	public Market() {
		super(texturePath);
		
		width = .185f*Gdx.graphics.getWidth();
		height = .294f*Gdx.graphics.getHeight();
		bounds = new Rectangle(position.x - width/2,position.y - height/2,width,height);
	}
	
	@Override
	public boolean isMarket() {
		return true;
	}

}
