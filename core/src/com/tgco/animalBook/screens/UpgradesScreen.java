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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.handlers.SoundHandler;
import com.tgco.animalBook.view.World;

public class UpgradesScreen extends ButtonScreenAdapter implements Screen {

	//reference to the game screen
	private GameScreen gameScreen;
	private BitmapFont font;
	
	//buttons
	private Button leaveButton;
	private Button fruitfullButton;
	private Button LongerButton;
	private Button MoreButton;
	private World world;
	
	public UpgradesScreen(AnimalBookGame gameInstance, GameScreen gameScreen, World world) {
		super(gameInstance);

		this.gameScreen = gameScreen;
		font = new BitmapFont();
		//Background rendering
		batch = new SpriteBatch();
		this.world = world;
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/marketScreenBackground.png"));


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
		ShapeRenderer shapeRender= new ShapeRenderer();
		shapeRender.begin(ShapeType.Filled);
		shapeRender.setColor(Color.WHITE);
		shapeRender.rect(Gdx.graphics.getWidth()/2 - 100+25, Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE+25, 0, 0);
		shapeRender.end();
		
		drawAmounts();
	}

	@Override
	public void resize(int width, int height) {
		if ( buttonStage == null)
			buttonStage = new Stage();
		buttonStage.clear();
		//reinit buttons
		initializeButtons();
	}

	public void drawAmounts(){
		batch.begin();
		font.setColor(Color.BLACK);
		font.draw(batch, "$" + String.valueOf((int) 500), Gdx.graphics.getWidth()/2 - 100 - BUTTON_WIDTH +40, Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE +40);
		font.draw(batch, "$" + String.valueOf((int) 1000), Gdx.graphics.getWidth()/2 +40, Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE +40);
		font.draw(batch, "$" + String.valueOf((int) 1500), Gdx.graphics.getWidth()/2 + 100 + BUTTON_WIDTH +40, Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE +40);
		batch.end();
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
		fruitfullButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		fruitfullButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		//fruitfullButtonStyle.disabled = new Drawable();

		fruitfullButton = new Button(fruitfullButtonStyle);
		fruitfullButton.setWidth(BUTTON_WIDTH);
		fruitfullButton.setHeight(BUTTON_HEIGHT);
		fruitfullButton.setX(Gdx.graphics.getWidth()/2 - 100 - BUTTON_WIDTH);
		fruitfullButton.setY(Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE);
		

		//longer BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/upgradesScreen/LongerButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle LongerButtonStyle = new ButtonStyle();
		LongerButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		LongerButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		LongerButton = new Button(LongerButtonStyle);
		LongerButton.setWidth(BUTTON_WIDTH);
		LongerButton.setHeight(BUTTON_HEIGHT);
		LongerButton.setX(Gdx.graphics.getWidth()/2);
		LongerButton.setY(Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE);
		
		//More drops BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/upgradesScreen/MoreButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle MoreButtonStyle = new ButtonStyle();
		MoreButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		MoreButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		MoreButton = new Button(MoreButtonStyle);
		MoreButton.setWidth(BUTTON_WIDTH);
		MoreButton.setHeight(BUTTON_HEIGHT);
		MoreButton.setX(Gdx.graphics.getWidth()/2 + 100 + BUTTON_WIDTH);
		MoreButton.setY(Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE);

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
				gameInstance.setScreen(gameScreen);
			}
		});


		if(world.getPlayer().getPlayerMoney() < 500){
			fruitfullButton.setDisabled(true);
			
		}
		buttonStage.addActor(leaveButton);
		buttonStage.addActor(fruitfullButton);
		buttonStage.addActor(LongerButton);
		buttonStage.addActor(MoreButton);

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
