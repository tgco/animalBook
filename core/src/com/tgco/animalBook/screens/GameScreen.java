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
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.utils.Array;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.gameObjects.Consumable;
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
	private Image menuGroupImage;

	private HorizontalGroup inventoryGroup, upgradesGroup, optionsGroup;

	private static DragAndDrop dnd;

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
		initializeButtons();
	}

	/**
	 * Initializes all button objects
	 */
	@Override
	protected void initializeButtons() {
		dnd = new DragAndDrop();
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
				System.out.println("Alex clicked.");
			}
		});
		
		dnd.addTarget(new Target(alexButton){
			
			@Override
			public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
				System.out.println("Stop tickling me!");
				System.out.println("Player health:" + getWorld().getPlayer().getHealth());
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
		buttonStage.addActor(alexButton);
		inputMultiplexer.addProcessor(buttonStage);
	}

	public void initializeMenuItems(){
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
				handleMainMenu(false);
				alexButton.setChecked(false);
				inventoryGroupButton.setChecked(false);
				//upgradesGroupButton.setChecked(false);
				//optionsGroupButton.setChecked(false);
				
			}
		});

		//Main Group
		menuGroup = new VerticalGroup();
		menuGroup.left();
		menuGroup.setPosition(alexButton.getX(), EDGE_TOLERANCE);
		menuGroup.setSize(alexButton.getWidth(), alexButton.getY() - EDGE_TOLERANCE);

		menuGroupImage = new Image(new Texture(Gdx.files.internal("backgrounds/menuBackground.png")));
		menuGroupImage.setPosition(alexButton.getX(), EDGE_TOLERANCE);
		menuGroupImage.setSize(alexButton.getWidth(), alexButton.getY() - EDGE_TOLERANCE);

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
				handleInventoryMenu(inventoryGroupButton.isChecked());
				inventoryGroupButton.setChecked(inventoryGroupButton.isChecked());
			}
		});
		menuGroup.addActor(inventoryGroupButton);




		//Upgrade Group Button

		//Option Group Button

		//Inventory Group
		inventoryGroup = new HorizontalGroup();
		inventoryGroup.center();
		inventoryGroup.setPosition((inventoryGroupButton.getX() + inventoryGroupButton.getPrefWidth()) + EDGE_TOLERANCE, 
				inventoryGroupButton.getY());
		inventoryGroup.setSize(Gdx.graphics.getWidth() - (inventoryGroupButton.getX() + inventoryGroupButton.getPrefWidth()) + EDGE_TOLERANCE,
				alexButton.getY() - EDGE_TOLERANCE );
		inventoryGroup.space(EDGE_TOLERANCE);
		
		System.out.println("inventoryGroup position" + inventoryGroup.getX() + " " + inventoryGroup.getY());

		Image inventoryGroupImage = new Image(new Texture(Gdx.files.internal("backgrounds/menuBackground.png")));
		inventoryGroupImage.setPosition((inventoryGroupButton.getX() + inventoryGroupButton.getPrefWidth()) + EDGE_TOLERANCE, 
				inventoryGroupButton.getY());
		inventoryGroupImage.setSize(Gdx.graphics.getWidth() - (inventoryGroupButton.getX() + inventoryGroupButton.getPrefWidth()) + EDGE_TOLERANCE,
				alexButton.getY() - EDGE_TOLERANCE);

		System.out.println("inventoryGroupImage position" + inventoryGroupImage.getX() + " " + inventoryGroupImage.getY() + "Size: " + inventoryGroupImage.getWidth() + " " + inventoryGroupImage.getHeight());
		//Upgrade Group
		upgradesGroup = new HorizontalGroup();

		//Option Group
		optionsGroup = new HorizontalGroup();

		System.out.println("Main menu bar initialized.");

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
			inventoryButtonStyle.font = new BitmapFont();

			//create a new button using aforementioned button style and set stuff up
			final ImageTextButton inventoryButton = new ImageTextButton("", inventoryButtonStyle){
				@Override
				public float getPrefHeight(){
					return BUTTON_HEIGHT/2;
				};

				@Override
				public float getPrefWidth(){
					return BUTTON_WIDTH/2;
				}
			};
			inventoryButton.setText("x" + getWorld().getPlayer().getInventory().getInventory().get(Consumable.DropType.values()[index]).size);
			inventoryButton.pad(EDGE_TOLERANCE);

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
						//change fonts/size attributes use inventoryButton.getLabel().(invoke methods)
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
	}
	public void initializeUpgradeItems(){}
	public void initializeOptionItems(){}

	
	public void handleMainMenu(boolean checked) {
		if (checked){
			buttonStage.addActor(menuBackgroundButton);
			buttonStage.addActor(menuGroupImage);
			buttonStage.addActor(menuGroup);
		}
		else{
			menuGroupImage.remove();
			menuGroup.remove();
			menuBackgroundButton.remove();

			//collapse children menus
			handleInventoryMenu(false);
			handleUpgradeMenu(false);
			handleOptionsMenu(false);
		}
	}

	public void handleInventoryMenu(boolean checked){
		if (checked){
			buttonStage.addActor(inventoryGroup);
			alexButton.toFront();
		}
		else{
			inventoryGroup.remove();
		}
	}

	public void handleUpgradeMenu(boolean checked){

	}

	public void handleOptionsMenu(boolean checked){

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

	public Array<Actor> getScreenActors(){
		Array<Actor> menuActors = new Array<Actor>();
		menuActors.add(alexButton);
		if (alexButton.isChecked())
			menuActors.add(menuGroup);
		/*if (inventoryButton.isChecked())
			menuActors.add(inventoryGroup);
		if (upgradesButton.isChecked())
			menuActors.add(upgradesGroup);
		if (optionsButton.isChecked())
			menuActors.add(optionsGroup);*/
		return menuActors;
	}
}
