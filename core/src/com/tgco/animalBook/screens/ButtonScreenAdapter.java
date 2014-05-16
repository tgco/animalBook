package com.tgco.animalBook.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tgco.animalBook.AnimalBookGame;

public abstract class ButtonScreenAdapter {

	private Texture backgroundTexture;
	private Skin buttonSkin;
	private Stage buttonStage;
	private AnimalBookGame gameInstance;

	public ButtonScreenAdapter(AnimalBookGame gameInstance) {
		this.gameInstance = gameInstance;
	}

}
