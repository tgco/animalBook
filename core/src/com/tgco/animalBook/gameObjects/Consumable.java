package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.math.Vector2;

//Dropped by animals for collection by the player
public class Consumable extends ABDrawable {
	private DropType type;
	
	public Consumable(DropType dropType){
		super(dropType.getTexturePath());
		type = dropType;
	}
	public Consumable(DropType dropType, Vector2 position){
		super(dropType.getTexturePath());
	}
	
	public DropType getType(){
		return type;
	}

	public enum DropType {
		//ENUM_NAME ("string path of object texture", "string path of object atlas", marketValue, hungerValue);
		EGG("objectTextures/dropEgg.jpg", "objectTextures/eggButton.atlas", 2000, 10),
		BACON("objectTextures/bacon.png", "objectTextures/baconButton.atlas", 3000, 20),
		CHEESE("objectTextures/cheese.png", "objectTextures/cheeseButton.atlas", 4000, 15), 
		WOOL("objectTextures/wool.png", "objectTextures/woolButton.atlas", 5000, 5),
		MILK("objectTextures/milk.png", "objectTextures/milkButton.atlas", 1000, 25);
		//DRAGON("objectTextures/milk.png", "objectTextures/milkButton.atlas", 1000, 25);
		
		private final String texturePath;
		private final String atlasPath;
		private final int marketValue;
		private final int hungerValue;
		private DropType(String tp, String ap, int mv, int hv){
			texturePath = tp;
			atlasPath = ap;
			marketValue = mv;
			hungerValue = hv;
		}
		
		public final String getTexturePath(){
			return texturePath;
		}
		public final String getAtlasPath(){
			return atlasPath;
		}
		public int getMarketValue(){
			return marketValue;
		}
		public int getHungerValue(){
			return hungerValue;
		}
	}
}
