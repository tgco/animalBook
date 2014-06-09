package com.tgco.animalBook.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
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

/**
 * Draws the user interface for game play, and creates a world where all game objects are made.
 * Also draws the background and draws a lose screen once the player has lost.
 * 
 * @author
 *
 * 
 */
public class GameScreen extends ButtonScreenAdapter implements Screen {

	/**
	 * Reference to the game world where all objects are located
	 */
	private World gameWorld;

	/**
	 * Each button used on the game screen user interface overlay
	 */
	private Button inventoryButton;
	private Button upgradeButton;
	private Button pauseButton;
	private Button eatButton;

	/**
	 * Stage to draw the screen once the player has lost
	 */
	private Stage popupStage;

	/**
	 * Determines if the player has lost
	 */
	private boolean hasLost = false;

	/**
	 * The font used when rendering strings
	 */
	private BitmapFont font;

	/**
	 * Determines if the game is paused
	 */
	boolean paused;

	private boolean isMain = true;

	/**
	 * Constructor using the running game instance
	 * 
	 * @param gameInstance a reference to the currently running game instance
	 */
	public GameScreen(AnimalBookGame gameInstance) {
		super(gameInstance);

		popupStage = new Stage();
		paused = false;

		//Initialize game world
		gameWorld = new World(gameInstance);

		//Initialize rendering objects
		font = new BitmapFont(Gdx.files.internal("fonts/SketchBook.fnt"));
		batch = new SpriteBatch();
		batch.setProjectionMatrix(gameWorld.getCamera().combined);
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/gameScreenGrass2.jpg"));

		//Setup input processing
		inputMultiplexer = new InputMultiplexer();
		GameScreenInputHandler touchControls = new GameScreenInputHandler(gameInstance,this);
		inputMultiplexer.addProcessor(touchControls);
		Gdx.input.setInputProcessor(inputMultiplexer);
	
	}

	/**
	 * Draws the buttons and background as well as handles button presses
	 * 
	 * @param delta the time between two frames
	 */
	@Override
	public void render(float delta) {

		if(!hasLost) {
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

			//Draw players current money
			font.setColor(Color.WHITE);
			font.setScale(1.2f);
			Vector3 vect = new Vector3(Gdx.graphics.getWidth()/2 +10,0 +3*EDGE_TOLERANCE,0);
			gameWorld.getCamera().unproject(vect);
			font.draw(batch, "Your Money: $" + String.valueOf(gameWorld.getPlayer().getPlayerMoney()), vect.x ,vect.y );

			//Draw world over background
			gameWorld.render(batch,paused,delta);
			
			batch.end();

			//Draw buttons over the screen
			buttonStage.draw();
			
		}
		else { //if player lost
			Gdx.gl.glClearColor(1, 1, 1, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			popupStage.act(delta);
			popupStage.draw();
		}
	}

	/**
	 * Returns the point in world coordinates that grass should be drawn around so that grass
	 * is only rendered when it is on screen.
	 * 
	 * @return		the point on screen to draw grass around
	 */
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

	/**
	 * Runs when the screen is resized in order to keep button sizes and locations correct
	 * 
	 * @param width the new width of the screen
	 * @param height the new height of the screen
	 */
	@Override
	public void resize(int width, int height) {
		if ( buttonStage == null)
			buttonStage = new Stage();
		buttonStage.clear();
		//reinit buttons
		initializeButtons();
	}

	/**
	 * Initializes all button objects
	 */
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
		eatButton.setColor(Color.CLEAR);

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
				isMain = false;
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
				isMain = false;
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

		//add to stage for input detection
		buttonStage.addActor(inventoryButton);
		buttonStage.addActor(upgradeButton);
		buttonStage.addActor(pauseButton);
		buttonStage.addActor(eatButton);

		inputMultiplexer.addProcessor(buttonStage);
	}

	/**
	 * Disposes of all objects that should release memory
	 */
	@Override
	public void dispose() {
		super.dispose();
		gameWorld.dispose();
	}

	/**
	 * Sets the game to the lost mode when the player has lost
	 * 
	 * @param noAnimals true if the player lost due to losing all animals
	 */
	public void setLost(boolean noAnimals){
		hasLost = true;
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		LostDialog lostD = new LostDialog("You Lost", skin, gameInstance,noAnimals);
		lostD.show(popupStage);
		popupStage.addActor(lostD);
		inputMultiplexer.addProcessor(popupStage);
	}
	
	/**
	 * Returns the instance of the world that game screen is using
	 * 
	 * @return		reference to the current game world
	 */
	public World getWorld() {
		return gameWorld;
	}

	/**
	 * Returns if the game is currently paused
	 * 
	 * @return		true if the game is paused
	 */
	public boolean isPaused() {
		return paused;
	}
	
	/**
	 * Unused methods for detecting screen events
	 */
	@Override
	public void show() {

	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	public boolean toMain() {
		return isMain ;
	}

	public void setMain() {
		isMain = true;
		
	}

}
