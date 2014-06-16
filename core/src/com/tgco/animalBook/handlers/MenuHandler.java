package com.tgco.animalBook.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.gameObjects.Animal;
import com.tgco.animalBook.gameObjects.Consumable;
import com.tgco.animalBook.gameObjects.Dog;
import com.tgco.animalBook.gameObjects.Movable;
import com.tgco.animalBook.screens.ButtonScreenAdapter;
import com.tgco.animalBook.screens.GameScreen;
import com.tgco.animalBook.view.World;

public class MenuHandler{
	
	private Stage buttonStage;
	
	private VerticalGroup menuGroup;
	private Image alexInfoImage, menuGroupImage, inventoryGroupImage, upgradesGroupImage, optionsGroupImage, upgradesStatusGroupImage;
	private Label infoLabel;
	private HorizontalGroup inventoryGroup, upgradesGroup1, upgradesGroup2, optionsGroup, upgradesStatusGroup;
	
	//dimensions for buttons
		protected static final float BUTTON_WIDTH = (1f/10f)*Gdx.graphics.getWidth();
		protected static final float BUTTON_HEIGHT = (1f/10f)*Gdx.graphics.getWidth();
		//distance between buttons and between the edge and a button
		protected static final float EDGE_TOLERANCE = (.03f)*Gdx.graphics.getHeight();

		//Button texture initialization
		protected TextureAtlas atlas;

	
	 /** The font used when rendering strings
	 */
	private BitmapFont font;
	private final float FONT_SCALE = Gdx.graphics.getHeight()/750f;


	/**
	 * Each button used on the game screen user interface overlay
	 */
	private Button inventoryGroupButton, optionsGroupButton, upgradesGroupButton, menuBackgroundButton, fruitfulButton, longerButton, moreButton, dogButton;

	private World gWorld;
	private GameScreen gScreen;

	/**
	 * Amounts of each upgrade
	 */
	private int fruitfulMoney;
	private int longerMoney;
	private int moreMoney;
	private int dogMoney;
	
	private AnimalBookGame gameInstance;
	private Skin buttonSkin;
	
	public MenuHandler(Stage bStage, AnimalBookGame gameInstance, World world, GameScreen gScreen) {
		buttonStage = bStage;
		this.gScreen = gScreen;
		this.gWorld = world;
		this.gameInstance = gameInstance;
		//Initialize rendering objects
		font = new BitmapFont(Gdx.files.internal("fonts/Dimbo2.fnt"));
		font.setScale(FONT_SCALE);

	}

	
	/**
	 * Initialize main menu group items
	 */
	public void initializeMenuItems(){
		gScreen.setMainMenuInitialized(true);
		Gdx.app.log("My Tagg", "DND is: " + (gScreen.getDnd() != null) + " Alex is " + (gScreen.getAlexButton()));
		gScreen.getDnd().addTarget(new Target(gScreen.getAlexButton()){

			@Override
			public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
				if (payload.getObject() instanceof Consumable)
					if (gWorld.getPlayer().getHealth() == 100f){
						this.getActor().setColor(Color.RED);
						return false;
					}
				this.getActor().setColor(Color.GREEN);
				return true;
			}

			@Override
			public void reset( Source source, Payload payload) {
				getActor().setColor(Color.WHITE);
			}

			@Override
			public void drop(Source source, Payload payload, float x, float y, int pointer) {
				if (payload.getObject() instanceof Consumable)
					gWorld.getPlayer().eat(((Consumable)payload.getObject()).getType().getHungerValue());
			}
		});

