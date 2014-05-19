package com.tgco.animalBook.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.Handlers.SoundHandler;

public class OptionsScreen extends ButtonScreenAdapter implements Screen {

	//buttons
	private Button soundButton;
	private Button musicButton;
	private Button mainMenuButton;
	private Button helpButton;
	
	//dimensions
	private final float BUTTON_WIDTH = 100;
	private final float BUTTON_HEIGHT = 100;
	
	//Input handler
	private InputMultiplexer inputMultiplexer;
	
	//Background
	private SpriteBatch batch;
	
	
	public OptionsScreen(AnimalBookGame gameInstance) {
		super(gameInstance);
		
		//Background Rendering
		batch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/optionsBackground.jpg"));
		
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
	}

	@Override
	public void resize(int width, int height) {
		if ( buttonStage == null)
			buttonStage = new Stage();
		buttonStage.clear();
		//reinit buttons
		initializeButtons();
	}

	private void initializeButtons() {
		
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
		soundButton.setX(Gdx.graphics.getWidth() - BUTTON_WIDTH - 20);
		soundButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - 20);
		
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
		musicButton.setX(20);
		musicButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - 20);
		
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
		mainMenuButton.setX(Gdx.graphics.getWidth() - BUTTON_WIDTH - 20);
		mainMenuButton.setY(0 + BUTTON_HEIGHT - 20);
		
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
		helpButton.setX(20);
		helpButton.setY(0 + BUTTON_HEIGHT - 20);
		
		//LISTENERS
		soundButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
			
			}
		});
		
		musicButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
			
			}
		});
		
		mainMenuButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				gameInstance.setScreen(new MainMenuScreen(gameInstance));
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

	@Override
	public void dispose() {
		super.dispose();
		
	}

}
