package com.tgco.animalBook.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tgco.animalBook.AnimalBookGame;

public class GameScreen extends ButtonScreenAdapter implements Screen {

	//buttons
	private Button inventoryButton;
	private Button marketButton;
	
	//dimensions
	private final float BUTTON_WIDTH = 100;
	private final float BUTTON_HEIGHT = 100;
	
	//Input handler
	private InputMultiplexer inputMultiplexer;
	
	
	public GameScreen(AnimalBookGame gameInstance) {
		super(gameInstance);
		
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		buttonStage.act(delta);
		buttonStage.draw();
	}

	@Override
	public void resize(int width, int height) {
		if ( buttonStage == null)
			buttonStage = new Stage();
		buttonStage.clear();
		//reinit buttons
		initializeButtons();
	}

	private void initializeButtons() {
		
		//MARKET BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/gameScreen/marketButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		
		ButtonStyle marketButtonStyle = new ButtonStyle();
		marketButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		marketButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		
		marketButton = new Button(marketButtonStyle);
		marketButton.setWidth(BUTTON_WIDTH);
		marketButton.setHeight(BUTTON_HEIGHT);
		marketButton.setX(Gdx.graphics.getWidth() - BUTTON_WIDTH - 20);
		marketButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - 20);
		
		//INVENTORY BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/gameScreen/inventoryButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		
		ButtonStyle inventoryButtonStyle = new ButtonStyle();
		inventoryButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		inventoryButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		
		inventoryButton = new Button(inventoryButtonStyle);
		inventoryButton.setWidth(BUTTON_WIDTH);
		inventoryButton.setHeight(BUTTON_HEIGHT);
		inventoryButton.setX(20);
		inventoryButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - 20);
		
		//LISTENERS
		marketButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				gameInstance.setScreen(new MarketScreen(gameInstance));
			}
		});
		
		inventoryButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				gameInstance.setScreen(new InventoryScreen(gameInstance));
			}
		});
		
		buttonStage.addActor(inventoryButton);
		buttonStage.addActor(marketButton);
		
		inputMultiplexer.addProcessor(buttonStage);
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		super.dispose();
		
	}

}
