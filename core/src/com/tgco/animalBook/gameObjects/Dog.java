package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.gameObjects.Consumable.DropType;


public class Dog extends Animal {
	private static final String texturePath = "objectTextures/dog.png";
	
	//distance between buttons and between the edge and an object
	protected static final float EDGE_TOLERANCE = (.03f)*Gdx.graphics.getHeight();

	public Dog(Vector2 position, int animalX, int animalY) {
		super(texturePath, position, animalY, animalY);
		width = .04f*Gdx.graphics.getWidth();
		height = .147f*Gdx.graphics.getHeight();
	}
	
	@Override
	public void move(float cameraSpeed, float delta) {
		//hold old position
		Vector2 temp = this.position.cpy();
		
		position.y += (moveBias*cameraSpeed) * (AnimalBookGame.TARGET_FRAME_RATE*delta);
		
		if (position.x > (Gdx.graphics.getWidth() - EDGE_TOLERANCE - width)) {
			currentTarget = new Vector2(EDGE_TOLERANCE, Gdx.graphics.getHeight() - EDGE_TOLERANCE);
		}
		if (position.x < EDGE_TOLERANCE + width) {
			currentTarget = new Vector2(Gdx.graphics.getWidth() - EDGE_TOLERANCE, Gdx.graphics.getHeight() - EDGE_TOLERANCE);
		}
		
		position.lerp(currentTarget, (1/14f)*delta);
		Gdx.app.log("DogeMove", "position: " + position.toString() + "; target: " + currentTarget.toString());
		
		//Find rotation
		float targetRotation = this.position.cpy().sub(temp).angle() - 90f;
		//float targetRotation = (this.previousTarget.cpy().sub(this.position).add(new Vector2(0,moveBias*cameraSpeed))).angle() - 90f;
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
