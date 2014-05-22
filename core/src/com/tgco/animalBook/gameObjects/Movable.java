package com.tgco.animalBook.gameObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

//Objects that will move on screen
public abstract class Movable extends Drawable {

	protected float speed;
	protected float cameraSpeed;
	protected Vector2 previousTarget;
	protected Vector2 currentTarget;


	public Movable(String texturePath) {
		super(texturePath);

	}

	public void move(float cameraSpeed) {
		//move bias with camera direction
		position.y += 1.5*cameraSpeed;
		
		//Lerp the position to the target //previousTarget.len2()/currentTarget.len2()*1.5f
		position.lerp(previousTarget, speed*Gdx.graphics.getDeltaTime());

		//Lerp the previous target to the current
		//currentTarget, previousTarget.len2()/currentTarget.len2()
		previousTarget.lerp(currentTarget,30*speed*Gdx.graphics.getDeltaTime());

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
