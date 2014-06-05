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
import com.tgco.animalBook.gameObjects.Consumable;
import com.tgco.animalBook.gameObjects.Consumable.DropType;
import com.tgco.animalBook.gameObjects.Dropped;
import com.tgco.animalBook.handlers.GameScreenInputHandler;
import com.tgco.animalBook.handlers.SoundHandler;
import com.tgco.animalBook.handlers.TutorialScreenInputHandler;
import com.tgco.animalBook.view.TutorialWorld;
import com.tgco.animalBook.view.World;

/**
 * Extends the button screen to play a sequence of tutorials and teach the player
 * the game and controls.
 * 
 * @author
 *
 * 
 */
public class TutorialScreen extends ButtonScreenAdapter implements Screen {

	/**
	 * Reference to the game world where all objects are located
	 */
	private TutorialWorld tutorialWorld;

	/**
	 * Each button used on the game screen user interface overlay
	 */
	private Button inventoryButton;
	private Button upgradeButton;
	private Button skipButton;

	/**
	 * The font used when rendering strings
	 */
	private BitmapFont font;

	/**
	 * Determines if the game is paused
	 */
	boolean paused;

	/**
	 * Booleans used for tutorial progression
	 */
	private boolean swiped = false;
	private boolean tapped = false;
	private boolean pickedUp = false;
	private boolean ate = false;
	private boolean upgraded = false;

	/**
	 * Distance to move before screen pauses again for next part of tutorial
	 */
	private float moveDistance = 70;
	private float moved = 0;

	/**
	 * The stage of the tutorial the player is on for switch cases
	 */
	private int tutorialStage;

	/**
	 * overlays the instructions for the tutorial
	 */
	private Texture overlay;

	/**
	 * Constructor using the running game instance
	 * 
	 * @param gameInstance a reference to the currently running game instance
	 */
	public TutorialScreen(AnimalBookGame gameInstance) {
		super(gameInstance);

		paused = false;
		tutorialStage = 1;

		//Initialize game world
		tutorialWorld = new TutorialWorld(gameInstance);

		//Initialize rendering objects
		font = new BitmapFont(Gdx.files.internal("fonts/SketchBook.fnt"));
		batch = new SpriteBatch();
		batch.setProjectionMatrix(tutorialWorld.getCamera().combined);
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/gameScreenGrass2.jpg"));

		//Setup input processing
		inputMultiplexer = new InputMultiplexer();
		TutorialScreenInputHandler touchControls = new TutorialScreenInputHandler(gameInstance,this);
		inputMultiplexer.addProcessor(touchControls);
		Gdx.input.setInputProcessor(inputMultiplexer);
		Gdx.input.setCatchBackKey(true);
	}

