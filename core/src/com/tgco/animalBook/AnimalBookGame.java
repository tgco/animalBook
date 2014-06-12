/**
 * File: AnimalBookGame.java
 * 
 * This is the gameInstance class which houses the Game and all the screens and current information
 */
package com.tgco.animalBook;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.utils.Array;
import com.tgco.animalBook.gameObjects.Consumable;
import com.tgco.animalBook.gameObjects.Consumable.DropType;
import com.tgco.animalBook.gameObjects.Player;
import com.tgco.animalBook.handlers.LevelHandler;
import com.tgco.animalBook.handlers.SoundHandler;
import com.tgco.animalBook.screens.GameScreen;
import com.tgco.animalBook.screens.SplashScreen;

public class AnimalBookGame extends Game {

	/**Version string  */
	public static final String version = "0.7.0";

	/** debug variables */
	public static final Boolean debugMode = true;
	private FPSLogger fpsLogger;

	/** levelData array is used hold the data for the current level */
	private static Array<Object> levelData = new Array<Object>(5);

	/** the current state of the cycle*/
	public enum state{
		RESUME, PAUSE, GOING
	}
	
	public static state currState;
	//private DatabaseHandler dbHand;

	/**
	 * The target frame rate for all motion updates.  Movement is calculated relative to this frame rate.
	 */
	public static final float TARGET_FRAME_RATE = 30;

	/**
	 * The current level from the preferences file
	 */
	private  int level = 1;

	/**
	 * Load all information that differs between levels
	 */
	private LevelHandler levelHandler;
	
	/** variable to store each consumable and retrieve them */
	private int numConsumables;

	/** DATA_PREFS is the preference file for the data of the game*/
	private static final String DATA_PREFS = "tgco.AnimalBookGame_data";

	/**continueable is used for the MainMenu continue button disable */
	private boolean continueable = false;

	/**
	 * Determines if the player hit the back button and can resume
	 */
	private boolean hitBack = false;

	/**
	 * true if the game is in kid mode, where levels are easier
	 */
	private static boolean kidMode = false;

	/**
	 * every game starts with the create function.
	 * this sets the initial screen to splash screen
	 */
	@Override
	public void create () {

		for(int i=0; i< 5; i++){
			levelData.insert(i, null);
		}

		//Set the initial screen
		setScreen(new SplashScreen(this));

		if (debugMode)
			fpsLogger = new FPSLogger();

		//DB stuff if we go this route
		/*dbHand = new DatabaseHandler();
		 dbHand.getValue( "0");*/
		
		Preferences prefs = Gdx.app.getPreferences(DATA_PREFS);
		int lev = prefs.getInteger("level");
		kidMode = prefs.getBoolean("kidMode");

		if(lev >0 ){
			continueable = true;
		}
		
		currState = state.RESUME;
	}

	/**
	 * only overridden for debugging purposes
	 */
	@Override
	public void render () {
		super.render();
		if (debugMode) 
			fpsLogger.log();
		
	}

	/**
	 * removes the textures from the memory of the device when the app game has exited
	 */
	@Override
	public void dispose() {
		super.dispose();
		getScreen().dispose();
		SoundHandler.dispose();

		//dbHand.close();
	}

	/**
	 * resizes at the start of the game creating
	 * @param width the width of the game window  
	 * @param height the height the the game window
	 */
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	/**
	 * Overrides the pause from the Normal life cycle and saves to the preferences 
	 * if the game has been played
	 */
	@Override
	public void pause() {
		super.pause();
		if(levelHandler != null && levelData.get(0) != null){
			Gdx.app.log("My Tagg", "the level is " + level);
			setPrefsToFile();
		}
		currState = state.PAUSE;
	}
	
	@Override
	public void resume(){
		super.resume();
		Gdx.app.log("My tagg", "The app is resumeing");
		currState = state.RESUME;
		
	}

