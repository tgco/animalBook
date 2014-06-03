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
import com.tgco.animalBook.gameObjects.Player;

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
		drawMap.put("Obstacle", new Array<ABDrawable>());
		drawMap.put("Player", new Array<ABDrawable>());
		drawMap.get("Player").add(player);

	}

	public void addFruitfullMoneyP() {
		this.fruitfullMoneyP += 1;
	}

	public void addLongerMoneyP() {
		LongerMoneyP += 1;
	}

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
	 * Returns the camera used to view the world
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

	public int getFruitfullMoneyP() {
		return fruitfullMoneyP;
	}

	public int getLongerMoneyP() {
		return LongerMoneyP;
	}

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
		worldRender.render(batch, drawMap, player.getHealth(),camera);
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


		/*for (ABDrawable dropped : drawMap.get("Dropped")){
			((Movable) dropped).move(cameraSpeed, delta);
			//Remove uncollected drops
			if((((Dropped) dropped).getTimeLeft() <= 0) && !(((Dropped) dropped).isPickedUp())){
				drawMap.get("Dropped").removeValue(dropped, true);
				dropped.dispose();
			}
		}*/

		for (ABDrawable movable : drawMap.get("Movable")) {
			//move animals if necessary
			((Movable) movable).move(speed,delta);
			/*
			//Drop new items
			if(rand.nextInt(100) <= 50){
				ABDrawable dropping =  ((Animal)movable).drop();
				if(dropping != null){
					drawMap.get("Dropped").add(dropping);
				}
			}
			 */
		}		

		//check for collisions between the market and the animals
		/*for (ABDrawable movable : drawMap.get("Movable")) {
			if (movable.getBounds().overlaps(market.getBounds())) {
				gameInstance.getLevelHandler().increaseStored();
				drawMap.get("Movable").removeValue(movable, false);
				movable.dispose();
			}
		}*/

		//check for collisions between movable and obstacles
		/*for (ABDrawable movable : drawMap.get("Movable")){
			for (ABDrawable obstacle : drawMap.get("Obstacle")){
				if (movable.getBounds().overlaps(obstacle.getBounds()) && !(movable instanceof Player)){
					((Movable)movable).bounce((Movable)movable, (Obstacle)obstacle);
				}
			}
		}*/

		//check if player reached market
		/*if (player.getBounds().overlaps(market.getBounds())) {
			SoundHandler.pauseBackgroundMusic();
			gameInstance.setScreen(new MarketScreen(gameInstance, gameInstance.getGameScreen()));
		}*/
	}

	public void reinitTextureMovable(){
		for (ABDrawable movable : drawMap.get("Movable")) {
			((Animal)movable).resetTexture();
		}
	}
	public void reinitTextureDropped(){
		for (ABDrawable dropped : drawMap.get("Dropped")) {
			((Dropped)dropped).resetTexture();
		}
	}

}
