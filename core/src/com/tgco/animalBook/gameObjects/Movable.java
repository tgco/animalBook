package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

//Objects that will move on screen
public abstract class Movable extends ABDrawable {

	protected float speed;
	protected float cameraSpeed;
	protected Vector2 previousTarget;
	protected Vector2 currentTarget;


	public Movable(String texturePath) {
		super(texturePath);
		
		//default speed
		speed = 1/14f;

	}

	public void move(float cameraSpeed) {
		//move bias with camera direction
		position.y += 1.5*cameraSpeed;
		
		//Lerp the position to the target
		position.lerp(previousTarget, speed*Gdx.graphics.getDeltaTime());

		//Lerp the previous target to the current
		previousTarget.lerp(currentTarget,30*speed*Gdx.graphics.getDeltaTime());
		
		//update bounds
		bounds.setX(position.x - width/2);
		bounds.setY(position.y - height/2);

	}

	public void addToCurrentTarget(Vector2 addition) {
		currentTarget.add(addition);
	}

	public void setCurrentTarget(Vector2 target) {
		currentTarget = target.cpy();
	}

	@Override
	public boolean isMovable() {
		return true;
	}

}
