package com.tgco.animalBook.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.gameObjects.Drawable;
import com.tgco.animalBook.gameObjects.Goose;
import com.tgco.animalBook.gameObjects.Market;
import com.tgco.animalBook.gameObjects.Movable;
import com.tgco.animalBook.gameObjects.Player;
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
	private Array<Drawable> drawables;
	private Market market;

	//Lane length for this level
	private float laneLength;
	//Distance where two object classify as colliding
	private static final float COLLISION_TOLERANCE = 100;

	//The player character
	private Player player;

	private static int level = 0;
	private static final int NUM_ANIMALS = 5;

	public World(AnimalBookGame gameInstance) {
		this.gameInstance = gameInstance;

		drawables = new Array<Drawable>();

		//Camera initialization
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		camera.update();
		cameraSpeed = .2f;

		int x;
		if(level == 0){
			for(int i = 0; i < NUM_ANIMALS; i++){
				if(i < .5*NUM_ANIMALS){
					x = -i;
					drawables.add(new Goose(new Vector2(Gdx.graphics.getWidth()/2 + x*40 -50, (float) (Gdx.graphics.getHeight()/2 -x*x*25 + 10*x -50))));
				}
				else {
					x = (i - (int)Math.floor(.5*NUM_ANIMALS));
					Gdx.app.log("aasdf", "x is " + x);
					drawables.add(new Goose(new Vector2(Gdx.graphics.getWidth()/2 + x*40, (float) (Gdx.graphics.getHeight()/2 -x*x*30 + 15*x -50))));
				}
			}

		}

		player = new Player(cameraSpeed);

		//Make the market and set it at the end
		laneLength = 1000;
		market = new Market();
		market.setPosition(new Vector2(player.getPosition().x, player.getPosition().y + laneLength));

		drawables.add(market);

		worldRender = new WorldRenderer();
	}

	public void render(SpriteBatch batch, boolean paused) {
		if (!paused)
			updateGameLogic();

		//draw objects
		worldRender.render(batch, drawables, player);
	}

	public void updateGameLogic() {
		//move the camera
		moveCameraUp(cameraSpeed);

		//move animals if necessary
		for (Drawable drawable : drawables) {
			if (drawable.isMovable())
				((Movable) drawable).move(cameraSpeed);
		}

		//move player
		player.move(cameraSpeed);

		//check for collisions between the market and the player/geese
		for (Drawable drawable : drawables) {
			if (drawable.getPosition().cpy().sub(market.getPosition()).len() < COLLISION_TOLERANCE) {
				if (!drawable.isMarket()) {
					drawables.removeValue(drawable, false);
				}
			}
		}

		if (player.getPosition().cpy().sub(market.getPosition()).len() < COLLISION_TOLERANCE) {
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

	public void setPlayerTarget(Vector2 playerTarget) {
		player.setCurrentTarget(playerTarget);
	}

	public void dispose() {
		for (Drawable drawable : drawables) {
			drawable.dispose();
		}
	}

	public Array<Movable> getMovables() {
		Array<Movable> movables = new Array<Movable>();
		for (Drawable drawable : drawables) {
			if (drawable.isMovable())
				movables.add((Movable) drawable);
		}
		return movables;
	}

}
