package com.tgco.animalBook.screens;

import com.badlogic.gdx.Gdx;
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

	//dimensions for buttons
	protected static final float BUTTON_WIDTH = (1f/6f)*Gdx.graphics.getWidth();
	protected static final float BUTTON_HEIGHT = (1f/5f)*Gdx.graphics.getHeight();
	
	//Menu buttons are larger
	protected static final float MENU_BUTTON_WIDTH = (1f/3f)*Gdx.graphics.getWidth();
	protected static final float MENU_BUTTON_HEIGHT = (1f/5f)*Gdx.graphics.getHeight();
	
	//distance between buttons and between the edge and a button
	protected static final float EDGE_TOLERANCE = 20;

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
	}
	
	protected abstract void initializeButtons();

}
