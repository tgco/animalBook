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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.gameObjects.ABDrawable;
import com.tgco.animalBook.gameObjects.Animal;
import com.tgco.animalBook.gameObjects.Consumable;
import com.tgco.animalBook.gameObjects.Dog;
import com.tgco.animalBook.gameObjects.Movable;
import com.tgco.animalBook.AnimalBookGame.state;
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

	private Array<ABDrawable> boostArray = new Array<ABDrawable>();

	/**
	 * Reference to the game world where all objects are located
	 */
	private World gameWorld;
	
	/**
	 * The width and height of tiles for the background
	 */
	private float tileWidth = Gdx.graphics.getWidth()/4f;
	private float tileHeight = Gdx.graphics.getHeight()/4f;

	/**
	 * Each button used on the game screen user interface overlay
	 */
	private Button alexButton, inventoryGroupButton, optionsGroupButton, upgradesGroupButton, menuBackgroundButton, dogButton;

	private VerticalGroup menuGroup;
	private Image alexInfoImage, menuGroupImage, inventoryGroupImage, upgradesGroupImage, optionsGroupImage, upgradesStatusGroupImage;
	private Label infoLabel;
	private HorizontalGroup inventoryGroup, upgradesGroup1, upgradesGroup2, optionsGroup, upgradesStatusGroup;

	private DragAndDrop dnd;

	/**
	 * Amounts of each upgrade
	 */
	private int fruitfulMoney;
	private int longerMoney;
	private int moreMoney;
	private int dogMoney;

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
	private final float FONT_SCALE = Gdx.graphics.getHeight()/1000f;

	/**
	 * textures for health bar, etc
	 */
	private Texture black = new Texture(Gdx.files.internal("primitiveTextures/black.png"));
	private Texture red = new Texture(Gdx.files.internal("primitiveTextures/red.png"));
	private Texture yellow = new Texture(Gdx.files.internal("primitiveTextures/yellow.png"));
	private Texture green = new Texture(Gdx.files.internal("primitiveTextures/green.png"));

	private boolean isMain = true;

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
		font = new BitmapFont(Gdx.files.internal("fonts/SketchBook.fnt"));
		font.setScale(FONT_SCALE);
		//font = new BitmapFont();
		batch = new SpriteBatch();
		batch.setProjectionMatrix(gameWorld.getCamera().combined);
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/gameScreenGrass2.jpg"));

		//Setup input processing
		inputMultiplexer = new InputMultiplexer();
		GameScreenInputHandler touchControls = new GameScreenInputHandler(gameInstance,this);
		inputMultiplexer.addProcessor(touchControls);
		Gdx.input.setInputProcessor(inputMultiplexer);

		Gdx.input.setCatchBackKey(true);

		//initialize some DnD components and set drag actor based on first Consumable texture
		dnd = new DragAndDrop();
		if (Consumable.DropType.values().length > 0){
			Image test = new Image(new Texture(Gdx.files.internal(Consumable.DropType.values()[0].getTexturePath())));
			//CRITICAL: tweak these values for niceness of dragging
			dnd.setDragActorPosition(-test.getWidth()/4f, test.getHeight()/4f);
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

				drawTiledBackground(batch);

				//Draw world over background
				gameWorld.render(batch,alexButton.isChecked(),delta);
				batch.end();

				//Draw buttons over the screen
				buttonStage.draw();

				batch.begin();
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
					initializeMenuItems();
				handleMainMenu(alexButton.isChecked());
			}
		});
		buttonStage.addActor(alexButton);

		//Information Label
		LabelStyle infoLabelStyle = new LabelStyle();
		infoLabelStyle.font = font;
		infoLabelStyle.fontColor = Color.WHITE;
		infoLabelStyle.font.setScale(FONT_SCALE);
		infoLabel = new Label("Money: ##" + getWorld().getPlayer().getPlayerMoney(), infoLabelStyle);
		infoLabel.setPosition(alexButton.getX() + alexButton.getWidth() + 1.5f*EDGE_TOLERANCE,
				alexButton.getY() + alexButton.getHeight() - 2.5f*EDGE_TOLERANCE);

		//The label's background image
		alexInfoImage = new Image(new Texture(Gdx.files.internal("backgrounds/menuBackground.png")));
		alexInfoImage.setPosition(alexButton.getX() + alexButton.getWidth() + EDGE_TOLERANCE,
				infoLabel.getY() - EDGE_TOLERANCE*.5f);
		alexInfoImage.setSize(11f*EDGE_TOLERANCE, alexButton.getHeight() - 2f*EDGE_TOLERANCE);

		buttonStage.addActor(alexInfoImage);
		buttonStage.addActor(infoLabel);

		inputMultiplexer.addProcessor(buttonStage);
	}

	/**
	 * Initialize main menu group items
	 */
	public void initializeMenuItems(){
		mainMenuInitialized = true;
		dnd.addTarget(new Target(alexButton){

			@Override
			public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
				if (payload.getObject() instanceof Consumable)
					if (getWorld().getPlayer().getHealth() == 100f){
						this.getActor().setColor(Color.RED);
						return false;
					}
				this.getActor().setColor(Color.GREEN);
				return true;
			}

			@Override
			public void reset( Source source, Payload payload) {
				getActor().setColor(Color.WHITE);
			}

			@Override
			public void drop(Source source, Payload payload, float x, float y, int pointer) {
				if (payload.getObject() instanceof Consumable)
					getWorld().getPlayer().eat(((Consumable)payload.getObject()).getType().getHungerValue());
			}
		});

		//Initialze Background Button
		atlas = new TextureAtlas(Gdx.files.internal("buttons/gameScreen/backgroundMenuButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle backgroundMenuButtonStyle = new ButtonStyle();
		backgroundMenuButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		backgroundMenuButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		menuBackgroundButton = new Button(backgroundMenuButtonStyle);
		menuBackgroundButton.setWidth(Gdx.graphics.getWidth());
		menuBackgroundButton.setHeight(Gdx.graphics.getHeight());
		menuBackgroundButton.setX(0);
		menuBackgroundButton.setY(0);
		menuBackgroundButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.changeBackgroundVolume((float) .1);
				handleMainMenu(false);
				alexButton.setChecked(false);
				inventoryGroupButton.setChecked(false);
				upgradesGroupButton.setChecked(false);
				optionsGroupButton.setChecked(false);
			}
		});

		//Main Group
		menuGroup = new VerticalGroup();
		menuGroup.center();

		menuGroupImage = new Image(new Texture(Gdx.files.internal("backgrounds/menuBackground.png")));
		menuGroup.space(EDGE_TOLERANCE);

		//Inventory Group Button
		atlas = new TextureAtlas(Gdx.files.internal("buttons/gameScreen/inventoryButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle inventoryButtonStyle = new ButtonStyle();
		inventoryButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		inventoryButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		inventoryGroupButton = new Button(inventoryButtonStyle){
			@Override
			public float getPrefWidth(){
				return BUTTON_WIDTH;
			}

			@Override
			public float getPrefHeight(){
				return BUTTON_HEIGHT;
			}
		};
		inventoryGroupButton.setWidth(BUTTON_WIDTH);
		inventoryGroupButton.setHeight(BUTTON_HEIGHT);
		inventoryGroupButton.setChecked(false);
		inventoryGroupButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.changeBackgroundVolume((float) .1);
				if (!inventoryMenuInitialized)
					initializeInventoryItems();
				handleInventoryMenu(inventoryGroupButton.isChecked());
				handleUpgradesMenu(false);
				upgradesGroupButton.setChecked(false);
				handleOptionsMenu(false);
				optionsGroupButton.setChecked(false);

			}
		});

		menuGroup.addActor(inventoryGroupButton);

		//Upgrade Group Button
		atlas = new TextureAtlas(Gdx.files.internal("buttons/gameScreen/upgradeButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle upgradeButtonStyle = new ButtonStyle();
		upgradeButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		upgradeButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		upgradesGroupButton = new Button(upgradeButtonStyle){
			@Override
			public float getPrefWidth(){
				return BUTTON_WIDTH;
			}

			@Override
			public float getPrefHeight(){
				return BUTTON_HEIGHT;
			}
		};
		upgradesGroupButton.setWidth(BUTTON_WIDTH);
		upgradesGroupButton.setHeight(BUTTON_HEIGHT);
		upgradesGroupButton.setChecked(false);
		upgradesGroupButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if (getWorld().getMovables().size > 0){
					SoundHandler.playButtonClick();
					SoundHandler.changeBackgroundVolume((float) .1);
					if (!upgradesMenuInitialized)
						initializeUpgradeItems();
					handleUpgradesMenu(upgradesGroupButton.isChecked());
					handleInventoryMenu(false);
					inventoryGroupButton.setChecked(false);
					handleOptionsMenu(false);
					optionsGroupButton.setChecked(false);
				}
			}
		});
		menuGroup.addActor(upgradesGroupButton);

		//Option Group Button
		atlas = new TextureAtlas(Gdx.files.internal("buttons/gameScreen/optionsButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle optionsButtonStyle = new ButtonStyle();
		optionsButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		optionsButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		optionsGroupButton = new Button(optionsButtonStyle){
			@Override
			public float getPrefWidth(){
				return BUTTON_WIDTH;
			}

			@Override
			public float getPrefHeight(){
				return BUTTON_HEIGHT;
			}
		};
		optionsGroupButton.setWidth(BUTTON_WIDTH);
		optionsGroupButton.setHeight(BUTTON_HEIGHT);
		optionsGroupButton.setChecked(false);
		optionsGroupButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.changeBackgroundVolume((float) .1);
				if (!optionsMenuInitialized)
					initializeOptionItems();
				handleOptionsMenu(optionsGroupButton.isChecked());
				handleInventoryMenu(false);
				inventoryGroupButton.setChecked(false);
				handleUpgradesMenu(false);
				upgradesGroupButton.setChecked(false);
			}
		});
		menuGroup.addActor(optionsGroupButton);

		//Inventory Group
		inventoryGroup = new HorizontalGroup();
		inventoryGroup.center();
		inventoryGroup.setPosition(alexButton.getX() + alexButton.getWidth() + EDGE_TOLERANCE*2f,
				alexButton.getY() - BUTTON_HEIGHT - EDGE_TOLERANCE);
		inventoryGroup.space(EDGE_TOLERANCE);

		inventoryGroupImage = new Image(new Texture(Gdx.files.internal("backgrounds/menuBackground.png")));
		inventoryGroupImage.setPosition(alexButton.getX() + alexButton.getWidth() + EDGE_TOLERANCE,
				alexButton.getY() - BUTTON_HEIGHT - EDGE_TOLERANCE);

		//Upgrade Group 1
		upgradesGroup1 = new HorizontalGroup();
		upgradesGroup1.center();
		upgradesGroup1.setPosition(alexButton.getX() + alexButton.getWidth() + EDGE_TOLERANCE*2f,
				alexButton.getY() - 2f*BUTTON_HEIGHT - 2f*EDGE_TOLERANCE);
		upgradesGroup1.space(EDGE_TOLERANCE);

		//Upgrade Group 2
		upgradesGroup2 = new HorizontalGroup();
		upgradesGroup2.center();
		upgradesGroup2.setPosition(alexButton.getX() + alexButton.getWidth() + EDGE_TOLERANCE*2f,
				alexButton.getY() - 3f*BUTTON_HEIGHT - EDGE_TOLERANCE);
		upgradesGroup2.space(EDGE_TOLERANCE);

		upgradesGroupImage = new Image(new Texture(Gdx.files.internal("backgrounds/menuBackground.png")));

		//Option Group
		optionsGroup = new HorizontalGroup();
		optionsGroup.center();
		optionsGroup.setPosition(alexButton.getX() + alexButton.getWidth() + EDGE_TOLERANCE*2f,
				alexButton.getY() - 3f*BUTTON_HEIGHT - 3f*EDGE_TOLERANCE);
		optionsGroup.space(EDGE_TOLERANCE);

		optionsGroupImage = new Image(new Texture(Gdx.files.internal("backgrounds/menuBackground.png")));
		optionsGroupImage.setPosition(alexButton.getX() + alexButton.getWidth() + EDGE_TOLERANCE,
				alexButton.getY() - 3f*BUTTON_HEIGHT - 3f*EDGE_TOLERANCE);

		//after all components are taken care of...
		menuGroup.pack();
		menuGroup.setPosition(alexButton.getX(), alexButton.getY() - menuGroup.getHeight() - EDGE_TOLERANCE);
		menuGroupImage.setPosition(menuGroup.getX() - .5f*EDGE_TOLERANCE, menuGroup.getY() - EDGE_TOLERANCE*.5f);
		menuGroupImage.setSize(menuGroup.getWidth() + EDGE_TOLERANCE, menuGroup.getHeight() + EDGE_TOLERANCE);
	}

	/**
	 * Initilize inventory group items
	 */
	public void initializeInventoryItems(){
		inventoryMenuInitialized = true;
		for (int i = 0; i < Consumable.DropType.values().length; i++){
			final int index = i;

			//create atlas and add it to a new skin
			atlas = new TextureAtlas(Gdx.files.internal(Consumable.DropType.values()[i].getAtlasPath()));
			buttonSkin = new Skin();
			buttonSkin.addRegions(atlas);

			//create a Buttonstyle
			ImageTextButtonStyle inventoryButtonStyle = new ImageTextButtonStyle();
			inventoryButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
			inventoryButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
			//set the font here
			inventoryButtonStyle.font = font;
			//create a new button using aforementioned button style and set stuff up
			final ImageTextButton inventoryButton = new ImageTextButton("", inventoryButtonStyle){
				@Override
				public float getPrefHeight(){
					return BUTTON_HEIGHT*2/3;
				};

				@Override
				public float getPrefWidth(){
					return BUTTON_WIDTH*2/3;
				}
			};
			inventoryButton.getLabel().setColor(Color.RED);
			inventoryButton.getStyle().font.setScale(FONT_SCALE);
			inventoryButton.setText("x" + getWorld().getPlayer().getInventory().getInventory().get(Consumable.DropType.values()[index]).size);
			inventoryButton.bottom();
			inventoryButton.right();
			inventoryButton.setName(Consumable.DropType.values()[i].getName());
			dnd.addSource(new Source(inventoryButton){

				/**
				 * Overriding dragStart to initialize drag and drop payload
				 */
				@Override
				public Payload dragStart(InputEvent event, float x, float y,int pointer) {
					System.out.println("Drag started @ x:" + x + " y:" + y);
					Payload payload = new Payload();
					if (getWorld().getPlayer().getInventory().removeItem(Consumable.DropType.values()[index])){
						inventoryButton.setText("x" + getWorld().getPlayer().getInventory().getInventory().get(Consumable.DropType.values()[index]).size);
						payload.setObject(new Consumable(Consumable.DropType.values()[index]));
						payload.setDragActor(new Image(inventoryButton.getBackground()));
						return payload;
					}
					return null;
				}

				/**
				 * Overriding dragStop to determine if drag has stopped over a valid target
				 */
				@Override
				public void dragStop(InputEvent event, float x, float y,int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target){
					System.out.println("Drag stopped @ x:" + x + " y:" + y);
					if (target == null){
						getWorld().getPlayer().getInventory().addItem(new Consumable(Consumable.DropType.values()[index]));
						inventoryButton.setText("x" + getWorld().getPlayer().getInventory().getInventory().get(Consumable.DropType.values()[index]).size);
					}
				}
			}
					);
			inventoryGroup.addActor(inventoryButton);
		}
		//some whacky code down here...

		inventoryGroup.pack();
		inventoryGroup.setHeight(BUTTON_HEIGHT);
		inventoryGroupImage.setSize(inventoryGroup.getWidth() + EDGE_TOLERANCE*2f, inventoryGroup.getHeight());
	}

	/**
	 * Initialize upgrade group items
	 */
	public void initializeUpgradeItems(){
		upgradesMenuInitialized = true;
		//initialize upgrade monies
		fruitfulMoney = (int) (15*(Math.pow(2,gameInstance.getLevelHandler().getFruitfullMoneyP())));
		longerMoney = (int) (5*(Math.pow(2,gameInstance.getLevelHandler().getLongerMoneyP())));
		moreMoney = (int) (10*(Math.pow(2,gameInstance.getLevelHandler().getMoreMoneyP())));
		dogMoney = 100;
		final Button fruitfulButton, longerButton, moreButton;
		final Label upgradeLabel, fruitfulLabel, longerLabel, moreLabel;

		//fruitfulbutton
		atlas = new TextureAtlas(Gdx.files.internal("buttons/upgradesScreen/fruitfullButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle fruitfulButtonStyle = new ButtonStyle();
		fruitfulButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		fruitfulButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		TextureRegion trFruitfulButton = new TextureRegion(new Texture(Gdx.files.internal("buttons/upgradesScreen/fruitfullButtonDis.png")) );
		trFruitfulButton.setRegionHeight((int) (BUTTON_HEIGHT*30/8));
		trFruitfulButton.setRegionWidth((int) (BUTTON_HEIGHT*30/8));

		fruitfulButtonStyle.disabled = new TextureRegionDrawable(trFruitfulButton);

		fruitfulButton = new Button(fruitfulButtonStyle){
			@Override
			public float getPrefHeight(){
				return BUTTON_HEIGHT*2/3;
			};

			@Override
			public float getPrefWidth(){
				return BUTTON_WIDTH*2/3;
			}
		};
		fruitfulButton.setName("fruitfulButton");

		//longerButton
		atlas = new TextureAtlas(Gdx.files.internal("buttons/upgradesScreen/LongerButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle longerButtonStyle = new ButtonStyle();
		longerButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		longerButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		TextureRegion trLongerButton = new TextureRegion(new Texture(Gdx.files.internal("buttons/upgradesScreen/LongerButtonDis.png")) );
		trLongerButton.setRegionHeight((int) (BUTTON_HEIGHT*30/8));
		trLongerButton.setRegionWidth((int) (BUTTON_HEIGHT*30/8));
		longerButtonStyle.disabled = new TextureRegionDrawable(trLongerButton);

		longerButton = new Button(longerButtonStyle){
			@Override
			public float getPrefHeight(){
				return BUTTON_HEIGHT*2/3;
			};

			@Override
			public float getPrefWidth(){
				return BUTTON_WIDTH*2/3;
			}
		};
		longerButton.setName("longerButton");

		//moreButton
		atlas = new TextureAtlas(Gdx.files.internal("buttons/upgradesScreen/MoreButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle MoreButtonStyle = new ButtonStyle();
		MoreButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		MoreButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		TextureRegion trMoreButton = new TextureRegion(new Texture(Gdx.files.internal("buttons/upgradesScreen/MoreButtonDis.png")) );
		trMoreButton.setRegionHeight((int) (BUTTON_HEIGHT*30/8));
		trMoreButton.setRegionWidth((int) (BUTTON_HEIGHT*30/8));

		MoreButtonStyle.disabled = new TextureRegionDrawable(trMoreButton);

		moreButton = new Button(MoreButtonStyle) {
			@Override
			public float getPrefHeight(){
				return BUTTON_HEIGHT*2/3;
			};

			@Override
			public float getPrefWidth(){
				return BUTTON_WIDTH*2/3;
			}
		};
		moreButton.setName("moreButton");

		//dogButton
		atlas = new TextureAtlas(Gdx.files.internal("buttons/upgradesScreen/dogButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle dogButtonStyle = new ButtonStyle();
		dogButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		dogButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		TextureRegion trDogButton = new TextureRegion(new Texture(Gdx.files.internal("buttons/upgradesScreen/dogButtonDis.png")) );
		trDogButton.setRegionHeight((int) (BUTTON_HEIGHT*30/8));
		trDogButton.setRegionWidth((int) (BUTTON_HEIGHT*30/8));

		dogButtonStyle.disabled = new TextureRegionDrawable(trDogButton);

		dogButton = new Button(dogButtonStyle) {
			@Override
			public float getPrefHeight(){
				return BUTTON_HEIGHT*2/3;
			};

			@Override
			public float getPrefWidth(){
				return BUTTON_WIDTH*2/3;
			}
		};
		dogButton.setName("dogButton");



		//UpgradeStatusGroup
		upgradesStatusGroup = new HorizontalGroup();
		upgradesStatusGroup.center();
		upgradesStatusGroup.space(EDGE_TOLERANCE);

		//fruitfulLabel
		LabelStyle upgradeLabelStyle = new LabelStyle();
		//upgradeLabelStyle.font = new BitmapFont(Gdx.files.internal("fonts/SketchBook.fnt"));
		upgradeLabelStyle.font = font;
		//upgradeLabelStyle.fontColor = Color.WHITE;

		upgradeLabel = new Label(
				"\n" +
						"Upgrade Level: \n" +
						"Next Upgrade: \n" +
						"Upgrade Cost: \n" +
						"Current:"
						, upgradeLabelStyle);
		upgradeLabel.setAlignment(Align.left);

		fruitfulLabel = new Label(
				"Fruitfulness\n" +
						String.valueOf(gameInstance.getLevelHandler().getFruitfullMoneyP()) + "\n" +
						"+" + String.valueOf(5) + "%\n" +
						"$" + String.valueOf(fruitfulMoney) + "\n" +
						String.format("%.1f",((Animal) getWorld().getMovables().get(0)).getFertilityRate())+ "%"			 
						, upgradeLabelStyle);
		fruitfulLabel.setAlignment(Align.right);

		longerLabel = new Label(
				"Item Duration\n" +
						String.valueOf(gameInstance.getLevelHandler().getLongerMoneyP()) + "\n" +
						"+" + String.format("%.2f",5/60.0) + " s\n" +
						"$" + String.valueOf(longerMoney) + "\n" +
						String.format("%.2f",((Animal) getWorld().getMovables().get(0)).getTimeOnGround())+ "%"			 
						, upgradeLabelStyle);
		longerLabel.setAlignment(Align.right);

		moreLabel = new Label(
				"Drop Interval\n" +
						String.valueOf(gameInstance.getLevelHandler().getMoreMoneyP()) + "\n" +
						"-" + String.format("%.2f",5/60.0) + " s\n" +
						"$" + String.valueOf(moreMoney) + "\n" +
						String.format("%.2f",((Animal) getWorld().getMovables().get(0)).getDropInterval())+ "%"	
						, upgradeLabelStyle);
		moreLabel.setAlignment(Align.right);

		//add listeners to buttons
		fruitfulButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if(!fruitfulButton.isDisabled()){
					SoundHandler.playButtonClick();
					//take away player money and add more to percentage of droppings
					//Gdx.input.setCatchBackKey(true);

					Array<Movable> animals = getWorld().getMovables();
					for(Movable animal : animals){
						((Animal) animal).upgradeFertilityRate(5);
					}
					getWorld().getPlayer().subtractPlayerMoney(fruitfulMoney);
					System.out.println(fruitfulMoney +"  "+getWorld().getPlayer().getPlayerMoney());
					fruitfulMoney += fruitfulMoney;
					gameInstance.getLevelHandler().addFruitfullMoneyP();

					fruitfulLabel.setText(
							"Fruitfulness\n" +
									String.valueOf(gameInstance.getLevelHandler().getFruitfullMoneyP()) + "\n" +
									"+" + String.valueOf(5) + "%\n" +
									"$" + String.valueOf(fruitfulMoney) + "\n" +
									String.format("%.1f",((Animal) getWorld().getMovables().get(0)).getFertilityRate())+ "%"
							);
					infoLabel.setText("Money: ##" + getWorld().getPlayer().getPlayerMoney());
				}
				if(getWorld().getPlayer().getPlayerMoney() < fruitfulMoney)
					fruitfulButton.setDisabled(true);
				else
					fruitfulButton.setDisabled(false);

				if(getWorld().getPlayer().getPlayerMoney() < longerMoney)
					longerButton.setDisabled(true);
				else
					longerButton.setDisabled(false);

				if(getWorld().getPlayer().getPlayerMoney() < moreMoney)
					moreButton.setDisabled(true);
				else
					moreButton.setDisabled(false);
				
				if(getWorld().getPlayer().getPlayerMoney() < dogMoney)
					dogButton.setDisabled(true);
				else
					dogButton.setDisabled(false);
			}
		});
		longerButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if(!longerButton.isDisabled()){
					SoundHandler.playButtonClick();
					//take away player money and add more to precentage of droppings
					//Gdx.input.setCatchBackKey(true);

					Array<Movable> animals = getWorld().getMovables();
					for(Movable animal : animals){
						((Animal) animal).upgradeTimeOnGround(5);
					}
					getWorld().getPlayer().subtractPlayerMoney(longerMoney);
					longerMoney += longerMoney;
					gameInstance.getLevelHandler().addLongerMoneyP();
					longerLabel.setText(
							"Item Duration\n" +
									String.valueOf(gameInstance.getLevelHandler().getLongerMoneyP()) + "\n" +
									"+" + String.format("%.2f",5/60.0) + " s\n" +
									"$" + String.valueOf(longerMoney) + "\n" +
									String.format("%.2f",((Animal) getWorld().getMovables().get(0)).getTimeOnGround())+ "%"
							);
					infoLabel.setText("Money: ##" + getWorld().getPlayer().getPlayerMoney());
				}
				if(getWorld().getPlayer().getPlayerMoney() < fruitfulMoney)
					fruitfulButton.setDisabled(true);
				else
					fruitfulButton.setDisabled(false);

				if(getWorld().getPlayer().getPlayerMoney() < longerMoney)
					longerButton.setDisabled(true);
				else
					longerButton.setDisabled(false);

				if(getWorld().getPlayer().getPlayerMoney() < moreMoney)
					moreButton.setDisabled(true);
				else
					moreButton.setDisabled(false);
				
				if(getWorld().getPlayer().getPlayerMoney() < dogMoney)
					dogButton.setDisabled(true);
				else
					dogButton.setDisabled(false);
			}
		});
		moreButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if(!moreButton.isDisabled()){
					SoundHandler.playButtonClick();
					//take away player money and add more to precentage of droppings
					//Gdx.input.setCatchBackKey(true);

					Array<Movable> animals = getWorld().getMovables();
					for(Movable animal : animals){
						((Animal) animal).upgradeDropInterval(5);
					}
					getWorld().getPlayer().subtractPlayerMoney(moreMoney);
					gameInstance.getLevelHandler().addMoreMoneyP();
					moreMoney += moreMoney;
					moreLabel.setText(
							"Drop Interval\n" +
									String.valueOf(gameInstance.getLevelHandler().getMoreMoneyP()) + "\n" +
									"-" + String.format("%.2f",5/60.0) + " s\n" +
									"$" + String.valueOf(moreMoney) + "\n" +
									String.format("%.2f",((Animal) getWorld().getMovables().get(0)).getDropInterval())+ "%"	
							);
					infoLabel.setText("Money: ##" + getWorld().getPlayer().getPlayerMoney());
				}
				if(getWorld().getPlayer().getPlayerMoney() < fruitfulMoney)
					fruitfulButton.setDisabled(true);
				else
					fruitfulButton.setDisabled(false);

				if(getWorld().getPlayer().getPlayerMoney() < longerMoney)
					longerButton.setDisabled(true);
				else
					longerButton.setDisabled(false);

				if(getWorld().getPlayer().getPlayerMoney() < moreMoney)
					moreButton.setDisabled(true);
				else
					moreButton.setDisabled(false);
				
				if(getWorld().getPlayer().getPlayerMoney() < dogMoney)
					dogButton.setDisabled(true);
				else
					dogButton.setDisabled(false);
			}
		});
		
		dogButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if(!dogButton.isDisabled()) {
					SoundHandler.playButtonClick();
					getWorld().getPlayer().subtractPlayerMoney(dogMoney);
					boostArray.add(new Dog(new Vector2(0, Gdx.graphics.getHeight()), gameInstance.getLevelHandler().animalChangeX(), gameInstance.getLevelHandler().animalChangeY()));
					getWorld().getDrawMap().put("Boosts", boostArray);
					infoLabel.setText("Money: ##" + getWorld().getPlayer().getPlayerMoney());
				}
				if(getWorld().getPlayer().getPlayerMoney() < fruitfulMoney)
					fruitfulButton.setDisabled(true);
				else
					fruitfulButton.setDisabled(false);

				if(getWorld().getPlayer().getPlayerMoney() < longerMoney)
					longerButton.setDisabled(true);
				else
					longerButton.setDisabled(false);

				if(getWorld().getPlayer().getPlayerMoney() < moreMoney)
					moreButton.setDisabled(true);
				else
					moreButton.setDisabled(false);
				
				if(getWorld().getPlayer().getPlayerMoney() < dogMoney)
					dogButton.setDisabled(true);
				else
					dogButton.setDisabled(false);
			}
			
		});

		//set disabled
		if(getWorld().getPlayer().getPlayerMoney() < 15){
			fruitfulButton.setDisabled(true);
		}
		if(getWorld().getPlayer().getPlayerMoney() < 5){
			longerButton.setDisabled(true);
		}
		if(getWorld().getPlayer().getPlayerMoney() < 10){
			moreButton.setDisabled(true);
		}
		if(getWorld().getPlayer().getPlayerMoney() < 100){
			dogButton.setDisabled(true);
		}

		//pack labels
		upgradesStatusGroup.addActor(upgradeLabel);
		upgradesStatusGroup.addActor(fruitfulLabel);
		upgradesStatusGroup.addActor(longerLabel);
		upgradesStatusGroup.addActor(moreLabel);
		upgradesStatusGroup.addActor(dogButton);

		upgradesStatusGroup.pack();
		upgradesStatusGroup.setPosition(Gdx.graphics.getWidth()/2f - upgradesStatusGroup.getWidth()/2f,
				EDGE_TOLERANCE*2f);

		upgradesStatusGroupImage = new Image(new Texture(Gdx.files.internal("backgrounds/menuBackground.png")));
		upgradesStatusGroupImage.setPosition(upgradesStatusGroup.getX() - EDGE_TOLERANCE,
				upgradesStatusGroup.getY() - EDGE_TOLERANCE);
		upgradesStatusGroupImage.setSize(upgradesStatusGroup.getWidth() + EDGE_TOLERANCE*2f,
				upgradesStatusGroup.getHeight() + EDGE_TOLERANCE*2f);


		upgradesGroup1.addActor(fruitfulButton);
		upgradesGroup1.addActor(longerButton);
		upgradesGroup1.addActor(moreButton);
		upgradesGroup2.addActor(dogButton);

		upgradesGroup1.pack();
		upgradesGroup1.setHeight(BUTTON_HEIGHT);
		upgradesGroup2.pack();
		upgradesGroup2.setHeight(BUTTON_HEIGHT);

		//For two rows
		upgradesGroupImage.setSize(Math.max(upgradesGroup1.getWidth(), upgradesGroup2.getWidth()) + EDGE_TOLERANCE*2f,
				upgradesGroup1.getHeight() + upgradesGroup2.getHeight() - EDGE_TOLERANCE);
		upgradesGroupImage.setPosition(alexButton.getX() + alexButton.getWidth() + EDGE_TOLERANCE,
				alexButton.getY() - 3f*BUTTON_HEIGHT - EDGE_TOLERANCE);
		//For single row
		/*upgradesGroupImage.setSize(upgradesGroup1.getWidth() + 2f*EDGE_TOLERANCE, upgradesGroup1.getHeight());
		upgradesGroupImage.setPosition(alexButton.getX() + alexButton.getWidth() + EDGE_TOLERANCE,
				alexButton.getY() - 2f*BUTTON_HEIGHT - 2f*EDGE_TOLERANCE);*/
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

		optionsGroup.addActor(mainMenuButton);
		optionsGroup.addActor(soundButton);
		optionsGroup.addActor(musicButton);
		optionsGroup.addActor(helpButton);

		optionsGroup.pack();
		optionsGroup.setHeight(BUTTON_HEIGHT);
		optionsGroupImage.setSize(optionsGroup.getWidth() + EDGE_TOLERANCE*2f, optionsGroup.getHeight());
	}

	/**
	 * Toggles main menu group items
	 * @param checked
	 */
	public void handleMainMenu(boolean checked) {
		if (checked){
			buttonStage.addActor(menuGroupImage);
			buttonStage.addActor(menuBackgroundButton);
			buttonStage.addActor(menuGroup);
			menuBackgroundButton.toBack();
		}
		else{
			menuGroupImage.remove();
			menuGroup.remove();
			menuBackgroundButton.remove();

			//collapse children menus
			handleInventoryMenu(false);
			handleUpgradesMenu(false);
			handleOptionsMenu(false);
		}
	}

	/**
	 * Toggles inventory menu items
	 * @param checked
	 */
	public void handleInventoryMenu(boolean checked){
		if (checked){
			buttonStage.addActor(inventoryGroupImage);
			buttonStage.addActor(inventoryGroup);
			for (Consumable.DropType d : Consumable.DropType.values()){
				((ImageTextButton) inventoryGroup.findActor(d.getName())).setText("x" + getWorld().getPlayer().getInventory().getInventory().get(d).size);
			}
			//from here has to update on button visibility
		}
		else{
			inventoryGroup.remove();
			inventoryGroupImage.remove();
			if (!alexButton.isChecked())
				inventoryGroupButton.setChecked(false);
		}
	}

	/**
	 * Toggles upgrades menu items
	 * @param checked
	 */
	public void handleUpgradesMenu(boolean checked){
		if (checked){
			buttonStage.addActor(upgradesGroupImage);
			buttonStage.addActor(upgradesGroup1);
			buttonStage.addActor(upgradesStatusGroupImage);
			buttonStage.addActor(upgradesStatusGroup);
			buttonStage.addActor(upgradesGroup2);

			if(getWorld().getPlayer().getPlayerMoney() < fruitfulMoney)
				((Button) upgradesGroup1.findActor("fruitfulButton")).setDisabled(true);
			else
				((Button) upgradesGroup1.findActor("fruitfulButton")).setDisabled(false);

			if(getWorld().getPlayer().getPlayerMoney() < longerMoney)
				((Button) upgradesGroup1.findActor("longerButton")).setDisabled(true);
			else
				((Button) upgradesGroup1.findActor("longerButton")).setDisabled(false);

			if(getWorld().getPlayer().getPlayerMoney() < moreMoney)
				((Button) upgradesGroup1.findActor("moreButton")).setDisabled(true);
			else
				((Button) upgradesGroup1.findActor("moreButton")).setDisabled(false);

			if(getWorld().getPlayer().getPlayerMoney() < dogMoney)
				((Button) upgradesGroup2.findActor("dogButton")).setDisabled(true);
			else
				((Button) upgradesGroup2.findActor("dogButton")).setDisabled(false);

			System.out.println(((Button) upgradesGroup1.findActor("fruitfulButton")).isDisabled());
		}
		else{
			upgradesGroup1.remove();
			upgradesGroup2.remove();
			upgradesGroupImage.remove();
			if (upgradesStatusGroup!=null){
				upgradesStatusGroup.remove();
				upgradesStatusGroupImage.remove();
			}
			if (!alexButton.isChecked())
				upgradesGroupButton.setChecked(false);
		}
	}

	/**
	 * Toggles options menu items
	 * @param checked
	 */
	public void handleOptionsMenu(boolean checked){
		if (checked){
			buttonStage.addActor(optionsGroupImage);
			buttonStage.addActor(optionsGroup);
		}
		else{
			optionsGroup.remove();
			optionsGroupImage.remove();
			if (!alexButton.isChecked())
				optionsGroupButton.setChecked(false);
		}
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
		black.dispose();
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
		handleMainMenu(true);
		optionsGroupButton.setChecked(true);
		handleOptionsMenu(true);
	}

	@Override
	public void pause() {
		Gdx.app.log("My Tagg", "This is screen pause");

	}
}
