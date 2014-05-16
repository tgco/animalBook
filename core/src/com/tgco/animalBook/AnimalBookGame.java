package com.tgco.animalBook;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.tgco.animalBook.screens.MainMenuScreen;
import com.tgco.animalBook.screens.SplashScreen;

public class AnimalBookGame extends Game {

	//Version string, debug variables
	public static final String version = "0.0.1";
	public static final Boolean debugMode = true;
	private FPSLogger fpsLogger;

	@Override
	public void create () {
		//Set the initial screen
		setScreen(new MainMenuScreen(this));

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
}
