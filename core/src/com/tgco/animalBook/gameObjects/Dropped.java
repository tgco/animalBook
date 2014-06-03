/**
 * File: Dropped.java
 * a dropped object houses an animal or consumable, and how long it has until it disappers. 
 * One has to click on the object on screen to collect the prize inside
 */
package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Dropped extends Movable{
	
	/** consume is 1 item it may carry */
	private Consumable consume = null;
	/** animal is 1 item it may carry */
	private Animal animal = null;
	
	/** how much time it has left before disappearing */
	private double timeLeft;
	
	private boolean isPickedUp;
	protected static final float BUTTON_WIDTH = (1f/10f)*Gdx.graphics.getWidth();
	protected static final float BUTTON_HEIGHT = (1f/10f)*Gdx.graphics.getWidth();
	protected static final float EDGE_TOLERANCE = (.03f)*Gdx.graphics.getHeight();
	
	private final Vector2 buttonLoc = new Vector2(EDGE_TOLERANCE, Gdx.graphics.getHeight() - BUTTON_HEIGHT);


	/**
	 *  the constructor with Consumable exists if the dropped item should house a Consumable.
	 * @param consume is passed in a new Consumable
	 * @param pos starting position of the dropped object
	 * @param timeLeft how much time is left
	 */
	public Dropped(Consumable consume, Vector2 pos, double timeLeft) {
		super(consume.getType().getTexturePath());
		this.consume = consume;
		this.position = pos;
		width = .028f*Gdx.graphics.getWidth();
		height = .044f*Gdx.graphics.getHeight();
		bounds = new Rectangle(position.x - width/2,position.y - height/2,width,height);
		this.timeLeft = timeLeft;

	}
	 /**
	  * the constructor with Animal exists if the dropped item should house an Animal
	  * @param animal animal is passed as a new child of Animal
	  * @param pos the starting position of the animal
	  * @param timeLeft how much time is left
	  */
	public Dropped(Animal animal, Vector2 pos, double timeLeft) {
		super(animal.getDropType().getTexturePath());
		this.animal = animal;
		this.position = pos;
		width = .028f*Gdx.graphics.getWidth();
		height = .044f*Gdx.graphics.getHeight();
		bounds = new Rectangle(position.x - width/2,position.y - height/2,width,height);
		this.timeLeft = timeLeft;
	}

	/**
	 * getDropped is used when object is clicked on to retrieve the correct prize.
	 * @return ABDrawable for the world to use
	 */
	public ABDrawable getDropped(){
		if(animal !=null){
			return animal;
		}
		else{
			return consume;
		}
	}

	/** 
	 * draw calls super on ABDrawables and ticks the time down.
	 * @param batch batch is used for ABDrawables
	 */
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		timeLeft--;
	}
	
	@Override
	public void move(float cameraSpeed, float delta) {
		if (isPickedUp) {
			position.lerp(buttonLoc, delta);
		} else {
			return;
		}
	}

 /**
  * returns the time left for this object used in the world
  * @return timeLeft
  */
	public double getTimeLeft() {
		return timeLeft;
	}
	
	public void pickUp() {
		isPickedUp = !isPickedUp;
	}
	
	public boolean isPickedUp() {
		return isPickedUp;
	}

	/**
	 * overrides the variable in ABDrawables for the world to pick out all dropped objects.
	 */
	@Override
	public boolean isDropping() {
		return true;
	}
	
	public void resetTexture() {
		if(consume != null){
		super.resetTexture(consume.getType().getTexturePath());
		}else{
			super.resetTexture(animal.getDropType().getTexturePath());
		}
		
	}
}
