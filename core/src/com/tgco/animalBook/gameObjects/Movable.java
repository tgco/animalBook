/**
 * File: Movable.java
 * 
 * This is the abstract class for anything that moves.
 * At this time it is only animals that are moving
 */
package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public abstract class Movable extends ABDrawable {
	
	/** each movable will have a speed which is how much they move per frame*/
	protected float speed;
	/** each movable will have a speed that the camera is moving due to they should move faster*/
	protected float cameraSpeed;
	
	/**each movable will have a previous target which lerps to the current target */
	protected Vector2 previousTarget;
	
	/**the current target is where the movable should be heading to  */
	protected Vector2 currentTarget;

	/**
	 * the constructor creates a movable with a given speed
	 * @param texturePath due to its a child of ABDrawable
	 */
	public Movable(String texturePath) {
		super(texturePath);
		
		//default speed
		speed = 1/14f;
	}

	/**
	 * every movable will move after it draws in the world based on the given variables
	 * @param cameraSpeed to relativlety move with the camera
	 */
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

	/**
	 * this is used in the GameScreenInputHandler for vector addition after herding the movable
	 * @param addition the herding amount
	 */
	public void addToCurrentTarget(Vector2 addition) {
		currentTarget.add(addition);
	}

	/**
	 * the current target is changed in the Animal class when it randomly chooses to move
	 * @param target  changes the currentTarget when randomly change 
	 */
	public void setCurrentTarget(Vector2 target) {
		currentTarget = target.cpy();
	}

	/**
	 * overriding the ABDrawable function becuase it is a movable for the world use
	 */
	@Override
	public boolean isMovable() {
		return true;
	}
}
