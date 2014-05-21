package com.tgco.animalBook.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
	private Sprite fadingSprite;
	private SpriteBatch batch;
	private int pageNumber;
	
	private final float FADE_IN_TIME = 1;
	private final float FADE_OUT_TIME = 1;
	private float timeCounter;
	private boolean fadingIn;
	private boolean fadingOut;
	private boolean displaying;
	
	public StoryScreen(AnimalBookGame gameInstance) {
		super(gameInstance);
		SoundHandler.playStoryBackgroundMusic(true);
		batch = new SpriteBatch();
		pageNumber = 0;
		storyFilepaths[0][0] = "story/story1.png";
		storyFilepaths[0][1] = "story/story2.jpg";
		backgroundTexture =  new Texture(Gdx.files.internal(storyFilepaths[0][0]));
		//backgroundTexture =  new Texture(Gdx.files.internal(storyFilepaths[gameInstance.getLevel()][0]));
		fadingSprite = new Sprite(backgroundTexture);
		fadingSprite.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		timeCounter = 0;
		fadingIn = true;
		fadingOut = false;
		displaying = false;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		
		/*if ( fadingIn && timeCounter <= FADE_IN_TIME ) {}else{if(fadingIn){}
		}
		
		if ( displaying && timeCounter <= DISPLAY_TIME) {}else{if(displaying){}
		}
		
		if ( fadingOut && timeCounter <= FADE_OUT_TIME) {}else{if(fadingOut){}
		}*/
		
		if ( fadingIn && timeCounter <= FADE_IN_TIME ) {
			//draw with an increasing alpha
			fadingSprite.draw(batch, timeCounter/FADE_IN_TIME);
		} else {
			if (fadingIn) {
				//runs first time timeCounter is over the desired time
				fadingIn = false;
				//reset for next phase
				timeCounter = 0;
				displaying = true;
			}
		}

			if (displaying) {
				fadingSprite.draw(batch, 1);
			}

		if ( fadingOut && timeCounter <= FADE_OUT_TIME) {
			//draw with decreasing alpha
			float alphaValue = 1f - timeCounter;
			if (alphaValue < 0f){
				alphaValue = 0f;
			}
			fadingSprite.draw(batch, alphaValue/FADE_OUT_TIME);
		} else {
			if( fadingOut) {
				//runs after done fading out
				fadingSprite = new Sprite(backgroundTexture);
				fadingSprite.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				fadingOut = false;
				fadingIn = true;
				timeCounter = 0;
			}
		}
		
		
		//batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
		
		buttonStage.act(delta);
		buttonStage.draw();
		timeCounter += delta;
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
		atlas = new TextureAtlas(Gdx.files.internal("buttons/storyScreen/skipButton.atlas"));
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
		//Gdx.input.setInputProcessor(buttonStage);
		
		atlas = new TextureAtlas(Gdx.files.internal("buttons/storyScreen/continueButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		buttonStyle = new ButtonStyle();
		buttonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		buttonStyle.down = buttonSkin.getDrawable("buttonPressed");
		continueButton = new Button(buttonStyle);
		continueButton.setWidth(BUTTON_WIDTH);
		continueButton.setHeight(BUTTON_HEIGHT);
		continueButton.setPosition(BUTTON_WIDTH + EDGE_TOLERANCE,  BUTTON_HEIGHT + EDGE_TOLERANCE );
		continueButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				//next background
				/*
				    if (pageNumber < storyFilepaths[gameInstance.getLevel()].length){
					backgroundTexture =  new Texture(Gdx.files.internal(storyFilepaths[gameInstance.getLevel()][pageNumber]));
				}
				 */
				pageNumber++;
				if (pageNumber < storyFilepaths[0].length){
					timeCounter = 0;
					backgroundTexture =  new Texture(Gdx.files.internal(storyFilepaths[0][pageNumber]));
					displaying = false;
					fadingOut = true;
				}
				else{
					SoundHandler.playButtonClick();
					SoundHandler.pauseStoryBackgroundMusic();
					SoundHandler.playBackgroundMusic(true);
					gameInstance.setScreen(new GameScreen(gameInstance));
				}
			}
		});
		buttonStage.addActor(continueButton);
		
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
