package com.tgco.animalBook.gameObjects;

/**
 * This class holds all necessary information regarding the animal drops. The enumeration keeps track of each drop characteristic
 * in the form: ENUM_NAME("path_to_object_texture", "path_to_object_atlas", int marketValue, int hungerValue)
 * The enumeration is designed to make it easier to make additions.
 * 
 * @author Kelly Masuda
 */
public class Consumable extends ABDrawable {
	
	/**
	 * DropType type
	 */
	private DropType type;
	
	/**
	 * Constructor creates a consumable given a DropType type.
	 * @param dropType - The objects intended DropType
	 */
	public Consumable(DropType dropType){
		super(dropType.getTexturePath());
		this.type = dropType;
	}
	
	/**
	 * Returns this object's DropType.
	 * @return type - returns the DropType
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
		EGG			("Eggs", "objectTextures/egg.png", "objectTextures/eggButton.atlas", 5, 5),
		BACON	("Bacon", "objectTextures/bacon.png", "objectTextures/baconButton.atlas", 10, 7),
		CHEESE	("Cheese", "objectTextures/cheese.png", "objectTextures/cheeseButton.atlas", 15, 10), 
		MUTTON		("Mutton", "objectTextures/mutton.png", "objectTextures/muttonButton.atlas", 20, 13),
		MILK		("Milk", "objectTextures/milk.png", "objectTextures/milkButton.atlas", 25, 15);
		
		private final String 	name;
		private final String	texturePath;
		private final String	atlasPath;
		private final int		marketValue;
		private final int		hungerValue;
		
		/**
		 * Private constructor necessary to create Consumable enumeration.
		 * @param texturePath - String path to the texture file
		 * @param atlasPath - String path to the atlas file
		 * @param marketValue - The Consumable's market value
		 * @param hungerValue - The Consumable's hunger value
		 */
		private DropType(String name, String texturePath, String atlasPath, int marketValue, int hungerValue){
			this.name				=	name;
			this.texturePath		=	texturePath;
			this.atlasPath			=	atlasPath;
			this.marketValue 	=	marketValue;
			this.hungerValue 	=	hungerValue;
		}
		
		/**
		 * Returns this Consumable.DropType's name.
		 * @return name
		 */
		public final String getName(){
			return name;
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