	/**
	 * saves the data for the level to the preferences 
	 */
	public void setPrefsToFile(){
		Preferences prefs = Gdx.app.getPreferences(DATA_PREFS);
		if(level < levelHandler.getLevel()){
			
			prefs.putInteger("level", levelHandler.getLevel());

			//Player data
			prefs.putInteger("money", ((Player) levelData.get(1)).getPlayerMoney());
			prefs.putFloat("health", ((Player) levelData.get(1)).getHealth());
			for (int i = 0; i < Consumable.DropType.values().length; i++){
				prefs.putInteger(Consumable.DropType.values()[i].getName(),
						((Player) levelData.get(1)).getInventory().getInventory().get(Consumable.DropType.values()[i]).size);
			}
			//prefs.putInteger("Bacon", ((Player) levelData.get(1)).getInventory().getInventory().get(Consumable.DropType.values()[1]).size);
			//prefs.putInteger("Cheese", ((Player) levelData.get(1)).getInventory().getInventory().get(Consumable.DropType.values()[2]).size);
			//prefs.putInteger("Wool", ((Player) levelData.get(1)).getInventory().getInventory().get(Consumable.DropType.values()[3]).size);
			//prefs.putInteger("Milk", ((Player) levelData.get(1)).getInventory().getInventory().get(Consumable.DropType.values()[4]).size);

			//animal data,   0 = fruitfullness, 1=dropInterval, 2=duration
			prefs.putInteger("numAnimals", levelHandler.getNextLevelStart());
			prefs.putInteger("Fruitfulness", levelHandler.getFruitfullMoneyP());
			prefs.putInteger("More", levelHandler.getMoreMoneyP());
			prefs.putInteger("Longer", levelHandler.getLongerMoneyP());

			
			
			Gdx.app.log("My Tagg", "After flush of save");
		}
		
		//app settings
		prefs.putBoolean("music", SoundHandler.isMusicMuted());
		prefs.putBoolean("sound", SoundHandler.isSoundMuted());
		prefs.putBoolean("tutorial", levelHandler.isDoTutorial());
		prefs.putBoolean("kidMode", kidMode);
		
		prefs.flush();

	}
	
	public void setPrefsToFileLevelChange(){
		if(level < levelHandler.getLevel()){
			Preferences prefs = Gdx.app.getPreferences(DATA_PREFS);
			prefs.putInteger("level", levelHandler.getLevel());

			//Player data
			prefs.putInteger("money", ((Player) levelData.get(1)).getPlayerMoney());
			prefs.putFloat("health", ((Player) levelData.get(1)).getHealth());
			prefs.putInteger("Eggs", ((Player) levelData.get(1)).getInventory().getInventory().get(Consumable.DropType.values()[0]).size);
			prefs.putInteger("Bacon", ((Player) levelData.get(1)).getInventory().getInventory().get(Consumable.DropType.values()[1]).size);
			prefs.putInteger("Cheese", ((Player) levelData.get(1)).getInventory().getInventory().get(Consumable.DropType.values()[2]).size);
			prefs.putInteger("Wool", ((Player) levelData.get(1)).getInventory().getInventory().get(Consumable.DropType.values()[3]).size);
			prefs.putInteger("Milk", ((Player) levelData.get(1)).getInventory().getInventory().get(Consumable.DropType.values()[4]).size);
			
			//animal data
			prefs.putInteger("numAnimals", levelHandler.getNextLevelStart());
			prefs.putBoolean("kidMode", kidMode);
			
			prefs.flush();
		}
	}

	/**
	 * Used to cast the current screen to gameScreen to
	 * provide gameScreen functionality
	 * 
	 * @return GameScreen
	 */
	public GameScreen getGameScreen(){
		return (GameScreen) getScreen();
	}

	/**
	 * resets the audio stuff because of how android works with static. Static is not deleted at onStop().
	 */
	public void reset() {
		SoundHandler.resetAudio();
	}

	/** 
	 * getter for the level data used in World constructor
	 * @return the level data
	 */
	public Array<Object> getLevelData() {
		return levelData;
	}

