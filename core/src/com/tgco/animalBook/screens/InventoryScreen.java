package com.tgco.animalBook.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.gameObjects.Consumable;
import com.tgco.animalBook.handlers.SoundHandler;

/**
 * The InventoryScreen object. Is responsible for providing users an overview of their in-game Consumables as well as the option
 * to drag and drop what they want to consume to stay alive
 * 
 * @author Kelly Masuda
 *
 */
public class InventoryScreen extends ButtonScreenAdapter implements Screen {
	
	
	private Texture imageTexture = new Texture(Gdx.files.internal("objectTextures/eatzone.png"));
	private Texture black = new Texture(Gdx.files.internal("primitiveTextures/black.png"));
	private Texture red = new Texture(Gdx.files.internal("primitiveTextures/red.png"));
	
	/**
	 * The image that represents the drop zone for the player's eat() function
	 */
	private Image eatZone = new Image(imageTexture);
	
	/**
	 * DragAndDrop object that facilitates drag and drop functionality in this screen
	 */
	private static final DragAndDrop dnd = new DragAndDrop();
	
	/**
	 * instance of gameScreen
	 */
	private GameScreen gameScreen;
	
	/**
	 * Button for leaving the inventory screen
	 */
	private Button leaveButton;
	
	/**
	 * Draws amounts of each inventory item
	 */
	private static final BitmapFont[] fonts = new BitmapFont[Consumable.DropType.values().length];

	/**
	 * The inventory screen constructor.
	 * 
	 * @param gameInstance - The current game instance
	 * @param gameScreen - The current game screen
	 */
	public InventoryScreen(AnimalBookGame gameInstance, final GameScreen gameScreen) {
		super(gameInstance);
		this.gameScreen = gameScreen;

		//Rendering
		batch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/inventoryBackground.jpg"));
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);

