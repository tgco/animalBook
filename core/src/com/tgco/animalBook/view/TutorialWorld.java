package com.tgco.animalBook.view;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.gameObjects.ABDrawable;
import com.tgco.animalBook.gameObjects.Animal;
import com.tgco.animalBook.gameObjects.Consumable;
import com.tgco.animalBook.gameObjects.Dropped;
import com.tgco.animalBook.gameObjects.Market;
import com.tgco.animalBook.gameObjects.Movable;
import com.tgco.animalBook.gameObjects.Obstacle;
import com.tgco.animalBook.gameObjects.Player;
import com.tgco.animalBook.handlers.SoundHandler;
import com.tgco.animalBook.screens.TutorialMarketScreen;
import com.tgco.animalBook.screens.TutorialScreen;

public class TutorialWorld {

	/**
	 * The number of animals the player has
	 */
	private int numAnimals = 5;

	/**
	 * The camera used to view the world
	 */
	private OrthographicCamera camera;

	/**
	 * The speed the camera moves up the lane
	 */
	private float cameraSpeed;

	/**
	 * The speed the camera moves up the lane when all animals have reached market
	 */
	private float increasedCameraSpeed;

	/**
	 * Array of objects that will be drawn to the screen
	 */
	private ArrayMap<String, Array<ABDrawable>> drawMap;

	/**
	 * Number of times each upgrade button has been pressed
	 */
	private int fruitfullMoneyP	= 0;

	/**
	 * Reference to the running game instance
	 */
	private AnimalBookGame gameInstance;

	private int LongerMoneyP	= 0;

	/**
	 * Market located at end of the game level
	 */
	private Market market;
	
	/**
	 * Number of times the More Drops have been counted
	 */
	private int MoreMoneyP		= 0;
	/**
	 * The main player
	 */
	private Player player;

	/**
	 * Generates random numbers for probability
	 */
	private Random rand = new Random();

	/**
	 * Handles all of the game object rendering responsibility
	 */
	private WorldRenderer worldRender;

	/**
	 * Boolean for generating obstacle and market once during tutorial
	 */
	private boolean generatedMarketAndObstacle = false;

	/**
	 * Constructor with game instance
	 * 
	 * @param gameInstance reference to the running game instance
	 */
	public TutorialWorld(AnimalBookGame gameInstance) {
		this.gameInstance = gameInstance;
		drawMap = new ArrayMap<String, Array<ABDrawable>>();


		worldRender = new WorldRenderer();


		//Camera initialization
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		camera.update();

		cameraSpeed =  gameInstance.getLevelHandler().returnCameraSpeed(gameInstance.getLevelHandler().getLevel());	
		increasedCameraSpeed = 2f*cameraSpeed;

		player = new Player(cameraSpeed);

		drawMap.put("Movable", gameInstance.getLevelHandler().addAnimals(1));
		drawMap.put("Dropped", new Array<ABDrawable>());
		drawMap.put("Market", new Array<ABDrawable>());
		drawMap.put("Obstacle", new Array<ABDrawable>());
		drawMap.put("Player", new Array<ABDrawable>());
		drawMap.get("Player").add(player);

	}

	/**
	 * adds 1 to the fruitfull upgrade 
	 */
	public void addFruitfullMoneyP() {
		this.fruitfullMoneyP += 1;
	}

	/**
	 * adds 1 to the Longer duration upgrade
	 */
	public void addLongerMoneyP() {
		LongerMoneyP += 1;
	}

	/**
	 * adds 1 to the more drops upgrade
	 */
	public void addMoreMoneyP() {
		MoreMoneyP += 1;
	}

	/**
	 * Adds a swipe line to the world so it will be rendered
	 * 
	 * @param begin the point where the swipe begins
	 * @param end	the point where the swipe ends
	 */
	public void addSwipeToWorld(Vector3 begin, Vector3 end) {
		worldRender.addSwipe(new Vector2(begin.x,begin.y), new Vector2(end.x,end.y));
	}

	/**
	 * Disposes all objects that need to release memory
	 */
	public void dispose() {
		for (Array<ABDrawable> drawableArrays : drawMap.values()){
			for (ABDrawable drawable : drawableArrays) {
				drawable.dispose();
			}
		}
		worldRender.dispose();
	}

	/**
	 * Returns the camera used to view the world to get proper coordinates
	 * 
	 * @return		the camera currently in use
	 */
	public OrthographicCamera getCamera() {
		return camera;
	}

	/**
	 * Finds which drawables are dropped items, casts them to dropped and returns them in an array
	 * 
	 * @return		an array of all dropped items currently in the world
	 */
	public Array<Dropped> getDropped() {
		Array<Dropped> droppings = new Array<Dropped>();
		for (ABDrawable aBDrawable : drawMap.get("Dropped")) {
			droppings.add((Dropped) aBDrawable);
		}
		return droppings;
	}

	/**
	 * returns the number of presses for the fruitful upgrade for which level it is on
	 * @return
	 */
	public int getFruitfullMoneyP() {
		return fruitfullMoneyP;
	}

	/**
	 * returns the number of presses for the longer duration upgrade for which level it is on
	 * @return
	 */
	public int getLongerMoneyP() {
		return LongerMoneyP;
	}

	/**
	 * returns the  number of presses for the more drops upgrade for which level it is on
	 * @return
	 */
	public int getMoreMoneyP() {
		return MoreMoneyP;
	}

	/**
	 * Finds which drawables are movable, casts them to movable and returns them in an array
	 * 
	 * @return		an array of all movable objects currently in the world
	 */
	public Array<Movable> getMovables() {
		Array<Movable> movables = new Array<Movable>();
		for (ABDrawable aBDrawable : drawMap.get("Movable")){
			movables.add((Movable) aBDrawable);
		}
		return movables;
	}

