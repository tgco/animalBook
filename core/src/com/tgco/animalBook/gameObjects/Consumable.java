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
		MILK("objectTextures/milk.png", "inventoryScreen/milkButton.atlas", 1000, 1000), 
		EGG("objectTextures/egg.png", "inventoryScreen/eggButton.atlas", 2000, 2000), 
		BACON("objectTextures/bacon.png", "inventoryScreen/baconButton.atlas", 3000, 3000), 
		CHEESE("objectTextures/cheese.png", "inventoryScreen/cheeseButton.atlas", 4000, 4000), 
		WOOL("objectTextures/wool.png", "inventoryScreen/woolButton.atlas", 5000, 5000);
		
		private final String texturePath;
		private final String atlasPath;
		private final int value;
		private final int foodValue;
		private DropType(String tp, String ap, int v, int vf){
			texturePath = tp;
			atlasPath = ap;
			value = v;
			foodValue = vf;
		}
		
		public final String getTexturePath(){
			return texturePath;
		}
		public final String getAtlasPath(){
			return atlasPath;
		}
		public final int getValue(){
			return value;
		}
		public final int getFoodValue(){
			return foodValue;
		}
	}
}
