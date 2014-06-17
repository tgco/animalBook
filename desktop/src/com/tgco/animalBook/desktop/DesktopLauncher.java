package com.tgco.animalBook.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tgco.animalBook.AnimalBookGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Aleksandra - " + AnimalBookGame.version;
		config.width = 1024;
		config.height = 600;
		
		new LwjglApplication(new AnimalBookGame(), config);
	}
}
