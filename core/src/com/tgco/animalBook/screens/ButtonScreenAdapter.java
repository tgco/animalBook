package com.tgco.animalBook.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tgco.animalBook.AnimalBookGame;

public abstract class ButtonScreenAdapter {


	protected Texture backgroundTexture;
	protected Skin buttonSkin;
	protected Stage buttonStage;
	protected AnimalBookGame gameInstance;
	protected InputMultiplexer inputMultiplexer;

	//dimensions for buttons
	protected static final float BUTTON_WIDTH = (1f/10f)*Gdx.graphics.getWidth();
	protected static final float BUTTON_HEIGHT = (1f/10f)*Gdx.graphics.getWidth();

	//dimensions for upgrades screen
	protected static final float UPGRADE_BUTTON_WIDTH = (1f/6f)*Gdx.graphics.getWidth();
	protected static final float UPGRADE_BUTTON_HEIGHT = (1f/6f)*Gdx.graphics.getWidth();

	//Menu buttons are larger
	protected static final float MENU_BUTTON_WIDTH = (2f/5f)*Gdx.graphics.getWidth();
	protected static final float MENU_BUTTON_HEIGHT = (1f/10f)*Gdx.graphics.getWidth();

	//distance between buttons and between the edge and a button
	protected static final float EDGE_TOLERANCE = (.03f)*Gdx.graphics.getHeight();

	//Button texture initialization
	protected TextureAtlas atlas;

	//Batch for background rendering
	protected SpriteBatch batch;

	/**
	 * Constructs a new Button Screen Adapter with a game instance
	 * 
	 * @param gameInstance the game instance to reference
	 */
	public ButtonScreenAdapter(AnimalBookGame gameInstance) {
		this.gameInstance = gameInstance;
	}

	/**
	 * Disposes of all objects contained in the button screen adapter
	 */
	public void dispose() {
		atlas.dispose();
		backgroundTexture.dispose();
		buttonSkin.dispose();
		buttonStage.dispose();
		if (batch != null)
			batch.dispose();
	}

	protected abstract void initializeButtons();

	public void resetInputProcessors() {
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

}
