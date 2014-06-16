package com.tgco.animalBook.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.handlers.SoundHandler;

public class EndGameDialog extends Dialog{

	private AnimalBookGame gameInstance;
	private EndGameStory story;
	
	/**
	 * Constructs a new lose dialog with a title, a skin, and a game instance
	 * 
	 * @param title the title of the dialog
	 * @param skin the skin to be used by the dialog
	 * @param gameInstance the game instance to reference
	 */
	public EndGameDialog(String title, Skin skin, AnimalBookGame gameInstance, EndGameStory story) {
		super(title, skin);
		this.gameInstance = gameInstance;
		this.story = story;
		this.text("Congratulations! You have won the game!");
	}
	
	{
		
		button("Reset Data", "reset");
		button("Main Menu", "mainmenu");
		
	}
	
	/**
	 * Performs actions depending on which button is pressed
	 * <p>
	 * If the player presses the reset button, data is reset and they may replay the game from the beginning.
	 * If the player presses the main menu button, they are returned to the main menu with no data reset.
	 * 
	 * @param object what button gets clicked
	 */
	@Override
	public void result(Object object){
		//SoundHandler.toggleSounds();
		//SoundHandler.toggleMusic();
		if(object.equals("reset")){
			hide();
			story.setConfirmDialog();
		}
		else if(object.equals("mainmenu")){
			SoundHandler.pauseStoryBackgroundMusic();
			EndGameStory temp = (EndGameStory) gameInstance.getScreen();
			gameInstance.setScreen(new MainMenuScreen(gameInstance));
			temp.dispose();
		}
	}
}
