package com.tgco.animalBook.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.tgco.animalBook.AnimalBookGame;

public class TutorialScreen extends ButtonScreenAdapter implements Screen {

	//buttons
	private Button tutorialButton;

	//dimensions
	private final float BUTTON_WIDTH = 100;
	private final float BUTTON_HEIGHT = 100;

	//Input handler
	private InputMultiplexer inputMultiplexer;

	public TutorialScreen(AnimalBookGame gameInstance) {
		super(gameInstance);

		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
		
		//MARKET BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/tutorialScreen/ButtonSkip.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		
		ButtonStyle leaveButtonStyle = new ButtonStyle();
		leaveButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		leaveButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		
		tutorialButton = new Button(leaveButtonStyle);
		tutorialButton.setWidth(BUTTON_WIDTH);
		tutorialButton.setHeight(BUTTON_HEIGHT);
		tutorialButton.setX(20);
		tutorialButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - 20);
		
		
		//LISTENERS
		tutorialButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				gameInstance.setScreen(new GameScreen(gameInstance));
			}
		});
		
		
		buttonStage.addActor(tutorialButton);
		
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
		// TODO Auto-generated method stub

	}

}
