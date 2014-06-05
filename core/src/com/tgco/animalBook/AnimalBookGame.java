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
import com.tgco.animalBook.handlers.DatabaseHandler;
import com.tgco.animalBook.handlers.LevelHandler;
import com.tgco.animalBook.handlers.SoundHandler;
import com.tgco.animalBook.screens.GameScreen;
import com.tgco.animalBook.screens.SplashScreen;

public class AnimalBookGame extends Game {

	/**Version string  */
	public static final String version = "0.2.0";

	/** debug variables */
	public static final Boolean debugMode = true;
	private FPSLogger fpsLogger;
	
	
	private static Array<Object> levelData = new Array<Object>(4);
	
	
	private DatabaseHandler dbHand;
	
	/**
	 * The target frame rate for all motion updates.  Movement is calculated relative to this frame rate.
	 */
	public static final float TARGET_FRAME_RATE = 30;
	
	/**
	 * The current level being played
	 */
	private  int level = 1;
	/**
	 * Load all information that differs between levels
	 */
	private LevelHandler levelHandler;
	/**
	 * every game starts with the create function.
	 * this sets the initial screen to splash screen
	 */

	private int numConsumables;
	
	/** DATA_PREFS is the preference file for the data of the game*/
	private static final String DATA_PREFS = "tgco.AnimalBookGame_data";
	
	private boolean continueable = false;
			
	@Override
	public void create () {
		
		for(int i=0; i< 4; i++){
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
		
		if(lev >0 ){
			continueable = true;
		}
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
	 * life cycle of the app in which the saving of data will go here
	 */
	@Override
	public void pause() {
		super.pause();
		Gdx.app.log("My Tagg", "The app is calling pause");
		if(levelHandler != null){
			Gdx.app.log("My Tagg", "the level is " + level);
			setPrefsToFile();
		}
	}

	public void setPrefsToFile(){
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
			
			//animal data,   0 = fruitfullness, 1=dropInterval, 2=duration
			prefs.putInteger("numAnimals", levelHandler.getNextLevelStart());
			prefs.putInteger("Fruitfulness", levelHandler.getFruitfullMoneyP());
			prefs.putInteger("More", levelHandler.getMoreMoneyP());
			prefs.putInteger("Longer", levelHandler.getLongerMoneyP());
				
			//app settings
			prefs.putBoolean("music", SoundHandler.isMusicMuted());
			prefs.putBoolean("sound", SoundHandler.isSoundMuted());
			prefs.putBoolean("tutorial", levelHandler.isDoTutorial());
			
			prefs.flush();
			Gdx.app.log("My Tagg", "After flush of save");
		}
	}
	/**
	 * life cycle of the app in which the loading of the data will go here
	 */
	@Override
	public void resume() {
		super.resume();
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

	public Array<Object> getLevelData() {
		return levelData;
	}
	
	public void addToDatalevel(Object obj, int pos){
		levelData.set(pos, obj);
	}


	public LevelHandler getLevelHandler() {
		return levelHandler;
	}

	public void addToLevel(int i) {
		levelHandler.addLevel();
	}
	


	public void setDataCont() {
			Preferences prefs = Gdx.app.getPreferences(DATA_PREFS);
			level = prefs.getInteger("level");
			levelHandler = new LevelHandler(level);	
			Player player = new Player(levelHandler.returnCameraSpeed(level));
			player.setValues(prefs.getFloat("health"), prefs.getInteger("money"));
			
			addToDatalevel(player,1);
			
			
			//the consumables adding back in
			
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
				player.getInventory().addItem(new Consumable(DropType.WOOL));
			}
			
			numConsumables = prefs.getInteger("Milk");
			for(int i =0; i<numConsumables; i++){
				player.getInventory().addItem(new Consumable(DropType.MILK));
			}
			
			
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
	
	public void setDataPlay(){
		boolean levelSize =getLevelData().size >0;
		if(levelSize && getLevelData().get(0) !=null){
			level = (Integer) getLevelData().get(0);
		}
		levelHandler = new LevelHandler(level);
		
	}

	public boolean isContinueable() {
		return continueable;
	}
	
	
}
