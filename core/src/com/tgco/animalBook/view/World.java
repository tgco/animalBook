package com.tgco.animalBook.view;

import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
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
 * 
 * @author
 *
 * Initializes game objects and handles the logic between them for game play
 */
public class World {

	/**
	 * Reference to the running game instance
	 */
	private AnimalBookGame gameInstance;

	/**
	 * The camera used to view the world
	 */
	private OrthographicCamera camera;
	
	/**
	 * The speed the camera moves up the lane
	 */
	private float cameraSpeed;

	/**
	 * Handles all of the game object rendering responsibility
	 */
	private WorldRenderer worldRender;

	/**
	 * Array of objects that will be drawn to the screen
	 */
	private Array<ABDrawable> aBDrawables;
	
	/**
	 * Market located at end of the game level
	 */
	private Market market;

	/**
	 * The distance the player must walk to reach the market
	 */
	private float laneLength;
	
	/**
	 * Distance an animal can be from the player before it is lost and removed
	 */
	private static final float LOST_ANIMAL_TOLERANCE = 2*Gdx.graphics.getWidth()/4;

	/**
	 * The main player
	 */
	private Player player;

	/**
	 * The current level being played
	 */
	private static int level = 1;

	/**
	 * The number of animals the player has
	 */
	private static final int NUM_ANIMALS = 5;
	
	/**
	 * Number of times each upgrade button has been pressed
	 */
	private int fruitfullMoneyP =0;
	private int LongerMoneyP =0;
	private int MoreMoneyP =0;
	
	/**
	 * Generates random numbers for probability
	 */
	private Random rand = new Random();
	
	/**
	 * Load all information that differs between levels
	 */
	private LevelHandler levelHandler;
	
	/**
	 * Constructor with game instance
	 * 
	 * @param gameInstance reference to the running game instance
	 */
	public World(AnimalBookGame gameInstance) {
		this.gameInstance = gameInstance;

		aBDrawables = new Array<ABDrawable>();
		worldRender = new WorldRenderer();
		
		levelHandler = new LevelHandler(level);

		//Camera initialization
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		camera.update();
		cameraSpeed = levelHandler.returnCameraSpeed(level);
		
		aBDrawables.addAll(levelHandler.addAnimals(level, NUM_ANIMALS));

		player = new Player(cameraSpeed);

		//Make the market and set it at the end
		laneLength = levelHandler.returnLaneLength(level);
		market = new Market();
		market.setPosition(new Vector2(player.getPosition().cpy().x, player.getPosition().cpy().y + laneLength + player.getHeight()));

		aBDrawables.add(market);
		
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
		worldRender.render(batch, aBDrawables, player, 1f - (market.getPosition().y - player.getPosition().y - player.getHeight())/(laneLength),camera);
	}

