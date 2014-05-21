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

public class LoseScreen extends ButtonScreenAdapter implements Screen {

	private Button mainMenuButton, retryButton;
	
	public LoseScreen(AnimalBookGame gameInstance) {
		super(gameInstance);

		//Background rendering
		batch = new SpriteBatch();
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/gameScreenGrass2.jpg"));
		
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//render background
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

	@Override
	protected void initializeButtons() {
		atlas = new TextureAtlas(Gdx.files.internal("buttons/loseScreen/retryButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		ButtonStyle buttonStyle = new ButtonStyle();
		buttonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		buttonStyle.down = buttonSkin.getDrawable("buttonPressed");
		retryButton = new Button(buttonStyle);
		retryButton.setWidth(BUTTON_WIDTH);
		retryButton.setHeight(BUTTON_HEIGHT);
		retryButton.setPosition(Gdx.graphics.getWidth() - BUTTON_WIDTH - EDGE_TOLERANCE, Gdx.graphics.getHeight() - BUTTON_HEIGHT - EDGE_TOLERANCE );
		retryButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				//TODO add if statement to determine if we should be going to market or to game
				gameInstance.setScreen(new MarketScreen(gameInstance, new GameScreen(gameInstance)));
			}
		});
		buttonStage.addActor(retryButton);
		Gdx.input.setInputProcessor(buttonStage);
		
		
		atlas = new TextureAtlas(Gdx.files.internal("buttons/loseScreen/mainMenuButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		buttonStyle = new ButtonStyle();
		buttonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		buttonStyle.down = buttonSkin.getDrawable("buttonPressed");
		mainMenuButton = new Button(buttonStyle);
		mainMenuButton.setWidth(BUTTON_WIDTH);
		mainMenuButton.setHeight(BUTTON_HEIGHT);
		mainMenuButton.setPosition(EDGE_TOLERANCE, Gdx.graphics.getHeight() -  BUTTON_HEIGHT - EDGE_TOLERANCE );
		mainMenuButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				gameInstance.setScreen(new MainMenuScreen(gameInstance));
			}
		});
		buttonStage.addActor(mainMenuButton);

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