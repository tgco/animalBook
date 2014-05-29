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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.gameObjects.Consumable;
import com.tgco.animalBook.handlers.SoundHandler;

public class MarketScreen extends ButtonScreenAdapter implements Screen {

	//reference to the game screen
	private GameScreen gameScreen;

	//buttons
	private Button nextLevelButton;
	private Button levelAnimalButton;
	private Button nextLevelAnimalButton;
	
	private static final BitmapFont[] fonts = new BitmapFont[Consumable.DropType.values().length];
	private BitmapFont font;

	/**
	 * Constructs a new Market Screen with a game instance and a game screen.
	 * <p>
	 * Initializes a new SpriteBatch for rendering objects. Initializes the proper texture to be displayed
	 * as the background. Initializes a new BitmapFont using a custom font located in the assets folder.
	 * Starts playing the background music. Initializes a new input multiplexer and processor to handle
	 * user inputs.
	 * 
	 * @param gameInstance the game instance to reference
	 * @param gameScreen the game screen to reference
	 */
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
	
	/**
	 * Renders the on screen objects.
	 * <p>
	 * First clears the screen of any previous objects that had been drawn. Then renders the background texture
	 * and the player's current money, as well as the required amount of animals to progress to the next level.
	 * Next renders all of the buttons to be displayed, as well as updates the screen to show how many of each
	 * consumable item the player has in his/her inventory.
	 * </p>
	 * @param delta the time between frames
	 */
	@Override
	public void render(float delta) {
		int storedAnimal = gameScreen.getWorld().getLevelHandler().getStoredAmount();
		int nextLevel = gameScreen.getWorld().getLevelHandler().getNextLevelStart();
		int needAnimals = gameScreen.getWorld().getLevelHandler().getPassLevelAmount();
		String needAnimalsString = "Need: " + String.valueOf(needAnimals);
		
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
		font.draw(batch, String.valueOf(storedAnimal), Gdx.graphics.getWidth()/3 - BUTTON_WIDTH/2, Gdx.graphics.getHeight()/3 - BUTTON_HEIGHT - EDGE_TOLERANCE);
		font.draw(batch, String.valueOf(nextLevel), Gdx.graphics.getWidth()/1.5f - BUTTON_WIDTH/2, Gdx.graphics.getHeight()/3 - BUTTON_HEIGHT - EDGE_TOLERANCE);
		font.draw(batch, needAnimalsString, Gdx.graphics.getWidth()/2 - (needAnimalsString.length()*5f)/2, Gdx.graphics.getHeight()/3 - BUTTON_HEIGHT - EDGE_TOLERANCE - font.getCapHeight());
		
		batch.end();

		//Draw buttons
		buttonStage.draw();
		int numConsumables = Consumable.DropType.values().length;
		for (int i = 0; i < numConsumables; i ++){
			updateMarketScreenItems(i);
		}
	
	}
	
	/**
	 * Redraws the screen to scale everything properly when the application window is resized.
	 * <p>
	 * Creates new button stage if there is none, then clears the stage to prepare for redrawing. Then
	 * reinitializes the market interface and the buttons.
	 * 
	 * @param width the width of the resized screen
	 * @param height the height of the resized screen
	 */
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
			ButtonStyle marketButtonStyle = new ButtonStyle();
			marketButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
			marketButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

			//create a new button using aforementioned button style and set stuff up
			Button marketButton = new Button(marketButtonStyle);
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

		//NEXT LEVEL BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/marketScreen/nextLevelButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle nextLevelButtonStyle = new ButtonStyle();
		nextLevelButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		nextLevelButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		nextLevelButton = new Button(nextLevelButtonStyle);
		nextLevelButton.setWidth(BUTTON_WIDTH);
		nextLevelButton.setHeight(BUTTON_HEIGHT);
		nextLevelButton.setX(Gdx.graphics.getWidth() - BUTTON_WIDTH - EDGE_TOLERANCE);
		nextLevelButton.setY(EDGE_TOLERANCE);
		
		//CURRENT LEVEL ANIMAL BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("objectTextures/gooseButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		
		ButtonStyle levelAnimalButtonStyle = new ButtonStyle();
		levelAnimalButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		levelAnimalButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		levelAnimalButton = new Button(levelAnimalButtonStyle);
		levelAnimalButton.setWidth(BUTTON_WIDTH);
		levelAnimalButton.setHeight(BUTTON_HEIGHT);
		levelAnimalButton.setX(Gdx.graphics.getWidth()/3 - BUTTON_WIDTH/2);
		levelAnimalButton.setY(Gdx.graphics.getHeight()/3 - BUTTON_HEIGHT - EDGE_TOLERANCE);
		
		//LEVEL ANIMAL BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("objectTextures/pigButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
				
		ButtonStyle nextLevelAnimalButtonStyle = new ButtonStyle();
		nextLevelAnimalButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		nextLevelAnimalButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		nextLevelAnimalButton = new Button(nextLevelAnimalButtonStyle);
		nextLevelAnimalButton.setWidth(BUTTON_WIDTH);
		nextLevelAnimalButton.setHeight(BUTTON_HEIGHT);
		nextLevelAnimalButton.setX(Gdx.graphics.getWidth()/1.5f - BUTTON_WIDTH/2);
		nextLevelAnimalButton.setY(Gdx.graphics.getHeight()/3 - BUTTON_HEIGHT - EDGE_TOLERANCE);

		//LISTENERS
		nextLevelButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.pauseMarketBackgroundMusic();
				SoundHandler.playBackgroundMusic(true);
				gameScreen.dispose();
				//Grab the world
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
					gameScreen.getWorld().getLevelHandler().increaseNextLevelStart();
				}
			}
		});
		buttonStage.addActor(nextLevelButton);
		buttonStage.addActor(levelAnimalButton);
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

	/**
	 * Disposes of all objects contained in the market screen.
	 */
	@Override
	public void dispose() {
		super.dispose();
	}

}