	/**
	 * Updates all logic between game objects and moves them if necessary
	 */
	public void updateGameLogic() {
		//move the camera
		moveCameraUp(cameraSpeed);

		for (ABDrawable aBDrawable : aBDrawables) {
			//Remove uncollected drops
			if(aBDrawable.isDropping() && ((Dropped) aBDrawable).getTimeLeft() <= 0){
				aBDrawables.removeValue(aBDrawable, true);
			}
			
			if (aBDrawable.isMovable()){
				//move animals if necessary
				((Movable) aBDrawable).move(cameraSpeed);
				//Drop new items
				if(rand.nextInt(100) <= 50){
					ABDrawable dropping =  ((Animal)aBDrawable).drop();
					if(dropping != null){
						aBDrawables.add(dropping);
					}
				}
					
			}
				
		}

		//move player
		player.move(cameraSpeed);

		//Health effects
		player.decreaseHealth(.01f);
		player.setSpeed(.2f*(player.getHealth()/100));
		cameraSpeed = .2f*(player.getHealth()/100);

		//check for and remove lost animals
		for (ABDrawable drawable : aBDrawables) {
			if (!drawable.isMarket()) {
				if (drawable.getPosition().cpy().sub(player.getPosition()).len() > LOST_ANIMAL_TOLERANCE) {
					aBDrawables.removeValue(drawable, false);
				}
			}
		}

		//check for collisions between the market and the player/geese
		for (ABDrawable aBDrawable : aBDrawables) {
			
			if (aBDrawable.getBounds().overlaps(market.getBounds())) {
				if (!aBDrawable.isMarket()) {
					levelHandler.increaseStored();
					aBDrawables.removeValue(aBDrawable, false);
				}
			}
		}

		//check if player reached market
		if (player.getBounds().overlaps(market.getBounds())) {
			SoundHandler.pauseBackgroundMusic();
			gameInstance.setScreen(new MarketScreen(gameInstance,gameInstance.getGameScreen()));
		}
		
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
	 * Updates the camera position to progress towards the end of the level
	 * 
	 * @param speed the desired speed for the camera movement
	 */
	public void moveCameraUp(float speed) {
		camera.position.y += speed;
		camera.update();
	}

	/**
	 * Disposes all objects that need to release memory
	 */
	public void dispose() {
		for (ABDrawable aBDrawable : aBDrawables) {
			aBDrawable.dispose();
		}
		
		worldRender.dispose();
	}

	/**
	 * Finds which drawables are movable, casts them to movable and returns them in an array
	 * 
	 * @return		an array of all movable objects currently in the world
	 */
	public Array<Movable> getMovables() {
		Array<Movable> movables = new Array<Movable>();
		for (ABDrawable aBDrawable : aBDrawables) {
			if (aBDrawable.isMovable())
				movables.add((Movable) aBDrawable);
		}
		return movables;
	}
	
	/**
	 * Finds which drawables are dropped items, casts them to dropped and returns them in an array
	 * 
	 * @return		an array of all dropped items currently in the world
	 */
	public Array<Dropped> getDropped() {
		Array<Dropped> droppings = new Array<Dropped>();
		for (ABDrawable aBDrawable : aBDrawables) {
			if (aBDrawable.isDropping()){
				droppings.add((Dropped) aBDrawable);
			}
			
		}
		return droppings;
	}
	
	/**
	 * Adds a swipe line to the world so it will be rendered
	 * 
	 * @param begin the point where the swipe begins
	 * @param end	the point where the swipe ends
	 */
	public void addSwipeToWorld(Vector3 begin, Vector3 end) {
		//camera.project(begin);
		//camera.project(end);
		worldRender.addSwipe(new Vector2(begin.x,begin.y), new Vector2(end.x,end.y));
	}
	
	/**
	 * Removes the dropped item from the drawn objects, and handles the logic for the collection of the item
	 * 
	 * @param dropped the dropped item on screen that is to be removed
	 */
	public void removeFromABDrawable(ABDrawable dropped){
			aBDrawables.removeValue(dropped, true);
			if(((Dropped) dropped).getDropped() instanceof Animal){
				aBDrawables.add(((Dropped) dropped).getDropped());
			}
			else{
				player.getInventory().addItem((Consumable) ((Dropped) dropped).getDropped());
			}
	}
	
	/**
	 * Returns the camera used to view the world
	 * 
	 * @return		the camera currently in use
	 */
	public OrthographicCamera getCamera() {
		return camera;
	}
	
	
	public int getFruitfullMoneyP() {
		return fruitfullMoneyP;
	}

	public void addFruitfullMoneyP() {
		this.fruitfullMoneyP += 1;
	}

	public int getLongerMoneyP() {
		return LongerMoneyP;
	}

	public void addLongerMoneyP() {
		LongerMoneyP += 1;
	}

	public int getMoreMoneyP() {
		return MoreMoneyP;
	}

	public void addMoreMoneyP() {
		MoreMoneyP += 1;
	}
	
	public LevelHandler getLevelHandler() {
		return levelHandler;
	}
	
	public Player getPlayer() {
		return player;
	}
}
