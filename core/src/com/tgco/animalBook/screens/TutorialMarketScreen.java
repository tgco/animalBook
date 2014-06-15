package com.tgco.animalBook.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.gameObjects.Consumable;
import com.tgco.animalBook.handlers.SoundHandler;

public class TutorialMarketScreen extends ButtonScreenAdapter implements Screen {

	//reference to the game screen
	private TutorialScreen tutorialScreen;

	//buttons
	private Button levelAnimalButton;
	private Button nextLevelAnimalButton;
	private Button playButton;

	private static final Label[] labels = new Label[Consumable.DropType.values().length];
	private BitmapFont font;

	/**
	 * Stage to draw the screen once the user has pressed the back button
	 */
	private Stage popupStage;

	private boolean hasConfirm = false;
	private final float FONT_SCALE = Gdx.graphics.getHeight()/750f;
	private Label infoLabel;

	private Image alexInfoImage;
	
	/**
	 * textures for health bar, etc
	 */
	private Texture black = new Texture(Gdx.files.internal("primitiveTextures/black.png"));
	private Texture red = new Texture(Gdx.files.internal("primitiveTextures/red.png"));
	private Texture yellow = new Texture(Gdx.files.internal("primitiveTextures/yellow.png"));
	private Texture green = new Texture(Gdx.files.internal("primitiveTextures/green.png"));

	private HorizontalGroup consumablesStatusGroup;

	private Image ConsumablesStatusGroupImage;

	private Label consumeLabel;

