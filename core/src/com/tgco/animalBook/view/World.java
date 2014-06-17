package com.tgco.animalBook.view;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.gameObjects.ABDrawable;
import com.tgco.animalBook.gameObjects.Animal;
import com.tgco.animalBook.gameObjects.Consumable;
import com.tgco.animalBook.gameObjects.Dog;
import com.tgco.animalBook.gameObjects.Dropped;
import com.tgco.animalBook.gameObjects.Market;
import com.tgco.animalBook.gameObjects.Movable;
import com.tgco.animalBook.gameObjects.Obstacle;
import com.tgco.animalBook.gameObjects.Player;
import com.tgco.animalBook.gameObjects.RainDrop;
import com.tgco.animalBook.gameObjects.SnowDrop;
import com.tgco.animalBook.handlers.SoundHandler;
import com.tgco.animalBook.handlers.Weather;
import com.tgco.animalBook.handlers.Weather.WeatherType;
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
	 * The camera used to view the world
	 */
	private OrthographicCamera camera;

	/**
	 * The speed the camera moves up the lane
	 */
	private float cameraSpeed;

	/**
	 * Bounds for losing animals
	 */
	private Rectangle cameraBounds;

	/**
	 * How far off screen an animal can be before it is lost
	 */
	private float tolerance;

	/**
	 * The speed the camera moves up the lane when all animals have reached market
	 */
	private float increasedCameraSpeed;

	/**
	 * Array of objects that will be drawn to the screen
	 */
	private ArrayMap<String, Array<ABDrawable>> drawMap;

	/**
	 * Reference to the running game instance
	 */
	private AnimalBookGame gameInstance;

	/**
	 * The distance the player must walk to reach the market
	 */
	private float laneLength;

	/**
	 * Market located at end of the game level
	 */
	private Market market;

	/**
	 * The main player
	 */
	private Player player;

	private boolean hasDog = false;

	protected static final float BUTTON_WIDTH = (1f/10f)*Gdx.graphics.getWidth();
	protected static final float BUTTON_HEIGHT = (1f/10f)*Gdx.graphics.getWidth();
	protected static final float EDGE_TOLERANCE = (.03f)*Gdx.graphics.getHeight();



	/**
	 * Generates random numbers for probability
	 */
	private Random rand = new Random();

	/**
	 * Handles all of the game object rendering responsibility
	 */
	private WorldRenderer worldRender;

	private Weather weather;
	private float weatherTime = 0f;
	private float targetWeatherTime = 0f;
	private float WEATHER_DURATION = 3f;
	private Vector2 windVector;
	private static final float WEATHER_CLICK = .30f;
	private float weatherClick;
	private float weatherIterator;

	/**
	 * Constructor with game instance and pulls stored info from gameInstance if there is anything stored
	 * 
	 * @param gameInstance reference to the running game instance
	 */
	@SuppressWarnings("unchecked")
	public World(AnimalBookGame gameInstance) {

		this.gameInstance = gameInstance;
		drawMap = new ArrayMap<String, Array<ABDrawable>>();

		boolean levelSize = gameInstance.getLevelData().size > 0;
		// spot 1 is current level
		//spot 2 is player			
		//spot 4 is storing dropped items array

		worldRender = new WorldRenderer();
		laneLength =  gameInstance.getLevelHandler().returnLaneLength(gameInstance.getLevelHandler().getLevel());


		//Camera initialization
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		camera.update();

		Array<ABDrawable> movables;
		boolean reinit = false;


		//spot 3 is storing movable array
		if(levelSize && gameInstance.getLevelData().get(2) != null){
			movables = (Array<ABDrawable>) gameInstance.getLevelData().get(2);	
			reinit = true;
		}else{
			movables = gameInstance.getLevelHandler().addAnimals( gameInstance.getLevelHandler().getLevel());
			//drawMap.put("Movable", gameInstance.getLevelHandler().addAnimals( gameInstance.getLevelHandler().getLevel()));
		}

		tolerance = movables.get(0).getWidth();

		cameraBounds = new Rectangle(camera.position.x - Gdx.graphics.getWidth()/2 - tolerance, camera.position.y - Gdx.graphics.getHeight()/2 - tolerance, Gdx.graphics.getWidth() + 2f*tolerance, Gdx.graphics.getHeight() + 2f*tolerance);

		cameraSpeed =  gameInstance.getLevelHandler().returnCameraSpeed(gameInstance.getLevelHandler().getLevel());	

		if(gameInstance.getLevelHandler().getLevel()>1){
			increasedCameraSpeed = 3f*cameraSpeed;
		}else{
			increasedCameraSpeed = 4f*cameraSpeed;
		}

		drawMap.put("Boosts", new Array<ABDrawable>());

		//Make the market and set it at the end
		laneLength =  gameInstance.getLevelHandler().returnLaneLength(gameInstance.getLevelHandler().getLevel());


		market = new Market();
		market.setPosition(new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 + laneLength));
		drawMap.put("Market", new Array<ABDrawable>());
		drawMap.get("Market").add(market);

		if(levelSize && gameInstance.getLevelData().get(1) != null){
			player = (Player) gameInstance.getLevelData().get(1);
		
			if (!gameInstance.isHitBack()) {
				reinitTexturePlayer();
				player.resetPlayerPosition();
			} else {
				
				camera.position.set(Gdx.graphics.getWidth()/2, gameInstance.getProgPercentage()*laneLength + Gdx.graphics.getHeight()/2, 0);
				reinitTexturePlayer();
				market.setPosition(new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2 + laneLength));
			}
		}else{
			
			player = new Player(cameraSpeed);
		
		}

		//Make the obstacles
		if(levelSize && gameInstance.getLevelData().get(4) !=null){
			drawMap.put("Obstacle", (Array<ABDrawable>) gameInstance.getLevelData().get(4));
			reinitTextureObstacle();
		}else{
			drawMap.put("Obstacle",  gameInstance.getLevelHandler().addObstacles( gameInstance.getLevelHandler().getLevel(), market.getPosition()));
		}

		drawMap.put("Movable", movables);
		if (reinit)
			reinitTextureMovable();

		if(levelSize && gameInstance.getLevelData().get(3) != null){
			drawMap.put("Dropped", (Array<ABDrawable>) gameInstance.getLevelData().get(3));
			reinitTextureDropped();
		}else{
			drawMap.put("Dropped", new Array<ABDrawable>());
		}

		drawMap.put("WeatherDrop", new Array<ABDrawable>());
		
		if (gameInstance.isKidMode()) {
			WEATHER_DURATION = 10f;
		} else {
			WEATHER_DURATION = 7f;
		}

		weather = new Weather();
		weather.setWeatherType(WeatherType.CLEAR);
		targetWeatherTime = rand.nextFloat()%WEATHER_DURATION + WEATHER_DURATION;
		windVector = new Vector2();
		weatherClick = 0;
		worldRender.setClear(true);
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
	 * Checks if the player lost the current level either by no animals on screen or by running out of health
	 */
	public void checkLost(){
		if(getMovables().size <=0 &&  gameInstance.getLevelHandler().getStoredAmount() <= 0 ){
			SoundHandler.toggleSounds();
			SoundHandler.toggleMusic();
			gameInstance.setHitBack(true);
			gameInstance.setProgPercentage(getPrecentage());
			gameInstance.getGameScreen().setLost(true);
		}
		else if( player.getHealth() <=0){
			SoundHandler.toggleSounds();
			SoundHandler.toggleMusic();
			gameInstance.setHitBack(true);
			gameInstance.setProgPercentage(getPrecentage());
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
	 * returns the player for the screens to use health, money, and inventory
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
			checkLost();
		}


		worldRender.render(batch, drawMap, player.getHealth(), 1f - (market.getPosition().y - camera.position.y)/(laneLength),camera,delta);
	}

	/**
	 * Updates all logic between game objects and moves them if necessary
	 * 
	 * @param delta the time between frames on the device running
	 */
	public void updateGameLogic(float delta) {

		float speed;

		//Health effects
		player.decreaseHealth(gameInstance.getLevelHandler().getHealthDecrease());

		if(getMovables().size <=0 && gameInstance.getLevelHandler().getStoredAmount() > 0) {
			speed = increasedCameraSpeed*(player.getHealth()/100);
		} else if (weather.getWeather() == WeatherType.RAINY) {
			if (gameInstance.isKidMode()) {
				speed = (cameraSpeed*player.getHealth()/100)/(4f/3f);
			} else {
				speed = (cameraSpeed*(player.getHealth()/100))/2f;
			}
		} else if (weather.getWeather() == WeatherType.SNOWY){
			speed = 0f;

			if (gameInstance.isKidMode()) {
				speed = (cameraSpeed*(player.getHealth()/100))/(3f/2f);
			} else {
				speed = 0;
			}
		}
		else {
			speed = (cameraSpeed*(player.getHealth()/100));
		}

		player.setSpeed(speed);

		//move the camera and player
		moveCameraUp(speed,delta);
		player.move(speed,delta);

		//Update Camera bounds
		cameraBounds.setY(camera.position.y - Gdx.graphics.getHeight()/2 - tolerance);
		Vector3 buttonLoc = new Vector3(EDGE_TOLERANCE + BUTTON_WIDTH/2, 
				BUTTON_HEIGHT - EDGE_TOLERANCE, 0);
		camera.unproject(buttonLoc);
		Vector2 buttonLoc2 = new Vector2();
		Rectangle buttonBounds = new Rectangle();

		for (ABDrawable dropped : drawMap.get("Dropped")){
			((Dropped) dropped).decreaseTimeLeft();
			buttonBounds.setX(buttonLoc.x - BUTTON_WIDTH/2);
			buttonBounds.setY(buttonLoc.y - BUTTON_HEIGHT);
			buttonBounds.setWidth(BUTTON_WIDTH);
			buttonBounds.setHeight(BUTTON_HEIGHT*2);
			buttonLoc2.x = buttonLoc.x - EDGE_TOLERANCE;
			buttonLoc2.y = buttonLoc.y + EDGE_TOLERANCE;
			((Dropped) dropped).droppedMove(buttonLoc2.cpy(), delta);
			//Remove uncollected drops
			if((((Dropped) dropped).getTimeLeft() <= 0) && !((Dropped) dropped).isPickedUp()){
				drawMap.get("Dropped").removeValue(dropped, true);
				dropped.dispose();
			}

			if(buttonBounds.contains(((Dropped) dropped).getPosition())) {
				
				removeFromABDrawable(dropped);
				dropped.dispose();
			}

		}

		for (ABDrawable boosts : drawMap.get("Boosts")) {

			if (((Dog) boosts).getTimeLeft() >= 0) {
				((Dog) boosts).decreaseTimeLeft();
				((Dog) boosts).move(speed, delta);
			} else {
				drawMap.get("Boosts").removeValue(boosts, true);
				setDog(false);
			}
		}

		for (ABDrawable movable : drawMap.get("Movable")) {
			//move animals if necessary
			((Movable) movable).move(speed,delta);
			if (weather.getWeather() == WeatherType.WINDY){
				if (rand.nextBoolean())
					((Movable) movable).addToCurrentTarget(windVector);
			}
			else if (weather.getWeather() == WeatherType.SNOWY){
				((Movable) movable).adjustForwardBias(1, speed, delta);
			}
			//Reduce upward bias if there's a dog
			if(hasDog()) {
				((Movable) movable).adjustForwardBias(.5f, speed, delta);
			} else {
				((Movable) movable).adjustForwardBias(0, speed, delta);
			}

			//Drop new items
			if(rand.nextInt(100) <= 50 && drawMap.get("Movable").size <= 30){
				ABDrawable dropping =  ((Animal)movable).drop(gameInstance.getLevelHandler().animalChangeX(), gameInstance.getLevelHandler().animalChangeY());
				if(dropping != null){
					drawMap.get("Dropped").add(dropping);
				}
			}
		}

		//check for and remove lost animals
		for (ABDrawable drawable : drawMap.get("Movable")) {

			if (!(cameraBounds.contains(drawable.getPosition()))) {
				drawMap.get("Movable").removeValue(drawable, false);
				drawable.dispose();
			}
		}

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
						((Movable)movable).adjustForwardBias(1,cameraSpeed,delta);
				}
			}
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

		gameInstance.getGameScreen().setInfolabel();

		//check if market is in middle of screen to move on
		if (Math.abs(market.getPosition().y - camera.position.y) < 20) {
			SoundHandler.pauseBackgroundMusic();
			gameInstance.getLevelHandler().resetNextLevelStart();
			gameInstance.setHitBack(false);
			gameInstance.setScreen(new MarketScreen(gameInstance, gameInstance.getGameScreen()));
		}

		//handle weather elements if its time for the weather to change
		if (weatherTime > targetWeatherTime){
			weather.setWeatherType(weather.getNewWeather());

			//set clear on/off
			worldRender.setClear(weather.getWeather() == WeatherType.CLEAR);

			//set rainy on/off
			worldRender.setRainy(weather.getWeather() == WeatherType.RAINY);

			//set snowy on/off
			worldRender.setSnowy(weather.getWeather() == WeatherType.SNOWY);

			//set windy on/off
			if (weather.getWeather() == WeatherType.WINDY){
				double magnitude;
				if (gameInstance.isKidMode()) {
					magnitude = (float)(rand.nextInt(15) - 10);
				} else {
					magnitude = (float)(rand.nextInt(35) - 10);
				}
				if (magnitude < 0)
					magnitude-=10.0;
				else
					magnitude +=10.0;
				double radian = (((double)rand.nextInt(360))*2.0*Math.PI/360.0);
				windVector = new Vector2((float)(magnitude*Math.cos(radian)),(float)(magnitude*Math.sin(radian)));
				worldRender.setWindy(true, windVector.getAngleRad(), windVector.dst(new Vector2()));
			}
			else{
				worldRender.setWindy(false, null, null);
			}
			weatherTime = 0f;
			targetWeatherTime = rand.nextFloat()%WEATHER_DURATION + WEATHER_DURATION;
		}
		else{
			weatherTime += delta;
		}

		if (weatherClick > WEATHER_CLICK){
			weatherClick = 0f;
			weatherIterator = ((int)(weatherIterator + 300f)%((int)(Gdx.graphics.getWidth())));
			switch(weather.getWeather()){
			case RAINY: {
				drawMap.get("WeatherDrop").add(new RainDrop("weather/rain.png",
						new Vector2(((float)(weatherIterator)), Gdx.graphics.getHeight())));
			}
			case WINDY: break;
			case CLEAR: {weatherIterator = 0f; break;}
			case SNOWY: {
				if (rand.nextBoolean())
					drawMap.get("WeatherDrop").add(new SnowDrop("weather/snow.png",
							new Vector2(((float)(weatherIterator)), Gdx.graphics.getHeight())));
			}
			default: break;
			}
		}
		else{
			weatherClick += delta;
		}
		for (ABDrawable m : drawMap.get("WeatherDrop")){
			if (weather.getWeather()==WeatherType.CLEAR || weather.getWeather() == WeatherType.WINDY){
				((Movable) m).move(speed,  delta);
				((Movable) m).move(speed,  delta);
			}
			else
				((Movable) m).move(speed,  delta);
			Vector2 p = m.getPosition();
			if (p.y + m.getHeight() < 1f){
				drawMap.get("WeatherDrop").removeValue(m, false);
				m.dispose();
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
	 * on resetting after dispose, this reinitalizes the dropped object Textures
	 */
	public void reinitTextureDropped(){
		for (ABDrawable dropped : drawMap.get("Dropped")) {
			((Dropped)dropped).resetTexture();
		}
	}

	/**
	 * on resetting objects , this reinitializes the Obstacle object textures
	 */
	public void reinitTextureObstacle(){
		for (ABDrawable obstacle : drawMap.get("Obstacle")) {
			((Obstacle) obstacle).resetText();
		}
	}

	/**
	 * returns the obstacles array for use in changing to Main Menu and gameScreen
	 * @return
	 */
	public Array<Obstacle> getObstacles() {
		Array<Obstacle> obstacles = new Array<Obstacle>();
		for (ABDrawable aBDrawable : drawMap.get("Obstacle")) {
			obstacles.add((Obstacle) aBDrawable);
		}
		return obstacles;
	}

	/**
	 * on resetting objects, this reinitializes the players texture
	 */
	public void reinitTexturePlayer() {
		player.resetTexture("objectTextures/player.png");
	}

	public ArrayMap<String, Array<ABDrawable>> getDrawMap() {
		return drawMap;
	}

	public void setDog(boolean hasDog) {
		this.hasDog = hasDog;
	}

	public boolean hasDog() {
		return hasDog;
	}

	public float getPrecentage(){
		return (camera.position.y - Gdx.graphics.getHeight()/2f)/(laneLength);
	}

	public WeatherType getWeather(){
		return weather.getWeather();
	}


}
