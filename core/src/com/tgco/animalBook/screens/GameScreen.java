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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Array;
import com.tgco.animalBook.AnimalBookGame;
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
	private Button alexButton;

	private VerticalGroup menuGroup;
	private Image menuGroupImage;

	private HorizontalGroup inventoryGroup;
	private HorizontalGroup upgradeGroup;
	private HorizontalGroup optionGroup;

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
				handleMenu(alexButton.isChecked());
			}
		});

		//initialize all menuGroup buttons
		menuGroup = new VerticalGroup();
		menuGroup.left();
		menuGroup.setPosition(alexButton.getX(), alexButton.getY());
		menuGroup.setColor(Color.BLACK);
		menuGroup.setSize(alexButton.getWidth(), Gdx.graphics.getHeight());
		
		menuGroupImage = new Image(new Texture(Gdx.files.internal("backgrounds/menuBackground.png")));
		menuGroupImage.setPosition(alexButton.getX(), 0);
		menuGroupImage.setSize(alexButton.getWidth(), alexButton.getY());
		
		inventoryGroup = new HorizontalGroup();

		upgradeGroup = new HorizontalGroup();

		optionGroup = new HorizontalGroup();

		





		buttonStage.addActor(alexButton);
		inputMultiplexer.addProcessor(buttonStage);
	}

	public void handleMenu(boolean checked) {
		if (checked){
			buttonStage.addActor(menuGroupImage);
			buttonStage.addActor(menuGroup);
		}
		else{
			menuGroupImage.remove();
			menuGroup.remove();
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

	public Array<Actor> getScreenActors() {
		return buttonStage.getActors();
	}

}
