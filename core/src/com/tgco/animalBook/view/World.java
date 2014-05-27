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

//Generates game objects and handles game logic between them
public class World {

	//reference to the game instance
	private AnimalBookGame gameInstance;

	//Camera to view the world
	private OrthographicCamera camera;
	private float cameraSpeed;

	//Rendering object
	private WorldRenderer worldRender;

	//All game objects to be drawn
	private Array<ABDrawable> aBDrawables;
	private Market market;

	//Lane length for this level
	private float laneLength;
	//Distance an animal is from player before it is lost
	private static final float LOST_ANIMAL_TOLERANCE = 2*Gdx.graphics.getWidth()/3;

	//The player character
	private Player player;

	private static int level = 5;

	private static final int NUM_ANIMALS = 5;
	
	//upgrade presses
	private int fruitfullMoneyP =0;
	private int LongerMoneyP =0;
	private int MoreMoneyP =0;
	
	private Random rand = new Random();
	//Displays the amount of animals left
	private BitmapFont font;
	
	//manages level creation
	private LevelHandler levelHandler;
	
	public World(AnimalBookGame gameInstance) {
		this.gameInstance = gameInstance;

		aBDrawables = new Array<ABDrawable>();
		
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

		worldRender = new WorldRenderer();
		
	}

	public void render(SpriteBatch batch, boolean paused) {
		if (!paused)
			updateGameLogic();

		//draw objects
		worldRender.render(batch, aBDrawables, player, 1f - (market.getPosition().y - player.getPosition().y - player.getHeight())/(laneLength),camera);
	}

	public void updateGameLogic() {
		//move the camera
		moveCameraUp(cameraSpeed);

		//move animals if necessary
		for (ABDrawable aBDrawable : aBDrawables) {
			if(aBDrawable.isDropping() && ((Dropped) aBDrawable).getTimeLeft() <=0){
				aBDrawables.removeValue(aBDrawable, true);
			}
			
			if (aBDrawable.isMovable()){
				//Gdx.app.log("My tag", "the size is " + aBDrawable.getClass());
				((Movable) aBDrawable).move(cameraSpeed);
				
				
				
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
					aBDrawables.removeValue(aBDrawable, false);
				}
			}
		}

		
		if (player.getBounds().overlaps(market.getBounds())) {
			SoundHandler.pauseBackgroundMusic();
			gameInstance.setScreen(new MarketScreen(gameInstance,gameInstance.getGameScreen()));
		}
		
	}


	//Moves the camera up at the desired speed
	public void moveCameraUp(float speed) {
		camera.position.y += speed;
		camera.update();
	}


	public OrthographicCamera getCamera() {
		return camera;
	}

	public Player getPlayer() {
		return player;
	}

	public void dispose() {
		for (ABDrawable aBDrawable : aBDrawables) {
			aBDrawable.dispose();
		}
		
		worldRender.dispose();
	}

	public Array<Movable> getMovables() {
		Array<Movable> movables = new Array<Movable>();
		for (ABDrawable aBDrawable : aBDrawables) {
			if (aBDrawable.isMovable())
				movables.add((Movable) aBDrawable);
		}
		return movables;
	}
	
	public Array<Dropped> getDropped() {
		Array<Dropped> droppings = new Array<Dropped>();
		for (ABDrawable aBDrawable : aBDrawables) {
			if (aBDrawable.isDropping()){
				droppings.add((Dropped) aBDrawable);
			}
			
		}
		return droppings;
	}
	public void addSwipeToWorld(Vector3 begin, Vector3 end) {
		//camera.project(begin);
		//camera.project(end);
		worldRender.addSwipe(new Vector2(begin.x,begin.y), new Vector2(end.x,end.y));
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
	public void removeFromABDrawable(ABDrawable dropped){
		//if(aBDrawables.get(index) instanceof Dropped){
		
			aBDrawables.removeValue(dropped, true);
			if(((Dropped) dropped).getDropped() instanceof Animal){
				Gdx.app.log("this is my tag", "the animal is hatched");
				aBDrawables.add(((Dropped) dropped).getDropped());
			}
			else{
				player.getInventory().addItem((Consumable) ((Dropped) dropped).getDropped());
			}
		//}
	}
	
	public LevelHandler getLevelHandler() {
		return levelHandler;
	}
}
