package com.tgco.animalBook.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.handlers.SoundHandler;

/**
 * End Game Story screen takes care of the end game story element of the game.
 *
 */
public class EndGameStory extends ButtonScreenAdapter implements Screen {

	/**
	 * Buttons
	 */
	private Button 			skipButton, continueButton;

	private Stage popupStage;

	private boolean endGame;

	/**
	 * Holds story screen string paths
	 */
	private static final	ArrayMap<Integer, Array<String>> storyMap = new ArrayMap<Integer, Array<String>>();

	/**
	 * Fading sprite for story screen transitions
	 */
	private Sprite			fadingSprite;

	/**
	 * batch for rendering
	 */
	private SpriteBatch	batch;

	/**
	 * simple page number for storyMap
	 */
	private int					pageNumber;

	/**
	 * Fading screen constants
	 */
	private final float	FADE_IN_TIME = 1;
	private final float	FADE_OUT_TIME = 1;
	private float				timeCounter;
	private boolean		fadingIn, fadingOut, displaying;

	/**
	 * StoryScreen constructor
	 * 
	 * @param gameInstance - The current AnimalBookGame instance.
	 */
	public EndGameStory(AnimalBookGame gameInstance) {
		super(gameInstance);

		popupStage = new Stage();

		for (int i = 0; i < 5; i++){
			storyMap.put(i, new Array<String>());
		}

		SoundHandler.playStoryBackgroundMusic(true);
		batch = new SpriteBatch();
		pageNumber = 0;
		storyMap.get(0).add("endGameStory/endGameStory1.png");
		storyMap.get(0).add("endGameStory/endGameStory2.png");
		backgroundTexture =  new Texture(Gdx.files.internal(storyMap.get(1-1).first()));
		fadingSprite = new Sprite(backgroundTexture);
		fadingSprite.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		timeCounter = 0;
		fadingIn = true;
		fadingOut = false;
		displaying = false;

		//Setup input processing
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
		Gdx.input.setCatchBackKey(true);
	}

	/**
	 * Renders screen objects
	 * @param delta - amount of time between each frame
	 */
	@Override
	public void render(float delta) {
		if(!endGame) {
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			batch.begin();
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
			batch.end();

			buttonStage.act(delta);
			buttonStage.draw();

			timeCounter += delta;
		} else {
			Gdx.gl.glClearColor(1, 1, 1, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			popupStage.act(delta);
			popupStage.draw();
		}
	}

	/**
	 * Called if screen has been resized
	 * 
	 * @param width
	 * @param height
	 */
	@Override
	public void resize(int width, int height) {
		if ( buttonStage == null)
			buttonStage = new Stage();
		buttonStage.clear();
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

		continueButton = new Button(buttonStyle);
		continueButton.setWidth(BUTTON_WIDTH);
		continueButton.setHeight(BUTTON_HEIGHT);
		continueButton.setPosition(EDGE_TOLERANCE, EDGE_TOLERANCE );

		continueButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				pageNumber++;
				if (pageNumber < storyMap.get(1-1).size){
					timeCounter = 0;
					backgroundTexture =  new Texture(Gdx.files.internal(storyMap.get(1-1).get(pageNumber)));
					displaying = false;
					fadingOut = true;
				}
				else{
					setEndDialog();
				}
			}
		});
		buttonStage.addActor(continueButton);
		inputMultiplexer.addProcessor(buttonStage);
	}
	
	/**
	 * Sets the game to the end game dialog when the continue button is pressed on the last story screen
	 */
	
	public void setEndDialog() {
		endGame  = true;
		Skin skin = new Skin(Gdx.files.internal("confirmSkin/uiskin.json"));
		EndGameDialog endD = new EndGameDialog("You Won!", skin, gameInstance, this);
		endD.show(popupStage);
		popupStage.addActor(endD);
		inputMultiplexer.addProcessor(popupStage);
		inputMultiplexer.removeProcessor(buttonStage);
	}
	
	public void setConfirmDialog() {
		//endGame  = true;
		Skin skin = new Skin(Gdx.files.internal("confirmSkin/uiskin.json"));
		ConfirmDialog confD = new ConfirmDialog("RESET DATA", skin, gameInstance, "All progress will be deleted. Are you Sure you want to reset?", 3);
		confD.show(popupStage);
		popupStage.addActor(confD);
		//inputMultiplexer.addProcessor(popupStage);
		//inputMultiplexer.removeProcessor(buttonStage);
	}
	
	public void setDone() {
		endGame = false;
		inputMultiplexer.removeProcessor(popupStage);
		inputMultiplexer.addProcessor(buttonStage);
		popupStage = new Stage();	
	}


	@Override
	public void show() {}

	@Override
	public void hide() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
		super.dispose();
	}

}
