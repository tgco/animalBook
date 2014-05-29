package com.tgco.animalBook.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.handlers.SoundHandler;

public class LostDialog extends Dialog{

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
	public LostDialog(String title, Skin skin, AnimalBookGame gameInstance, boolean noAnimals) {
		super(title, skin);
		this.gameInstance = gameInstance;
		if(noAnimals){
			this.text("Aleksandra has lost all her animals");
		}
		else{
			this.text("Aleksandra has not eaten enough, so she was too hungry to continue");
		}
	}
	
	{
		
		button("Retry", "retry");
		button("Quit", "quit");
		
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
		if(object.equals("retry")){
			GameScreen temp = gameInstance.getGameScreen();
			gameInstance.setScreen(new GameScreen(gameInstance));
			temp.dispose();
		}
		else if(object.equals("quit")){
			GameScreen temp = gameInstance.getGameScreen();
			gameInstance.setScreen(new MainMenuScreen(gameInstance));
			temp.dispose();
		}
	}
}
