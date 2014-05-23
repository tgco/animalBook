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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.gameObjects.ABDrawable;
import com.tgco.animalBook.gameObjects.Animal;
import com.tgco.animalBook.gameObjects.Movable;
import com.tgco.animalBook.handlers.SoundHandler;
import com.tgco.animalBook.view.World;

public class UpgradesScreen extends ButtonScreenAdapter implements Screen {

	//reference to the game screen
	private GameScreen gameScreen;
	private BitmapFont font;
	private int fruitfullMoney;
	private int LongerMoney;
	private int MoreMoney;
	
	//buttons
	private Button leaveButton;
	private Button fruitfullButton;
	private Button LongerButton;
	private Button MoreButton;
	private World world;
	private static final double REGION_HEIGHT = BUTTON_HEIGHT*2.5f;
	private static final double REGION_WIDTH = BUTTON_WIDTH*2.5f;
	
	
	public UpgradesScreen(AnimalBookGame gameInstance, GameScreen gameScreen) {
		super(gameInstance);

		this.gameScreen = gameScreen;
		font = new BitmapFont();
		//Background rendering
		batch = new SpriteBatch();
		this.world = gameScreen.getWorld();
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/marketScreenBackground.png"));
		
		fruitfullMoney = (int) (100*(Math.pow(2,gameScreen.getWorld().getFruitfullMoneyP())));
		LongerMoney = (int) (500*(Math.pow(2,gameScreen.getWorld().getLongerMoneyP())));
		MoreMoney = (int) (1000*(Math.pow(2,gameScreen.getWorld().getMoreMoneyP())));

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
		
		drawAmounts(0.5f, 0);
		drawAmounts(0.5f, 1);
		drawAmounts(0.5f, 2);
		
		drawData(0.5f, 0);
		drawData(0.5f, 1);
		drawData(0.5f, 2);
		reinitButtons();
	}

	@Override
	public void resize(int width, int height) {
		if ( buttonStage == null)
			buttonStage = new Stage();
		buttonStage.clear();
		//reinit buttons
		initializeButtons();
	}

	public void drawAmounts(float alpha, int boxName){
		batch.begin();
		font.setColor(0, 0, 0, alpha);
		switch(boxName){
			case 0: 
				font.draw(batch, "$" + String.valueOf(fruitfullMoney), Gdx.graphics.getWidth()/2 - 100 - UPGRADE_BUTTON_WIDTH +40, Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE +40);
				break;
			case 1:
				font.draw(batch, "$" + String.valueOf(LongerMoney), Gdx.graphics.getWidth()/2 +40, Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE +40);
				break;
			case 2:
				font.draw(batch, "$" + String.valueOf(MoreMoney), Gdx.graphics.getWidth()/2 + 100 + UPGRADE_BUTTON_WIDTH +40, Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE +40);
				break;
		}
		
		batch.end();
	}
	