		//drag and drop target initialization
		dnd.addTarget(new Target(eatZone) {

			/**
			 * Overriding drag function to move Consumable to correct locations
			 */
			@Override
			public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
				if (payload.getObject() instanceof Consumable)
					if ((gameScreen.getWorld().getPlayer().getHealth() == 100)){
						this.getActor().setColor(Color.RED);
						return false;
					}
				this.getActor().setColor(Color.GREEN);
				return true;
			}

			/**
			 * Overriding reset function to reset objects to correct colors
			 */
			@Override
			public void reset( Source source, Payload payload) {
				getActor().setColor(Color.WHITE);
			}

			/**
			 * Overriding drop function to move object from one location to another
			 */
			@Override
			public void drop(Source source, Payload payload, float x, float y, int pointer) {
				if (payload.getObject() instanceof Consumable)
					gameScreen.getWorld().getPlayer().eat(((Consumable)payload.getObject()).getType().getHungerValue());
			}
		});
	}

	/**
	 * Overriding render function to display inventory items correctly
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//draw background
		batch.begin();
		batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		updateHealthBar();
		batch.end();

		buttonStage.act(delta);
		buttonStage.draw();
		int numConsumables = Consumable.DropType.values().length;
		for (int i = 0; i < numConsumables; i ++){
			updateInventoryScreenItems(i);
		}
		
		if(Gdx.input.isKeyPressed(Keys.BACK)){
			SoundHandler.changeBackgroundVolume((float) .5);
			gameScreen.resetInputProcessors();
			gameInstance.setScreen(gameScreen);
			dispose();
		}
	}

	/**
	 * Overriding resize function to initialize inventory interface
	 */
	@Override
	public void resize(int width, int height) {
		if ( buttonStage == null)
			buttonStage = new Stage();
		buttonStage.clear();
		//reinit buttons
		initializeInventoryInterface();
		initializeButtons();
		//updateHealthBar();
	}

	/**
	 * Creates buttons for inventory interface, health bar, and drag and drop functionality
	 */
	private void initializeInventoryInterface() {

		for (int i = 0; i < Consumable.DropType.values().length; i++){
			final int index = i;

			//associated BitmapFont object for consumable[i]
			fonts[i] = new BitmapFont(Gdx.files.internal("fonts/Dimbo2.fnt"));

			//create atlas and add it to a new skin
			atlas = new TextureAtlas(Gdx.files.internal(Consumable.DropType.values()[i].getAtlasPath()));
			buttonSkin = new Skin();
			buttonSkin.addRegions(atlas);

			//create a Buttonstyle
			ButtonStyle inventoryButtonStyle = new ButtonStyle();
			inventoryButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
			inventoryButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

			//create a new button using aforementioned button style and set stuff up
			final Button inventoryButton = new Button(inventoryButtonStyle){
					@Override
					public float getPrefHeight(){
						return BUTTON_HEIGHT;
					};
					
					@Override
					public float getPrefWidth(){
						return BUTTON_WIDTH;
					}
			};
			//inventoryButton.setWidth(BUTTON_WIDTH/2);
			//inventoryButton.setHeight(BUTTON_HEIGHT/2);
			inventoryButton.getPrefHeight();
			inventoryButton.setX(Gdx.graphics.getWidth()/2 - BUTTON_WIDTH/2*(Consumable.DropType.values().length*2-1)/2 + BUTTON_WIDTH*i);
			inventoryButton.setY(Gdx.graphics.getHeight()/2);

			//add actor inventoryButton actor to the buttonStage
			buttonStage.addActor(inventoryButton);
			eatZone.setPosition(Gdx.graphics.getWidth()/2 - eatZone.getWidth()/2, 10);
			buttonStage.addActor(eatZone);

			//update the text for the corresponding item in the inventory
			updateInventoryScreenItems(i);
			dnd.addSource(new Source(inventoryButton){

				/**
				 * Overriding dragStart to initialize drag and drop payload
				 */
				@Override
				public Payload dragStart(InputEvent event, float x, float y,int pointer) {
					Payload payload = new Payload();
					if (gameScreen.getWorld().getPlayer().getInventory().removeItem(Consumable.DropType.values()[index])){
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
					if (target == null){
						gameScreen.getWorld().getPlayer().getInventory().addItem(new Consumable(Consumable.DropType.values()[index]));
					}
				}
			}
					);
		}
		//updateHealthBar();
	}

	/**
	 * Creates and maintains players health bar
	 */
	private void updateHealthBar() {
		batch.draw(black,Gdx.graphics.getWidth()/2 - .046f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - .073f*Gdx.graphics.getHeight(), .093f*Gdx.graphics.getWidth(), .037f*Gdx.graphics.getHeight());
		batch.draw(red,Gdx.graphics.getWidth()/2 - .0435f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - .069f*Gdx.graphics.getHeight(), 94*(gameScreen.getWorld().getPlayer().getHealth()/100), .028f*Gdx.graphics.getHeight());
	}

	/**
	 * Text representation of number of objects each inventory item has
	 * 
	 * @param consumableIndex - The Consumable.DropType index
	 */
	protected void updateInventoryScreenItems(int consumableIndex) {
		//get the number of items in the player's inventory for the given index
		batch.begin();
		fonts[consumableIndex].setColor(55,55,55,1f);
		fonts[consumableIndex].draw(batch,
				"x" + String.valueOf(gameScreen.getWorld().getPlayer().getInventory().getInventory().get(Consumable.DropType.values()[consumableIndex]).size),
				Gdx.graphics.getWidth()/2 - BUTTON_WIDTH/2*(Consumable.DropType.values().length*2-1)/2 + BUTTON_WIDTH*consumableIndex,
				Gdx.graphics.getHeight()/2 + 16);
		batch.end();
	}

	/**
	 * Overriding initializeButtons to create main buttons
	 */
	@Override
	protected void initializeButtons() {

		//LEAVE BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/inventoryScreen/leaveButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle leaveButtonStyle = new ButtonStyle();
		leaveButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		leaveButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		leaveButton = new Button(leaveButtonStyle);
		leaveButton.setWidth(BUTTON_WIDTH);
		leaveButton.setHeight(BUTTON_HEIGHT);
		leaveButton.setX(Gdx.graphics.getWidth() - BUTTON_WIDTH - EDGE_TOLERANCE);
		leaveButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - EDGE_TOLERANCE);

		//LISTENERS
		leaveButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.changeBackgroundVolume((float) .5);
				gameScreen.resetInputProcessors();
				gameInstance.setScreen(gameScreen);
				dispose();
			}
		}
				);

		buttonStage.addActor(leaveButton);
		inputMultiplexer.addProcessor(buttonStage);
	}

	@Override
	public void show() {}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
		super.dispose();
		black.dispose();
		red.dispose();
		imageTexture.dispose();
		for (BitmapFont font : fonts) {
			font.dispose();
		}
	}
}
