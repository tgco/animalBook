package com.tgco.animalBook.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.handlers.SoundHandler;

public class EndGameDialog extends Dialog{

	private AnimalBookGame gameInstance;
	
	/**
	 * Constructs a new lose dialog with a title, a skin, a game instance, and a boolean option
	 * <p>
	 * The boolean noAnimals determines what message is output to the player
	 * 
	 * @param title the title of the dialog
	 * @param skin the skin to be used by the dialog
	 * @param gameInstance the game instance to reference
	 * @param noAnimals represents cause of loss
	 */
	public EndGameDialog(String title, Skin skin, AnimalBookGame gameInstance) {
		super(title, skin);
		this.gameInstance = gameInstance;
		this.text("Congratulations! You have won the game!");
	}
	
	{
		
		button("Reset Data", "reset");
		button("Main Menu", "mainmenu");
		
	}
	
	/**
	 * Performs actions depending on which button is pressed
	 * <p>
	 * If the player presses the retry button, a new game screen is instantiated, and the player
	 * replays the previous level. If they press the quit button, they are returned to the main
	 * menu.
	 * 
	 * @param object what button gets clicked
	 */
	@Override
	public void result(Object object){
		SoundHandler.toggleSounds();
		SoundHandler.toggleMusic();
		if(object.equals("reset")){
			gameInstance.resetData();
			GameScreen temp = gameInstance.getGameScreen();
			gameInstance.setScreen(new MainMenuScreen(gameInstance));
			temp.dispose();
		}
		else if(object.equals("mainmenu")){
			GameScreen temp = gameInstance.getGameScreen();
			gameInstance.setScreen(new MainMenuScreen(gameInstance));
			temp.dispose();
		}
	}
}
