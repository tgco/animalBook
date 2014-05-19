package com.tgco.animalBook.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

//Generates game objects and handles game logic between them
public class World {

	//Camera to view the world
	private OrthographicCamera camera;
	private Vector3 cameraTarget;

	//Rendering object
	private WorldRenderer worldRender;

	public World() {

		//Camera initialization
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, 0);
		camera.update();

		//touch variable
		cameraTarget = new Vector3(camera.position);
		
		worldRender = new WorldRenderer();
	}

	public void render(SpriteBatch batch) {

		//move the camera if necessary
		moveCameraToTouch(cameraTarget);

		//draw objects
		worldRender.render(camera, batch);
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

}
