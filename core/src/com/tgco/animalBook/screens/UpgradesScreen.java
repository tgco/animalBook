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
import com.tgco.animalBook.view.World;

public class UpgradesScreen extends ButtonScreenAdapter implements Screen {

	//reference to the game screen
	private GameScreen gameScreen;

	//buttons
	private Button leaveButton;
	private Button fruitfullButton;
	private Button LongerButton;

	public UpgradesScreen(AnimalBookGame gameInstance, GameScreen gameScreen) {
		super(gameInstance);

		this.gameScreen = gameScreen;
		
		//Background rendering
		batch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/romaninaLandscape.jpg"));


		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		buttonStage.act(delta);

		//render background
		batch.begin();
		batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();

		//Draw buttons
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

	@Override
	protected void initializeButtons() {

		//Leave BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/upgradesScreen/leaveButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle leaveButtonStyle = new ButtonStyle();
		leaveButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		leaveButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		leaveButton = new Button(leaveButtonStyle);
		leaveButton.setWidth(BUTTON_WIDTH);
		leaveButton.setHeight(BUTTON_HEIGHT);
		leaveButton.setX(EDGE_TOLERANCE);
		leaveButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - EDGE_TOLERANCE);

		//fruitfull BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/upgradesScreen/fruitfullButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle fruitfullButtonStyle = new ButtonStyle();
		leaveButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		leaveButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		fruitfullButton = new Button(fruitfullButtonStyle);
		fruitfullButton.setWidth(BUTTON_WIDTH);
		fruitfullButton.setHeight(BUTTON_HEIGHT);
		fruitfullButton.setX(Gdx.graphics.getWidth()/2 - 100);
		fruitfullButton.setY(Gdx.graphics.getHeight()/2 - BUTTON_HEIGHT - EDGE_TOLERANCE);
		

		//longer BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/upgradesScreen/LongerButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle LongerButtonStyle = new ButtonStyle();
		leaveButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		leaveButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		LongerButton = new Button(fruitfullButtonStyle);
		LongerButton.setWidth(BUTTON_WIDTH);
		LongerButton.setHeight(BUTTON_HEIGHT);
		LongerButton.setX(Gdx.graphics.getWidth()/2 + 100);
		LongerButton.setY(Gdx.graphics.getHeight()/2 - BUTTON_HEIGHT - EDGE_TOLERANCE);

		//LISTENERS
		leaveButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.pauseMarketBackgroundMusic();
				SoundHandler.playBackgroundMusic(true);
				gameScreen.resetInputProcessors();
				//Grab the world
				World world = gameScreen.getWorld();
				world.setPlayerTarget(world.getPlayer().getPosition());
				gameInstance.setScreen(gameScreen);
			}
		});


		buttonStage.addActor(leaveButton);

		inputMultiplexer.addProcessor(buttonStage);
	}

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

	@Override
	public void dispose() {
		super.dispose();

	}

}
