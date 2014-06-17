package com.tgco.animalBook.handlers;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tgco.animalBook.gameObjects.ABDrawable;
import com.tgco.animalBook.gameObjects.Cow;
import com.tgco.animalBook.gameObjects.Goat;
import com.tgco.animalBook.gameObjects.Goose;
import com.tgco.animalBook.gameObjects.Obstacle;
import com.tgco.animalBook.gameObjects.Pig;
import com.tgco.animalBook.gameObjects.Sheep;

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
	 * Number of times each upgrade button has been pressed
	 */
	private int fruitfullMoneyP	= 0;
	private int LongerMoneyP	= 0;
	private int MoreMoneyP		= 0;
	
	/**
	 * Boolean that stores if the user is retrying the level or not
	 */
	private boolean onRetry = false;

	private boolean kidMode;

	/**
	 * Boolean to determine if the tutorial should be played
	 */
	private static boolean doTutorial = true;
	


	/**
	 * Constructor that takes the level in order to initialize pass amounts
	 * 
	 * @param level the level that the handler will return values for
	 */
	public LevelHandler(int level, boolean kidMode) {
		this.level = level;
		this.kidMode = kidMode;
		storedAmount = 0;

		//regular +5 for each level. kidMode +3
		switch(level) {
		case 1:
			if (kidMode)
				passLevelAmount = 5;
			else
				passLevelAmount = 5;
			break;
		case 2:
			if (kidMode)
				passLevelAmount = 8;
			else
				passLevelAmount = 10;
			break;
		case 3:
			if (kidMode)
				passLevelAmount = 11;
			else
				passLevelAmount = 15;
			break;
		case 4:
			if (kidMode)
				passLevelAmount = 14;
			else
				passLevelAmount = 20;
			break;
		case 5:
			if (kidMode)
				passLevelAmount = 17;
			else
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
		if(startAnimals <= 5 && !onRetry)
			startAnimals =5;
		else if(onRetry){
			startAnimals = (int) Math.ceil(passLevelAmount/2.0);
		}
		else if(startAnimals <= 5){
			startAnimals =5;
		}

		Array<ABDrawable> animals = new Array<ABDrawable>();

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
					animals.add(new Goose(new Vector2(animalWidth, animalHeight), animalChangeX(), animalChangeY()));
					break;
				case 2 :
					animals.add(new Pig(new Vector2(animalWidth, animalHeight), animalChangeX(), animalChangeY()));
					break;
				case 3 :
					animals.add(new Goat(new Vector2(animalWidth, animalHeight), animalChangeX(), animalChangeY()));
					break;
				case 4 :
					animals.add(new Sheep(new Vector2(animalWidth, animalHeight), animalChangeX(), animalChangeY()));
					break;
				case 5 :
					animals.add(new Cow(new Vector2(animalWidth, animalHeight), animalChangeX(), animalChangeY()));
					break;
				default: 
					animals.add(new Goose(new Vector2(animalWidth, animalHeight), animalChangeX(), animalChangeY()));
					break;
				}

			}
			else {
				x = (i - (int)Math.floor(.5*startAnimals));
				animalWidth = Gdx.graphics.getWidth()/2 + x*30 +30;
				animalHeight = (float) (Gdx.graphics.getHeight()/2 -1*x*x - 2*x );
				switch(level) {
				case 1 :
					animals.add(new Goose(new Vector2(animalWidth, animalHeight), animalChangeX(), animalChangeY()));
					break;
				case 2 :
					animals.add(new Pig(new Vector2(animalWidth, animalHeight), animalChangeX(), animalChangeY()));
					break;
				case 3 :
					animals.add(new Goat(new Vector2(animalWidth, animalHeight), animalChangeX(), animalChangeY()));
					break;
				case 4 :
					animals.add(new Sheep(new Vector2(animalWidth, animalHeight), animalChangeX(), animalChangeY()));
					break;
				case 5 :
					animals.add(new Cow(new Vector2(animalWidth, animalHeight), animalChangeX(), animalChangeY()));
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
		if (kidMode)
			return .24f*level;
		else
			return .26f*level;
	}

	/**
	 * Selects and returns the correct lane length for the level
	 * 
	 * @param level the level to get the lane length for
	 * @return		the length of the lane for the specified level
	 */
	public float returnLaneLength(int level) {
		if (kidMode)
			return 800 + 1000*(level - 1);
		else
			return 1000 + 1200*(level - 1);
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

	public void resetStoredAmount(){
		storedAmount = 0;
	}

	public int getPassLevelAmount() {
		return passLevelAmount;
	}

	public int getNextLevelStart() {
		return nextLevelStart;
	}

	/**
	 * Called every time player reaches the market
	 */
	public void resetNextLevelStart() {
		if(!onRetry ){
			nextLevelStart = 0;
		}else{
			onRetry = false;
		}
	}

	/**
	 * Returns the atlas for the animal that corresponds to the current level
	 * @return 		the file handle for the current level's animal
	 */
	public FileHandle currentLevelTexture() {
		switch(level) {
		case 1 :
			return Gdx.files.internal("objectTextures/gooseButton.atlas");
		case 2 :
			return Gdx.files.internal("objectTextures/pigButton.atlas");
		case 3 :
			return Gdx.files.internal("objectTextures/goatButton.atlas");
		case 4 :
			return Gdx.files.internal("objectTextures/sheepButton.atlas");
		case 5 :
			return Gdx.files.internal("objectTextures/cowButton.atlas");
		}
		return null;
	}

	/**
	 * Returns the correct atlas for the animal that corresponds to the next level
	 * 
	 * @return		the file handle for the next level's animal
	 */
	public FileHandle nextLevelTexture() {
		switch(level) {
		case 1 :
			return Gdx.files.internal("objectTextures/pigButton.atlas");
		case 2 :
			return Gdx.files.internal("objectTextures/goatButton.atlas");
		case 3 :
			return Gdx.files.internal("objectTextures/sheepButton.atlas");
		case 4 :
			return Gdx.files.internal("objectTextures/cowButton.atlas");
		case 5 :
			return Gdx.files.internal("objectTextures/bookButton.atlas");
		}
		return null;
	}
	
	/**
	 * Returns the width of the current level's animal texture for use on the market screen
	 * 
	 * @return the width of the current level's animal texture
	 */
	public int getCurrentLevelWidth() {
		switch(level) {
		case 1:
			return 245/6;
		case 2:
			return 522/6;
		case 3:
			return 329/6;
		case 4:
			return 401/6;
		case 5:
			return 342/6;
		}
		return 0;
	}
	
	/**
	 * Returns the height of the current level's animal texture for use on the market screen
	 * 
	 * @return the height of the current level's animal texture
	 */
	public int getCurrentLevelHeight() {
		switch(level) {
		case 1:
			return 465/6;
		case 2:
			return 770/6;
		case 3:
			return 881/6;
		case 4:
			return 823/6;
		case 5:
			return 1038/6;
		}
		return 0;
	}
	
	/**
	 * Returns the width of the next level's animal texture for use on the market screen
	 * 
	 * @return the width of the next level's animal texture
	 */
	public int getNextLevelWidth() {
		switch(level) {
		case 1:
			return 522/6;
		case 2:
			return 329/6;
		case 3:
			return 401/6;
		case 4:
			return 342/6;
		case 5:
			return 512/4;
		}
		return 0;
	}
	
	/**
	 * Returns the height of the next level's animal texture for use on the market screen
	 * 
	 * @return the height of the next level's animal texture
	 */
	public int getNextLevelHeight() {
		switch(level) {
		case 1:
			return 770/6;
		case 2:
			return 881/6;
		case 3:
			return 823/6;
		case 4:
			return 1038/6;
		case 5:
			return 512/4;
		}
		return 0;
	}
	
	/**
	 * Increases the level and the pass level amount
	 * 
	 */
	public void addLevel(){
		level++;
		switch(level) {
		case 1:
			if (kidMode)
				passLevelAmount = 5;
			else
				passLevelAmount = 5;
			break;
		case 2:
			if (kidMode)
				passLevelAmount = 8;
			else
				passLevelAmount = 10;
			break;
		case 3:
			if (kidMode)
				passLevelAmount = 11;
			else
				passLevelAmount = 15;
			break;
		case 4:
			if (kidMode)
				passLevelAmount = 14;
			else
				passLevelAmount = 20;
			break;
		case 5:
			if (kidMode)
				passLevelAmount = 17;
			else
				passLevelAmount = 25;
			break;
		}
	}

	/**
	 * Getters and setters
	 * 
	 * @return
	 */
	public int getLevel(){
		return level;
	}

	public int getFruitfullMoneyP() {
		return fruitfullMoneyP;
	}

	public int getLongerMoneyP() {
		return LongerMoneyP;
	}

	public int getMoreMoneyP() {
		return MoreMoneyP;
	}

	public void setNextLevelStart(int nextStart) {
		nextLevelStart = nextStart;
	}

	public void addFruitfullMoneyP() {
		this.fruitfullMoneyP += 1;
	}

	public void addLongerMoneyP() {
		LongerMoneyP += 1;
	}

	public void addMoreMoneyP() {
		MoreMoneyP += 1;
	}

	public void setFruitfullMoneyP(int fruitfull) {
		this.fruitfullMoneyP = fruitfull;
	}

	public void setLongerMoneyP(int longer) {
		LongerMoneyP = longer;
	}

	public void setMoreMoneyP(int more) {
		MoreMoneyP = more;
	}

	public boolean isDoTutorial() {
		return doTutorial;
	}

	public void setDoTutorial(boolean doTutorial) {
		LevelHandler.doTutorial = doTutorial;
	}

	public void setRetry(){
		onRetry = true;
	}
	
	public void updateKidMode(boolean kM){
		this.kidMode = kM;
	}

	public boolean isKidMode() {
		return kidMode;
	}
	
	public int animalChangeX(){
		switch(level) {
		case 1:
			if (kidMode)
				return  (int) (.139f*Gdx.graphics.getWidth());
			else
				return  (int) (.171f*Gdx.graphics.getWidth());
		case 2:
			if (kidMode)
				return  (int) (.2f*Gdx.graphics.getWidth());
			else
				return  (int) (.285f*Gdx.graphics.getWidth());
		case 3:
			if (kidMode)
				return  (int) (.285f*Gdx.graphics.getWidth());
			else
				return  (int) (.428f*Gdx.graphics.getWidth());
		case 4:
			if (kidMode)
				return  (int) (.371f*Gdx.graphics.getWidth());
			else
				return  (int) (.571f*Gdx.graphics.getWidth());
		case 5:
			if (kidMode)
				return  (int) (.485f*Gdx.graphics.getWidth());
			else
				return  (int) (.714f*Gdx.graphics.getWidth());
		}
		return  (int) (.139f*Gdx.graphics.getWidth()*level);
	}
	
	public int animalChangeY(){
		switch(level) {
		case 1:
			if (kidMode)
				return  (int) (.2f*Gdx.graphics.getHeight());
			else
				return  (int) (.228f*Gdx.graphics.getHeight());
		case 2:
			if (kidMode)
				return  (int) (.285f*Gdx.graphics.getHeight());
			else
				return  (int) (.485f*Gdx.graphics.getHeight());
		case 3:
			if (kidMode)
				return  (int) (.428f*Gdx.graphics.getHeight());
			else
				return (int) (.714f*Gdx.graphics.getHeight());
		case 4:
			if (kidMode)
				return  (int) (.628f*Gdx.graphics.getHeight());
			else
				return  (int) (.914f*Gdx.graphics.getHeight());
		case 5:
			if (kidMode)
				return  (int) (.857f*Gdx.graphics.getHeight());
			else
				return  (int) (1.142f*Gdx.graphics.getHeight());
		}
		return  (int) (.221f*Gdx.graphics.getHeight()*level);
	}

	public Array<ABDrawable> addObstacles(int level2, Vector2 mPos) {
		Random rand = new Random();
		Array<ABDrawable> obstacles = new Array<ABDrawable>();
		Obstacle o;
		if(kidMode){
			for (float i = 700f; i < returnLaneLength(level); i += 10f){
				//50% of time, level number -1
				if (rand.nextInt(level) > 0 && rand.nextInt(5) > 3){
					o = new Obstacle();
					o.setPosition(new Vector2(((float)(rand.nextInt(Gdx.graphics.getWidth()))- o.getWidth()/2), i));
				
					if (o.getPosition().dst(mPos) > 250f)
						obstacles.add(o);
					i += 400f;
				}
			}
		}else{

			for (float i = 700f; i < returnLaneLength(level); i += 10f){
				if (rand.nextInt(level) > 0 && rand.nextInt(1) == 0){
					o = new Obstacle();
					o.setPosition(new Vector2(((float)(rand.nextInt(Gdx.graphics.getWidth()))- o.getWidth()/2), i));
					
					if (o.getPosition().dst(mPos) > 250f)
						obstacles.add(o);
					i += 400f;
				}
			}
		}
		return obstacles;
	}

	public float getHealthDecrease() {
		float healthAmount;
		if(kidMode){
			healthAmount = .005f;
		}else{
			healthAmount = .01f;
		}
		return healthAmount;
	}

	public int getMaxLevel() {
		return 5;
	}
}