	/** 
	 * The add function is the place to reset the data in LevelData when nextLevel button is pressed and back button
	 * @param obj the object that will be stored in LevelData
	 * @param pos the position that the object will be stored in LevelData
	 */
	public void addToDatalevel(Object obj, int pos){
		levelData.set(pos, obj);
	}

	/**
	 * getter for the LevelHandler for the other classes to use the info of the level
	 * @return the levelHandler of the Game
	 */
	public LevelHandler getLevelHandler() {
		return levelHandler;
	}

	/**
	 * The Game adds a level to levelHandler and resets the storedAmount to 0 for the market
	 */
	public void addToLevel() {
		levelHandler.addLevel();
		levelHandler.resetStoredAmount();
	}

	public void setData(){
		if(continueable){
			setDataCont();
		}else{
			Gdx.app.log("My Tagg", "This is doing play");
			setDataPlay();
		}
		
		//continuable should only be used after loading app
		continueable = false;
	}
	/**
	 * This sets the levelHandler and it's data from preferences for the continue. 
	 * if nothing in preferences it will return 0.
	 */
	private void setDataCont() {
		Preferences prefs = Gdx.app.getPreferences(DATA_PREFS);
		level = prefs.getInteger("level");
		levelHandler = new LevelHandler(level, kidMode);	
		Player player = new Player(levelHandler.returnCameraSpeed(level));
		player.setValues(prefs.getFloat("health"), prefs.getInteger("money"));

		addToDatalevel(player,1);


		//the consumables adding back in
		for (int i = 0; i < Consumable.DropType.values().length; i++){
			numConsumables = prefs.getInteger(Consumable.DropType.values()[i].getName());
			for(int j =0; j<numConsumables; j++){
				player.getInventory().addItem(new Consumable(DropType.values()[i]));
			}
		}

		/*numConsumables = prefs.getInteger("Eggs");
		for(int i =0; i<numConsumables; i++){
			player.getInventory().addItem(new Consumable(DropType.EGG));
		}

		numConsumables = prefs.getInteger("Bacon");
		for(int i =0; i<numConsumables; i++){
			player.getInventory().addItem(new Consumable(DropType.BACON));
		}

		numConsumables = prefs.getInteger("Cheese");
		for(int i =0; i<numConsumables; i++){
			player.getInventory().addItem(new Consumable(DropType.CHEESE));
		}

		numConsumables = prefs.getInteger("Mutton");
		for(int i =0; i<numConsumables; i++){
			player.getInventory().addItem(new Consumable(DropType.MUTTON));
		}

		numConsumables = prefs.getInteger("Milk");
		for(int i =0; i<numConsumables; i++){
			player.getInventory().addItem(new Consumable(DropType.MILK));
		}*/


		//animal data
		levelHandler.setNextLevelStart(prefs.getInteger("numAnimals"));
		levelHandler.setFruitfullMoneyP(prefs.getInteger("Fruitfulness"));
		levelHandler.setMoreMoneyP(prefs.getInteger("More"));
		levelHandler.setLongerMoneyP(prefs.getInteger("Longer"));


		//app settings
		SoundHandler.setMusicMuted(prefs.getBoolean("music"));
		SoundHandler.setSoundMuted(prefs.getBoolean("sound"));
		levelHandler.setDoTutorial(prefs.getBoolean("tutorial"));
		
	}

	/**
	 * sets the levelHandler when the play button is clicked.
	 */
	public void setDataPlay(){
		boolean levelSize = getLevelData().size >0;
		if(levelSize && getLevelData().get(0) !=null){
			level = (Integer) getLevelData().get(0);
		}
		if (levelHandler == null) {
			levelHandler = new LevelHandler(level, kidMode);
		}

	}
	
