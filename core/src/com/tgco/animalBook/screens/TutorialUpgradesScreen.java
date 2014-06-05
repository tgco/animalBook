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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.gameObjects.Animal;
import com.tgco.animalBook.gameObjects.Movable;
import com.tgco.animalBook.handlers.SoundHandler;
import com.tgco.animalBook.view.TutorialWorld;
import com.tgco.animalBook.view.World;

public class TutorialUpgradesScreen extends ButtonScreenAdapter implements Screen{

	/**
	 * The game screen
	 */
	private TutorialScreen tutorialScreen;

	/**
	 * The font to be used to display data
	 */
	private BitmapFont font;

	/**
	 * Amounts of each upgrade
	 */
	private int fruitfullMoney;
	private int LongerMoney;
	private int MoreMoney;

	/**
	 * Buttons
	 */
	private Button leaveButton;
	private Button fruitfullButton;
	private Button LongerButton;
	private Button MoreButton;

	/**
	 * the world
	 */
	private TutorialWorld tutorialWorld;

	/**
	 * Button specs
	 */
	private static final double REGION_HEIGHT = UPGRADE_BUTTON_HEIGHT*2.25f;
	private static final double REGION_WIDTH = UPGRADE_BUTTON_WIDTH*2.25f;

	/**
	 * Button text specs
	 */
	private static final float STRING_WIDTH_FR = Gdx.graphics.getWidth()/2 - 100 - UPGRADE_BUTTON_WIDTH +50;
	private static final float STRING_WIDTH_LD = Gdx.graphics.getWidth()/2 +50;
	private static final float STRING_WIDTH_MD =  Gdx.graphics.getWidth()/2 + 100 + UPGRADE_BUTTON_WIDTH +50;

	/**
	 * The UpgradeScreen constructor. Sets values of upgrade costs and initializes and on screen components
	 * 
	 * @param gameInstance - The current game instance
	 * @param tutorialScreen - The current game screen
	 */
	public TutorialUpgradesScreen(AnimalBookGame gameInstance, TutorialScreen tutorialScreen) {
		super(gameInstance);

		this.tutorialScreen = tutorialScreen;
		font = new BitmapFont(Gdx.files.internal("fonts/SketchBook.fnt"));
		font.setScale(.75f);
		//Background rendering
		batch = new SpriteBatch();
		this.tutorialWorld = tutorialScreen.getWorld();
		backgroundTexture = new Texture(Gdx.files.internal("backgrounds/tutorialUpgradesScreenBackground.png"));

		fruitfullMoney = (int) (100*(Math.pow(2,tutorialScreen.getWorld().getFruitfullMoneyP())));
		LongerMoney = (int) (500*(Math.pow(2,tutorialScreen.getWorld().getLongerMoneyP())));
		MoreMoney = (int) (1000*(Math.pow(2,tutorialScreen.getWorld().getMoreMoneyP())));

		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	/**
	 * Overriding render due to special functionality with upgrade buttons
	 */
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

		drawAmounts(0.5f, 0);
		drawAmounts(0.5f, 1);
		drawAmounts(0.5f, 2);

		drawData(0.5f, 0);
		drawData(0.5f, 1);
		drawData(0.5f, 2);
		reinitButtons();
	}

	/**
	 * Overriding resize due to differing button configurations
	 */
	@Override
	public void resize(int width, int height) {
		if ( buttonStage == null)
			buttonStage = new Stage();
		buttonStage.clear();
		//reinit buttons
		initializeButtons();
	}

	/**
	 * Draws the amounts of fertility, length, and more money upgrades
	 * 
	 * @param alpha
	 * @param boxName
	 */
	public void drawAmounts(float alpha, int boxName){
		batch.begin();
		font.setColor(0, 0, 0, alpha);
		switch(boxName){
		case 0: 
			font.draw(batch, "+" + String.valueOf(5), STRING_WIDTH_FR +5, Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE +60);
			font.draw(batch, "$" + String.valueOf(fruitfullMoney), STRING_WIDTH_FR, Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE +40);
			break;
		case 1:
			font.draw(batch, "+" + String.format("%.2f",5/60.0) + " s", STRING_WIDTH_LD, Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE + 60);
			font.draw(batch, "$" + String.valueOf(LongerMoney), STRING_WIDTH_LD, Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE + 40);
			break;
		case 2:
			font.draw(batch, "-" + String.format("%.2f",5/60.0) + " s", STRING_WIDTH_MD, Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE + 60);
			font.draw(batch, "$" + String.valueOf(MoreMoney),STRING_WIDTH_MD, Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE + 40);
			break;
		}

		batch.end();
	}

