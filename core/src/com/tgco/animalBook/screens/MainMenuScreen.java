/**
 * File : MainMenuScreen.java
 * The menu screen is after the splash screen and has 2 buttons; option and play the game. Exit through this screen.
 * 
 */
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

public class MainMenuScreen extends ButtonScreenAdapter implements Screen {
	
	/**
	 * buttons for the user to click on 
	 */
	private Button playButton;
	private Button optionsButton;
	private Button continueButton;

	public MainMenuScreen(AnimalBookGame gameInstance) {
		super(gameInstance);
		
		//Background rendering
		batch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/mainback.png"));
		
		//libgdx should not catch the back key, the device should catch the back button
		Gdx.input.setCatchBackKey(false);
		
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);

	}

	/** render draws everything for the screen at every frame
	 * 
	 * @param delta the time between two frames
	 */
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

	/**
	 * resize is called at the begining of launch and also when ever the screen gets resized.
	 * @param width the width of the new size
	 * @param height the height of the new size
	 */
	@Override
	public void resize(int width, int height) {
		if (buttonStage == null)
			buttonStage = new Stage();
		buttonStage.clear();
		initializeButtons();
	}

	/**
	 * the show, hide, pause, resume buttons are not being used becuase
	 *  the life cycle of the screen is not needed at this point.
	 */
	@Override
	public void show() {}

	@Override
	public void hide() {}

	@Override	
	public void pause() {}

	@Override
	public void resume() {}

	/**
	 * all textures that the screen has created should get deleted.
	 * super is from ButtonScreenAdapter 
	 */
	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * Sets up the menu buttons and adds them to the stage to be rendered
	 * adds the listeners to the buttons, for the buttons to act when pressed
	 */
	@Override
	protected void initializeButtons() {
		
		//create style for play button
		atlas = new TextureAtlas(Gdx.files.internal("buttons/mainMenu/button.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		ButtonStyle style = new ButtonStyle();
		style.up = buttonSkin.getDrawable("buttonUnpressed");
		style.down = buttonSkin.getDrawable("buttonPressed");

		//Create play button
		playButton = new Button(style);
		playButton.setWidth(MENU_BUTTON_WIDTH);
		playButton.setHeight(MENU_BUTTON_HEIGHT);
		playButton.setX(Gdx.graphics.getWidth()/2 - MENU_BUTTON_WIDTH/2);
		playButton.setY(Gdx.graphics.getHeight()/2);

		//create style for options button, must start over
		atlas = new TextureAtlas(Gdx.files.internal("buttons/mainMenu/buttonO.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		style = new ButtonStyle();
		style.up = buttonSkin.getDrawable("buttonUnpressed");
		style.down = buttonSkin.getDrawable("buttonPressed");
		
		optionsButton = new Button(style);
		optionsButton.setWidth(MENU_BUTTON_WIDTH);
		optionsButton.setHeight(MENU_BUTTON_HEIGHT);
		optionsButton.setX(Gdx.graphics.getWidth()/2 - MENU_BUTTON_WIDTH/2);
		optionsButton.setY( EDGE_TOLERANCE);
		
		//Test
		//This button is just to test the story screen
		atlas = new TextureAtlas(Gdx.files.internal("buttons/button.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		style = new ButtonStyle();
		style.up = buttonSkin.getDrawable("buttonUnpressed");
		style.down = buttonSkin.getDrawable("buttonPressed");

		continueButton = new Button(style);
		continueButton.setWidth(MENU_BUTTON_WIDTH);
		continueButton.setHeight(MENU_BUTTON_HEIGHT);
		continueButton.setX(Gdx.graphics.getWidth()/2 - MENU_BUTTON_WIDTH/2);
		continueButton.setY(  Gdx.graphics.getHeight()/2 - MENU_BUTTON_HEIGHT - EDGE_TOLERANCE);

		//Create listeners
		playButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				//Change the screen when the button is let go
				gameInstance.setDataPlay();
				gameInstance.setScreen(new GameScreen(gameInstance));
				//gameInstance.setScreen(new TutorialScreen(gameInstance)); //FOR TESTING TUTORIAL
			}
		});

		optionsButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				//Change the screen when the button is let go
				Gdx.input.setCatchBackKey(true);
				gameInstance.setScreen(new OptionsScreen(gameInstance));
				dispose();
			}
		});
		
		continueButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				//Change the screen when the button is let go
				gameInstance.setDataCont();
				gameInstance.setScreen(new GameScreen(gameInstance));
			}
		});

		//Add to stage
		buttonStage.addActor(playButton);
		buttonStage.addActor(optionsButton);
		buttonStage.addActor(continueButton);
		
		inputMultiplexer.addProcessor(buttonStage);
	}

}
