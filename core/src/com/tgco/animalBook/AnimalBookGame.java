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
	//Testing new control system (taps)
	public static final boolean tapControls = true;
	
	
	private static Array<Object> levelData = new Array<Object>(4);
	
	private DatabaseHandler dbHand;
	
	/**
	 * The target frame rate for all motion updates.  Movement is calculated relative to this frame rate.
	 */
	public static final float TARGET_FRAME_RATE = 30;
	
	/**
	 * The current level being played
	 */
	private static int level = 1;
	/**
	 * Load all information that differs between levels
	 */
	private LevelHandler levelHandler;
	/**
	 * every game starts with the create function.
	 * this sets the initial screen to splash screen
	 */
	
	/** DATA_PREFS is the preference file for the data of the game*/
	private static final String DATA_PREFS = "tgco.AnimalBookGame_data";
			
	/** pastLevel is the original level when is started used for storing the data */
	private int pastLevel = level;
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
		if(level < levelHandler.getLevel()){
			Gdx.app.log("My Tagg", "PastLevel: " + pastLevel + " level: " + level);
			Preferences prefs = Gdx.app.getPreferences(DATA_PREFS);
			prefs.putInteger("level", levelHandler.getLevel());
			
			//getPlayer data
			prefs.putInteger("money", ((Player) levelData.get(1)).getPlayerMoney());
			prefs.putFloat("health", ((Player) levelData.get(1)).getHealth());
			
			
			prefs.flush();
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
	
	public static int getLevel() {
		return level;
	}

	public void setDataCont() {
			Preferences prefs = Gdx.app.getPreferences(DATA_PREFS);
			level = prefs.getInteger("level");
			levelHandler = new LevelHandler(level);	
			pastLevel = level;
	}
	
	public void setDataPlay(){
		boolean levelSize =getLevelData().size >0;
		if(levelSize && getLevelData().get(0) !=null){
			level = (Integer) getLevelData().get(0);
		}
		levelHandler = new LevelHandler(level);
		pastLevel = level;
	}
}