	/**
	 * Draws the data of each upgrade
	 * 
	 * @param alpha
	 * @param boxName
	 */
	public void drawData(float alpha, int boxName){
		batch.begin();
		font.setColor(0, 0, 0, alpha);
		switch(boxName){
		case 0: 
			font.draw(batch, "current: " + String.format("%.1f",((Animal) tutorialWorld.getMovables().get(0)).getFertilityRate())+ " %", STRING_WIDTH_FR -15, Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE +20);

			break;
		case 1:
			font.draw(batch, "current: " + String.format("%.3f",((Animal) tutorialWorld.getMovables().get(0)).getTimeOnGround()) + " s", STRING_WIDTH_LD-15, Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE +20);
			break;
		case 2:
			font.draw(batch, "current: " + String.format("%.2f",((Animal) tutorialWorld.getMovables().get(0)).getDropInterval()) + " s", STRING_WIDTH_MD-15, Gdx.graphics.getHeight()/2 - EDGE_TOLERANCE +20);

			break;
		}

		batch.end();
	}

	/**
	 * Reinitializes buttons
	 */
	public void reinitButtons(){
		if(tutorialWorld.getPlayer().getPlayerMoney() < fruitfullMoney){
			fruitfullButton.setDisabled(true);
			drawAmounts(0.5f, 0);
			drawData(0.5f, 0);
		}
		else{
			fruitfullButton.setDisabled(false);
			drawAmounts(1f, 0);
			drawData(1f, 0);
		}
		if(tutorialWorld.getPlayer().getPlayerMoney() < LongerMoney){
			LongerButton.setDisabled(true);
			drawAmounts(0.5f, 1);
			drawData(0.5f, 1);
		}
		else{
			LongerButton.setDisabled(false);
			drawAmounts(1, 1);
			drawData(1, 1);
		}
		if(tutorialWorld.getPlayer().getPlayerMoney() < MoreMoney){
			MoreButton.setDisabled(true);
			drawAmounts(0.5f, 2);
			drawData(0.5f, 2);
		}
		else{
			MoreButton.setDisabled(false);
			drawAmounts(1, 2);
			drawData(1, 2);
		}

		batch.begin();
		font.setColor(Color.BLACK);
		font.setScale(1.2f);
		font.draw(batch, "Your Money: $" + String.valueOf(tutorialWorld.getPlayer().getPlayerMoney()), Gdx.graphics.getWidth()/2 -10, Gdx.graphics.getHeight() -2*EDGE_TOLERANCE);
		font.setScale(.75f);
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
				tutorialScreen.resetInputProcessors();
				gameInstance.setScreen(tutorialScreen);
				dispose();
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

					Array<Movable> animals = tutorialWorld.getMovables();
					for(Movable animal : animals){
						((Animal) animal).upgradeFertilityRate(5);
					}
					tutorialWorld.getPlayer().subtractPlayerMoney(fruitfullMoney);
					fruitfullMoney += fruitfullMoney;
					tutorialScreen.getWorld().addFruitfullMoneyP();
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

					Array<Movable> animals = tutorialWorld.getMovables();
					for(Movable animal : animals){
						((Animal) animal).upgradeTimeOnGround(5);
					}
					tutorialWorld.getPlayer().subtractPlayerMoney(LongerMoney);
					LongerMoney += LongerMoney;
					tutorialScreen.getWorld().addLongerMoneyP();
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

					Array<Movable> animals = tutorialWorld.getMovables();
					for(Movable animal : animals){
						((Animal) animal).upgradeDropInterval(5);
					}
					tutorialWorld.getPlayer().subtractPlayerMoney(MoreMoney);
					tutorialScreen.getWorld().addMoreMoneyP();
					MoreMoney += MoreMoney;
				}
			}
		});

		if(tutorialWorld.getPlayer().getPlayerMoney() < 500){
			fruitfullButton.setDisabled(true);
			drawAmounts(0.5f, 0);

		}
		if(tutorialWorld.getPlayer().getPlayerMoney() < 1000){
			LongerButton.setDisabled(true);
			drawAmounts(0.5f, 1);
		}
		if(tutorialWorld.getPlayer().getPlayerMoney() < 1500){
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
		font.dispose();
	}
}