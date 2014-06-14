/**
 * File: Movable.java
 * 
 * This is the abstract class for anything that moves.
 * At this time it is only animals that are moving
 */
package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.AnimalBookGame;

public abstract class Movable extends ABDrawable {

	/** each movable will have a speed which is how much they move per frame*/
	protected float speed;
	/** each movable will have a speed that the camera is moving due to they should move faster*/
	protected float cameraSpeed;

	/** the multiple of the camera speed that an animal moves up with the camera every move call */
	protected float moveBias;

	/**each movable will have a previous target which lerps to the current target */
	protected Vector2 previousTarget;

	/**the current target is where the movable should be heading to  */
	protected Vector2 currentTarget;
	
	/**The rotation of the movable texture*/
	protected float rotation;
	
	/**The speed that an object rotates with.  Lower values rotate faster*/
	protected float rotationSpeed;

	/**
	 * the constructor creates a movable with a given speed
	 * @param texturePath due to its a child of ABDrawable
	 */
	public Movable(String texturePath) {
		super(texturePath);

		//default speed
		speed = 1/14f;

		moveBias = 1.5f;
		
		rotation = 0;
		rotationSpeed = 50f;
	}
	/**
	 * Overridden to draw with rotation so movables face the direction they are moving
	 */
	@Override
	public void draw(SpriteBatch batch) {
		if (this instanceof Dog) {
			Gdx.app.log("Sanity2", "Wat2");
			batch.draw(texture, position.x - width/2, position.y - height/2, width, height);
		}else{
			batch.draw(texture, position.x - width/2, position.y - height/2, width/2, height/2, width, height, 1, 1, rotation, 0, 0, texture.getWidth(), texture.getHeight(), false, false);

		}
	}

	/**
	 * every movable will move after it draws in the world based on the given variables
	 * @param cameraSpeed the current cam speed to move with the camera
	 * @param delta		  the time between two frames
	 */
	public void move(float cameraSpeed, float delta) {
		//hold old position
		Vector2 temp = this.position.cpy();
		
		//move bias with camera direction
		position.y += (moveBias*cameraSpeed) * (AnimalBookGame.TARGET_FRAME_RATE*delta);

		//Lerp the position to the target
		position.lerp(previousTarget, speed*Gdx.graphics.getDeltaTime());

		//Lerp the previous target to the current
		previousTarget.lerp(currentTarget,30*speed*Gdx.graphics.getDeltaTime());

		//update bounds
		bounds.setX(position.x - width/2);
		bounds.setY(position.y - height/2);
		
		//Find rotation
		float targetRotation = this.position.cpy().sub(temp).angle() - 90f;
		//float targetRotation = (this.previousTarget.cpy().sub(this.position).add(new Vector2(0,moveBias*cameraSpeed))).angle() - 90f;
		float difference = targetRotation - rotation;
		rotation += difference/rotationSpeed;
	}

	/**
	 * Stops the constant forward motion for use with colliding with an obstacle from below. 1 eliminates forward bias, animals will move fully randomly.
	 * 0 is unchanged from normal motion.
	 * 
	 * @param amount		float from 0 to 1
	 * @param cameraSpeed
	 * @param delta
	 */
	public void adjustForwardBias(float amount,float cameraSpeed,float delta) {
		position.y -= amount * (moveBias*cameraSpeed) * (AnimalBookGame.TARGET_FRAME_RATE*delta);
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
	 * handles collisions between movable objects and obstacles
	 * 
	 * @param movable
	 * @param obstacle
	 */
	public void bounce(Movable movable, Obstacle obstacle){
		
		//Collision with obstacle
		if (movable == null) {
			Vector2 oPos, baseVector;
			oPos = obstacle.getPosition().cpy();
			baseVector = new Vector2(this.position.x - oPos.x, this.position.y - oPos.y);
			float dist = this.position.cpy().dst(oPos);
			float deg = baseVector.getAngleRad();
			addToCurrentTarget(new Vector2(((float)(dist*Math.cos((double)deg)))/4,((float)(dist*Math.sin((double)deg))/4)));
			this.previousTarget = this.currentTarget.cpy();
		}
		//collision with other movable
		else if (obstacle == null) {
			Vector2 mPos, baseVector;
			mPos = movable.getPosition().cpy();
			baseVector = new Vector2(this.position.x - mPos.x, this.position.y - mPos.y);
			float dist = this.position.cpy().dst(mPos);
			float deg = baseVector.getAngleRad();
			addToCurrentTarget(new Vector2(((float)(dist*Math.cos((double)deg)))/24,((float)(dist*Math.sin((double)deg))/24)));
			this.previousTarget = this.currentTarget.cpy();
		}

	}

	/**
	 * overriding the ABDrawable function because it is a movable for the world use
	 */
	@Override
	public boolean isMovable() {
		return true;
	}
}
