package com.tgco.animalBook.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tgco.animalBook.AnimalBookGame;

public class GameScreen extends ButtonScreenAdapter implements Screen {

	//buttons
	private Button inventoryButton;
	private Button marketButton;

	//dimensions
	private final float BUTTON_WIDTH = 100;
	private final float BUTTON_HEIGHT = 100;

	//Rendering objects
	private SpriteBatch batch;

	//Camera and one finger touch motion variables
	private OrthographicCamera camera;
	private Vector3 lastTouch;

	//Input handler
	private InputMultiplexer inputMultiplexer;
	
	//DEBUGGING SHAPE RENDERER
	private ShapeRenderer sr;


	public GameScreen(AnimalBookGame gameInstance) {
		super(gameInstance);

		//Camera initialization
		lastTouch = new Vector3(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,0);
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		camera.update();

		//Initialize rendering objects
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/gameScreenGrass.jpg"));

		//Setup input processing
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		//DEBUGGING SHAPE RENDERER
		sr = new ShapeRenderer();
		sr.setColor(Color.BLACK);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Process button presses
		buttonStage.act(delta);
		//Process camera motion
		moveCameraToTouch();

		//render background
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
		
		//DEBUGGING CIRCLES FOR TOUCH
		sr.setProjectionMatrix(camera.combined);
		sr.begin(ShapeType.Line);
		sr.circle(camera.position.x, camera.position.y, 30);
		sr.circle(lastTouch.x, lastTouch.y, 30);
		sr.end();

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

	//Finds the newest touch and interpolates the camera to its position
	private void moveCameraToTouch() {
		if(Gdx.input.isTouched()) {
			lastTouch = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
			camera.unproject(lastTouch);
		}

		camera.position.lerp(lastTouch.cpy(),Gdx.graphics.getDeltaTime());
		camera.update();
	}

	private void initializeButtons() {

		//MARKET BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/gameScreen/marketButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle marketButtonStyle = new ButtonStyle();
		marketButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		marketButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		marketButton = new Button(marketButtonStyle);
		marketButton.setWidth(BUTTON_WIDTH);
		marketButton.setHeight(BUTTON_HEIGHT);
		marketButton.setX(Gdx.graphics.getWidth() - BUTTON_WIDTH - 20);
		marketButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - 20);

		//INVENTORY BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/gameScreen/inventoryButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle inventoryButtonStyle = new ButtonStyle();
		inventoryButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		inventoryButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		inventoryButton = new Button(inventoryButtonStyle);
		inventoryButton.setWidth(BUTTON_WIDTH);
		inventoryButton.setHeight(BUTTON_HEIGHT);
		inventoryButton.setX(20);
		inventoryButton.setY(Gdx.graphics.getHeight() - BUTTON_HEIGHT - 20);

		//LISTENERS
		marketButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				gameInstance.setScreen(new MarketScreen(gameInstance));
			}
		});

		inventoryButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				gameInstance.setScreen(new InventoryScreen(gameInstance));
			}
		});

		buttonStage.addActor(inventoryButton);
		buttonStage.addActor(marketButton);

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

	@Override
	public void dispose() {
		super.dispose();
		sr.dispose();
	}

}