	public void drawData(float alpha, int boxName){
		batch.begin();
		font.setColor(0, 0, 0, alpha);
		switch(boxName){
			case 0: 
				font.draw(batch, "current: " + String.format("%.1f",((Animal) world.getMovables().get(0)).getFertilityRate())+ " %", Gdx.graphics.getWidth()/2 - 100 - BUTTON_WIDTH +25, Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE +25);

				break;
			case 1:
				font.draw(batch, "current: " + String.format("%.3f",((Animal) world.getMovables().get(0)).getDropInterval()) + " s", Gdx.graphics.getWidth()/2 +25, Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE +25);
				break;
			case 2:

				font.draw(batch, "current: " + String.format("%.2f",((Animal) world.getMovables().get(0)).getTimeOnGround()) + " s", Gdx.graphics.getWidth()/2 + 100 + BUTTON_WIDTH +25, Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE +25);

				break;
		}
		
		batch.end();
	}
	public void reinitButtons(){
		if(world.getPlayer().getPlayerMoney() < fruitfullMoney){
			fruitfullButton.setDisabled(true);
			drawAmounts(0.5f, 0);
			drawData(0.5f, 0);
		}
		else{
			fruitfullButton.setDisabled(false);
			drawAmounts(1f, 0);
			drawData(1f, 0);
		}
		 if(world.getPlayer().getPlayerMoney() < LongerMoney){
			LongerButton.setDisabled(true);
			drawAmounts(0.5f, 1);
			drawData(0.5f, 1);
		}
		 else{
			 LongerButton.setDisabled(false);
				drawAmounts(1, 1);
				drawData(1, 1);
		 }
		if(world.getPlayer().getPlayerMoney() < MoreMoney){
			MoreButton.setDisabled(true);
			drawAmounts(0.5f, 2);
			drawData(0.5f, 2);
		}
		else{
			MoreButton.setDisabled(false);
			drawAmounts(1, 2);
			drawData(1, 2);
		}
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
		leaveButton.setWidth(UPGRADE_BUTTON_WIDTH);
		leaveButton.setHeight(UPGRADE_BUTTON_HEIGHT);
		leaveButton.setX(EDGE_TOLERANCE);
		leaveButton.setY(Gdx.graphics.getHeight() - UPGRADE_BUTTON_HEIGHT - EDGE_TOLERANCE);
		
		//fruitfull BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/upgradesScreen/fruitfullButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle fruitfullButtonStyle = new ButtonStyle();
		fruitfullButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		fruitfullButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		TextureRegion trFruitfullButton = new TextureRegion(new Texture(Gdx.files.internal("buttons/upgradesScreen/fruitfullButtonDis.png")) );

		trFruitfullButton.setRegionHeight((int) (REGION_HEIGHT));
		trFruitfullButton.setRegionWidth((int) (REGION_WIDTH));

		fruitfullButtonStyle.disabled = new TextureRegionDrawable(trFruitfullButton);

		fruitfullButton = new Button(fruitfullButtonStyle);
		fruitfullButton.setWidth(UPGRADE_BUTTON_WIDTH);
		fruitfullButton.setHeight(UPGRADE_BUTTON_HEIGHT);
		fruitfullButton.setX(Gdx.graphics.getWidth()/2 - 100 - UPGRADE_BUTTON_WIDTH);
		fruitfullButton.setY(Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE);
		

		//longer BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/upgradesScreen/LongerButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle LongerButtonStyle = new ButtonStyle();
		LongerButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		LongerButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		TextureRegion trLongerButton = new TextureRegion(new Texture(Gdx.files.internal("buttons/upgradesScreen/LongerButtonDis.png")) );

		trLongerButton.setRegionHeight((int) (REGION_HEIGHT));
		trLongerButton.setRegionWidth((int) (REGION_WIDTH));

		LongerButtonStyle.disabled = new TextureRegionDrawable(trLongerButton);
		
		
		
		LongerButton = new Button(LongerButtonStyle);
		LongerButton.setWidth(UPGRADE_BUTTON_WIDTH);
		LongerButton.setHeight(UPGRADE_BUTTON_HEIGHT);
		LongerButton.setX(Gdx.graphics.getWidth()/2);
		LongerButton.setY(Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE);
		
		//More drops BUTTON
		atlas = new TextureAtlas(Gdx.files.internal("buttons/upgradesScreen/MoreButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);
		
		ButtonStyle MoreButtonStyle = new ButtonStyle();
		MoreButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		MoreButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		TextureRegion trMoreButton = new TextureRegion(new Texture(Gdx.files.internal("buttons/upgradesScreen/MoreButtonDis.png")) );

		trMoreButton.setRegionHeight((int) (REGION_HEIGHT));
		trMoreButton.setRegionWidth((int) (REGION_WIDTH));

		
		MoreButtonStyle.disabled = new TextureRegionDrawable(trMoreButton);
		
		MoreButton = new Button(MoreButtonStyle);
		MoreButton.setWidth(UPGRADE_BUTTON_WIDTH);
		MoreButton.setHeight(UPGRADE_BUTTON_HEIGHT);
		MoreButton.setX(Gdx.graphics.getWidth()/2 + 100 + UPGRADE_BUTTON_WIDTH);
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

		
		fruitfullButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if(!fruitfullButton.isDisabled()){
				SoundHandler.playButtonClick();
				//take away player money and add more to precentage of droppings
				//Gdx.input.setCatchBackKey(true);
				
				 Array<Movable> animals = world.getMovables();
				 for(Movable animal : animals){
					 ((Animal) animal).upgradeFertilityRate(5);
				 }
				 world.getPlayer().subtractPlayerMoney(fruitfullMoney);
				 fruitfullMoney += fruitfullMoney;
				 gameScreen.getWorld().addFruitfullMoneyP();
				}
			}
		});
		
		LongerButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if(!LongerButton.isDisabled()){
				SoundHandler.playButtonClick();
				//take away player money and add more to precentage of droppings
				//Gdx.input.setCatchBackKey(true);
				
				 Array<Movable> animals = world.getMovables();
				 for(Movable animal : animals){
					 ((Animal) animal).upgradeDropInterval(5);
				 }
				 world.getPlayer().subtractPlayerMoney(LongerMoney);
				 LongerMoney += LongerMoney;
				 gameScreen.getWorld().addLongerMoneyP();
				}
			}
		});
		MoreButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if(!MoreButton.isDisabled()){
				SoundHandler.playButtonClick();
				//take away player money and add more to precentage of droppings
				//Gdx.input.setCatchBackKey(true);
				
				 Array<Movable> animals = world.getMovables();
				 for(Movable animal : animals){
					 ((Animal) animal).upgradeTimeOnGround(5);
				 }
				 world.getPlayer().subtractPlayerMoney(MoreMoney);
				 gameScreen.getWorld().addMoreMoneyP();
				 MoreMoney += MoreMoney;
				}
			}
		});
		
		if(world.getPlayer().getPlayerMoney() < 500){
			fruitfullButton.setDisabled(true);
			drawAmounts(0.5f, 0);
			
		}
		 if(world.getPlayer().getPlayerMoney() < 1000){
			LongerButton.setDisabled(true);
			drawAmounts(0.5f, 1);
		}
		if(world.getPlayer().getPlayerMoney() < 1500){
			MoreButton.setDisabled(true);
			drawAmounts(0.5f, 2);
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
