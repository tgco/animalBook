package com.tgco.animalBook.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.handlers.SoundHandler;

public class lostDialog extends Dialog{

	private AnimalBookGame gameInstance;
	public lostDialog(String title, Skin skin, AnimalBookGame gameInstance, boolean noAnimals) {
		super(title, skin);
		this.gameInstance = gameInstance;
		if(noAnimals){
			this.text("Aleksandra has lost all her animals");
		}
		else{
			this.text("Aleksandra has not eaten enough, so she starved to death");
		}
	}
	
	{
		
		button("Retry", "retry");
		button("Quit", "quit");
		
	}
	
	
	@Override
	public void result(Object object){
		if(object.equals("retry")){
			gameInstance.setScreen(new GameScreen(gameInstance));
			SoundHandler.toggleSounds();
			SoundHandler.toggleMusic();
		}
		else if(object.equals("quit")){
			gameInstance.setScreen(new MainMenuScreen(gameInstance));
		}
	}
}
