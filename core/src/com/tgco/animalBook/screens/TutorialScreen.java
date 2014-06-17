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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.AnimalBookGame.state;
import com.tgco.animalBook.gameObjects.Consumable;
import com.tgco.animalBook.gameObjects.Consumable.DropType;
import com.tgco.animalBook.handlers.MenuHandlerTut;
import com.tgco.animalBook.handlers.SoundHandler;
import com.tgco.animalBook.handlers.TutorialScreenInputHandler;
import com.tgco.animalBook.view.TutorialWorld;

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
	 * The width and height of tiles for the background
	 */
	private float tileWidth = Gdx.graphics.getWidth()/1f;
	private float tileHeight = Gdx.graphics.getHeight()/1f;

/**
	 * Each button used on the game screen user interface overlay
	 */
	private Button alexButton;
	
	private Image alexInfoImage; 
	private Label infoLabel;

	private DragAndDrop dnd;

	private static boolean mainMenuInitialized, inventoryMenuInitialized, upgradesMenuInitialized, optionsMenuInitialized;

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
	private final float FONT_SCALE = Gdx.graphics.getHeight()/750f;

	/**
	 * If the tutorial screen is paused
	 */
	private boolean paused;
	
	private MenuHandlerTut menuHandler;

	/**
	 * textures for health bar, etc
	 */
	private Texture black = new Texture(Gdx.files.internal("primitiveTextures/black.png"));
	private Texture red = new Texture(Gdx.files.internal("primitiveTextures/red.png"));
	private Texture yellow = new Texture(Gdx.files.internal("primitiveTextures/yellow.png"));
	private Texture green = new Texture(Gdx.files.internal("primitiveTextures/green.png"));

	private boolean isMain = true;

	/**
	 * Booleans used for tutorial progression
	 */
	private boolean start = false;
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

	private Actor skipButton;

	/**
	 * Constructor using the running game instance
	 * 
	 * @param gameInstance a reference to the currently running game instance
	 */
	public TutorialScreen(AnimalBookGame gameInstance) {
		super(gameInstance);

		paused = true;

		popupStage = new Stage();

		tutorialStage = 0;

		//Initial overlay
		overlay = new Texture(Gdx.files.internal("tutorialMessages/tutMessage1.png"));

		//Initialize game world
		tutorialWorld = new TutorialWorld(gameInstance);

		//Initialize rendering objects
		font = new BitmapFont(Gdx.files.internal("fonts/Dimbo2.fnt"));
		font.setScale(FONT_SCALE);
		//font = new BitmapFont();
		batch = new SpriteBatch();
		batch.setProjectionMatrix(tutorialWorld.getCamera().combined);
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/gameScreenGrass2.jpg"));

		//Setup input processing
		inputMultiplexer = new InputMultiplexer();
		TutorialScreenInputHandler touchControls = new TutorialScreenInputHandler(gameInstance,this);
		inputMultiplexer.addProcessor(touchControls);
		Gdx.input.setInputProcessor(inputMultiplexer);

		Gdx.input.setCatchBackKey(true);

		//initialize some DnD components and set drag actor based on first Consumable texture
		dnd = new DragAndDrop();
		if (Consumable.DropType.values().length > 0){
			Image test = new Image(new Texture(Gdx.files.internal(Consumable.DropType.values()[0].getTexturePath())));
			dnd.setDragActorPosition(-test.getWidth()/2f, test.getHeight()/2f);
		}
	}

	/**
	 * Draws the buttons and background as well as handles button presses
	 * 
	 * @param delta the time between two frames
	 */
	@Override
	public void render(float delta) {
		if(AnimalBookGame.currState == state.RESUME){
			if(!hasLost) {

				updateTutorialLogic(delta);

				Gdx.gl.glClearColor(0, 0, 0, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

				//Process button presses
				buttonStage.act(delta);

				//render background and world
				batch.setProjectionMatrix(tutorialWorld.getCamera().combined);

				batch.begin();

				drawTiledBackground(batch);

				//Draw world over background
				tutorialWorld.render(batch,paused,delta);

				//Select the correct waiting function and draw correct words
				if ((paused && start) || tutorialStage == 5) {
					waitForInput();
				}

				//Initial message
				if (tutorialStage == 0)
					batch.draw(overlay, tutorialWorld.getCamera().position.x - Gdx.graphics.getWidth()/2, tutorialWorld.getCamera().position.y - Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

				batch.end();

				//Draw buttons over the screen
				buttonStage.draw();

				batch.begin();
				//Draw health bar (test)
				Vector3 vectHealth = new Vector3(alexButton.getX() + alexButton.getWidth() + 1.4f*EDGE_TOLERANCE,
						Gdx.graphics.getHeight() - (alexButton.getY() + alexButton.getHeight() - 1.5f*EDGE_TOLERANCE)
						,0);
				tutorialWorld.getCamera().unproject(vectHealth);
				batch.draw(black,vectHealth.x, vectHealth.y, 10.2f*EDGE_TOLERANCE, 1.2f*EDGE_TOLERANCE);
				if (getWorld().getPlayer().getHealth()/100f > .50f)	
					batch.draw(green,vectHealth.x + .1f*EDGE_TOLERANCE, vectHealth.y + .1f*EDGE_TOLERANCE, 10f*EDGE_TOLERANCE*(getWorld().getPlayer().getHealth()/100f), EDGE_TOLERANCE);
				else if (getWorld().getPlayer().getHealth()/100f > .25f)
					batch.draw(yellow,vectHealth.x + .1f*EDGE_TOLERANCE, vectHealth.y + .1f*EDGE_TOLERANCE, 10f*EDGE_TOLERANCE*(getWorld().getPlayer().getHealth()/100f), EDGE_TOLERANCE);
				else
					batch.draw(red,vectHealth.x + .1f*EDGE_TOLERANCE, vectHealth.y + .1f*EDGE_TOLERANCE, 10f*EDGE_TOLERANCE*(getWorld().getPlayer().getHealth()/100f), EDGE_TOLERANCE);

				batch.end();

			} else { //if player lost
				Gdx.gl.glClearColor(1, 1, 1, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				popupStage.act(delta);
				popupStage.draw();
			}
		}
	}

	public void drawTiledBackground(SpriteBatch batch) {

		//Find the node on screen to draw grass around
		Array<Vector2> tileNodes = findTileNodesOnScreen();

		for(Vector2 tileNode : tileNodes) {
			//Draw four grass textures around the node on screen
			batch.draw(backgroundTexture, tileNode.x*tileWidth, tileNode.y*tileHeight, tileWidth, tileHeight);
			batch.draw(backgroundTexture, (tileNode.x-1)*tileWidth, tileNode.y*tileHeight, tileWidth, tileHeight);
			batch.draw(backgroundTexture, tileNode.x*tileWidth, (tileNode.y-1)*tileHeight, tileWidth, tileHeight);
			batch.draw(backgroundTexture, (tileNode.x-1)*tileWidth, (tileNode.y-1)*tileHeight, tileWidth, tileHeight);
		}
	}

	/**
	 * Returns the point in world coordinates that grass should be drawn around so that grass
	 * is only rendered when it is on screen.
	 * 
	 * @return		the point on screen to draw grass around
	 */
	public Array<Vector2> findTileNodesOnScreen() {

		//If camera is in the negative quadrant (doesn't happen in new lane game play)
		/*boolean flipX = false;
		boolean flipY = false;
		if (gameWorld.getCamera().position.x < 0)
			flipX = true;
		if (gameWorld.getCamera().position.y < 0)
			flipY = true;
		 */

		//Reference to the center of the camera
		Vector3 camCorner = tutorialWorld.getCamera().position.cpy();
		//adjust to lower left of camera
		camCorner.sub(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,0);

		int xCoordinate = (int) ( Math.abs(camCorner.x / tileWidth) - .5);
		int yCoordinate = (int) ( Math.abs(camCorner.y / tileHeight) - .5);

		//Fill array with all nodes on screen
		Array<Vector2> tileNodes = new Array<Vector2>();
		Vector2 node;

		int initYCoordinate = yCoordinate;

		while (xCoordinate*tileWidth < (camCorner.x + Gdx.graphics.getWidth()) + tileWidth) {
			while(yCoordinate*tileHeight < (camCorner.y + Gdx.graphics.getHeight() + tileHeight)) {
				node = new Vector2(xCoordinate,yCoordinate);
				tileNodes.add(node);
				yCoordinate += 1;
			}
			xCoordinate += 1;
			yCoordinate = initYCoordinate;
		}

		//Provides correct float truncation for tiling in the negative x/y direction
		/*if (flipX)
			xCoordinate *= -1;
		if (flipY)
			yCoordinate *= -1;
		 */

		return tileNodes;
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
			if (tutorialStage == 0) {
				tutorialStage++;
				overlay.dispose();
				overlay = null;
			}
			paused = true;
			moved = 0;
		}
	}

	/**
	 * Functions that wait for the correct gesture to move on the tutorial
	 */
	public void waitForSwipe(SpriteBatch batch) {
		if (swiped) {
			paused = false;
			overlay.dispose();
			overlay = null;
			if (tutorialStage == 1)
				tutorialStage += 1;
			return;
		}
		else {
			if (overlay == null) {
				overlay = new Texture(Gdx.files.internal("tutorialMessages/tutMessage2.png"));
			}
			batch.draw(overlay, tutorialWorld.getCamera().position.x - Gdx.graphics.getWidth()/2, tutorialWorld.getCamera().position.y - Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			return;
		}
	}

	public void waitForTap(SpriteBatch batch) {
		if (tapped) {
			paused = false;
			overlay.dispose();
			overlay = null;
			if (tutorialStage == 2)
				tutorialStage += 1;
			return;
		}
		else {
			if (overlay == null) {
				overlay = new Texture(Gdx.files.internal("tutorialMessages/tutMessage3.png"));
			}
			batch.draw(overlay, tutorialWorld.getCamera().position.x - Gdx.graphics.getWidth()/2, tutorialWorld.getCamera().position.y - Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			return;
		}
	}

	public void waitForPickup(SpriteBatch batch) {
		if (pickedUp) {
			paused = false;
			overlay.dispose();
			overlay = null;
			tutorialWorld.removeSpawnedEgg();
			if (tutorialStage == 3)
				tutorialStage += 1;
			return;
		}
		else {
			if (overlay == null) {
				overlay = new Texture(Gdx.files.internal("tutorialMessages/tutMessage4.png"));
			}
			batch.draw(overlay, tutorialWorld.getCamera().position.x - Gdx.graphics.getWidth()/2, tutorialWorld.getCamera().position.y - Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			tutorialWorld.spawnEgg();
		}
		return;
	}

	public void waitForEat(SpriteBatch batch) {
		if (ate) {
			paused = false;
			overlay.dispose();
			overlay = null;
			if (tutorialStage == 4)
				tutorialStage += 1;
			return;
		}
		else {
			if (overlay == null) {
				overlay = new Texture(Gdx.files.internal("tutorialMessages/tutMessage5.png"));
			}
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
			//Continuously runs when player is told to herd into the market to end the tutorial
			tutorialWorld.getPlayer().setHealth(100f);
			if (overlay == null) {
				overlay = new Texture(Gdx.files.internal("tutorialMessages/tutMessage7.png"));
			}
			batch.draw(overlay, tutorialWorld.getCamera().position.x - Gdx.graphics.getWidth()/2, tutorialWorld.getCamera().position.y - Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			return;
		}
		else {
			if (tutorialWorld.getPlayer().getPlayerMoney() == 0) {
				tutorialWorld.getPlayer().addPlayerMoney(1000);
			}
			if (overlay == null) {
				overlay = new Texture(Gdx.files.internal("tutorialMessages/tutMessage6.png"));
			}
			batch.draw(overlay, tutorialWorld.getCamera().position.x - Gdx.graphics.getWidth()/2, tutorialWorld.getCamera().position.y - Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			return;
		}
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
		mainMenuInitialized = false;
		inventoryMenuInitialized = false;
		upgradesMenuInitialized = false;
		optionsMenuInitialized = false;
		initializeButtons();
		if(menuHandler == null)
			menuHandler = new MenuHandlerTut(buttonStage, gameInstance, tutorialWorld, this );
	}

	/**
	 * Initializes all button objects
	 */
	@Override
	protected void initializeButtons() {

		//ALEXBUTTON BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/gameScreen/playerButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle alexButtonStyle = new ButtonStyle();
		alexButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		alexButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		alexButton = new Button(alexButtonStyle);
		alexButton.setWidth(BUTTON_WIDTH);
		alexButton.setHeight(BUTTON_HEIGHT);
		alexButton.setX(EDGE_TOLERANCE);
		alexButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - EDGE_TOLERANCE);
		alexButton.setChecked(false);
		alexButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.changeBackgroundVolume((float) .1);
				if (!mainMenuInitialized)
					menuHandler.initializeMenuItems();
				menuHandler.handleMainMenu(alexButton.isChecked());
			}
		});
		buttonStage.addActor(alexButton);

		//Information Label
		LabelStyle infoLabelStyle = new LabelStyle();
		infoLabelStyle.font = font;
		infoLabelStyle.fontColor = Color.WHITE;
		infoLabelStyle.font.setScale(FONT_SCALE);
		infoLabel = new Label("Money: $" + getWorld().getPlayer().getPlayerMoney(), infoLabelStyle);
		infoLabel.setPosition(alexButton.getX() + alexButton.getWidth() + 1.5f*EDGE_TOLERANCE,
				alexButton.getY() + alexButton.getHeight() - 2.5f*EDGE_TOLERANCE);
		infoLabel.pack();
		infoLabel.setPosition(alexButton.getX() + alexButton.getWidth() + 1.5f*EDGE_TOLERANCE,
				alexButton.getY() + alexButton.getHeight() - infoLabel.getHeight() - 1.7f*EDGE_TOLERANCE);
		infoLabel.setAlignment(Align.left);

		//The label's background image
		alexInfoImage = new Image(new Texture(Gdx.files.internal("backgrounds/menuBackground.png")));
		alexInfoImage.setPosition(alexButton.getX() + alexButton.getWidth() + EDGE_TOLERANCE,
				infoLabel.getY() - EDGE_TOLERANCE*.5f);
		alexInfoImage.setSize(11f*EDGE_TOLERANCE, alexButton.getHeight() - 2f*EDGE_TOLERANCE);

		//SKIP BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/tutorialScreen/skipButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle skipButtonStyle = new ButtonStyle();
		skipButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		skipButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		skipButton = new Button(skipButtonStyle);
		skipButton.setWidth(BUTTON_WIDTH);
		skipButton.setHeight(BUTTON_HEIGHT);
		skipButton.setX(Gdx.graphics.getWidth() - BUTTON_WIDTH - EDGE_TOLERANCE);
		skipButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - EDGE_TOLERANCE);

		skipButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				gameInstance.getLevelHandler().setDoTutorial(false);
				gameInstance.setScreen(new GameScreen(gameInstance));
				dispose();
			}
		});

		buttonStage.addActor(skipButton);
		buttonStage.addActor(alexInfoImage);
		buttonStage.addActor(infoLabel);

		inputMultiplexer.addProcessor(buttonStage);
	}


	public Vector2 alexsPosition(){
		return new Vector2(alexButton.getX() + alexButton.getWidth(),alexButton.getY() + alexButton.getHeight());
	}
	/**
	 * Initialize option group items
	 */
	public void initializeOptionItems(){
		optionsMenuInitialized = true;
		Button soundButton, musicButton, mainMenuButton, helpButton;

		//MAIN MENU BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/optionsScreen/mainMenuButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle mainMenuButtonStyle = new ButtonStyle();
		mainMenuButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		mainMenuButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		mainMenuButton = new Button(mainMenuButtonStyle){
			@Override
			public float getPrefHeight(){
				return BUTTON_HEIGHT*2/3;
			};

			@Override
			public float getPrefWidth(){
				return BUTTON_WIDTH*2/3;
			}
		};

		//SOUND BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/optionsScreen/soundButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle soundButtonStyle = new ButtonStyle();
		soundButtonStyle.up = buttonSkin.getDrawable("unmuted");
		soundButtonStyle.down = buttonSkin.getDrawable("muted");
		soundButtonStyle.checked = buttonSkin.getDrawable("muted");

		soundButton = new Button(soundButtonStyle){
			@Override
			public float getPrefHeight(){
				return BUTTON_HEIGHT*2/3;
			};

			@Override
			public float getPrefWidth(){
				return BUTTON_WIDTH*2/3;
			}
		};
		if(SoundHandler.isSoundMuted()){
			soundButton.setChecked(true);
		}
		//MUSIC BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/optionsScreen/musicButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle musicButtonStyle = new ButtonStyle();
		musicButtonStyle.up = buttonSkin.getDrawable("unmuted");
		musicButtonStyle.down = buttonSkin.getDrawable("muted");
		musicButtonStyle.checked = buttonSkin.getDrawable("muted");

		musicButton = new Button(musicButtonStyle){
			@Override
			public float getPrefHeight(){
				return BUTTON_HEIGHT*2/3;
			};

			@Override
			public float getPrefWidth(){
				return BUTTON_WIDTH*2/3;
			}
		};
		if(SoundHandler.isMusicMuted()){
			musicButton.setChecked(true);
		}

		//HELP BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/optionsScreen/helpButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle helpButtonStyle = new ButtonStyle();
		helpButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		helpButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		helpButton = new Button(helpButtonStyle){
			@Override
			public float getPrefHeight(){
				return BUTTON_HEIGHT*2/3;
			};

			@Override
			public float getPrefWidth(){
				return BUTTON_WIDTH*2/3;
			}
		};

		//LISTENERS
		mainMenuButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				gameInstance.setScreen(new MainMenuScreen(gameInstance));
			}
		});

		soundButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.toggleSounds();
			}
		});

		musicButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.toggleMusic();

			}
		});

		helpButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();

			}
		});

		menuHandler.getOptionsGroup().addActor(mainMenuButton);
		menuHandler.getOptionsGroup().addActor(soundButton);
		menuHandler.getOptionsGroup().addActor(musicButton);
		menuHandler.getOptionsGroup().addActor(helpButton);

		menuHandler.getOptionsGroup().pack();
		menuHandler.getOptionsGroup().setHeight(BUTTON_HEIGHT);
		menuHandler.getOptionsGroupImage().setSize(menuHandler.getOptionsGroup().getWidth() + EDGE_TOLERANCE*2f, menuHandler.getOptionsGroup().getHeight());
	}

	/**
	 * Returns if alexButton is checked or not for pausing purposes
	 * @return
	 */
	public boolean inMenu() {
		return alexButton.isChecked();
	}

	public boolean isPaused() {
		return paused;
	}

	/**
	 * Disposes of all objects that should release memory
	 */
	@Override
	public void dispose() {
		super.dispose();
		font.dispose();
		tutorialWorld.dispose();
		yellow.dispose();
		green.dispose();
		red.dispose();
		black.dispose();
		menuHandler.dispose();
		popupStage.dispose();
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
	 * @return		reference to the current game world
	 */
	public TutorialWorld getWorld() {
		return tutorialWorld;
	}

	/**
	 * Unused methods for detecting screen events
	 */
	@Override
	public void show() {}

	@Override
	public void hide() {}

	@Override
	public void resume() {
		AnimalBookGame.currState = state.RESUME;

	}

	public boolean toMain() {
		return isMain ;
	}

	public void setMain() {
		isMain = true;

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

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	@Override
	public void pause() {
		Gdx.app.log("My Tagg", "This is screen pause");

	}


	public Label getInfoLabel() {
		return infoLabel;
	}
	
	public void setMainMenuInitialized(boolean b) {
		mainMenuInitialized = b;	
	}
	public boolean getMainMenuInitialized() {
		return mainMenuInitialized;
	}
	public boolean getInventoryMenuInitialized() {
		return inventoryMenuInitialized;
	}
	public void setInventoryMenuInitialized(boolean b) {
	 inventoryMenuInitialized = b;
	}

	public boolean getUpgradesMenuInitialized() {
		return upgradesMenuInitialized;
	}

	public boolean getOptionsMenuInitialized() {
		return optionsMenuInitialized;
	}

	public void setUpgradesMenuInitialized(boolean b) {
		upgradesMenuInitialized = b;
	}

	public DragAndDrop getDnd() {
		return dnd;
	}

	public Button getAlexButton() {
		return alexButton;
	}

	public Texture getOverlay() {
		return overlay;
	}
	public void setOverlay(Texture tex){
		overlay = tex;
	}

}
