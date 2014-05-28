package com.tgco.animalBook.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.handlers.SoundHandler;

/**
 * Extends the button screen to play a sequence of tutorials and teach the player
 * the game and controls.
 * 
 * @author
 *
 * 
 */
public class TutorialScreen extends ButtonScreenAdapter implements Screen {

	/**
	 * Button used for the user interface
	 */
	private Button tutorialButton;

	/**
	 * Constructor that takes a game instance
	 * 
	 * @param gameInstance the currently running game instance
	 */
	public TutorialScreen(AnimalBookGame gameInstance) {
		super(gameInstance);

		//Background rendering
		batch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/gameScreenGrass2.jpg"));

		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	/**
	 * Renders the background and buttons
	 * 
	 * @param delta the time between frames
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		//render background
		batch.begin();
		batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();

		buttonStage.act(delta);
		buttonStage.draw();
	}

	/**
	 * Runs when the screen is resized to resize and relocate buttons
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
	 * Initializes all buttons that are part of the user interface
	 */
	@Override
	protected void initializeButtons() {

		//MARKET BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/tutorialScreen/ButtonSkip.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle leaveButtonStyle = new ButtonStyle();
		leaveButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		leaveButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		tutorialButton = new Button(leaveButtonStyle);
		tutorialButton.setWidth(BUTTON_WIDTH);
		tutorialButton.setHeight(BUTTON_HEIGHT);
		tutorialButton.setX(EDGE_TOLERANCE);
		tutorialButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - EDGE_TOLERANCE);


		//LISTENERS
		tutorialButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				gameInstance.setScreen(new GameScreen(gameInstance));
			}
		});


		buttonStage.addActor(tutorialButton);

		inputMultiplexer.addProcessor(buttonStage);
	}
	
	/**
	 * Disposed of objects that should release memory
	 */
	@Override
	public void dispose() {
		super.dispose();

	}

	/**
	 * Unused functions for screen events
	 */
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

}
