package com.tgco.animalBook.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.handlers.GameScreenInputHandler;
import com.tgco.animalBook.handlers.SoundHandler;
import com.tgco.animalBook.view.World;


public class GameScreen extends ButtonScreenAdapter implements Screen {

	//World of objects
	private World gameWorld;
	
	//buttons
	private Button inventoryButton;
	private Button marketButton;

	//Rendering objects
	private SpriteBatch batch;

	//Input handler
	private InputMultiplexer inputMultiplexer;


	public GameScreen(AnimalBookGame gameInstance) {
		super(gameInstance);

		//Initialize game world
		gameWorld = new World();
		
		//Initialize rendering objects
		batch = new SpriteBatch();
		batch.setProjectionMatrix(gameWorld.getCamera().combined);
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/gameScreenGrass2.jpg"));

		//Setup input processing
		inputMultiplexer = new InputMultiplexer();
		GameScreenInputHandler touchControls = new GameScreenInputHandler(gameInstance,this);
		inputMultiplexer.addProcessor(touchControls);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Process button presses
		buttonStage.act(delta);
		
		//render background and world
		batch.setProjectionMatrix(gameWorld.getCamera().combined);
		
		//Find the node on screen to draw grass around
		Vector2 tileNode = findTileNodeOnScreen();
		
		batch.begin();
		
		//Draw four grass textures around the node on screen
		batch.draw(backgroundTexture, tileNode.x*Gdx.graphics.getWidth(), tileNode.y*Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(backgroundTexture, (tileNode.x-1)*Gdx.graphics.getWidth(), tileNode.y*Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(backgroundTexture, tileNode.x*Gdx.graphics.getWidth(), (tileNode.y-1)*Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(backgroundTexture, (tileNode.x-1)*Gdx.graphics.getWidth(), (tileNode.y-1)*Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//Draw world over background
		gameWorld.render(batch);
		batch.end();

		//Draw buttons over the screen
		buttonStage.draw();
	}
	
	//Finds which "node" is visible on screen and draws four grass tiles around it
	public Vector2 findTileNodeOnScreen() {
		
		//If camera is in the negative quadrant
		boolean flipX = false;
		boolean flipY = false;
		
		if (gameWorld.getCamera().position.x < 0)
			flipX = true;
		if (gameWorld.getCamera().position.y < 0)
			flipY = true;
		
		int xCoordinate = (int) ( Math.abs(gameWorld.getCamera().position.x / Gdx.graphics.getWidth()) + .5);
		int yCoordinate = (int) ( Math.abs(gameWorld.getCamera().position.y / Gdx.graphics.getHeight()) + .5);
		
		//Provides correct float truncation for tiling in the negative x/y direction
		if (flipX)
			xCoordinate *= -1;
		if (flipY)
			yCoordinate *= -1;
		
		return new Vector2(xCoordinate,yCoordinate);
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
		marketButton.setX(Gdx.graphics.getWidth() - BUTTON_WIDTH - EDGE_TOLERANCE);
		marketButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - EDGE_TOLERANCE);

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
		inventoryButton.setX(EDGE_TOLERANCE);
		inventoryButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - EDGE_TOLERANCE);

		//LISTENERS
		marketButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.pauseBackgroundMusic();
				gameInstance.setScreen(new MarketScreen(gameInstance,GameScreen.this));
			}
		});

		inventoryButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.changeBackgroundVolume((float) .1);
				gameInstance.setScreen(new InventoryScreen(gameInstance,GameScreen.this));
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
	
	public World getWorld() {
		return gameWorld;
	}
	
	public void resetInputProcessors() {
		Gdx.input.setInputProcessor(inputMultiplexer);
	}
	

}
