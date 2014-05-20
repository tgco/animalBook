package com.tgco.animalBook.screens;

import com.badlogic.gdx.Gdx;
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

public class StoryScreen extends ButtonScreenAdapter implements Screen {

	private Button skipButton, continueButton;
	private static final String[][] storyFilepaths = new String[1][2];
	private SpriteBatch batch;
	
	public StoryScreen(AnimalBookGame gameInstance) {
		super(gameInstance);
		SoundHandler.playStoryBackgroundMusic(true);
		batch = new SpriteBatch();
		storyFilepaths[0][0] = "story/story1.png";
		storyFilepaths[0][1] = "story/story2.jpg";
		backgroundTexture =  new Texture(Gdx.files.internal(storyFilepaths[0][0]));
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
		atlas = new TextureAtlas(Gdx.files.internal("buttons/storyScreen/continueButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		ButtonStyle buttonStyle = new ButtonStyle();
		buttonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		buttonStyle.down = buttonSkin.getDrawable("buttonPressed");
		skipButton = new Button(buttonStyle);
		skipButton.setWidth(BUTTON_WIDTH);
		skipButton.setHeight(BUTTON_HEIGHT);
		skipButton.setPosition(Gdx.graphics.getWidth() - BUTTON_WIDTH - EDGE_TOLERANCE, Gdx.graphics.getHeight() - BUTTON_HEIGHT - EDGE_TOLERANCE );
		skipButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.pauseStoryBackgroundMusic();
				SoundHandler.playBackgroundMusic(true);
				gameInstance.setScreen(new GameScreen(gameInstance));
			}
		});
		buttonStage.addActor(skipButton);
		Gdx.input.setInputProcessor(buttonStage);
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
