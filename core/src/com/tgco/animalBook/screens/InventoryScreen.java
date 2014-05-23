package com.tgco.animalBook.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.gameObjects.Consumable;
import com.tgco.animalBook.gameObjects.Inventory;
import com.tgco.animalBook.gameObjects.Player;
import com.tgco.animalBook.handlers.SoundHandler;
import com.tgco.animalBook.view.World;

public class InventoryScreen extends ButtonScreenAdapter implements Screen {

	//reference to maintain player position
	private GameScreen gameScreen;

	//buttons
	private Button leaveButton;

	//inventory nums
	private static final BitmapFont[] fonts = new BitmapFont[5];

	public InventoryScreen(AnimalBookGame gameInstance, GameScreen gameScreen) {
		super(gameInstance);

		this.gameScreen = gameScreen;

		//Background Rendering
		batch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/inventoryBackground.jpg"));

		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
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
		for (int i = 0; i < Consumable.DropType.values().length; i ++){
			updateInventoryScreenItems(i);
		}
	}

	@Override
	public void resize(int width, int height) {
		if ( buttonStage == null)
			buttonStage = new Stage();
		buttonStage.clear();
		//reinit buttons
		initializeInventoryInterface();
		initializeButtons();
		
	}
	private void initializeInventoryInterface() {
		for (int i = 0; i < 5; i++){
			//Bad coding here
			final int foodValue = Consumable.DropType.values()[i].getHungerValue();
			final int foodIndex = i;
			
			atlas = new TextureAtlas(Gdx.files.internal(Consumable.DropType.values()[i].getAtlasPath()));
			buttonSkin = new Skin();
			buttonSkin.addRegions(atlas);
			
			ButtonStyle inventoryButtonStyle = new ButtonStyle();
			inventoryButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
			inventoryButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
			
			Button inventoryButton = new Button(inventoryButtonStyle);
			inventoryButton.setWidth(BUTTON_WIDTH/2);
			inventoryButton.setHeight(BUTTON_HEIGHT/2);
			inventoryButton.setX(2*i*BUTTON_WIDTH/2 + BUTTON_WIDTH/2);
			inventoryButton.setY(Gdx.graphics.getHeight()/2);

			inventoryButton.addListener(new InputListener(){
				private int hungerValue = foodValue;
				private int consumableIndex = foodIndex;
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					return true;
				}

				public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
					System.out.println("Attempting to consume: " + Consumable.DropType.values()[consumableIndex]);
					System.out.println("Player has: " + gameScreen.getWorld().getPlayer().getInventory().getInventory().get(Consumable.DropType.values()[consumableIndex]).size);
					System.out.println("Attempting to regain: " + hungerValue + " hunger.");
					if(gameScreen.getWorld().getPlayer().getInventory().removeItem(Consumable.DropType.values()[consumableIndex])){
						gameScreen.getWorld().getPlayer().eat(hungerValue);
						System.out.println("Player consumed: " + Consumable.DropType.values()[consumableIndex]);
						System.out.println("Player now has: " + gameScreen.getWorld().getPlayer().getInventory().getInventory().get(Consumable.DropType.values()[consumableIndex]).size);
					}
				}
			});
			buttonStage.addActor(inventoryButton);
			updateInventoryScreenItems(i);
		}
	}

	protected void updateInventoryScreenItems(int consumableIndex) {
		fonts[consumableIndex] = new BitmapFont();
		batch.begin();
		fonts[consumableIndex].setColor(55,55,55,1f);
		fonts[consumableIndex].draw(batch,
				String.valueOf(gameScreen.getWorld().getPlayer().getInventory().getInventory().get(Consumable.DropType.values()[consumableIndex]).size),
				2*consumableIndex*BUTTON_WIDTH/2 + BUTTON_WIDTH/2,
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

	}

}
