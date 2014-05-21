package com.tgco.animalBook.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.tgco.animalBook.gameObjects.Drawable;
import com.tgco.animalBook.gameObjects.Goose;
import com.tgco.animalBook.gameObjects.Movable;
import com.tgco.animalBook.gameObjects.Player;

//Generates game objects and handles game logic between them
public class World {

	//Camera to view the world
	private OrthographicCamera camera;
	private Vector3 cameraTarget;

	//Rendering object
	private WorldRenderer worldRender;
	
	//All game objects to be drawn
	private Array<Drawable> drawables;
	
	//The player character
	private Player player;
	
	private static int level = 0;
	private static final int NUM_ANIMALS = 5;
	
	public World() {
		
		drawables = new Array<Drawable>();
		
		//Camera initialization
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		camera.update();

		if(level == 0){
			for(int i = 0; i < NUM_ANIMALS; i++){
				drawables.add(new Goose());
			}
		}
		
		player = new Player(camera);
		//touch variable
		cameraTarget = new Vector3(camera.position);
		
		worldRender = new WorldRenderer();
	}

	public void render(SpriteBatch batch) {
		//move the camera if necessary
		moveCameraToTouch(cameraTarget);
		
		//move animals if necessary
		for (Drawable drawable : drawables) {
			if (drawable.isMovable())
				((Movable) drawable).move();
		}
		
		//move player
		player.move();

		//draw objects
		worldRender.render(batch, drawables, player);
	}
	

	//Finds the newest touch and interpolates the camera to its position
	public void moveCameraToTouch(Vector3 lastTouch) {
		camera.position.lerp(lastTouch.cpy(),Gdx.graphics.getDeltaTime());
		camera.update();
	}

	public OrthographicCamera getCamera() {
		return camera;
	}
	
	public void setCameraTarget(Vector3 cameraTarget) {
		this.cameraTarget = cameraTarget;
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