		//Initialze Background Button
		atlas = new TextureAtlas(Gdx.files.internal("buttons/gameScreen/backgroundMenuButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle backgroundMenuButtonStyle = new ButtonStyle();
		backgroundMenuButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		backgroundMenuButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		menuBackgroundButton = new Button(backgroundMenuButtonStyle);
		menuBackgroundButton.setWidth(Gdx.graphics.getWidth());
		menuBackgroundButton.setHeight(Gdx.graphics.getHeight());
		menuBackgroundButton.setX(0);
		menuBackgroundButton.setY(0);
		menuBackgroundButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.changeBackgroundVolume((float) .5);
				handleMainMenu(false);
				gScreen.getAlexButton().setChecked(false);
				inventoryGroupButton.setChecked(false);
				upgradesGroupButton.setChecked(false);
				optionsGroupButton.setChecked(false);
			}
		});

		//Main Group
		menuGroup = new VerticalGroup();
		menuGroup.center();

		menuGroupImage = new Image(new Texture(Gdx.files.internal("backgrounds/menuBackground.png")));
		menuGroup.space(EDGE_TOLERANCE);

		//Inventory Group Button
		atlas = new TextureAtlas(Gdx.files.internal("buttons/gameScreen/inventoryButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle inventoryButtonStyle = new ButtonStyle();
		inventoryButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		inventoryButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		inventoryGroupButton = new Button(inventoryButtonStyle){
			@Override
			public float getPrefWidth(){
				return BUTTON_WIDTH;
			}

			@Override
			public float getPrefHeight(){
				return BUTTON_HEIGHT;
			}
		};
		inventoryGroupButton.setWidth(BUTTON_WIDTH);
		inventoryGroupButton.setHeight(BUTTON_HEIGHT);
		inventoryGroupButton.setChecked(false);
		inventoryGroupButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.changeBackgroundVolume((float) .1);
				if (!gScreen.getInventoryMenuInitialized())
					initializeInventoryItems();
				handleInventoryMenu(inventoryGroupButton.isChecked());
				handleUpgradesMenu(false);
				upgradesGroupButton.setChecked(false);
				handleOptionsMenu(false);
				optionsGroupButton.setChecked(false);

			}
		});

		menuGroup.addActor(inventoryGroupButton);

		//Upgrade Group Button
		atlas = new TextureAtlas(Gdx.files.internal("buttons/gameScreen/shopButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle upgradeButtonStyle = new ButtonStyle();
		upgradeButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		upgradeButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		upgradesGroupButton = new Button(upgradeButtonStyle){
			@Override
			public float getPrefWidth(){
				return BUTTON_WIDTH;
			}

			@Override
			public float getPrefHeight(){
				return BUTTON_HEIGHT;
			}
		};
		upgradesGroupButton.setWidth(BUTTON_WIDTH);
		upgradesGroupButton.setHeight(BUTTON_HEIGHT);
		upgradesGroupButton.setChecked(false);
		upgradesGroupButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if (gWorld.getMovables().size > 0){
					SoundHandler.playButtonClick();
					SoundHandler.changeBackgroundVolume((float) .1);
					if (!gScreen.getUpgradesMenuInitialized())
						initializeUpgradeItems();
					handleUpgradesMenu(upgradesGroupButton.isChecked());
					handleInventoryMenu(false);
					inventoryGroupButton.setChecked(false);
					handleOptionsMenu(false);
					optionsGroupButton.setChecked(false);
				}
			}
		});
		menuGroup.addActor(upgradesGroupButton);

		//Option Group Button
		atlas = new TextureAtlas(Gdx.files.internal("buttons/gameScreen/optionsButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle optionsButtonStyle = new ButtonStyle();
		optionsButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		optionsButtonStyle.down = buttonSkin.getDrawable("buttonPressed");

		optionsGroupButton = new Button(optionsButtonStyle){
			@Override
			public float getPrefWidth(){
				return BUTTON_WIDTH;
			}

			@Override
			public float getPrefHeight(){
				return BUTTON_HEIGHT;
			}
		};
		optionsGroupButton.setWidth(BUTTON_WIDTH);
		optionsGroupButton.setHeight(BUTTON_HEIGHT);
		optionsGroupButton.setChecked(false);
		optionsGroupButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				SoundHandler.playButtonClick();
				SoundHandler.changeBackgroundVolume((float) .1);
				if (!gScreen.getOptionsMenuInitialized())
					gScreen.initializeOptionItems();
				handleOptionsMenu(optionsGroupButton.isChecked());
				handleInventoryMenu(false);
				inventoryGroupButton.setChecked(false);
				handleUpgradesMenu(false);
				upgradesGroupButton.setChecked(false);
			}
		});
		menuGroup.addActor(optionsGroupButton);

		//Inventory Group
		inventoryGroup = new HorizontalGroup();
		inventoryGroup.center();
		inventoryGroup.setPosition(gScreen.getAlexButton().getX() + gScreen.getAlexButton().getWidth() + EDGE_TOLERANCE*2f,
				gScreen.getAlexButton().getY() - BUTTON_HEIGHT - EDGE_TOLERANCE);
		inventoryGroup.space(EDGE_TOLERANCE);

		inventoryGroupImage = new Image(new Texture(Gdx.files.internal("backgrounds/menuBackground.png")));
		inventoryGroupImage.setPosition(gScreen.getAlexButton().getX() +gScreen.getAlexButton().getWidth() + EDGE_TOLERANCE,
				gScreen.getAlexButton().getY() - BUTTON_HEIGHT - EDGE_TOLERANCE);

		//Upgrade Group 1
		upgradesGroup1 = new HorizontalGroup();
		upgradesGroup1.center();
		upgradesGroup1.setPosition(gScreen.getAlexButton().getX() + gScreen.getAlexButton().getWidth() + EDGE_TOLERANCE*2f,
				gScreen.getAlexButton().getY() - 1.5f*BUTTON_HEIGHT - EDGE_TOLERANCE);
		upgradesGroup1.space(EDGE_TOLERANCE);

		//Upgrade Group 2
		upgradesGroup2 = new HorizontalGroup();
		upgradesGroup2.center();
		upgradesGroup2.setPosition(gScreen.getAlexButton().getX() + gScreen.getAlexButton().getWidth() + EDGE_TOLERANCE*2f,
				upgradesGroup1.getY() - BUTTON_HEIGHT - EDGE_TOLERANCE);
		upgradesGroup2.space(EDGE_TOLERANCE);

		upgradesGroupImage = new Image(new Texture(Gdx.files.internal("backgrounds/menuBackground.png")));

		//Option Group
		optionsGroup = new HorizontalGroup();
		optionsGroup.center();
		optionsGroup.setPosition(gScreen.getAlexButton().getX() + gScreen.getAlexButton().getWidth() + EDGE_TOLERANCE*2f,
				gScreen.getAlexButton().getY() - 3f*BUTTON_HEIGHT - 3f*EDGE_TOLERANCE);
		optionsGroup.space(EDGE_TOLERANCE);

		optionsGroupImage = new Image(new Texture(Gdx.files.internal("backgrounds/menuBackground.png")));
		optionsGroupImage.setPosition(gScreen.getAlexButton().getX() + gScreen.getAlexButton().getWidth() + EDGE_TOLERANCE,
				gScreen.getAlexButton().getY() - 3f*BUTTON_HEIGHT - 3f*EDGE_TOLERANCE);

		//after all components are taken care of...
		menuGroup.pack();
		menuGroup.setPosition(gScreen.getAlexButton().getX(), gScreen.getAlexButton().getY() - menuGroup.getHeight() - EDGE_TOLERANCE);
		menuGroupImage.setPosition(menuGroup.getX() - .5f*EDGE_TOLERANCE, menuGroup.getY() - EDGE_TOLERANCE*.5f);
		menuGroupImage.setSize(menuGroup.getWidth() + EDGE_TOLERANCE, menuGroup.getHeight() + EDGE_TOLERANCE);
	}

	/**
	 * Initilize inventory group items
	 */
	public void initializeInventoryItems(){
		gScreen.setInventoryMenuInitialized(true);
		for (int i = 0; i < Consumable.DropType.values().length; i++){
			final int index = i;

			//create atlas and add it to a new skin
			atlas = new TextureAtlas(Gdx.files.internal(Consumable.DropType.values()[i].getAtlasPath()));
			buttonSkin = new Skin();
			buttonSkin.addRegions(atlas);

			//create a Buttonstyle
			ImageTextButtonStyle inventoryButtonStyle = new ImageTextButtonStyle();
			inventoryButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
			inventoryButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
			//set the font here
			inventoryButtonStyle.font = font;
			//create a new button using aforementioned button style and set stuff up
			final ImageTextButton inventoryButton = new ImageTextButton("", inventoryButtonStyle){
				@Override
				public float getPrefHeight(){
					return BUTTON_HEIGHT*2f/3f;
				};

				@Override
				public float getPrefWidth(){
					return BUTTON_WIDTH*2f/3f;
				}
			};
			inventoryButton.getLabel().setColor(Color.RED);
			inventoryButton.getStyle().font.setScale(FONT_SCALE);
			inventoryButton.setText("x" + gWorld.getPlayer().getInventory().getInventory().get(Consumable.DropType.values()[index]).size);
			inventoryButton.bottom();
			inventoryButton.right();
			inventoryButton.setName(Consumable.DropType.values()[i].getName());
			gScreen.getDnd().addSource(new Source(inventoryButton){

				/**
				 * Overriding dragStart to initialize drag and drop payload
				 */
				@Override
				public Payload dragStart(InputEvent event, float x, float y,int pointer) {
					System.out.println("Drag started @ x:" + x + " y:" + y);
					Payload payload = new Payload();
					if (gWorld.getPlayer().getInventory().removeItem(Consumable.DropType.values()[index])){
						inventoryButton.setText("x" + gWorld.getPlayer().getInventory().getInventory().get(Consumable.DropType.values()[index]).size);
						payload.setObject(new Consumable(Consumable.DropType.values()[index]));
						payload.setDragActor(new Image(inventoryButton.getBackground()){
							@Override
							public float getPrefWidth(){
								return BUTTON_WIDTH*2f/3f;
							}
							@Override
							public float getPrefHeight(){
								return BUTTON_HEIGHT*2f/3f;
							}
						});
						return payload;
					}
					return null;
				}

				/**
				 * Overriding dragStop to determine if drag has stopped over a valid target
				 */
				@Override
				public void dragStop(InputEvent event, float x, float y,int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target){
					System.out.println("Drag stopped @ x:" + x + " y:" + y);
					if (target == null){
						gWorld.getPlayer().getInventory().addItem(new Consumable(Consumable.DropType.values()[index]));
						inventoryButton.setText("x" + gWorld.getPlayer().getInventory().getInventory().get(Consumable.DropType.values()[index]).size);
					}
				}
			}
					);
			inventoryGroup.addActor(inventoryButton);
		}
		//some whacky code down here...

		inventoryGroup.pack();
		inventoryGroup.setHeight(BUTTON_HEIGHT);
		inventoryGroupImage.setSize(inventoryGroup.getWidth() + EDGE_TOLERANCE*2f, inventoryGroup.getHeight());
	}

	/**
	 * Initialize upgrade group items
	 */
	public void initializeUpgradeItems(){
		gScreen.setUpgradesMenuInitialized( true);
		//initialize upgrade monies
		fruitfulMoney = (int) (15*(Math.pow(2,gameInstance.getLevelHandler().getFruitfullMoneyP())));
		longerMoney = (int) (5*(Math.pow(2,gameInstance.getLevelHandler().getLongerMoneyP())));
		moreMoney = (int) (10*(Math.pow(2,gameInstance.getLevelHandler().getMoreMoneyP())));
		dogMoney = 100;
		final Button fruitfulButton, longerButton, moreButton;
		final Label upgradeLabel, fruitfulLabel, longerLabel, moreLabel;

		//fruitfulbutton
		atlas = new TextureAtlas(Gdx.files.internal("buttons/upgradesScreen/fruitfullButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle fruitfulButtonStyle = new ButtonStyle();
		fruitfulButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		fruitfulButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		TextureRegion trFruitfulButton = new TextureRegion(new Texture(Gdx.files.internal("buttons/upgradesScreen/fruitfullButtonDis.png")) );
		trFruitfulButton.setRegionHeight((int) (.92f*BUTTON_HEIGHT));
		trFruitfulButton.setRegionWidth((int) (.92f*BUTTON_HEIGHT));
		
		fruitfulButtonStyle.disabled = new TextureRegionDrawable(trFruitfulButton);

		fruitfulButton = new Button(fruitfulButtonStyle){
			@Override
			public float getPrefHeight(){
				return BUTTON_HEIGHT;
			};

			@Override
			public float getPrefWidth(){
				return BUTTON_WIDTH;
			}
		};
		fruitfulButton.setName("fruitfulButton");

		//longerButton
		atlas = new TextureAtlas(Gdx.files.internal("buttons/upgradesScreen/LongerButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle longerButtonStyle = new ButtonStyle();
		longerButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		longerButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		TextureRegion trLongerButton = new TextureRegion(new Texture(Gdx.files.internal("buttons/upgradesScreen/LongerButtonDis.png")) );
		trLongerButton.setRegionHeight((int) (.92f*BUTTON_HEIGHT));
		trLongerButton.setRegionWidth((int) (.92f*BUTTON_HEIGHT));
		
		longerButtonStyle.disabled = new TextureRegionDrawable(trLongerButton);

		longerButton = new Button(longerButtonStyle){
			@Override
			public float getPrefHeight(){
				return BUTTON_HEIGHT;
			};

			@Override
			public float getPrefWidth(){
				return BUTTON_WIDTH;
			}
		};
		longerButton.setName("longerButton");

		//moreButton
		atlas = new TextureAtlas(Gdx.files.internal("buttons/upgradesScreen/MoreButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle MoreButtonStyle = new ButtonStyle();
		MoreButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		MoreButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		TextureRegion trMoreButton = new TextureRegion(new Texture(Gdx.files.internal("buttons/upgradesScreen/MoreButtonDis.png")) );
		//trMoreButton.setRegionHeight((int) (BUTTON_HEIGHT*30/8));
		//trMoreButton.setRegionWidth((int) (BUTTON_HEIGHT*30/8));
		trMoreButton.setRegionHeight((int) (.92f*BUTTON_HEIGHT));
		trMoreButton.setRegionWidth((int) (.92f*BUTTON_HEIGHT));
		
		MoreButtonStyle.disabled = new TextureRegionDrawable(trMoreButton);

		moreButton = new Button(MoreButtonStyle) {
			@Override
			public float getPrefHeight(){
				return BUTTON_HEIGHT;
			};

			@Override
			public float getPrefWidth(){
				return BUTTON_WIDTH;
			}
		};
		moreButton.setName("moreButton");

		//dogButton
		atlas = new TextureAtlas(Gdx.files.internal("buttons/upgradesScreen/dogButton.atlas"));
		buttonSkin = new Skin();
		buttonSkin.addRegions(atlas);

		ButtonStyle dogButtonStyle = new ButtonStyle();
		dogButtonStyle.up = buttonSkin.getDrawable("buttonUnpressed");
		dogButtonStyle.down = buttonSkin.getDrawable("buttonPressed");
		TextureRegion trDogButton = new TextureRegion(new Texture(Gdx.files.internal("buttons/upgradesScreen/dogButtonDis.png")) );
		//trDogButton.setRegionHeight((int) (BUTTON_HEIGHT*30/8));
		//trDogButton.setRegionWidth((int) (BUTTON_HEIGHT*30/8));
		trDogButton.setRegionHeight((int) (.92f*BUTTON_HEIGHT));
		trDogButton.setRegionWidth((int) (.92f*BUTTON_HEIGHT));

		dogButtonStyle.disabled = new TextureRegionDrawable(trDogButton);

		dogButton = new Button(dogButtonStyle) {
			@Override
			public float getPrefHeight(){
				return BUTTON_HEIGHT;
			};

			@Override
			public float getPrefWidth(){
				return BUTTON_WIDTH;
			}
		};
		dogButton.setName("dogButton");



		//UpgradeStatusGroup
		upgradesStatusGroup = new HorizontalGroup();
		upgradesStatusGroup.center();
		upgradesStatusGroup.space(EDGE_TOLERANCE);

		//fruitfulLabel
		LabelStyle upgradeLabelStyle = new LabelStyle();
		//upgradeLabelStyle.font = new BitmapFont(Gdx.files.internal("fonts/SketchBook.fnt"));
		upgradeLabelStyle.font = font;
		//upgradeLabelStyle.fontColor = Color.WHITE;

		upgradeLabel = new Label(
				"\n" +
						"Upgrade Level: \n" +
						"Next Upgrade: \n" +
						"Upgrade Cost: \n" +
						"Current:"
						, upgradeLabelStyle);
		upgradeLabel.setAlignment(Align.left);

		fruitfulLabel = new Label(
				"More Animals \n" +
						String.valueOf(gameInstance.getLevelHandler().getFruitfullMoneyP()) + "\n" +
						"+" + String.valueOf(5) + "%\n" +
						"$" + String.valueOf(fruitfulMoney) + "\n" +
						String.format("%.1f",((Animal) gWorld.getMovables().get(0)).getFertilityRate())+ "%"			 
						, upgradeLabelStyle);
		fruitfulLabel.setAlignment(Align.right);

		longerLabel = new Label(
				"Bigger Basket\n" +
						String.valueOf(gameInstance.getLevelHandler().getLongerMoneyP()) + "\n" +
						"+" + String.format("%.2f",5/60.0) + " s\n" +
						"$" + String.valueOf(longerMoney) + "\n" +
						String.format("%.2f",((Animal) gWorld.getMovables().get(0)).getTimeOnGround())+ "%"			 
						, upgradeLabelStyle);
		longerLabel.setAlignment(Align.right);

		moreLabel = new Label(
				"Drop Interval\n" +
						String.valueOf(gameInstance.getLevelHandler().getMoreMoneyP()) + "\n" +
						"-" + String.format("%.2f",5/60.0) + " s\n" +
						"$" + String.valueOf(moreMoney) + "\n" +
						String.format("%.2f",((Animal) gWorld.getMovables().get(0)).getDropInterval())+ "%"	
						, upgradeLabelStyle);
		moreLabel.setAlignment(Align.right);

		//add listeners to buttons
		fruitfulButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if(!fruitfulButton.isDisabled()){
					SoundHandler.playButtonClick();
					//take away player money and add more to percentage of droppings
					//Gdx.input.setCatchBackKey(true);

					Array<Movable> animals = gWorld.getMovables();
					for(Movable animal : animals){
						((Animal) animal).upgradeFertilityRate(5);
					}
					gWorld.getPlayer().subtractPlayerMoney(fruitfulMoney);
					System.out.println(fruitfulMoney +"  "+gWorld.getPlayer().getPlayerMoney());
					fruitfulMoney += fruitfulMoney;
					gameInstance.getLevelHandler().addFruitfullMoneyP();

					fruitfulLabel.setText(
							"Fruitfulness\n" +
									String.valueOf(gameInstance.getLevelHandler().getFruitfullMoneyP()) + "\n" +
									"+" + String.valueOf(5) + "%\n" +
									"$" + String.valueOf(fruitfulMoney) + "\n" +
									String.format("%.1f",((Animal) gWorld.getMovables().get(0)).getFertilityRate())+ "%"
							);
					infoLabel.setText("Money: $" + gWorld.getPlayer().getPlayerMoney()
							+ "\nNeeded: " + (gameInstance.getLevelHandler().getStoredAmount() + gWorld.getMovables().size) + " of " + gameInstance.getLevelHandler().getPassLevelAmount());
				}
				if(gWorld.getPlayer().getPlayerMoney() < fruitfulMoney)
					fruitfulButton.setDisabled(true);
				else
					fruitfulButton.setDisabled(false);

				if(gWorld.getPlayer().getPlayerMoney() < longerMoney)
					longerButton.setDisabled(true);
				else
					longerButton.setDisabled(false);

				if(gWorld.getPlayer().getPlayerMoney() < moreMoney)
					moreButton.setDisabled(true);
				else
					moreButton.setDisabled(false);

				if(gWorld.getPlayer().getPlayerMoney() < dogMoney)
					dogButton.setDisabled(true);
				else
					dogButton.setDisabled(false);
			}
		});
		longerButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if(!longerButton.isDisabled()){
					SoundHandler.playButtonClick();
					//take away player money and add more to precentage of droppings
					//Gdx.input.setCatchBackKey(true);

					Array<Movable> animals = gWorld.getMovables();
					for(Movable animal : animals){
						((Animal) animal).upgradeTimeOnGround(5);
					}
					gWorld.getPlayer().subtractPlayerMoney(longerMoney);
					longerMoney += longerMoney;
					gameInstance.getLevelHandler().addLongerMoneyP();
					longerLabel.setText(
							"Item Duration\n" +
									String.valueOf(gameInstance.getLevelHandler().getLongerMoneyP()) + "\n" +
									"+" + String.format("%.2f",5/60.0) + " s\n" +
									"$" + String.valueOf(longerMoney) + "\n" +
									String.format("%.2f",((Animal) gWorld.getMovables().get(0)).getTimeOnGround())+ "%"
							);
					infoLabel.setText("Money: $" + gWorld.getPlayer().getPlayerMoney()
							+ "\nNeeded: " + (gameInstance.getLevelHandler().getStoredAmount() + gWorld.getMovables().size) + " of " + gameInstance.getLevelHandler().getPassLevelAmount());
				}
				if(gWorld.getPlayer().getPlayerMoney() < fruitfulMoney)
					fruitfulButton.setDisabled(true);
				else
					fruitfulButton.setDisabled(false);

				if(gWorld.getPlayer().getPlayerMoney() < longerMoney)
					longerButton.setDisabled(true);
				else
					longerButton.setDisabled(false);

				if(gWorld.getPlayer().getPlayerMoney() < moreMoney)
					moreButton.setDisabled(true);
				else
					moreButton.setDisabled(false);

				if(gWorld.getPlayer().getPlayerMoney() < dogMoney)
					dogButton.setDisabled(true);
				else
					dogButton.setDisabled(false);
			}
		});
		moreButton.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if(!moreButton.isDisabled()){
					SoundHandler.playButtonClick();
					//take away player money and add more to precentage of droppings
					//Gdx.input.setCatchBackKey(true);

					Array<Movable> animals = gWorld.getMovables();
					for(Movable animal : animals){
						((Animal) animal).upgradeDropInterval(5);
					}
					gWorld.getPlayer().subtractPlayerMoney(moreMoney);
					gameInstance.getLevelHandler().addMoreMoneyP();
					moreMoney += moreMoney;
					moreLabel.setText(
							"Drop Interval\n" +
									String.valueOf(gameInstance.getLevelHandler().getMoreMoneyP()) + "\n" +
									"-" + String.format("%.2f",5/60.0) + " s\n" +
									"$" + String.valueOf(moreMoney) + "\n" +
									String.format("%.2f",((Animal) gWorld.getMovables().get(0)).getDropInterval())+ "%"	
							);
					infoLabel.setText("Money: $" + gWorld.getPlayer().getPlayerMoney()
							+ "\nNeeded: " + (gameInstance.getLevelHandler().getStoredAmount() + gWorld.getMovables().size) + " of " + gameInstance.getLevelHandler().getPassLevelAmount());
				}
				if(gWorld.getPlayer().getPlayerMoney() < fruitfulMoney)
					fruitfulButton.setDisabled(true);
				else
					fruitfulButton.setDisabled(false);

				if(gWorld.getPlayer().getPlayerMoney() < longerMoney)
					longerButton.setDisabled(true);
				else
					longerButton.setDisabled(false);

				if(gWorld.getPlayer().getPlayerMoney() < moreMoney)
					moreButton.setDisabled(true);
				else
					moreButton.setDisabled(false);

				if(gWorld.getPlayer().getPlayerMoney() < dogMoney)
					dogButton.setDisabled(true);
				else
					dogButton.setDisabled(false);
			}
		});

		dogButton.addListener(new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if(!dogButton.isDisabled()) {
					if(gWorld.getDrawMap().get("Boosts").size <= 0) {
						SoundHandler.playButtonClick();
						gWorld.getPlayer().subtractPlayerMoney(dogMoney);
						Dog newDog = new Dog(new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2), gameInstance.getLevelHandler().animalChangeX(), gameInstance.getLevelHandler().animalChangeY());
						Gdx.app.log("Doge", "Added new dog at " + newDog.getPosition().toString());
						gWorld.getDrawMap().get("Boosts").add(newDog);
						infoLabel.setText("Money: $" + gWorld.getPlayer().getPlayerMoney()
								+ "\nNeeded: " + (gameInstance.getLevelHandler().getStoredAmount() + gWorld.getMovables().size) + " of " + gameInstance.getLevelHandler().getPassLevelAmount());
					} else {
						gWorld.getDrawMap().get("Boosts").clear();
					}
				}
				if(gWorld.getPlayer().getPlayerMoney() < fruitfulMoney)
					fruitfulButton.setDisabled(true);
				else
					fruitfulButton.setDisabled(false);

				if(gWorld.getPlayer().getPlayerMoney() < longerMoney)
					longerButton.setDisabled(true);
				else
					longerButton.setDisabled(false);

				if(gWorld.getPlayer().getPlayerMoney() < moreMoney)
					moreButton.setDisabled(true);
				else
					moreButton.setDisabled(false);

				if(gWorld.getPlayer().getPlayerMoney() < dogMoney)
					dogButton.setDisabled(true);
				else
					dogButton.setDisabled(false);
			}

		});

		//set disabled
		if(gWorld.getPlayer().getPlayerMoney() < 15){
			fruitfulButton.setDisabled(true);
		}
		if(gWorld.getPlayer().getPlayerMoney() < 5){
			longerButton.setDisabled(true);
		}
		if(gWorld.getPlayer().getPlayerMoney() < 10){
			moreButton.setDisabled(true);
		}
		if(gWorld.getPlayer().getPlayerMoney() < 100){
			dogButton.setDisabled(true);
		}

		//pack labels
		upgradesStatusGroup.addActor(upgradeLabel);
		upgradesStatusGroup.addActor(fruitfulLabel);
		upgradesStatusGroup.addActor(longerLabel);
		upgradesStatusGroup.addActor(moreLabel);
		//upgradesStatusGroup.addActor(dogButton);

		upgradesStatusGroup.pack();
		upgradesStatusGroup.setPosition(Gdx.graphics.getWidth()/2f - upgradesStatusGroup.getWidth()/2f,
				EDGE_TOLERANCE*2f);

		upgradesStatusGroupImage = new Image(new Texture(Gdx.files.internal("backgrounds/menuBackground.png")));
		upgradesStatusGroupImage.setPosition(upgradesStatusGroup.getX() - EDGE_TOLERANCE,
				upgradesStatusGroup.getY() - EDGE_TOLERANCE);
		upgradesStatusGroupImage.setSize(upgradesStatusGroup.getWidth() + EDGE_TOLERANCE*2f,
				upgradesStatusGroup.getHeight() + EDGE_TOLERANCE*2f);


		upgradesGroup1.addActor(fruitfulButton);
		upgradesGroup1.addActor(longerButton);
		upgradesGroup1.addActor(moreButton);
		upgradesGroup2.addActor(dogButton);

		upgradesGroup1.pack();
		upgradesGroup1.setHeight(BUTTON_HEIGHT);
		upgradesGroup2.pack();
		upgradesGroup2.setHeight(BUTTON_HEIGHT);

		//For two rows
		upgradesGroupImage.setSize(Math.max(upgradesGroup1.getWidth(), upgradesGroup2.getWidth()) + EDGE_TOLERANCE*2f,
				upgradesGroup1.getHeight() + upgradesGroup2.getHeight() + 3f*EDGE_TOLERANCE);
		upgradesGroupImage.setPosition(gScreen.getAlexButton().getX() + gScreen.getAlexButton().getWidth() + EDGE_TOLERANCE,
				upgradesGroup2.getY() - EDGE_TOLERANCE);
		//For single row
		/*upgradesGroupImage.setSize(upgradesGroup1.getWidth() + 2f*EDGE_TOLERANCE, upgradesGroup1.getHeight());
		upgradesGroupImage.setPosition(alexButton.getX() + alexButton.getWidth() + EDGE_TOLERANCE,
				alexButton.getY() - 2f*BUTTON_HEIGHT - 2f*EDGE_TOLERANCE);*/
	}
	
	/**
	 * Toggles main menu group items
	 * @param checked
	 */
	public void handleMainMenu(boolean checked) {
		if (checked){
			Gdx.app.log("My Tagg", "menuGroupImage is " + (menuGroupImage != null));
			buttonStage.addActor(menuGroupImage);
			buttonStage.addActor(menuBackgroundButton);
			buttonStage.addActor(menuGroup);
			menuBackgroundButton.toBack();
		}
		else{
			menuGroupImage.remove();
			menuGroup.remove();
			menuBackgroundButton.remove();

			//collapse children menus
			handleInventoryMenu(false);
			handleUpgradesMenu(false);
			handleOptionsMenu(false);
		}
	}

	/**
	 * Toggles inventory menu items
	 * @param checked
	 */
	public void handleInventoryMenu(boolean checked){
		if (checked){
			buttonStage.addActor(inventoryGroupImage);
			buttonStage.addActor(inventoryGroup);
			for (Consumable.DropType d : Consumable.DropType.values()){
				((ImageTextButton) inventoryGroup.findActor(d.getName())).setText("x" + gWorld.getPlayer().getInventory().getInventory().get(d).size);
			}
			//from here has to update on button visibility
		}
		else{
			inventoryGroup.remove();
			inventoryGroupImage.remove();
			if (!gScreen.getAlexButton().isChecked())
				inventoryGroupButton.setChecked(false);
		}
	}

	/**
	 * Toggles upgrades menu items
	 * @param checked
	 */
	public void handleUpgradesMenu(boolean checked){
		if (checked){
			buttonStage.addActor(upgradesGroupImage);
			buttonStage.addActor(upgradesGroup1);
			buttonStage.addActor(upgradesStatusGroupImage);
			buttonStage.addActor(upgradesStatusGroup);
			buttonStage.addActor(upgradesGroup2);

			if(gWorld.getPlayer().getPlayerMoney() < fruitfulMoney)
				((Button) upgradesGroup1.findActor("fruitfulButton")).setDisabled(true);
			else
				((Button) upgradesGroup1.findActor("fruitfulButton")).setDisabled(false);

			if(gWorld.getPlayer().getPlayerMoney() < longerMoney)
				((Button) upgradesGroup1.findActor("longerButton")).setDisabled(true);
			else
				((Button) upgradesGroup1.findActor("longerButton")).setDisabled(false);

			if(gWorld.getPlayer().getPlayerMoney() < moreMoney)
				((Button) upgradesGroup1.findActor("moreButton")).setDisabled(true);
			else
				((Button) upgradesGroup1.findActor("moreButton")).setDisabled(false);

			if(gWorld.getPlayer().getPlayerMoney() < dogMoney)
				((Button) upgradesGroup2.findActor("dogButton")).setDisabled(true);
			else
				((Button) upgradesGroup2.findActor("dogButton")).setDisabled(false);

			System.out.println(((Button) upgradesGroup1.findActor("fruitfulButton")).isDisabled());
		}
		else{
			upgradesGroup1.remove();
			upgradesGroup2.remove();
			upgradesGroupImage.remove();
			if (upgradesStatusGroup!=null){
				upgradesStatusGroup.remove();
				upgradesStatusGroupImage.remove();
			}
			if (!gScreen.getAlexButton().isChecked())
				upgradesGroupButton.setChecked(false);
		}
	}
	/**
	 * Toggles options menu items
	 * @param checked
	 */
	public void handleOptionsMenu(boolean checked){
		if (checked){
			buttonStage.addActor(optionsGroupImage);
			buttonStage.addActor(optionsGroup);
		}
		else{
			optionsGroup.remove();
			optionsGroupImage.remove();
			if (!gScreen.getAlexButton().isChecked())
				optionsGroupButton.setChecked(false);
		}
	}


	public Image getOptionsGroupImage() {
		return optionsGroupImage;
	}


	public HorizontalGroup getOptionsGroup() {
		return optionsGroup;
	}


	public Button getOptionsGroupButton() {
		return optionsGroupButton;
	}
	

}