	private HorizontalGroup consumablesLabelGroup;

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
	public TutorialMarketScreen(AnimalBookGame gameInstance, TutorialScreen tutorialScreen) {
		super(gameInstance);
		popupStage = new Stage();
		this.tutorialScreen = tutorialScreen;

		//Background rendering
		batch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/tutorialMarketScreenBackground.png"));

		font = new BitmapFont(Gdx.files.internal("fonts/Dimbo2.fnt"));

		//consumableStatusGroup
		consumablesStatusGroup = new HorizontalGroup();
		consumablesStatusGroup.center();
		consumablesStatusGroup.space(EDGE_TOLERANCE);

		consumablesLabelGroup = new HorizontalGroup();
		consumablesLabelGroup.center();
		consumablesLabelGroup.space(.69f*BUTTON_WIDTH);
		
		//fruitfulLabel
		LabelStyle consumeLabelStyle = new LabelStyle();
		consumeLabelStyle.font = font;
		//upgradeLabelStyle.fontColor = Color.WHITE;

		consumeLabel = new Label(
				"\n" +
						"Your Amount: \n" +
						"Buy Amount: \n"
						, 
						consumeLabelStyle);
		consumeLabel.setAlignment(Align.left);
		
		for(int i=0; i<Consumable.DropType.values().length; ++i){
			labels[i] = new Label("0 \n 0 \n", consumeLabelStyle);
			labels[i].setAlignment(Align.right);
		}
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
		if(!hasConfirm){
			int storedAnimal = gameInstance.getLevelHandler().getStoredAmount();
			int nextLevel ;
			if(gameInstance.getLevelHandler().getLevel() == 5){
				if(gameInstance.getLevelHandler().getNextLevelStart() >= 25)
					nextLevel = 1;
				else
					nextLevel =0;
			}else{
				nextLevel = gameInstance.getLevelHandler().getNextLevelStart();
			}
				
			int needAnimals = gameInstance.getLevelHandler().getPassLevelAmount() - (gameInstance.getLevelHandler().getNextLevelStart());
			if(needAnimals <=0){
				needAnimals =0;
			}
			String needAnimalsString = "Need " + String.valueOf(needAnimals) + " more";

			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			buttonStage.act(delta);

			//render background
			batch.begin();
			batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

			
			font.setColor(Color.WHITE);
			font.draw(batch, String.valueOf(storedAnimal), Gdx.graphics.getWidth()/3 - BUTTON_WIDTH/2, Gdx.graphics.getHeight()/3 - EDGE_TOLERANCE);
			font.draw(batch, String.valueOf(nextLevel), Gdx.graphics.getWidth()/1.5f - BUTTON_WIDTH/2, Gdx.graphics.getHeight()/3 - EDGE_TOLERANCE);
			font.draw(batch, needAnimalsString, Gdx.graphics.getWidth()/2 - (needAnimalsString.length()*5f)/2, Gdx.graphics.getHeight()/3 - EDGE_TOLERANCE - font.getCapHeight());

			infoLabel.setText("Money: $" + tutorialScreen.getWorld().getPlayer().getPlayerMoney());
			batch.end();

			//Draw buttons
			buttonStage.draw();
			int numConsumables = Consumable.DropType.values().length;
			for (int i = 0; i < numConsumables; i ++){
				updateMarketScreenItems(i);
			}

			if(Gdx.input.isKeyPressed(Keys.BACK)){
				setDialog();
			}

			batch.begin();
			//Draw health bar (test)
			batch.draw(black,tutorialScreen.alexsPosition().x + 1.5f*EDGE_TOLERANCE, tutorialScreen.alexsPosition().y - 1.5f*EDGE_TOLERANCE, 10.2f*EDGE_TOLERANCE, 1.1f*EDGE_TOLERANCE);
			if (tutorialScreen.getWorld().getPlayer().getHealth()/100f > .50f)	
				batch.draw(green,tutorialScreen.alexsPosition().x + 1.6f*EDGE_TOLERANCE, tutorialScreen.alexsPosition().y
						- 1.5f*EDGE_TOLERANCE, 10f*EDGE_TOLERANCE*(tutorialScreen.getWorld().getPlayer().getHealth()/100f), EDGE_TOLERANCE);
			else if (tutorialScreen.getWorld().getPlayer().getHealth()/100f > .25f)
				batch.draw(yellow,tutorialScreen.alexsPosition().x + 1.6f*EDGE_TOLERANCE,tutorialScreen.alexsPosition().y
						- 1.5f*EDGE_TOLERANCE, 10f*EDGE_TOLERANCE*(tutorialScreen.getWorld().getPlayer().getHealth()/100f), EDGE_TOLERANCE);
			else
				batch.draw(red,tutorialScreen.alexsPosition().x + 1.6f*EDGE_TOLERANCE,tutorialScreen.alexsPosition().y
						- 1.5f*EDGE_TOLERANCE, 10f*EDGE_TOLERANCE*(tutorialScreen.getWorld().getPlayer().getHealth()/100f), EDGE_TOLERANCE);

	
			batch.end();
		}
		else{
			popupStage.act(delta);
			popupStage.draw();
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
					if(tutorialScreen.getWorld().getPlayer().getInventory().removeItem(Consumable.DropType.values()[consumableIndex])){
						tutorialScreen.getWorld().getPlayer().addPlayerMoney(sellValue);
					}
				}
			});
			//add actor inventoryButton actor to the buttonStage
			buttonStage.addActor(marketButton);

			//update the text for the corresponding item in the market
			updateMarketScreenItems(i);
		}
		//pack labels
		consumablesStatusGroup.addActor(consumeLabel);
		
		for(int i=0; i< Consumable.DropType.values().length; ++i){
			consumablesLabelGroup.addActor(labels[i]);
		}
		
		consumablesStatusGroup.pack();
		consumablesStatusGroup.setPosition(Gdx.graphics.getWidth()/2 - BUTTON_WIDTH/2*(Consumable.DropType.values().length*2-1)/2 - 1f*BUTTON_WIDTH,
				Gdx.graphics.getHeight()/1.5f -EDGE_TOLERANCE*.5f - .7f*BUTTON_HEIGHT);
		
		consumablesLabelGroup.pack();
		consumablesLabelGroup.setPosition(Gdx.graphics.getWidth()/2 - BUTTON_WIDTH/2*(Consumable.DropType.values().length*2-1)/2 + .05f*BUTTON_WIDTH,
				Gdx.graphics.getHeight()/1.5f -EDGE_TOLERANCE*.5f - .5f*BUTTON_HEIGHT);
		
		consumablesStatusGroup.setSize(Consumable.DropType.values().length*5f*BUTTON_WIDTH + EDGE_TOLERANCE*2f + .5f*BUTTON_WIDTH,
				 BUTTON_HEIGHT + EDGE_TOLERANCE*2f);
		ConsumablesStatusGroupImage = new Image(new Texture(Gdx.files.internal("backgrounds/menuBackground.png")));
		buttonStage.addActor(ConsumablesStatusGroupImage);
		
		ConsumablesStatusGroupImage.setPosition(Gdx.graphics.getWidth()/2 - BUTTON_WIDTH/2*(Consumable.DropType.values().length*2-1)/2 - 1f*BUTTON_WIDTH,
				Gdx.graphics.getHeight()/1.5f - .2f*EDGE_TOLERANCE - .5f*BUTTON_HEIGHT);
		ConsumablesStatusGroupImage.setSize(Consumable.DropType.values().length*1f*BUTTON_WIDTH + EDGE_TOLERANCE*2f + .5f*BUTTON_WIDTH,
				 BUTTON_HEIGHT + EDGE_TOLERANCE*2f);
		ConsumablesStatusGroupImage.toBack();
		
		buttonStage.addActor(consumablesStatusGroup);
		buttonStage.addActor(consumablesLabelGroup);
		consumablesLabelGroup.invalidate();
	}

	protected void updateMarketScreenItems(int consumableIndex) {
		//get the number of items in the player's inventory for the given index
		
		labels[consumableIndex].setText(String.valueOf(tutorialScreen.getWorld().getPlayer().getInventory().getInventory().get(Consumable.DropType.values()[consumableIndex]).size) + " \n $" +
						String.valueOf(Consumable.DropType.values()[consumableIndex].getMarketValue()) + " \n");
	}

	@Override
	protected void initializeButtons() {
		//PLAY BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/tutorialScreen/playButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle playButtonStyle = new ButtonStyle();
		playButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		playButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		playButton = new Button(playButtonStyle);
		playButton.setWidth(BUTTON_WIDTH);
		playButton.setHeight(BUTTON_HEIGHT);
		playButton.setX(Gdx.graphics.getWidth() - BUTTON_WIDTH - EDGE_TOLERANCE);
		playButton.setY(EDGE_TOLERANCE);
		
		//CURRENT LEVEL ANIMAL BUTTON
		atlas = new TextureAtlas(gameInstance.getLevelHandler().currentLevelTexture());
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle levelAnimalButtonStyle = new ButtonStyle();
		levelAnimalButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		levelAnimalButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		levelAnimalButton = new Button(levelAnimalButtonStyle);
		levelAnimalButton.setWidth(BUTTON_WIDTH);
		levelAnimalButton.setHeight(BUTTON_HEIGHT);
		levelAnimalButton.setX(Gdx.graphics.getWidth()/3 - BUTTON_WIDTH/2);
		levelAnimalButton.setY(Gdx.graphics.getHeight()/3 - EDGE_TOLERANCE);

		//NEXT LEVEL ANIMAL BUTTON
		atlas = new TextureAtlas(gameInstance.getLevelHandler().nextLevelTexture());
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle nextLevelAnimalButtonStyle = new ButtonStyle();
		nextLevelAnimalButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		nextLevelAnimalButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		nextLevelAnimalButton = new Button(nextLevelAnimalButtonStyle);
		nextLevelAnimalButton.setWidth(BUTTON_WIDTH);
		nextLevelAnimalButton.setHeight(BUTTON_HEIGHT);
		nextLevelAnimalButton.setX(Gdx.graphics.getWidth()/1.5f - BUTTON_WIDTH/2);
		nextLevelAnimalButton.setY(Gdx.graphics.getHeight()/3 - EDGE_TOLERANCE);

		//LISTENERS
		playButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.pauseMarketBackgroundMusic();
				SoundHandler.playBackgroundMusic(true);	
				gameInstance.getLevelHandler().setDoTutorial(false);
				tutorialScreen.dispose();
				gameInstance.setScreen(new GameScreen(gameInstance));
				dispose();
			}
		});
		
		levelAnimalButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				if (gameInstance.getLevelHandler().getStoredAmount() == 0) {
					return;
				} else {
					gameInstance.getLevelHandler().decreaseStored();
					gameInstance.getLevelHandler().increaseNextLevelStart();
				}
			}
		});
		
		buttonStage.addActor(playButton);
		buttonStage.addActor(levelAnimalButton);
		buttonStage.addActor(nextLevelAnimalButton);
		
		//Information Label
		LabelStyle infoLabelStyle = new LabelStyle();
		infoLabelStyle.font = font;
		infoLabelStyle.fontColor = Color.WHITE;
		infoLabelStyle.font.setScale(FONT_SCALE);
		infoLabel = new Label("Money: $" + tutorialScreen.getWorld().getPlayer().getPlayerMoney(), infoLabelStyle);
		infoLabel.pack();
		infoLabel.setPosition(tutorialScreen.alexsPosition().x + 1.5f*EDGE_TOLERANCE,
				tutorialScreen.alexsPosition().y - infoLabel.getHeight() - 1.7f*EDGE_TOLERANCE);
		infoLabel.setAlignment(Align.left);
		
		//The label's background image
		alexInfoImage = new Image(new Texture(Gdx.files.internal("backgrounds/menuBackground.png")));
		alexInfoImage.setSize(11f*EDGE_TOLERANCE, tutorialScreen.alexsPosition().y -infoLabel.getY() + .5f*EDGE_TOLERANCE);
		alexInfoImage.setPosition(infoLabel.getX() - .5f*EDGE_TOLERANCE,
				infoLabel.getY() - .5f*EDGE_TOLERANCE);
		buttonStage.addActor(alexInfoImage);
		buttonStage.addActor(infoLabel);
		

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
		font.dispose();
		
		red.dispose();
		green.dispose();
		black.dispose();
		yellow.dispose();
	}

	/**
	 * Sets the game to the confirm dialog when back is pressed
	 * 
	 */
	public void setDialog(){
		hasConfirm  = true;
		Skin skin = new Skin(Gdx.files.internal("confirmSkin/uiskin.json"));
		ConfirmDialog backD = new ConfirmDialog("Back Button Pressed", skin, gameInstance,"Please click  \"retry\" or \"Next Level\" button to save Progress", 0);
		backD.show(popupStage);
		popupStage.addActor(backD);
		inputMultiplexer.addProcessor(popupStage);
		inputMultiplexer.removeProcessor(buttonStage);
	}
	public void setCancel(){
		hasConfirm = false;
		inputMultiplexer.removeProcessor(popupStage);
		inputMultiplexer.addProcessor(buttonStage);
		popupStage = new Stage();	
	}
}
