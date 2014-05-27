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
import com.badlogic.gdx.math.Vector3;
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

public class MarketScreen extends ButtonScreenAdapter implements Screen {

	//reference to the game screen
	private GameScreen gameScreen;

	//buttons
	private Button leaveButton;
	private Button levelAnimalButton;
	
	private static final BitmapFont[] fonts = new BitmapFont[Consumable.DropType.values().length];
	private BitmapFont font;

	public MarketScreen(AnimalBookGame gameInstance, GameScreen gameScreen) {
		super(gameInstance);

		this.gameScreen = gameScreen;
		
		//Background rendering
		batch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/marketScreenBackground.png"));
		
		font = new BitmapFont(Gdx.files.internal("fonts/SketchBook.fnt"));

		SoundHandler.playMarketBackgroundMusic(true);

		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render(float delta) {
		int storedAnimal = gameScreen.getWorld().getLevelHandler().getStoredAmount();
		int needAnimals = gameScreen.getWorld().getLevelHandler().getPassLevelAmount();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		buttonStage.act(delta);

		//render background
		batch.begin();
		batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		//Draw money
		font.setColor(Color.WHITE);
		font.setScale(1.2f);
		font.draw(batch, "Your Money: $" + String.valueOf(gameScreen.getWorld().getPlayer().getPlayerMoney()), Gdx.graphics.getWidth()/2 , Gdx.graphics.getHeight() - 3*EDGE_TOLERANCE );
		
		font.setColor(Color.WHITE);
		font.draw(batch, String.valueOf(storedAnimal), Gdx.graphics.getWidth()/2 - BUTTON_WIDTH/2, Gdx.graphics.getHeight()/3 - BUTTON_HEIGHT - EDGE_TOLERANCE);
		font.draw(batch, "Need: " + String.valueOf(needAnimals), Gdx.graphics.getWidth()/2 - BUTTON_WIDTH/2, Gdx.graphics.getHeight()/3 - BUTTON_HEIGHT - EDGE_TOLERANCE - font.getCapHeight());
		
		batch.end();

		//Draw buttons
		buttonStage.draw();
		int numConsumables = Consumable.DropType.values().length;
		for (int i = 0; i < numConsumables; i ++){
			updateMarketScreenItems(i);
		}
	
	}

	@Override
	public void resize(int width, int height) {
		if ( buttonStage == null)
			buttonStage = new Stage();
		buttonStage.clear();
		//reinit buttons
		initializeMarketInterface();
		initializeButtons();
	}
	
	private void initializeMarketInterface() {
		for (int i = 0; i < Consumable.DropType.values().length; i++){
			//initialize variables needed for listeners
			final int marketValue = Consumable.DropType.values()[i].getMarketValue();
			final int foodIndex = i;

			//associated BitmapFont object for consumable[i]
			fonts[i] = new BitmapFont(Gdx.files.internal("fonts/SketchBook.fnt"));
			fonts[i].setColor(Color.CYAN);

			//create atlas and add it to a new skin
			atlas = new TextureAtlas(Gdx.files.internal(Consumable.DropType.values()[i].getAtlasPath()));
			buttonSkin = new Skin();
			buttonSkin.addRegions(atlas);

			//create a Buttonstyle
			ButtonStyle inventoryButtonStyle = new ButtonStyle();
			inventoryButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
			inventoryButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

			//create a new button using aforementioned button style and set stuff up
			Button marketButton = new Button(inventoryButtonStyle);
			marketButton.setWidth(BUTTON_WIDTH/2);
			marketButton.setHeight(BUTTON_HEIGHT/2);
			marketButton.setX(Gdx.graphics.getWidth()/2 - BUTTON_WIDTH/2*(Consumable.DropType.values().length*2-1)/2 + BUTTON_WIDTH*i);
			marketButton.setY(Gdx.graphics.getHeight()/1.5f);

			//then add a listener to the button
			marketButton.addListener(new InputListener(){

				//for use in the new listener
				private int consumableIndex = foodIndex;
				private int sellValue = marketValue;
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					return true;
				}
				public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

					//player eats when button of representing object gets eaten
					if(gameScreen.getWorld().getPlayer().getInventory().removeItem(Consumable.DropType.values()[consumableIndex])){
						gameScreen.getWorld().getPlayer().addPlayerMoney(sellValue);
					}
				}
			});
			//add actor inventoryButton actor to the buttonStage
			buttonStage.addActor(marketButton);
			
			//update the text for the corresponding item in the market
			updateMarketScreenItems(i);
		}
	}
	
	protected void updateMarketScreenItems(int consumableIndex) {
		//get the number of items in the player's inventory for the given index
		batch.begin();
		fonts[consumableIndex].setColor(55,55,55,1f);
		fonts[consumableIndex].draw(batch,
				String.valueOf(gameScreen.getWorld().getPlayer().getInventory().getInventory().get(Consumable.DropType.values()[consumableIndex]).size),
				Gdx.graphics.getWidth()/2 - BUTTON_WIDTH/2*(Consumable.DropType.values().length*2-1)/2 + BUTTON_WIDTH*consumableIndex,
				Gdx.graphics.getHeight()/1.5f);
		fonts[consumableIndex].draw(batch,
				String.valueOf(Consumable.DropType.values()[consumableIndex].getMarketValue()),
				Gdx.graphics.getWidth()/2 - BUTTON_WIDTH/2*(Consumable.DropType.values().length*2-1)/2 + BUTTON_WIDTH*consumableIndex,
				Gdx.graphics.getHeight()/1.5f - fonts[consumableIndex].getCapHeight());
		batch.end();
	}

	@Override
	protected void initializeButtons() {

		//MARKET BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/marketScreen/leaveButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle leaveButtonStyle = new ButtonStyle();
		leaveButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		leaveButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		leaveButton = new Button(leaveButtonStyle);
		leaveButton.setWidth(BUTTON_WIDTH);
		leaveButton.setHeight(BUTTON_HEIGHT);
		leaveButton.setX(EDGE_TOLERANCE);
		leaveButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - EDGE_TOLERANCE);
		
		//LEVEL ANIMAL BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/button.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		
		ButtonStyle levelAnimalButtonStyle = new ButtonStyle();
		levelAnimalButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		levelAnimalButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		levelAnimalButton = new Button(levelAnimalButtonStyle);
		levelAnimalButton.setWidth(BUTTON_WIDTH);
		levelAnimalButton.setHeight(BUTTON_HEIGHT);
		levelAnimalButton.setX(Gdx.graphics.getWidth()/2 - BUTTON_WIDTH/2);
		levelAnimalButton.setY(Gdx.graphics.getHeight()/3 - BUTTON_HEIGHT - EDGE_TOLERANCE);


		//LISTENERS
		leaveButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.pauseMarketBackgroundMusic();
				SoundHandler.playBackgroundMusic(true);
				gameScreen.resetInputProcessors();
				//Grab the world
				World world = gameScreen.getWorld();
				gameInstance.setScreen(new GameScreen(gameInstance));
			}
		});
		
		levelAnimalButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				if (gameScreen.getWorld().getLevelHandler().getStoredAmount() == 0) {
					return;
				} else {
					gameScreen.getWorld().getLevelHandler().decreaseStored();
				}
			}
		});


		buttonStage.addActor(leaveButton);
		buttonStage.addActor(levelAnimalButton);

		inputMultiplexer.addProcessor(buttonStage);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

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
