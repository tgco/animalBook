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
import com.tgco.animalBook.handlers.GameScreenInputHandler;
import com.tgco.animalBook.handlers.MenuHandler;
import com.tgco.animalBook.handlers.SoundHandler;
import com.tgco.animalBook.handlers.Weather.WeatherType;
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

	private static final float WEATHER_TIME = 3f;

	/**
	 * Reference to the game world where all objects are located
	 */
	private World gameWorld;

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
	 * textures for health bar, etc
	 */
	private Texture black = new Texture(Gdx.files.internal("primitiveTextures/black.png"));
	private Texture red = new Texture(Gdx.files.internal("primitiveTextures/red.png"));
	private Texture yellow = new Texture(Gdx.files.internal("primitiveTextures/yellow.png"));
	private Texture green = new Texture(Gdx.files.internal("primitiveTextures/green.png"));


	private Texture redOp = new Texture(Gdx.files.internal("primitiveTextures/redOp.png"));

	private boolean isMain = true;

	private MenuHandler menuHandler;

	private Texture snowBackgroundTexture;

	private boolean fadeToSnow, steadySnow, fadeToReturnS;

	private float timeCounterS;

	private boolean snowy, setOnce;

	/**
	 * Constructor using the running game instance
	 * 
	 * @param gameInstance a reference to the currently running game instance
	 */
	public GameScreen(AnimalBookGame gameInstance) {
		super(gameInstance);

		popupStage = new Stage();

		//Initialize game world
		gameWorld = new World(gameInstance);

		//Initialize rendering objects
		font = new BitmapFont(Gdx.files.internal("fonts/Dimbo2.fnt"));
		font.setScale(FONT_SCALE);
		//font = new BitmapFont();
		batch = new SpriteBatch();
		batch.setProjectionMatrix(gameWorld.getCamera().combined);
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/gameScreenGrass2.jpg"));
		snowBackgroundTexture = new Texture(Gdx.files.internal("backgrounds/gameScreenGrass2Snow.jpg"));

		//Setup input processing
		inputMultiplexer = new InputMultiplexer();
		GameScreenInputHandler touchControls = new GameScreenInputHandler(gameInstance,this);
		inputMultiplexer.addProcessor(touchControls);
		Gdx.input.setInputProcessor(inputMultiplexer);

		Gdx.input.setCatchBackKey(true);


		//initialize some DnD components and set drag actor based on first Consumable texture
		dnd = new DragAndDrop();
		if (Consumable.DropType.values().length > 0){
			Image test = new Image(new Texture(Gdx.files.internal(Consumable.DropType.values()[0].getTexturePath()))){
				@Override
				public float getPrefWidth(){
					return BUTTON_WIDTH*2f/3f;
				}
				@Override
				public float getPrefHeight(){
					return BUTTON_HEIGHT*2f/3f;
				}
			};
			//CRITICAL: tweak these values for niceness of dragging
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
				Gdx.gl.glClearColor(0, 0, 0, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

				//Process button presses
				buttonStage.act(delta);

				//render background and world
				batch.setProjectionMatrix(gameWorld.getCamera().combined);

				batch.begin();

				drawTiledBackground(batch, delta);
				//add in endline when market is on screen
				if(gameWorld.getCamera().position.y > gameInstance.getLevelHandler().returnLaneLength(gameInstance.getLevelHandler().getLevel())+ Gdx.graphics.getHeight()/4){
					Vector3 vectEnd = new Vector3(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
					gameWorld.getCamera().unproject(vectEnd);
					batch.draw(redOp, vectEnd.x - 75, vectEnd.y - .222f*Gdx.graphics.getHeight()/2, 150, 4);
				}

				//Draw world over background
				gameWorld.render(batch,alexButton.isChecked(),delta);

				batch.end();

				//Draw buttons over the screen
				buttonStage.draw();


				batch.begin();
				//Gdx.app.log("my Tagg", "Vert Group is: " + ((VerticalGroup) buttonStage.getActors().get(5)).getOriginX());
				//Draw health bar (test)
				Vector3 vectHealth = new Vector3(alexButton.getX() + alexButton.getWidth() + 1.4f*EDGE_TOLERANCE,
						Gdx.graphics.getHeight() - (alexButton.getY() + alexButton.getHeight() - 1.5f*EDGE_TOLERANCE)
						,0);
				gameWorld.getCamera().unproject(vectHealth);
				batch.draw(black,vectHealth.x, vectHealth.y, 10.2f*EDGE_TOLERANCE, 1.2f*EDGE_TOLERANCE);
				if (getWorld().getPlayer().getHealth()/100f > .50f)	
					batch.draw(green,vectHealth.x + .1f*EDGE_TOLERANCE, vectHealth.y + .1f*EDGE_TOLERANCE, 10f*EDGE_TOLERANCE*(getWorld().getPlayer().getHealth()/100f), EDGE_TOLERANCE);
				else if (getWorld().getPlayer().getHealth()/100f > .25f)
					batch.draw(yellow,vectHealth.x + .1f*EDGE_TOLERANCE, vectHealth.y + .1f*EDGE_TOLERANCE, 10f*EDGE_TOLERANCE*(getWorld().getPlayer().getHealth()/100f), EDGE_TOLERANCE);
				else
					batch.draw(red,vectHealth.x + .1f*EDGE_TOLERANCE, vectHealth.y + .1f*EDGE_TOLERANCE, 10f*EDGE_TOLERANCE*(getWorld().getPlayer().getHealth()/100f), EDGE_TOLERANCE);



				batch.end();

			}
			else { //if player lost
				Gdx.gl.glClearColor(1, 1, 1, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				popupStage.act(delta);
				popupStage.draw();
			}
		}
	}

	public void drawTiledBackground(SpriteBatch batch, float delta) {
		if (gameWorld.getWeather() == WeatherType.SNOWY && !setOnce){
			setOnce = true;
			setSnowy(true);
		}
		if (gameWorld.getWeather() != WeatherType.SNOWY && setOnce){
			setOnce = false;
			setSnowy(false);
		}

		//Find the node on screen to draw grass around
		Array<Vector2> tileNodes = findTileNodesOnScreen();

		for(Vector2 tileNode : tileNodes) {
			//Draw four grass textures around the node on screen
			batch.draw(backgroundTexture, tileNode.x*tileWidth, tileNode.y*tileHeight, tileWidth, tileHeight);
			batch.draw(backgroundTexture, (tileNode.x-1)*tileWidth, tileNode.y*tileHeight, tileWidth, tileHeight);
			batch.draw(backgroundTexture, tileNode.x*tileWidth, (tileNode.y-1)*tileHeight, tileWidth, tileHeight);
			batch.draw(backgroundTexture, (tileNode.x-1)*tileWidth, (tileNode.y-1)*tileHeight, tileWidth, tileHeight);
		}
		if (snowy){
			if (fadeToSnow && timeCounterS < WEATHER_TIME){
				//fade to clear				
				for(Vector2 tileNode : tileNodes) {
					batch.setColor(1f, 1f, 1f, timeCounterS/WEATHER_TIME);
					batch.draw(snowBackgroundTexture, tileNode.x*tileWidth, tileNode.y*tileHeight, tileWidth, tileHeight);
					batch.draw(snowBackgroundTexture, (tileNode.x-1)*tileWidth, tileNode.y*tileHeight, tileWidth, tileHeight);
					batch.draw(snowBackgroundTexture, tileNode.x*tileWidth, (tileNode.y-1)*tileHeight, tileWidth, tileHeight);
					batch.draw(snowBackgroundTexture, (tileNode.x-1)*tileWidth, (tileNode.y-1)*tileHeight, tileWidth, tileHeight);
					batch.setColor(Color.WHITE);
				}
			}
			else{
				if (fadeToSnow){
					fadeToSnow = false;
					steadySnow = true;
					timeCounterS = 0;
				}
			}
			if (steadySnow){
				for(Vector2 tileNode : tileNodes) {
					batch.draw(snowBackgroundTexture, tileNode.x*tileWidth, tileNode.y*tileHeight, tileWidth, tileHeight);
					batch.draw(snowBackgroundTexture, (tileNode.x-1)*tileWidth, tileNode.y*tileHeight, tileWidth, tileHeight);
					batch.draw(snowBackgroundTexture, tileNode.x*tileWidth, (tileNode.y-1)*tileHeight, tileWidth, tileHeight);
					batch.draw(snowBackgroundTexture, (tileNode.x-1)*tileWidth, (tileNode.y-1)*tileHeight, tileWidth, tileHeight);
				}
			}
		}
		else{
			if (fadeToReturnS && timeCounterS < WEATHER_TIME){
				//back to clear
				if (steadySnow){
					steadySnow = false;
				}
				for(Vector2 tileNode : tileNodes) {
					batch.setColor(1f, 1f, 1f, 1f - timeCounterS/WEATHER_TIME);
					batch.draw(snowBackgroundTexture, tileNode.x*tileWidth, tileNode.y*tileHeight, tileWidth, tileHeight);
					batch.draw(snowBackgroundTexture, (tileNode.x-1)*tileWidth, tileNode.y*tileHeight, tileWidth, tileHeight);
					batch.draw(snowBackgroundTexture, tileNode.x*tileWidth, (tileNode.y-1)*tileHeight, tileWidth, tileHeight);
					batch.draw(snowBackgroundTexture, (tileNode.x-1)*tileWidth, (tileNode.y-1)*tileHeight, tileWidth, tileHeight);
					batch.setColor(Color.WHITE);
				}
			}
			else{
				if (fadeToReturnS){
					fadeToReturnS = false;
					timeCounterS = 0;
				}
			}
		}
		if (!steadySnow && (fadeToSnow || fadeToReturnS))
			timeCounterS+=delta;
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
		Vector3 camCorner = gameWorld.getCamera().position.cpy();
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
			menuHandler = new MenuHandler(buttonStage, gameInstance, getWorld(), this);
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
				if(alexButton.isChecked())
					SoundHandler.changeBackgroundVolume((float) .1);
				else{
					SoundHandler.changeBackgroundVolume((float) .5);
				}
				if (!mainMenuInitialized){
					menuHandler.initializeMenuItems();
					//initializeMenuItems();
				}
				menuHandler.handleMainMenu(alexButton.isChecked());
				//handleMainMenu(alexButton.isChecked());
			}
		});
		buttonStage.addActor(alexButton);

		//Information Label
		LabelStyle infoLabelStyle = new LabelStyle();
		infoLabelStyle.font = font;
		infoLabelStyle.fontColor = Color.WHITE;
		infoLabelStyle.font.setScale(FONT_SCALE);
		infoLabel = new Label("Money: $" + getWorld().getPlayer().getPlayerMoney()
				+ "\nNeeded: " + (gameInstance.getLevelHandler().getStoredAmount() + gameWorld.getMovables().size) + " of " + gameInstance.getLevelHandler().getPassLevelAmount(), infoLabelStyle);
		infoLabel.pack();
		infoLabel.setPosition(alexButton.getX() + alexButton.getWidth() + 1.5f*EDGE_TOLERANCE,
				alexButton.getY() + alexButton.getHeight() - infoLabel.getHeight() - 1.7f*EDGE_TOLERANCE);
		infoLabel.setAlignment(Align.left);

		//The label's background image
		alexInfoImage = new Image(new Texture(Gdx.files.internal("backgrounds/menuBackground.png")));
		alexInfoImage.setSize(11f*EDGE_TOLERANCE, alexButton.getY() + alexButton.getHeight() - infoLabel.getY() + .5f*EDGE_TOLERANCE);
		alexInfoImage.setPosition(infoLabel.getX() - .5f*EDGE_TOLERANCE,
				infoLabel.getY() - .5f*EDGE_TOLERANCE);
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
				gameInstance.setHitBack(true);
				gameInstance.setScreen(new MainMenuScreen(gameInstance));
				//store the data in levelData of Game

				// spot 1 is current level
				gameInstance.addToDatalevel(gameInstance.getLevelHandler().getLevel(),0);

				//spot 2 is player			
				gameInstance.addToDatalevel(gameWorld.getPlayer(),1);

				//spot 3 is storing movable array
				gameInstance.addToDatalevel(gameWorld.getMovables(),2);

				//spot 4 is storing dropped items array
				gameInstance.addToDatalevel(gameWorld.getDropped(), 3);

				gameInstance.addToDatalevel(gameWorld.getObstacles(), 4);
				gameInstance.setProgPercentage(gameWorld.getPrecentage());

				dispose();
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
				gameInstance.setScreen(new HelpScreen(gameInstance, gameInstance.getGameScreen(), 1));
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

	/**
	 * Disposes of all objects that should release memory
	 */
	@Override
	public void dispose() {
		super.dispose();
		font.dispose();
		gameWorld.dispose();
		yellow.dispose();
		green.dispose();
		red.dispose();
		redOp.dispose();
		black.dispose();
		backgroundTexture.dispose();
		snowBackgroundTexture.dispose();
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
	public World getWorld() {
		return gameWorld;
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

	public void setMain(boolean set) {
		isMain = set;
	}

	public void setAlexButton(boolean set) {
		alexButton.setChecked(set);
	}

	public void comingFromHelpScreen(boolean set) {
		alexButton.setChecked(true);
		menuHandler.handleMainMenu(true);
		//handleMainMenu(true);
		menuHandler.getOptionsGroupButton().setChecked(true);
		menuHandler.handleOptionsMenu(true);
		//handleOptionsMenu(true);
	}

	@Override
	public void pause() {
		Gdx.app.log("My Tagg", "This is screen pause");

	}

	public void setInfolabel(){
		infoLabel.setText("Money: $" + getWorld().getPlayer().getPlayerMoney()
				+ "\nNeeded: " + (gameInstance.getLevelHandler().getStoredAmount() + gameWorld.getMovables().size) + " of " + gameInstance.getLevelHandler().getPassLevelAmount());
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

	public Label getInfoLabel() {
		return infoLabel;
	}
	
	public void setSnowy(boolean b) {

		if (b & !snowy){
			fadeToSnow = true;
		}
		else{
			if (!b & snowy){
				fadeToReturnS = true;
			}
		}
		snowy = b;
	}

}
