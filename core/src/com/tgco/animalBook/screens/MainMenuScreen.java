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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.handlers.SoundHandler;

public class MainMenuScreen extends ButtonScreenAdapter implements Screen {
	private static final float BUTTON_WIDTH = 200;
	private static final float BUTTON_HEIGHT = 200;

	private Button playButton;
	private Button optionsButton;
	private Button testButton;

	private InputMultiplexer inputMultiplexer;
	private SpriteBatch batch;

	public MainMenuScreen(AnimalBookGame gameInstance) {
		super(gameInstance);
		
		//Background rendering
		batch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/mainback.png"));
		
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);

	}

	@Override
	public void render(float delta) {
		//clear screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//render background
		batch.begin();
		batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
				
		//process button input
		buttonStage.act(delta);

		//draw button stage on top of images in the batch
		buttonStage.draw();


	}

	@Override
	public void resize(int width, int height) {
		if (buttonStage == null)
			buttonStage = new Stage();
		buttonStage.clear();
		initializeButtons();
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

	//Sets up the menu buttons and adds them to the stage to be rendered
	public void initializeButtons() {
		atlas = new TextureAtlas(Gdx.files.internal("buttons/mainMenu/button.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		//Design button style
		ButtonStyle style = new ButtonStyle();
		style.up = buttonSkin.getDrawable("buttonUnpressed");
		style.down = buttonSkin.getDrawable("buttonPressed");

		//Create buttons from style
		playButton = new Button(style);
		playButton.setWidth(BUTTON_WIDTH);
		playButton.setHeight(BUTTON_HEIGHT);
		playButton.setX(Gdx.graphics.getWidth()/2 - BUTTON_WIDTH/2);
		playButton.setY(Gdx.graphics.getHeight()/2);


		atlas = new TextureAtlas(Gdx.files.internal("buttons/mainMenu/buttonO.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		style = new ButtonStyle();
		style.up = buttonSkin.getDrawable("buttonUnpressed");
		style.down = buttonSkin.getDrawable("buttonPressed");

		optionsButton = new Button(style);
		optionsButton.setWidth(BUTTON_WIDTH);
		optionsButton.setHeight(BUTTON_HEIGHT);
		optionsButton.setX(Gdx.graphics.getWidth()/2 - BUTTON_WIDTH/2);
		optionsButton.setY(Gdx.graphics.getHeight()/2 - BUTTON_HEIGHT - 20);
		
		//Test
		//This button is just to test the story screen
		atlas = new TextureAtlas(Gdx.files.internal("buttons/button.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		style = new ButtonStyle();
		style.up = buttonSkin.getDrawable("buttonUnpressed");
		style.down = buttonSkin.getDrawable("buttonPressed");

		testButton = new Button(style);
		testButton.setWidth(BUTTON_WIDTH);
		testButton.setHeight(BUTTON_HEIGHT);
		testButton.setX(0);
		testButton.setY(0);

		//Create listeners
		playButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				//Change the screen when the button is let go
				gameInstance.setScreen(new GameScreen(gameInstance));
			}
		});

		optionsButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				//Change the screen when the button is let go
				gameInstance.setScreen(new OptionsScreen(gameInstance));
			}
		});
		
		testButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.pauseBackgroundMusic();
				//Change the screen when the button is let go
				gameInstance.setScreen(new StoryScreen(gameInstance));
			}
		});

		//Add to stage
		buttonStage.addActor(playButton);
		buttonStage.addActor(optionsButton);
		buttonStage.addActor(testButton);

		inputMultiplexer.addProcessor(buttonStage);
	}

}
