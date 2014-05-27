package com.tgco.animalBook.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
	private Button upgradeButton;
	private Button pauseButton;
	private Button eatButton;
	private UpgradesScreen upgradeScreen;
	
	private Stage popupStage;
	private boolean hasLost = false;
	
	
	
	private BitmapFont font;
	
	
	boolean paused;

	public GameScreen(AnimalBookGame gameInstance) {
		super(gameInstance);
		popupStage = new Stage();
		paused = false;

		//Initialize game world
		gameWorld = new World(gameInstance);
		font = new BitmapFont(Gdx.files.internal("fonts/SketchBook.fnt"));
		//Initialize rendering objects
		batch = new SpriteBatch();
		batch.setProjectionMatrix(gameWorld.getCamera().combined);
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/gameScreenGrass2.jpg"));

		//Setup input processing
		inputMultiplexer = new InputMultiplexer();
		GameScreenInputHandler touchControls = new GameScreenInputHandler(gameInstance,this);
		inputMultiplexer.addProcessor(touchControls);
		Gdx.input.setInputProcessor(inputMultiplexer);
		Gdx.input.setCatchBackKey(true);
		
		
	}

	@Override
	public void render(float delta) {
		
		if(!hasLost ){
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
		font.setColor(Color.BLACK);
		font.setScale(1.2f);
		Vector3 vect = new Vector3(Gdx.graphics.getWidth()/2 +10,0 +3*EDGE_TOLERANCE,0);
		gameWorld.getCamera().unproject(vect);
		font.draw(batch, "Your Money: $" + String.valueOf(gameWorld.getPlayer().getPlayerMoney()), vect.x ,vect.y );

		gameWorld.render(batch,paused);
		
		
		batch.end();

		//Draw buttons over the screen
		buttonStage.draw();
		
		}
		else{
			popupStage.act(delta);
			popupStage.draw();
		}
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

	@Override
	protected void initializeButtons() {

		//UPGRADE BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/gameScreen/upgradeButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle upgradeButtonStyle = new ButtonStyle();
		upgradeButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		upgradeButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		upgradeButton = new Button(upgradeButtonStyle);
		upgradeButton.setWidth(BUTTON_WIDTH);
		upgradeButton.setHeight(BUTTON_HEIGHT);
		upgradeButton.setX(EDGE_TOLERANCE);
		upgradeButton.setY(Gdx.graphics.getHeight() - 3*BUTTON_HEIGHT - 2*EDGE_TOLERANCE);

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
		inventoryButton.setY(Gdx.graphics.getHeight() - 2*BUTTON_HEIGHT - EDGE_TOLERANCE);
		
		//EAT BUTTON
		//TEMP FOR TESTING
		atlas = new TextureAtlas(Gdx.files.internal("buttons/gameScreen/eatButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		
		ButtonStyle eatButtonStyle = new ButtonStyle();
		eatButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		eatButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		eatButton = new Button(eatButtonStyle);
		eatButton.setWidth(BUTTON_WIDTH);
		eatButton.setHeight(BUTTON_HEIGHT);
		eatButton.setX(EDGE_TOLERANCE);
		eatButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT);

		//PAUSE BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/gameScreen/pauseButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle pauseButtonStyle = new ButtonStyle();
		if (!paused) {
			pauseButtonStyle.up = buttonSkin.getDrawable("pauseButton");
			pauseButtonStyle.checked = buttonSkin.getDrawable("playButton");
		} else {
			pauseButtonStyle.up = buttonSkin.getDrawable("playButton");
			pauseButtonStyle.checked = buttonSkin.getDrawable("pauseButton");
		}

		pauseButton = new Button(pauseButtonStyle);
		pauseButton.setWidth(BUTTON_WIDTH);
		pauseButton.setHeight(BUTTON_HEIGHT);
		pauseButton.setX(Gdx.graphics.getWidth() - BUTTON_WIDTH - EDGE_TOLERANCE);
		pauseButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - EDGE_TOLERANCE);

		//LISTENERS
		upgradeButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.pauseBackgroundMusic();
				gameInstance.setScreen(new UpgradesScreen(gameInstance,GameScreen.this));
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
		
		eatButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				gameWorld.getPlayer().eat(10f);
				gameWorld.getPlayer().addPlayerMoney(100);
			}
		});

		pauseButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.changeBackgroundVolume((float) .1);
				paused = !paused;
			}
		});

		buttonStage.addActor(inventoryButton);
		buttonStage.addActor(upgradeButton);
		buttonStage.addActor(pauseButton);
		buttonStage.addActor(eatButton);

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
		gameWorld.dispose();
	}

	public World getWorld() {
		return gameWorld;
	}
	
	public boolean isPaused() {
		return paused;
	}

	public void resetInputProcessors() {
		Gdx.input.setInputProcessor(inputMultiplexer);
	}
	
	public void setLost(){
		hasLost = true;
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		lostDialog lostD = new lostDialog("You Lost", skin);
		popupStage.addActor(lostD);
		inputMultiplexer.addProcessor(popupStage);
	}

}
