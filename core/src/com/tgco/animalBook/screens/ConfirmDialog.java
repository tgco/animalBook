package com.tgco.animalBook.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.handlers.SoundHandler;

public class ConfirmDialog extends Dialog{

	private AnimalBookGame gameInstance;
	
	/**
	 * Constructs a new confirm dialog with a title, a skin, a game instance, and a boolean option
	 * 
	 * @param title the title of the dialog
	 * @param skin the skin to be used by the dialog
	 * @param gameInstance the game instance to reference
	 * @param noAnimals represents cause of loss
	 */
	public ConfirmDialog(String title, Skin skin, AnimalBookGame gameInstance, String words, int box) {
		super(title, skin);
		this.gameInstance = gameInstance;
		this.text(words);
		switch(box){
			case 0:
				this.button("Main Menu", "Main Menu");
				this.button("Cancel", "Cancel");
				break;
			case 1:
				button("Yes", "Yes");
				button("No", "No");
				break;
			case 2:
				button("Yes", "yes2");
				button("No", "no2");
		}
	}
	
	
	/**
	 * Performs actions depending on which button is pressed
	 * 
	 * 
	 * @param object what button gets clicked
	 */
	@Override
	public void result(Object object){
		if(object.equals("Main Menu")){
			gameInstance.getScreen().dispose();
			gameInstance.setScreen(new MainMenuScreen(gameInstance));
			
		}
		else if(object.equals("Cancel")){
			((MarketScreen) gameInstance.getScreen()).setCancel();
		}else if(object.equals("Yes")){
			Gdx.app.exit();
		}
		else if(object.equals("No")){
			((MainMenuScreen) gameInstance.getScreen()).setCancel();
		}else if(object.equals("yes2")){
			gameInstance.resetData();
			((OptionsScreen) gameInstance.getScreen()).setExitDialog();
		}else if(object.equals("no2")){
			((OptionsScreen) gameInstance.getScreen()).setExitDialog();
		}
	}
}
