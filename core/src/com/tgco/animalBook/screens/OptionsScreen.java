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

public class OptionsScreen extends ButtonScreenAdapter implements Screen {

	//buttons
	private Button soundButton;
	private Button musicButton;
	private Button mainMenuButton;
	private Button helpButton;
	
	/**
	 * Constructs a new Options Screen with a game instance
	 * <p>
	 * Initializes a new SpriteBatch for rendering objects. Initializes the proper texture to be used
	 * as the background. Initializes a new input multiplexer and processor to handle user inputs.
	 * 
	 * @param gameInstance the game instance to reference
	 */
	public OptionsScreen(AnimalBookGame gameInstance) {
		super(gameInstance);
		
		//Background Rendering
		batch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/optionsBackground.jpg"));
		
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
		
		//SOUND BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/optionsScreen/soundButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		
		ButtonStyle soundButtonStyle = new ButtonStyle();
		soundButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		soundButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		
		soundButton = new Button(soundButtonStyle);
		soundButton.setWidth(BUTTON_WIDTH);
		soundButton.setHeight(BUTTON_HEIGHT);
		soundButton.setX(Gdx.graphics.getWidth() - BUTTON_WIDTH - EDGE_TOLERANCE);
		soundButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - EDGE_TOLERANCE);
		
		//MUSIC BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/optionsScreen/musicButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		
		ButtonStyle musicButtonStyle = new ButtonStyle();
		musicButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		musicButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		
		musicButton = new Button(musicButtonStyle);
		musicButton.setWidth(BUTTON_WIDTH);
		musicButton.setHeight(BUTTON_HEIGHT);
		musicButton.setX(EDGE_TOLERANCE);
		musicButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - EDGE_TOLERANCE);
		
		//MAIN MENU BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/optionsScreen/mainMenuButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		
		ButtonStyle mainMenuButtonStyle = new ButtonStyle();
		mainMenuButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		mainMenuButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
				
		mainMenuButton = new Button(mainMenuButtonStyle);
		mainMenuButton.setWidth(BUTTON_WIDTH);
		mainMenuButton.setHeight(BUTTON_HEIGHT);
		mainMenuButton.setX(Gdx.graphics.getWidth() - BUTTON_WIDTH - EDGE_TOLERANCE);
		mainMenuButton.setY(EDGE_TOLERANCE);
		
		//HELP BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/optionsScreen/helpButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		
		ButtonStyle helpButtonStyle = new ButtonStyle();
		helpButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		helpButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
				
		helpButton = new Button(helpButtonStyle);
		helpButton.setWidth(BUTTON_WIDTH);
		helpButton.setHeight(BUTTON_HEIGHT);
		helpButton.setX(EDGE_TOLERANCE);
		helpButton.setY(EDGE_TOLERANCE);
		
		//LISTENERS
		soundButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.toggleSounds();
			
			}
		});
		
		musicButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.toggleMusic();
			
			}
		});
		
		mainMenuButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				gameInstance.setScreen(new MainMenuScreen(gameInstance));
				dispose();
			}
		});
		
		helpButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
			
			}
		});
		
		buttonStage.addActor(soundButton);
		buttonStage.addActor(musicButton);
		buttonStage.addActor(mainMenuButton);
		buttonStage.addActor(helpButton);
		
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