	/**
	 * returns the player for the screens to use money, health and inventory
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Updates the camera position to progress towards the end of the level
	 * 
	 * @param speed the desired speed for the camera movement
	 */
	public void moveCameraUp(float speed,float delta) {
		camera.position.y += speed*(AnimalBookGame.TARGET_FRAME_RATE*delta);
		camera.update();
	}

	/**
	 * Removes the dropped item from the drawn objects, and handles the logic for the collection of the item
	 * 
	 * @param dropped the dropped item on screen that is to be removed
	 */
	public void removeFromABDrawable(ABDrawable dropped){
		if(((Dropped) dropped).getDropped() instanceof Animal){
			drawMap.get("Movable").add(((Dropped) dropped).getDropped());
		}
		else{
			player.getInventory().addItem((Consumable) ((Dropped) dropped).getDropped());
		}
		drawMap.get("Dropped").removeValue(dropped, true);
	}

	/**
	 * Updates game logic and begins all drawing
	 * 
	 * @param batch  the sprite batch that is being used for drawing
	 * @param paused if the game is currently paused
	 * @param delta  the time between frames
	 */
	public void render(SpriteBatch batch, boolean paused,float delta) {
		if (!paused){
			updateGameLogic(delta);
		}

		//draw objects
		worldRender.render(batch, drawMap, player.getHealth(),camera,delta);
	}


	/**
	 * Updates all logic between game objects and moves them if necessary
	 * 
	 * @param delta the time between frames on the device running
	 */
	public void updateGameLogic(float delta) {

		float speed;
		//Health effects
		player.decreaseHealth(.01f);

		if(getMovables().size <=0 && gameInstance.getLevelHandler().getStoredAmount() > 0) {
			speed = increasedCameraSpeed*(player.getHealth()/100);
		} else {
			speed = cameraSpeed*(player.getHealth()/100);
		}

		player.setSpeed(speed);

		//move the camera and player
		moveCameraUp(speed,delta);
		player.move(speed,delta);

		for (ABDrawable movable : drawMap.get("Movable")) {
			//move animals if necessary
			((Movable) movable).move(speed,delta);
		}		
		
		//Bounce animals off of each other
		Array<ABDrawable> movables = drawMap.get("Movable");
		for (int i = 0; i < movables.size; i ++) {
			for (int j = i + 1; j < movables.size; j++) {
				//collision check
				if (movables.get(i).getBounds().overlaps(movables.get(j).getBounds())) {
					if (movables.get(i).getPosition().cpy().dst(movables.get(j).getPosition().cpy()) < 2f*movables.get(i).getHeight()/3f)
						((Movable)movables.get(i)).bounce((Movable)(movables.get(j)), null);
				}
			}
		}

		if (generatedMarketAndObstacle) {
			//check for collisions between the market and the animals
			for (ABDrawable movable : drawMap.get("Movable")) {
				if (movable.getBounds().overlaps(market.getBounds())) {
					gameInstance.getLevelHandler().increaseStored();
					drawMap.get("Movable").removeValue(movable, false);
					movable.dispose();
				}
			}


			//check for collisions between movable and obstacles
			for (ABDrawable movable : drawMap.get("Movable")){
				for (ABDrawable obstacle : drawMap.get("Obstacle")){
					if (movable.getBounds().overlaps(obstacle.getBounds()) && !(movable.getClass().equals(Player.class))) {
						((Movable)movable).bounce(null, (Obstacle)obstacle);
						if ((movable.getPosition().y + movable.getHeight()/2) < (obstacle.getPosition().y))
							((Movable)movable).stopForwardBias(cameraSpeed,delta);
					}
				}
			}
			
			

			//check if player reached market
			if (player.getBounds().overlaps(market.getBounds())) {
				SoundHandler.pauseBackgroundMusic();
				gameInstance.setScreen(new TutorialMarketScreen(gameInstance, (TutorialScreen)gameInstance.getScreen()));
			}
		}
	}

	/**
	 * on reseting from Main menu this reinitalizes the movable object pictures
	 */
	public void reinitTextureMovable(){
		for (ABDrawable movable : drawMap.get("Movable")) {
			((Animal)movable).resetTexture();
		}
	}
	
	/**
	 * on resetting from Main Menu this reinitalizes the dropped object textures
	 */
	public void reinitTextureDropped(){
		for (ABDrawable dropped : drawMap.get("Dropped")) {
			((Dropped)dropped).resetTexture();
		}
	}

	/**
	 * Spawns one egg on screen for tutorial
	 */
	public void spawnEgg() {
		//Add an egg if there are none on screen at the center of the screen
		Movable movable = (Movable) drawMap.get("Movable").get(0);
		if (drawMap.get("Dropped").size == 0) {
			ABDrawable dropping =  ((Animal)movable).forceDropConsumable();
			drawMap.get("Dropped").add(dropping);
		}
	}

	/**
	 * Removes the tutorial egg
	 */
	public void removeSpawnedEgg() {
		drawMap.get("Dropped").get(0).dispose();
		drawMap.get("Dropped").clear();
	}

	/**
	 * Spawns the market and obstacle for the tutorial
	 */
	public void spawnObstacleAndMarket() {
		if (!generatedMarketAndObstacle) {
			market = new Market();
			Obstacle obstacle = new Obstacle();
			market.setPosition(new Vector2(player.getPosition().cpy().x, player.getPosition().cpy().y + Gdx.graphics.getHeight()));
			obstacle.setPosition(new Vector2(market.getPosition().cpy().x,market.getPosition().y - Gdx.graphics.getHeight()/3));
			drawMap.get("Market").add(market);
			drawMap.get("Obstacle").add(obstacle);
			generatedMarketAndObstacle = true;
		}
	}
}
