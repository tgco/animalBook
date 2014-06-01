package com.tgco.animalBook.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tgco.animalBook.gameObjects.ABDrawable;
import com.tgco.animalBook.gameObjects.Cow;
import com.tgco.animalBook.gameObjects.Goat;
import com.tgco.animalBook.gameObjects.Goose;
import com.tgco.animalBook.gameObjects.Pig;
import com.tgco.animalBook.gameObjects.Sheep;

//instantiates correct object for the given level
/**
 * This class returns the correct values for each variable that changes between levels 
 * 
 * @author
 *
 * 
 */
public class LevelHandler {

	/**
	 * The level to be initialized
	 */
	private int level;

	/**
	 * The amount of animals needed in order to progress to the next level
	 */
	private int passLevelAmount;
	
	/**
	 * Number of animals the player has already stored in the market
	 */
	private int storedAmount;
	
	/**
	 * Number of animals the player will start with next level
	 */
	private int nextLevelStart;

	/**
	 * Constructor that takes the level in order to initialize pass amounts
	 * 
	 * @param level the level that the handler will return values for
	 */
	public LevelHandler(int level) {
		this.level = level;

		storedAmount = 0;

		switch(level) {
		case 1:
			passLevelAmount = 5;
			break;
		case 2:
			passLevelAmount = 10;
			break;
		case 3:
			passLevelAmount = 15;
			break;
		case 4:
			passLevelAmount = 20;
			break;
		case 5:
			passLevelAmount = 25;
			break;
		}
	}

	/**
	 * Returns a array of the correct type of animal for the specified level
	 * 
	 * @param level		 the level to select animals for
	 * @param numAnimals the number of animals to initialize
	 * @return			 an array with the correct number of the correct animal type
	 */
	public Array<ABDrawable> addAnimals(int level) {
		int startAnimals = nextLevelStart;
		if(startAnimals == 0)
			startAnimals =5;
		Array<ABDrawable> animals = new Array<ABDrawable>();

		Gdx.app.log("My tag", "start: " + startAnimals + " nextStart: " + nextLevelStart);
		int x;
		float animalWidth;
		float animalHeight;
		for(int i = 0; i < startAnimals; i++){
			if(i < .5*startAnimals){
				x = -i;
				animalWidth = Gdx.graphics.getWidth()/2 + x*30 -10;
				animalHeight = (float) (Gdx.graphics.getHeight()/2 -1*x*x - 2*x );
				switch(level) {
				case 1 :
					animals.add(new Goose(new Vector2(animalWidth, animalHeight)));
					break;
				case 2 :
					animals.add(new Pig(new Vector2(animalWidth, animalHeight)));
					break;
				case 3 :
					animals.add(new Goat(new Vector2(animalWidth, animalHeight)));
					break;
				case 4 :
					animals.add(new Sheep(new Vector2(animalWidth, animalHeight)));
					break;
				case 5 :
					animals.add(new Cow(new Vector2(animalWidth, animalHeight)));
					break;
				}

			}
			else {
				x = (i - (int)Math.floor(.5*startAnimals));
				animalWidth = Gdx.graphics.getWidth()/2 + x*30 +30;
				animalHeight = (float) (Gdx.graphics.getHeight()/2 -1*x*x - 2*x );
				switch(level) {
				case 1 :
					animals.add(new Goose(new Vector2(animalWidth, animalHeight)));
					break;
				case 2 :
					animals.add(new Pig(new Vector2(animalWidth, animalHeight)));
					break;
				case 3 :
					animals.add(new Goat(new Vector2(animalWidth, animalHeight)));
					break;
				case 4 :
					animals.add(new Sheep(new Vector2(animalWidth, animalHeight)));
					break;
				case 5 :
					animals.add(new Cow(new Vector2(animalWidth, animalHeight)));
					break;
				}

			}
		}
		return animals;


	}

	/**
	 * Selects and returns the correct camera speed for the level
	 * 
	 * @param level the level to get the camera speed for
	 * @return		the speed for the camera
	 */
	public float returnCameraSpeed(int level) {
		return .25f*level;
	}

	/**
	 * Selects and returns the correct lane length for the level
	 * 
	 * @param level the level to get the lane length for
	 * @return		the length of the lane for the specified level
	 */
	public float returnLaneLength(int level) {

		return 2000*level;
	}
	
	/**
	 * Getters and increments for the specified variables
	 */
	public void increaseStored() {
		storedAmount++;
	}
	
	public void decreaseStored() {
		storedAmount--;
	}
	
	public void increaseNextLevelStart() {
		nextLevelStart++;
	}
	
	public int getStoredAmount() {
		return storedAmount;
	}
	
	public int getPassLevelAmount() {
		return passLevelAmount;
	}
	
	public int getNextLevelStart() {
		return nextLevelStart;
	}
	
	public int getLevel(){
		return level;
	}
	public void addLevel(){
		level++;
	}
}
