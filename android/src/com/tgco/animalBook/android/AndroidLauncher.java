package com.tgco.animalBook.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.screens.GameScreen;
import com.tgco.animalBook.screens.MainMenuScreen;

public class AndroidLauncher extends AndroidApplication {
	private AnimalBookGame ABGame;


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		ABGame = new AnimalBookGame();
		initialize(ABGame, config);
	}
	
	
	/* @Override
		public void onBackPressed(){
	    	
	     if(ABGame.getGScreen() instanceof MainMenuScreen){
	    		super.onBackPressed();
	     }
		}	
		*/	
}