	/**
	 * sets the levelHandler when the play button is clicked.
	 */
	public void setDataRetry(){
		if (levelHandler == null) {
			Preferences prefs = Gdx.app.getPreferences(DATA_PREFS);
			level = prefs.getInteger("level");
			levelHandler = new LevelHandler(level, kidMode);	
			Player player = new Player(levelHandler.returnCameraSpeed(level));
			player.setValues(prefs.getFloat("health"), prefs.getInteger("money"));

			addToDatalevel(player,1);


			//the consumables adding back in
			for (int i = 0; i < Consumable.DropType.values().length; i++){
				numConsumables = prefs.getInteger(Consumable.DropType.values()[i].getName());
				for(int j =0; j<numConsumables; j++){
					player.getInventory().addItem(new Consumable(DropType.values()[i]));
				}
			}

/*
			numConsumables = prefs.getInteger("Eggs");
			for(int i =0; i<numConsumables; i++){
				player.getInventory().addItem(new Consumable(DropType.EGG));
			}

			numConsumables = prefs.getInteger("Bacon");
			for(int i =0; i<numConsumables; i++){
				player.getInventory().addItem(new Consumable(DropType.BACON));
			}

			numConsumables = prefs.getInteger("Cheese");
			for(int i =0; i<numConsumables; i++){
				player.getInventory().addItem(new Consumable(DropType.CHEESE));
			}

			numConsumables = prefs.getInteger("Wool");
			for(int i =0; i<numConsumables; i++){
				player.getInventory().addItem(new Consumable(DropType.MUTTON));
			}

			numConsumables = prefs.getInteger("Milk");
			for(int i =0; i<numConsumables; i++){
				player.getInventory().addItem(new Consumable(DropType.MILK));
			}
		*/
		
			//animal data
			levelHandler.setNextLevelStart(prefs.getInteger("numAnimals"));
		}
	}

	/**
	 * getter for the continue varaible for the MainMenu
	 * @return if continue should be enabled
	 */
	public boolean isContinueable() {
		return continueable;
	}

	/**
	 * this resets the data to be like null to start from the begining 
	 * used in the Options menu reset button
	 *  
	 */
	public void resetData(){
		Preferences prefs = Gdx.app.getPreferences(DATA_PREFS);
		prefs.putInteger("level", 0);

		//Player data
		prefs.putInteger("money", 0);
		prefs.putFloat("health", 0);
		
		for (int i = 0; i < Consumable.DropType.values().length; i++)
			prefs.putInteger(Consumable.DropType.values()[i].getName(), 0);
		
		/*prefs.putInteger("Bacon", 0);
		prefs.putInteger("Cheese", 0);
		prefs.putInteger("Wool", 0);
		prefs.putInteger("Milk", 0);*/

		//animal data,   0 = fruitfullness, 1=dropInterval, 2=duration
		prefs.putInteger("numAnimals", 0);
		prefs.putInteger("Fruitfulness", 0);
		prefs.putInteger("More", 0);
		prefs.putInteger("Longer", 0);

		//app settings
		prefs.putBoolean("tutorial", true);
		if(levelHandler !=null){
			levelHandler.setDoTutorial(true);
		}
		prefs.flush();
		
		//after calling flush
		Preferences prefs2 = Gdx.app.getPreferences(DATA_PREFS);
		int lev = prefs2.getInteger("level");

		if(lev <=0 ){
			continueable=false;
		}
		
		//makes the stored levelData gone
		for(int i=0; i< 5; i++){
			levelData.set(i, null);
		}
		
		//makes the levelHandler gone and the level restart at 1
		levelHandler = null;
		level = 1;
	}

	/**
	 * getter for gameScreen loading data
	 * @return 
	 */
	public boolean isHitBack() {
		return hitBack;
	}

	/**
	 * setter for the back button press
	 * @param hitBack
	 */
	public void setHitBack(boolean hitBack) {
		this.hitBack = hitBack;
	}
	
	public void retryData(){
		levelHandler = null;
		for(int i=0; i< 5; i++){
			levelData.set(i, null);
		}
		setDataRetry();
	}
	public void retryLevel(){
		for(int i=0; i< 5; i++){
			levelData.set(i, null);
		}
	}
	
	public boolean isKidMode() {
		return kidMode;
	}
	
	public void setKidMode(boolean kidMode) {
		this.kidMode = kidMode;
	}
	
}
