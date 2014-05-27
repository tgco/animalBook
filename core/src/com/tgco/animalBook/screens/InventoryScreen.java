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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.gameObjects.Consumable;
import com.tgco.animalBook.handlers.SoundHandler;
import com.tgco.animalBook.view.World;

public class InventoryScreen extends ButtonScreenAdapter implements Screen {
	
	private ShapeRenderer shapeRender;

	//reference to maintain player position
	private GameScreen gameScreen;

	//buttons
	private Button leaveButton;

	//inventory nums
	private static final BitmapFont[] fonts = new BitmapFont[Consumable.DropType.values().length];

	public InventoryScreen(AnimalBookGame gameInstance, GameScreen gameScreen) {
		super(gameInstance);

		this.gameScreen = gameScreen;

		//Background Rendering
		batch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/inventoryBackground.jpg"));

		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		shapeRender = new ShapeRenderer();
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//draw background
		batch.begin();
		batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();

		buttonStage.act(delta);
		buttonStage.draw();
		int numConsumables = Consumable.DropType.values().length;
		for (int i = 0; i < numConsumables; i ++){
			updateInventoryScreenItems(i);
		}
		updateHealthBar();
	}

	@Override
	public void resize(int width, int height) {
		if ( buttonStage == null)
			buttonStage = new Stage();
		buttonStage.clear();
		//reinit buttons
		initializeInventoryInterface();
		initializeButtons();
		updateHealthBar();

	}
	private void initializeInventoryInterface() {
		for (int i = 0; i < Consumable.DropType.values().length; i++){
			//initialize variables needed for listeners
			final int foodValue = Consumable.DropType.values()[i].getHungerValue();
			final int foodIndex = i;

			//associated BitmapFont object for consumable[i]
			fonts[i] = new BitmapFont();

			//create atlas and add it to a new skin
			atlas = new TextureAtlas(Gdx.files.internal(Consumable.DropType.values()[i].getAtlasPath()));
			buttonSkin = new Skin();
			buttonSkin.addRegions(atlas);

			//create a Buttonstyle
			ButtonStyle inventoryButtonStyle = new ButtonStyle();
			inventoryButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
			inventoryButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

			//create a new button using aforementioned button style and set stuff up
			Button inventoryButton = new Button(inventoryButtonStyle);
			inventoryButton.setWidth(BUTTON_WIDTH/2);
			inventoryButton.setHeight(BUTTON_HEIGHT/2);
			inventoryButton.setX(Gdx.graphics.getWidth()/2 - BUTTON_WIDTH/2*(Consumable.DropType.values().length*2-1)/2 + BUTTON_WIDTH*i);
			inventoryButton.setY(Gdx.graphics.getHeight()/2);

			//then add a listener to the button
			inventoryButton.addListener(new InputListener(){

				//for use in the new listener
				private int hungerValue = foodValue;
				private int consumableIndex = foodIndex;
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					return true;
				}
				public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

					//player eats when button of representing object gets eaten
					if(gameScreen.getWorld().getPlayer().getInventory().removeItem(Consumable.DropType.values()[consumableIndex])){
						gameScreen.getWorld().getPlayer().eat(hungerValue);
					}
				}
			});
			//add actor inventoryButton actor to the buttonStage
			buttonStage.addActor(inventoryButton);
			
			//update the text for the corresponding item in the inventory
			updateInventoryScreenItems(i);
			updateHealthBar();
		}
	}

	private void updateHealthBar() {
		shapeRender.begin(ShapeType.Filled);
		shapeRender.setColor(Color.BLACK);
		shapeRender.rect(Gdx.graphics.getWidth()/2 - 50, Gdx.graphics.getHeight() - 50, 100, 25);
		shapeRender.setColor(Color.RED);
		shapeRender.rect(Gdx.graphics.getWidth()/2 - 47, Gdx.graphics.getHeight() - 47, 94*(gameScreen.getWorld().getPlayer().getHealth()/100), 19);
		shapeRender.end();
	}

	protected void updateInventoryScreenItems(int consumableIndex) {
		//get the number of items in the player's inventory for the given index
		batch.begin();
		fonts[consumableIndex].setColor(55,55,55,1f);
		fonts[consumableIndex].draw(batch,
				String.valueOf(gameScreen.getWorld().getPlayer().getInventory().getInventory().get(Consumable.DropType.values()[consumableIndex]).size),
				Gdx.graphics.getWidth()/2 - BUTTON_WIDTH/2*(Consumable.DropType.values().length*2-1)/2 + BUTTON_WIDTH*consumableIndex,
				Gdx.graphics.getHeight()/2);
		batch.end();
	}

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
				//Grab the world
				World world = gameScreen.getWorld();
				gameInstance.setScreen(gameScreen);
			}
		});

		buttonStage.addActor(leaveButton);

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
		shapeRender.dispose();
	}

}
