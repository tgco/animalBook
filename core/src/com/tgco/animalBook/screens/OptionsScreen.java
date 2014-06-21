package com.tgco.animalBook.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.handlers.SoundHandler;

public class OptionsScreen extends ButtonScreenAdapter implements Screen {

	//buttons
	private Button soundButton;
	private Button musicButton;
	private Button mainMenuButton;
	private Button helpButton;
	private Button resetButton;

	//Gamescreen
	GameScreen gameScreen;
	private boolean hasConfirm = false;
	private Stage popupStage;
	private Button kidCheck;
	private BitmapFont font;

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
		popupStage = new Stage();
		
		//Initialize rendering objects
		font = new BitmapFont(Gdx.files.internal("fonts/Dimbo2.fnt"));
		font.setScale(Gdx.graphics.getHeight()/750f);

		//Background Rendering
		batch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/options.png"));

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
		if(!hasConfirm){
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			//draw background
			batch.begin();
			batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.end();

			buttonStage.act(delta);
			buttonStage.draw();

			if(Gdx.input.isKeyPressed(Keys.BACK)){
				SoundHandler.playButtonClick();
				gameInstance.setScreen(new MainMenuScreen(gameInstance));
				dispose();
			}
		}else{
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			//draw background
			batch.begin();
			batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.end();

			buttonStage.act(delta);
			buttonStage.draw();
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
		initializeButtons();
	}

	@Override
	protected void initializeButtons() {

		//SOUND BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/optionsScreen/soundButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle soundButtonStyle = new ButtonStyle();

		if (SoundHandler.isSoundMuted()) {
			soundButtonStyle.up = buttonSkin.getDrawable("muted");
			soundButtonStyle.checked = buttonSkin.getDrawable("unmuted");
		} else {
			soundButtonStyle.up = buttonSkin.getDrawable("unmuted");
			soundButtonStyle.checked = buttonSkin.getDrawable("muted");
		}

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

		if (SoundHandler.isMusicMuted()) {
			musicButtonStyle.up = buttonSkin.getDrawable("muted");
			musicButtonStyle.checked = buttonSkin.getDrawable("unmuted");
		} else {
			musicButtonStyle.up = buttonSkin.getDrawable("unmuted");
			musicButtonStyle.checked = buttonSkin.getDrawable("muted");
		}

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

		//RESET BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/optionsScreen/resetButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle resetButtonStyle = new ButtonStyle();
		resetButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		resetButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		resetButton = new Button(resetButtonStyle);
		resetButton.setWidth(BUTTON_WIDTH);
		resetButton.setHeight(BUTTON_HEIGHT);
		resetButton.setX(Gdx.graphics.getWidth()/2 - BUTTON_WIDTH/2);
		resetButton.setY(Gdx.graphics.getHeight()/2 - BUTTON_HEIGHT/2);
		
		
		//KidMode BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/optionsScreen/kidCheck.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		CheckBoxStyle kidButtonStyle = new CheckBoxStyle();
		kidButtonStyle.font = font;
		kidButtonStyle.checkboxOn = buttonSkin.getDrawable("checked");
		kidButtonStyle.checkboxOff = buttonSkin.getDrawable("unchecked");

		kidCheck = new CheckBox("Kid Mode",kidButtonStyle);
		kidCheck.setWidth(BUTTON_WIDTH + 30);
		kidCheck.setHeight(BUTTON_HEIGHT/5f);
		kidCheck.setX(Gdx.graphics.getWidth()/2 - BUTTON_WIDTH/2);
		kidCheck.setY(Gdx.graphics.getHeight()/2 - 3*BUTTON_HEIGHT/2f);
		kidCheck.setChecked(gameInstance.isKidMode());

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
				if (gameScreen == null){
					SoundHandler.playButtonClick();
					gameInstance.setScreen(new MainMenuScreen(gameInstance));
					dispose();
				}
				else{
					SoundHandler.playButtonClick();
					gameScreen.resetInputProcessors();
					gameInstance.setScreen(gameScreen);
					dispose();
				}
			}
		});

		helpButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				gameInstance.setScreen(new HelpScreen(gameInstance, 0));
				dispose();
			}
		});

		resetButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				setDialog();
			}
		});
		
		kidCheck.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if(kidCheck.isChecked()){
					AnimalBookGame.setKidMode(true);
				}else {
					AnimalBookGame.setKidMode(false);
				}
				
				if(gameInstance.getLevelHandler() != null)
					gameInstance.getLevelHandler().updateKidMode(gameInstance.isKidMode());
			}
		});

		buttonStage.addActor(soundButton);
		buttonStage.addActor(musicButton);
		buttonStage.addActor(mainMenuButton);
		buttonStage.addActor(helpButton);

		buttonStage.addActor(resetButton);
		buttonStage.addActor(kidCheck);

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
		popupStage.dispose();
		font.dispose();
	}

	/**
	 * Sets the game to the confirm dialog when reset is pressed
	 * 
	 */
	public void setDialog(){
		hasConfirm  = true;
		Skin skin = new Skin(Gdx.files.internal("confirmSkin/uiskin.json"));
		ConfirmDialog resetD = new ConfirmDialog("RESET DATA", skin, gameInstance,"All progress will be lost. Are you sure you want to reset?", 2);
		resetD.show(popupStage);
		popupStage.addActor(resetD);
		inputMultiplexer.addProcessor(popupStage);
		inputMultiplexer.removeProcessor(buttonStage);
	}
	public void setExitDialog(){
		hasConfirm = false;
		inputMultiplexer.removeProcessor(popupStage);
		inputMultiplexer.addProcessor(buttonStage);
		popupStage = new Stage();	
	}

}
