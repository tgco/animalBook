package com.tgco.animalBook.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tgco.animalBook.AnimalBookGame;

public abstract class ButtonScreenAdapter {

	protected Texture backgroundTexture;
	protected Skin buttonSkin;
	protected Stage buttonStage;
	protected AnimalBookGame gameInstance;

	//Button texture initialization
	protected TextureAtlas atlas;

	public ButtonScreenAdapter(AnimalBookGame gameInstance) {
		this.gameInstance = gameInstance;
	}
	
	public void dispose() {
		atlas.dispose();
		backgroundTexture.dispose();
		buttonSkin.dispose();
		buttonStage.dispose();
		gameInstance.dispose();
	}

}