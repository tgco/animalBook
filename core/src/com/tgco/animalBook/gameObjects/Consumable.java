package com.tgco.animalBook.gameObjects;

/**
 * This class holds all necessary information regarding the animal drops. The enumeration keeps track of each drop characteristic
 * in the form: ENUM_NAME("path_to_object_texture", "path_to_object_atlas", int marketValue, int hungerValue)
 * The enumeration is designed to make it easier to make additions.
 * 
 * @author Kelly Masuda
 */
public class Consumable extends ABDrawable {
	private DropType type;
	
	/**
	 * Constructor creates a consumable given a DropType type.
	 * @param dropType
	 */
	public Consumable(DropType dropType){
		super(dropType.getTexturePath());
		this.type = dropType;
	}
	
	/**
	 * Returns this object's DropType.
	 * @return type
	 */
	public DropType getType(){
		return type;
	}

	/**
	 * Enumeration containing all information necessary to create a Consumable object.
	 * 
	 * @author Kelly Masuda
	 */
	public enum DropType {
		EGG			("objectTextures/dropEgg.jpg", "objectTextures/eggButton.atlas", 2000, 10),
		BACON	("objectTextures/bacon.png", "objectTextures/baconButton.atlas", 3000, 20),
		CHEESE	("objectTextures/cheese.png", "objectTextures/cheeseButton.atlas", 4000, 15), 
		WOOL		("objectTextures/wool.png", "objectTextures/woolButton.atlas", 5000, 5),
		MILK		("objectTextures/milk.png", "objectTextures/milkButton.atlas", 1000, 25);
		
		private final String	texturePath;
		private final String	atlasPath;
		private final int		marketValue;
		private final int		hungerValue;
		
		/**
		 * Private constructor necessary to create Consumable enumeration.
		 * @param texturePath
		 * @param atlasPath
		 * @param marketValue
		 * @param hungerValue
		 */
		private DropType(String texturePath, String atlasPath, int marketValue, int hungerValue){
			this.texturePath		=	texturePath;
			this.atlasPath			=	atlasPath;
			this.marketValue 	=	marketValue;
			this.hungerValue 	=	hungerValue;
		}
		
		/**
		 * Returns this Consumable.DropType's texture path.
		 * @return texturePath
		 */
		public final String getTexturePath(){
			return texturePath;
		}
		
		/**
		 * Returns this Consumable.DropType's atlas path.
		 * @return atlasPath
		 */
		public final String getAtlasPath(){
			return atlasPath;
		}
		
		/**
		 * Returns this Consumable.DropType's market value.
		 * @return marketValue
		 */
		public int getMarketValue(){
			return marketValue;
		}
		
		/**
		 * Returns this COnsumable.DropType's hunger value.
		 * @return hungerValue
		 */
		public int getHungerValue(){
			return hungerValue;
		}
	}
}