	/**
	 * Draws the buttons and background as well as handles button presses
	 * 
	 * @param delta the time between two frames
	 */
	@Override
	public void render(float delta) {

		updateTutorialLogic(delta);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Process button presses
		buttonStage.act(delta);

		//render background and world
		batch.setProjectionMatrix(tutorialWorld.getCamera().combined);

		//Find the node on screen to draw grass around
		Vector2 tileNode = findTileNodeOnScreen();

		batch.begin();


		//Draw four grass textures around the node on screen
		batch.draw(backgroundTexture, tileNode.x*Gdx.graphics.getWidth(), tileNode.y*Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(backgroundTexture, (tileNode.x-1)*Gdx.graphics.getWidth(), tileNode.y*Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(backgroundTexture, tileNode.x*Gdx.graphics.getWidth(), (tileNode.y-1)*Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(backgroundTexture, (tileNode.x-1)*Gdx.graphics.getWidth(), (tileNode.y-1)*Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		//Draw players current money
		font.setColor(Color.BLACK);
		font.setScale(1.2f);
		Vector3 vect = new Vector3(Gdx.graphics.getWidth()/2 +10,0 +3*EDGE_TOLERANCE,0);
		tutorialWorld.getCamera().unproject(vect);
		font.draw(batch, "Your Money: $" + String.valueOf(tutorialWorld.getPlayer().getPlayerMoney()), vect.x ,vect.y );

		//Draw world over background
		tutorialWorld.render(batch,paused,delta);


		//Select the correct waiting function and draw correct words
		if (paused || tutorialStage == 5) {
			waitForInput();
		}

		batch.end();

		//Draw buttons over the screen
		buttonStage.draw();


	}

	public void waitForInput() {
		switch(tutorialStage) {
		case 1:
			waitForSwipe(batch);
			break;
		case 2:
			waitForTap(batch);
			break;
		case 3:
			waitForPickup(batch);
			break;
		case 4:
			waitForEat(batch);
			break;
		case 5:
			waitForUpgrade(batch);
			break;
		}
	}

	public void updateTutorialLogic(float delta) {
		if (!paused) 
			moved += AnimalBookGame.TARGET_FRAME_RATE*delta;

		if ((moveDistance - moved) <= .5) {
			paused = true;
			moved = 0;
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
		if (tutorialWorld.getCamera().position.x < 0)
			flipX = true;
		if (tutorialWorld.getCamera().position.y < 0)
			flipY = true;

		int xCoordinate = (int) ( Math.abs(tutorialWorld.getCamera().position.x / Gdx.graphics.getWidth()) + .5);
		int yCoordinate = (int) ( Math.abs(tutorialWorld.getCamera().position.y / Gdx.graphics.getHeight()) + .5);

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
		upgradeButton.setY(Gdx.graphics.getHeight() - 3*BUTTON_HEIGHT - 3*EDGE_TOLERANCE);

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
		inventoryButton.setY(Gdx.graphics.getHeight() - 2*BUTTON_HEIGHT - 2*EDGE_TOLERANCE);

		//SKIP BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/tutorialScreen/buttonSkip.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle skipButtonStyle = new ButtonStyle();
		skipButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		skipButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		
		skipButton = new Button(skipButtonStyle);
		skipButton.setWidth(BUTTON_WIDTH);
		skipButton.setHeight(BUTTON_HEIGHT);
		skipButton.setX(EDGE_TOLERANCE);
		skipButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - EDGE_TOLERANCE);

		//LISTENERS
		upgradeButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.pauseBackgroundMusic();
				if (ate) {
					upgraded = true;
					//spawn these for the return to game tutorial
					tutorialWorld.spawnObstacleAndMarket();
				}
				gameInstance.setScreen(new TutorialUpgradesScreen(gameInstance,TutorialScreen.this));
			}
		});

		inventoryButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.changeBackgroundVolume((float) .1);
				gameInstance.setScreen(new TutorialInventoryScreen(gameInstance,TutorialScreen.this));
				if (swiped && tapped & pickedUp)
					ate = true;
			}
		});
		
		skipButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.pauseBackgroundMusic();
				gameInstance.setScreen(new GameScreen(gameInstance));
				dispose();
			}
		});


		//add to stage for input detection
		buttonStage.addActor(inventoryButton);
		buttonStage.addActor(upgradeButton);
		buttonStage.addActor(skipButton);

		inputMultiplexer.addProcessor(buttonStage);
	}

	/**
	 * Functions that wait for the correct gesture to move on the tutorial
	 */
	public void waitForSwipe(SpriteBatch batch) {
		if (swiped) {
			paused = false;
			if (tutorialStage == 1)
				tutorialStage += 1;
			return;
		}
		else {
			overlay = new Texture(Gdx.files.internal("tutorialMessages/tutMessage2.png"));
			batch.draw(overlay, tutorialWorld.getCamera().position.x - Gdx.graphics.getWidth()/2, tutorialWorld.getCamera().position.y - Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			return;
		}
	}

	public void waitForTap(SpriteBatch batch) {
		if (tapped) {
			paused = false;
			overlay.dispose();
			if (tutorialStage == 2)
				tutorialStage += 1;
			return;
		}
		else {
			overlay = new Texture(Gdx.files.internal("tutorialMessages/tutMessage3.png"));
			batch.draw(overlay, tutorialWorld.getCamera().position.x - Gdx.graphics.getWidth()/2, tutorialWorld.getCamera().position.y - Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			return;
		}
	}

	public void waitForPickup(SpriteBatch batch) {
		if (pickedUp) {
			paused = false;
			overlay.dispose();
			tutorialWorld.removeSpawnedEgg();
			if (tutorialStage == 3)
				tutorialStage += 1;
			return;
		}
		else
			overlay = new Texture(Gdx.files.internal("tutorialMessages/tutMessage4.png"));
		batch.draw(overlay, tutorialWorld.getCamera().position.x - Gdx.graphics.getWidth()/2, tutorialWorld.getCamera().position.y - Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		tutorialWorld.spawnEgg();
		return;
	}

	public void waitForEat(SpriteBatch batch) {
		if (ate) {
			paused = false;
			overlay.dispose();
			if (tutorialStage == 4)
				tutorialStage += 1;
			return;
		}
		else {
			overlay = new Texture(Gdx.files.internal("tutorialMessages/tutMessage5.png"));
			batch.draw(overlay, tutorialWorld.getCamera().position.x - Gdx.graphics.getWidth()/2, tutorialWorld.getCamera().position.y - Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			tutorialWorld.getPlayer().setHealth(50f);
			if(tutorialWorld.getPlayer().getInventory().getInventory().get(DropType.EGG).size == 0) {
				for (int i = 0; i < 5; i++) {
					tutorialWorld.getPlayer().getInventory().addItem(new Consumable(DropType.EGG));
				}
			}
			//Draw text that says go to inventory to eat
			return;
		}
	}

	public void waitForUpgrade(SpriteBatch batch) {
		if (upgraded) {
			paused = false;
			overlay.dispose();
			//Continuously runs when player is told to herd into the market to end the tutorial
			tutorialWorld.getPlayer().setHealth(100f);
			overlay = new Texture(Gdx.files.internal("tutorialMessages/tutMessage7.png"));
			batch.draw(overlay, tutorialWorld.getCamera().position.x - Gdx.graphics.getWidth()/2, tutorialWorld.getCamera().position.y - Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			return;
		}
		else {
			if (tutorialWorld.getPlayer().getPlayerMoney() == 0) {
				tutorialWorld.getPlayer().addPlayerMoney(1000);
			}
			overlay = new Texture(Gdx.files.internal("tutorialMessages/tutMessage6.png"));
			batch.draw(overlay, tutorialWorld.getCamera().position.x - Gdx.graphics.getWidth()/2, tutorialWorld.getCamera().position.y - Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			return;
		}
	}

	/**
	 * Disposes of all objects that should release memory
	 */
	@Override
	public void dispose() {
		super.dispose();
		tutorialWorld.dispose();
	}

	/**
	 * Returns the instance of the world that game screen is using
	 * 
	 * @return		reference to the current game world
	 */
	public TutorialWorld getWorld() {
		return tutorialWorld;
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


	public boolean isSwiped() {
		return swiped;
	}

	public void setSwiped(boolean swiped) {
		this.swiped = swiped;
	}

	public boolean isTapped() {
		return tapped;
	}

	public void setTapped(boolean tapped) {
		this.tapped = tapped;
	}

	public boolean isPickedUp() {
		return pickedUp;
	}

	public void setPickedUp(boolean pickedUp) {
		this.pickedUp = pickedUp;
	}

	public boolean isAte() {
		return ate;
	}

	public void setAte(boolean ate) {
		this.ate = ate;
	}

	public boolean isUpgraded() {
		return upgraded;
	}

	public void setUpgraded(boolean upgraded) {
		this.upgraded = upgraded;
	}

	public int getTutorialStage() {
		return tutorialStage;
	}

	public void incrementTutorialStage() {
		tutorialStage += 1;
	}



}
