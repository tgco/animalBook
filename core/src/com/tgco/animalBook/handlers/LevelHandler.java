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
 * 
 * @author
 *
 * This class returns the correct values for each variable that changes between levels 
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
	public Array<ABDrawable> addAnimals(int level, int numAnimals) {

		Array<ABDrawable> animals = new Array<ABDrawable>();

		int x;
		for(int i = 0; i < numAnimals; i++){
			if(i < .5*numAnimals){
				x = -i;

				switch(level) {
				case 1 :
					animals.add(new Goose(new Vector2(Gdx.graphics.getWidth()/2 + x*40 -50, (float) (Gdx.graphics.getHeight()/2 -x*x*25 + 10*x -50))));
					break;
				case 2 :
					animals.add(new Pig(new Vector2(Gdx.graphics.getWidth()/2 + x*40 -50, (float) (Gdx.graphics.getHeight()/2 -x*x*25 + 10*x -50))));
					break;
				case 3 :
					animals.add(new Goat(new Vector2(Gdx.graphics.getWidth()/2 + x*40 -50, (float) (Gdx.graphics.getHeight()/2 -x*x*25 + 10*x -50))));
					break;
				case 4 :
					animals.add(new Sheep(new Vector2(Gdx.graphics.getWidth()/2 + x*40 -50, (float) (Gdx.graphics.getHeight()/2 -x*x*25 + 10*x -50))));
					break;
				case 5 :
					animals.add(new Cow(new Vector2(Gdx.graphics.getWidth()/2 + x*40 -50, (float) (Gdx.graphics.getHeight()/2 -x*x*25 + 10*x -50))));
					break;
				}

			}
			else {
				x = (i - (int)Math.floor(.5*numAnimals));

				switch(level) {
				case 1 :
					animals.add(new Goose(new Vector2(Gdx.graphics.getWidth()/2 + x*40, (float) (Gdx.graphics.getHeight()/2 -x*x*30 + 15*x -50))));
					break;
				case 2 :
					animals.add(new Pig(new Vector2(Gdx.graphics.getWidth()/2 + x*40, (float) (Gdx.graphics.getHeight()/2 -x*x*30 + 15*x -50))));
					break;
				case 3 :
					animals.add(new Goat(new Vector2(Gdx.graphics.getWidth()/2 + x*40, (float) (Gdx.graphics.getHeight()/2 -x*x*30 + 15*x -50))));
					break;
				case 4 :
					animals.add(new Sheep(new Vector2(Gdx.graphics.getWidth()/2 + x*40, (float) (Gdx.graphics.getHeight()/2 -x*x*30 + 15*x -50))));
					break;
				case 5 :
					animals.add(new Cow(new Vector2(Gdx.graphics.getWidth()/2 + x*40, (float) (Gdx.graphics.getHeight()/2 -x*x*30 + 15*x -50))));
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
		return 2*level;
	}

	/**
	 * Selects and returns the correct lane length for the level
	 * 
	 * @param level the level to get the lane length for
	 * @return		the length of the lane for the specified level
	 */
	public float returnLaneLength(int level) {

		return 1000*level;
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
	
	public int getStoredAmount() {
		return storedAmount;
	}
	
	public int getPassLevelAmount() {
		return passLevelAmount;
	}
	
	public int getLevel(){
		return level;
	}

}
