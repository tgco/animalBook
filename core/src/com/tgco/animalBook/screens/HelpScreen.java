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

public class HelpScreen extends ButtonScreenAdapter implements Screen {

	//buttons
	private Button backButton;
	private int fromScreen;
	private GameScreen gameScreen;
	
	/**
	 * Constructs a new Help Screen with a game instance
	 * <p>
	 * Initializes a new SpriteBatch for rendering objects. Initializes the proper texture to be used
	 * as the background. Initializes a new input multiplexer and processor to handle user inputs.
	 * 
	 * @param gameInstance the game instance to reference
	 * @param fromScreen the screen help is called fron (0, Options; 1, Game)
	 */
	public HelpScreen(AnimalBookGame gameInstance, int fromScreen) {
		super(gameInstance);
		this.fromScreen = fromScreen;
		
		//Background Rendering
		batch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/helpScreenBackground.png"));
		
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
	}
	
	public HelpScreen(AnimalBookGame gameInstance, GameScreen gameScreen, int fromScreen) {
		super(gameInstance);
		this.fromScreen = fromScreen;
		this.gameScreen = gameScreen;
		
		//Background Rendering
		batch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/helpScreenBackground.png"));
		
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
	}
	
	/**
	 * Renders the on screen objects.
	 * <p>
	 * First clears the screen of any previous objects that had been drawn. Then renders the background.
	 * Next renders all the buttons to be displayed.
	 * 
	 * @param delta the time between frames
	 */
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
		initializeButtons();
	}

	@Override
	protected void initializeButtons() {
		
		//back BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/helpScreen/backButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		
		ButtonStyle backButtonStyle = new ButtonStyle();
		
		backButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		backButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		
		backButton = new Button(backButtonStyle);
		backButton.setWidth(BUTTON_WIDTH);
		backButton.setHeight(BUTTON_HEIGHT);
		backButton.setX(Gdx.graphics.getWidth() - BUTTON_WIDTH - EDGE_TOLERANCE);
		backButton.setY(EDGE_TOLERANCE);
		
		//LISTENERS
		backButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				switch(fromScreen){
					case 0:
						gameInstance.setScreen(new OptionsScreen(gameInstance));
						break;
					case 1:
						gameInstance.setScreen(gameScreen);
						gameScreen.resetInputProcessors();
						gameScreen.setAlexButton(true);
						gameScreen.handleMainMenu(true);
						gameScreen.handleOptionsMenu(true);
						break;
				}
				//gameInstance.setScreen(new OptionsScreen(gameInstance));
				dispose();
			}
		});
		
		buttonStage.addActor(backButton);
		
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

	/**
	 * Disposes of all objects contained in the options screen.
	 */
	@Override
	public void dispose() {
		super.dispose();
		
	}

}
