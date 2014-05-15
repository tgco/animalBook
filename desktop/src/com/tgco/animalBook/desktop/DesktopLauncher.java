package com.tgco.animalBook.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tgco.animalBook.AnimalBookGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "AnimalBookGame - " + AnimalBookGame.version;
		config.width = 1080;
		config.height = 720;
		
		new LwjglApplication(new AnimalBookGame(), config);
	}
}
