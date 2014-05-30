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
import com.tgco.animalBook.handlers.LevelHandler;
import com.tgco.animalBook.handlers.SoundHandler;
import com.tgco.animalBook.screens.MarketScreen;

/**
 *  Initializes game objects and handles the logic between them for game play
 *  
 * @author
 *
 *
 */
public class World {

	/**
	 * The current level being played
	 */
	private static int level = 1;

	/**
	 * Distance an animal can be from the player before it is lost and removed
	 */
	private static final float LOST_ANIMAL_TOLERANCE = 2*Gdx.graphics.getWidth()/4;

	/**
	 * The number of animals the player has
	 */
	private static int numAnimals = 5;

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
	private float increasedCameraSpeed = 2f*cameraSpeed;

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

	/**
	 * The distance the player must walk to reach the market
	 */
	private float laneLength;

	/**
	 * Load all information that differs between levels
	 */
	private LevelHandler levelHandler;

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
	public World(AnimalBookGame gameInstance) {
		this.gameInstance = gameInstance;
		drawMap = new ArrayMap<String, Array<ABDrawable>>();
		
		boolean levelSize = gameInstance.getLevelData().size >0;
		// spot 1 is current level
		//spot 2 is player			
		
		//spot 4 is storing dropped items array
		
		if(levelSize && gameInstance.getLevelData().get(0) !=null){
			level = (Integer) gameInstance.getLevelData().get(0);
		}
		worldRender = new WorldRenderer();
		levelHandler = new LevelHandler(level);
		
		//spot 3 is storing movable array
		if(levelSize && gameInstance.getLevelData().get(2) !=null){
			//Gdx.app.log("My tag", "the size of the movable is " +((Array<ABDrawable>)gameInstance.getLevelData().get(2)).size);
			drawMap.put("Movable", (Array<ABDrawable>) gameInstance.getLevelData().get(2));	
		}else{
			drawMap.put("Movable", levelHandler.addAnimals(level, numAnimals));
		}
		

		//Camera initialization
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		camera.update();
		cameraSpeed = levelHandler.returnCameraSpeed(level);	
		if(levelSize && gameInstance.getLevelData().get(1) != null){
			player = (Player) gameInstance.getLevelData().get(1);
			player.resetPlayerPosition();
		}else{
			player = new Player(cameraSpeed);
		}

		//Make the market and set it at the end
		laneLength = levelHandler.returnLaneLength(level);
		market = new Market();
		market.setPosition(new Vector2(player.getPosition().cpy().x, player.getPosition().cpy().y + laneLength + player.getHeight()));
		drawMap.put("Market", new Array<ABDrawable>());
		drawMap.get("Market").add(market);

		if(levelSize && gameInstance.getLevelData().get(3) != null){
			drawMap.put("Dropped", (Array<ABDrawable>) gameInstance.getLevelData().get(3));
		}else{
			drawMap.put("Dropped", new Array<ABDrawable>());
		}
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
	 * Checks if the player lost the current level
	 */
	public void checkLost(){
		if(getMovables().size <=0 && levelHandler.getStoredAmount() <= 0 ){
			SoundHandler.toggleSounds();
			SoundHandler.toggleMusic();
			gameInstance.getGameScreen().setLost(true);
		}
		else if( player.getHealth() <=0){
			SoundHandler.toggleSounds();
			SoundHandler.toggleMusic();
			gameInstance.getGameScreen().setLost(false);
		}
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

	public LevelHandler getLevelHandler() {
		return levelHandler;
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
	public void moveCameraUp(float speed) {
		camera.position.y += speed;
		camera.update();
	}

	/**
	 * Removes the dropped item from the drawn objects, and handles the logic for the collection of the item
	 * 
	 * @param dropped the dropped item on screen that is to be removed
	 */
	public void removeFromABDrawable(ABDrawable dropped){
		drawMap.get("Dropped").removeValue(dropped, true);
		if(((Dropped) dropped).getDropped() instanceof Animal){
			drawMap.get("Movable").add(((Dropped) dropped).getDropped());
		}
		else{
			player.getInventory().addItem((Consumable) ((Dropped) dropped).getDropped());
		}
	}

	/**
	 * Updates game logic and begins all drawing
	 * 
	 * @param batch  the sprite batch that is being used for drawing
	 * @param paused if the game is currently paused
	 */
	public void render(SpriteBatch batch, boolean paused) {
		if (!paused){
			updateGameLogic();
			checkLost();
		}

		//draw objects
		worldRender.render(batch, drawMap, player.getHealth(), 1f - (market.getPosition().y - player.getPosition().y - player.getHeight())/(laneLength),camera);
	}

	/**
	 * Updates all logic between game objects and moves them if necessary
	 */
	public void updateGameLogic() {
		
		float speed;
		//move the camera
		if(getMovables().size <=0 && levelHandler.getStoredAmount() > 0) {
			speed = increasedCameraSpeed;
		} else {
			speed = cameraSpeed;
		}
		Gdx.app.log("Speed is: ", String.valueOf(speed));
		
		moveCameraUp(speed);

		for (ABDrawable dropped : drawMap.get("Dropped")){
			//Remove uncollected drops
			if(((Dropped) dropped).getTimeLeft() <= 0){
				drawMap.get("Dropped").removeValue(dropped, true);
				dropped.dispose();
			}
		}

		for (ABDrawable movable : drawMap.get("Movable")) {
			//move animals if necessary
			((Movable) movable).move(speed);
			//Drop new items
			if(rand.nextInt(100) <= 50){
				ABDrawable dropping =  ((Animal)movable).drop();
				if(dropping != null){
					drawMap.get("Dropped").add(dropping);
				}
			}
		}

		//move player
		player.move(speed);

		//Health effects
		player.decreaseHealth(.01f);
		player.setSpeed(cameraSpeed*(player.getHealth()/100));
		speed = cameraSpeed*(player.getHealth()/100);

		//check for and remove lost animals
		for (ABDrawable drawable : drawMap.get("Movable")) {

			if (drawable.getPosition().cpy().sub(player.getPosition()).len() > LOST_ANIMAL_TOLERANCE) {
				drawMap.get("Movable").removeValue(drawable, false);
				drawable.dispose();
			}
		}

		//check for collisions between the market and the animals
		for (ABDrawable movable : drawMap.get("Movable")) {
			if (movable.getBounds().overlaps(market.getBounds())) {
				levelHandler.increaseStored();
				drawMap.get("Movable").removeValue(movable, false);
				movable.dispose();
			}
		}

		//check if player reached market
		if (player.getBounds().overlaps(market.getBounds())) {
			SoundHandler.pauseBackgroundMusic();
			gameInstance.setScreen(new MarketScreen(gameInstance, gameInstance.getGameScreen()));
		}
	}

	public void addToLevel(int i) {
		levelHandler.addLevel();	
	}
}
