package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.gameObjects.Consumable.DropType;


public class Dog extends Animal {
	private static final String texturePath = "objectTextures/dog.png";
	private int timeLeft;
	private OrthographicCamera camera;
	
	//distance between buttons and between the edge and an object
	protected static final float EDGE_TOLERANCE = (.03f)*Gdx.graphics.getHeight();

	public Dog(Vector2 position, int animalX, int animalY, OrthographicCamera camera) {
		super(texturePath, position, animalY, animalY);
		width = .04f*Gdx.graphics.getWidth();
		height = .147f*Gdx.graphics.getHeight();
		this.camera = camera;
		timeLeft = 1800;
	}
	
	public void decreaseTimeLeft() {
		timeLeft--;
	}
	
	public int getTimeLeft() {
		return timeLeft;
	}
	
	@Override
	public void move(float cameraSpeed, float delta) {
		//hold old position
		Vector2 temp = this.position.cpy();
		
		position.y += (cameraSpeed) * (AnimalBookGame.TARGET_FRAME_RATE*delta);
		
		if (position.x > (Gdx.graphics.getWidth() - EDGE_TOLERANCE - width)) {
			currentTarget = new Vector2(camera.position.x - Gdx.graphics.getWidth()/2 - 3*width, camera.position.y + Gdx.graphics.getHeight()/2);
		}
		if (position.x < EDGE_TOLERANCE + width) {
			currentTarget = new Vector2(camera.position.x + Gdx.graphics.getWidth()/2 + 3*width, camera.position.y + Gdx.graphics.getHeight()/2);
		}
		
		position.lerp(currentTarget, (1/8f)*delta);
		
		//Find rotation
		float targetRotation = this.position.cpy().sub(temp).angle() - 90f;
		float difference = targetRotation - rotation;
		rotation += difference/rotationSpeed;
	}

	@Override
	public DropType getDropType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetTexture() {
		// TODO Auto-generated method stub
		
	}

}