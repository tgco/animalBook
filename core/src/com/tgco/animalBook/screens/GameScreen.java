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
import com.badlogic.gdx.scenes.scene2d.Actor;
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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.utils.Array;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.gameObjects.Animal;
import com.tgco.animalBook.gameObjects.Consumable;
import com.tgco.animalBook.gameObjects.Movable;
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
	private Button alexButton, inventoryGroupButton, optionsGroupButton, upgradesGroupButton, menuBackgroundButton;

	private VerticalGroup menuGroup;
	private Image menuGroupImage, inventoryGroupImage, upgradesGroupImage, optionsGroupImage;

	private HorizontalGroup inventoryGroup, upgradesGroup, optionsGroup;

	private DragAndDrop dnd;

	/**
	 * Amounts of each upgrade
	 */
	private int fruitfulMoney;
	private int longerMoney;
	private int moreMoney;

	private static boolean mainMenuInitialized = false;

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
			font.setColor(Color.BLACK);
			font.setScale(1.2f);
			Vector3 vect = new Vector3(Gdx.graphics.getWidth()/2 +10,0 +3*EDGE_TOLERANCE,0);
			gameWorld.getCamera().unproject(vect);
			font.draw(batch, "Your Money: $" + String.valueOf(gameWorld.getPlayer().getPlayerMoney()), vect.x ,vect.y );

			//Draw world over background
			gameWorld.render(batch,alexButton.isChecked(),delta);

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
		mainMenuInitialized = false;
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
		inputMultiplexer.addProcessor(buttonStage);
	}

	//interface initialization by menu heirarchy
	public void initializeMenuItems(){
		dnd.addTarget(new Target(alexButton){

			@Override
			public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
				if (payload.getObject() instanceof Consumable)
					if (((Consumable)payload.getObject()).getType() == Consumable.DropType.WOOL ||
					getWorld().getPlayer().getHealth() == 100f){
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

		mainMenuInitialized = true;
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
				SoundHandler.playButtonClick();
				SoundHandler.changeBackgroundVolume((float) .1);
				handleUpgradesMenu(upgradesGroupButton.isChecked());
				handleInventoryMenu(false);
				inventoryGroupButton.setChecked(false);
				handleOptionsMenu(false);
				optionsGroupButton.setChecked(false);
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

		//Upgrade Group
		upgradesGroup = new HorizontalGroup();
		upgradesGroup.center();
		upgradesGroup.setPosition(alexButton.getX() + alexButton.getWidth() + EDGE_TOLERANCE*2f,
				alexButton.getY() - 2f*BUTTON_HEIGHT - 2f*EDGE_TOLERANCE);
		upgradesGroup.space(EDGE_TOLERANCE);

		upgradesGroupImage = new Image(new Texture(Gdx.files.internal("backgrounds/menuBackground.png")));
		upgradesGroupImage.setPosition(alexButton.getX() + alexButton.getWidth() + EDGE_TOLERANCE,
				alexButton.getY() - 2f*BUTTON_HEIGHT - 2f*EDGE_TOLERANCE);

		//Option Group
		optionsGroup = new HorizontalGroup();
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
		menuGroupImage.setPosition(alexButton.getX() - .5f*EDGE_TOLERANCE, alexButton.getY() - menuGroup.getHeight() - EDGE_TOLERANCE*2f);
		menuGroupImage.setSize(alexButton.getWidth() + EDGE_TOLERANCE, menuGroup.getHeight() + EDGE_TOLERANCE*2f);

		initializeInventoryItems();
		initializeUpgradeItems();
		initializeOptionItems();

	}

	public void initializeInventoryItems(){
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
			inventoryButtonStyle.font = new BitmapFont();
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
			inventoryButton.setText("x" + getWorld().getPlayer().getInventory().getInventory().get(Consumable.DropType.values()[index]).size);
			inventoryButton.bottom();
			inventoryButton.right();

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
	public void initializeUpgradeItems(){
		//initialize upgrade monies
		fruitfulMoney = (int) (100*(Math.pow(2,gameInstance.getLevelHandler().getFruitfullMoneyP())));
		longerMoney = (int) (500*(Math.pow(2,gameInstance.getLevelHandler().getLongerMoneyP())));
		moreMoney = (int) (1000*(Math.pow(2,gameInstance.getLevelHandler().getMoreMoneyP())));
		
		//fruitfulbutton
		atlas = new TextureAtlas(Gdx.files.internal("buttons/upgradesScreen/fruitfullButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle fruitfulButtonStyle = new ButtonStyle();
		fruitfulButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		fruitfulButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		TextureRegion trFruitfulButton = new TextureRegion(new Texture(Gdx.files.internal("buttons/upgradesScreen/fruitfullButtonDis.png")) );
		trFruitfulButton.setRegionHeight((int) (BUTTON_HEIGHT*1/3));
		trFruitfulButton.setRegionWidth((int) (BUTTON_HEIGHT*1/3));
		
		fruitfulButtonStyle.disabled = new TextureRegionDrawable(trFruitfulButton);

		final Button fruitfulButton = new Button(fruitfulButtonStyle){
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
		fruitfulButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if(!fruitfulButton.isDisabled()){
					SoundHandler.playButtonClick();
					//take away player money and add more to precentage of droppings
					//Gdx.input.setCatchBackKey(true);

					Array<Movable> animals = getWorld().getMovables();
					for(Movable animal : animals){
						((Animal) animal).upgradeFertilityRate(5);
					}
					getWorld().getPlayer().subtractPlayerMoney(fruitfulMoney);
					System.out.println(fruitfulMoney +"  "+getWorld().getPlayer().getPlayerMoney());
					fruitfulMoney += fruitfulMoney;
					gameInstance.getLevelHandler().addFruitfullMoneyP();
				}
				if(getWorld().getPlayer().getPlayerMoney() < fruitfulMoney)
					fruitfulButton.setDisabled(true);
				else
					fruitfulButton.setDisabled(false);
			}
		});

		//longerButton
		atlas = new TextureAtlas(Gdx.files.internal("buttons/upgradesScreen/LongerButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle longerButtonStyle = new ButtonStyle();
		longerButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		longerButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		TextureRegion trLongerButton = new TextureRegion(new Texture(Gdx.files.internal("buttons/upgradesScreen/LongerButtonDis.png")) );

		longerButtonStyle.disabled = new TextureRegionDrawable(trLongerButton);

		final Button longerButton = new Button(longerButtonStyle){
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
				}
			}
		});


		//moreButton
		atlas = new TextureAtlas(Gdx.files.internal("buttons/upgradesScreen/MoreButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle MoreButtonStyle = new ButtonStyle();
		MoreButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		MoreButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		TextureRegion trMoreButton = new TextureRegion(new Texture(Gdx.files.internal("buttons/upgradesScreen/MoreButtonDis.png")) );

		MoreButtonStyle.disabled = new TextureRegionDrawable(trMoreButton);

		final Button moreButton = new Button(MoreButtonStyle) {
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
				}
			}
		});

		if(getWorld().getPlayer().getPlayerMoney() < 500){
			fruitfulButton.setDisabled(true);
		}
		if(getWorld().getPlayer().getPlayerMoney() < 1000){
			longerButton.setDisabled(true);
		}
		if(getWorld().getPlayer().getPlayerMoney() < 1500){
			moreButton.setDisabled(true);
		}
		
		upgradesGroup.addActor(fruitfulButton);
		upgradesGroup.addActor(longerButton);
		upgradesGroup.addActor(moreButton);




		upgradesGroup.pack();
		upgradesGroup.setHeight(BUTTON_HEIGHT);
		//upgradesGroup.setWidth(BUTTON_HEIGHT);
		upgradesGroupImage.setSize(upgradesGroup.getWidth() + EDGE_TOLERANCE*2f, upgradesGroup.getHeight());
	}
	public void initializeOptionItems(){
		optionsGroup.pack();
		optionsGroup.setHeight(BUTTON_HEIGHT);
		optionsGroup.setWidth(BUTTON_HEIGHT);
		optionsGroupImage.setSize(optionsGroup.getWidth() + EDGE_TOLERANCE*2f, optionsGroup.getHeight());
	}

	//menu actions by heirarchy
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

	public void handleInventoryMenu(boolean checked){
		if (checked){
			buttonStage.addActor(inventoryGroupImage);
			buttonStage.addActor(inventoryGroup);
			//from here has to update on button visibility
		}
		else{
			inventoryGroup.remove();
			inventoryGroupImage.remove();
			if (!alexButton.isChecked())
				inventoryGroupButton.setChecked(false);
		}
	}

	public void handleUpgradesMenu(boolean checked){
		if (checked){
			buttonStage.addActor(upgradesGroupImage);
			buttonStage.addActor(upgradesGroup);
			if(getWorld().getPlayer().getPlayerMoney() < fruitfulMoney)
				((Button) upgradesGroup.findActor("fruitfulButton")).setDisabled(true);
			else
				((Button) upgradesGroup.findActor("fruitfulButton")).setDisabled(false);
			
			if(getWorld().getPlayer().getPlayerMoney() < longerMoney)
				((Button) upgradesGroup.findActor("longerButton")).setDisabled(true);
			else
				((Button) upgradesGroup.findActor("longerButton")).setDisabled(false);
			
			if(getWorld().getPlayer().getPlayerMoney() < moreMoney)
				((Button) upgradesGroup.findActor("moreButton")).setDisabled(true);
			else
				((Button) upgradesGroup.findActor("moreButton")).setDisabled(false);
			System.out.println(((Button) upgradesGroup.findActor("fruitfulButton")).isDisabled());
		}
		else{
			upgradesGroup.remove();
			upgradesGroupImage.remove();
			if (!alexButton.isChecked())
				upgradesGroupButton.setChecked(false);
		}
	}

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
	 * Unused methods for detecting screen events
	 */
	@Override
	public void show() {

	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	public Button getAlexButton() {
		return alexButton;
	}

	public boolean inMenu() {
		return alexButton.isChecked();
	}
	/*public void reinitButtons(){
		if(world.getPlayer().getPlayerMoney() < fruitfulMoney){
			fruitfullButton.setDisabled(true);
			drawAmounts(0.5f, 0);
			drawData(0.5f, 0);
		}
		else{
			fruitfullButton.setDisabled(false);
			drawAmounts(1f, 0);
			drawData(1f, 0);
		}
		if(world.getPlayer().getPlayerMoney() < longerMoney){
			longerButton.setDisabled(true);
			drawAmounts(0.5f, 1);
			drawData(0.5f, 1);
		}
		else{
			longerButton.setDisabled(false);
			drawAmounts(1, 1);
			drawData(1, 1);
		}
		if(world.getPlayer().getPlayerMoney() < moreMoney){
			moreeButton.setDisabled(true);
			drawAmounts(0.5f, 2);
			drawData(0.5f, 2);
		}
		else{
			moreeButton.setDisabled(false);
			drawAmounts(1, 2);
			drawData(1, 2);
		}

		batch.begin();
		font.setColor(Color.BLACK);
		font.setScale(1.2f);
		font.draw(batch, "Your Money: $" + String.valueOf(world.getPlayer().getPlayerMoney()), Gdx.graphics.getWidth()/2 -10, Gdx.graphics.getHeight() -2*EDGE_TOLERANCE);
		font.setScale(.75f);
		batch.end();
	}*/
}
