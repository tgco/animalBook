package com.tgco.animalBook.gameObjects;

//Instance of the market drawn at the end of the lane for each level
public class Market extends ABDrawable {
	
	protected static final String texturePath = "objectTextures/market.png";

	public Market() {
		super(texturePath);
	}
	
	@Override
	public boolean isMarket() {
		return true;
	}

}
