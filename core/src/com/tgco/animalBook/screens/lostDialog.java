package com.tgco.animalBook.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tgco.animalBook.AnimalBookGame;

public class lostDialog extends Dialog{

	private AnimalBookGame gameInstance;
	public lostDialog(String title, Skin skin, AnimalBookGame gameInstance) {
		super(title, skin);
		this.gameInstance = gameInstance;
	}
	
	{
		text("You lost all your animals");
		button("Retry", "retry");
		button("Quit", "quit");
		
	}
	
	@Override
	public void result(Object object){
		if(object.equals("retry")){
			gameInstance.setScreen(new GameScreen(gameInstance));
		}
	}
}
