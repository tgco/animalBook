package com.tgco.animalBook;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.tgco.animalBook.handlers.SoundHandler;
import com.tgco.animalBook.screens.MainMenuScreen;
import com.tgco.animalBook.screens.SplashScreen;

public class AnimalBookGame extends Game {

	//Version string, debug variables
	public static final String version = "0.0.5";
	public static final Boolean debugMode = true;
	private FPSLogger fpsLogger;

	@Override
	public void create () {
		//Set the initial screen

		setScreen(new SplashScreen(this));

		if (debugMode)
			fpsLogger = new FPSLogger();
	}

	@Override
	public void render () {
		super.render();
		if (debugMode)
			fpsLogger.log();
	}

	@Override
	public void dispose() {
		super.dispose();
		getScreen().dispose();
		SoundHandler.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	public Screen getGScreen(){
		return getScreen();
	}
	public void setMMScreen() {
		
		setScreen(new MainMenuScreen(this));
		
	}
}
