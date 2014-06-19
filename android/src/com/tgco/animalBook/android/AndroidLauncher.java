package com.tgco.animalBook.android;

import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.tgco.animalBook.AnimalBookGame;

public class AndroidLauncher extends AndroidApplication {
	private AnimalBookGame ABGame;


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		ABGame = new AnimalBookGame();
		initialize(ABGame, config);
	}
	
	
	 @Override
		public void onResume(){
		 super.onResume();
		 ABGame.reset();
		}	
	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.d("Aleksandra", "The onDestroy is called");
	}
}
