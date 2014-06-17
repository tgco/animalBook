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
import com.tgco.animalBook.AnimalBookGame;

public class Dropped extends ABDrawable{

	/** consume is 1 item it may carry */
	private Consumable consume = null;
	/** animal is 1 item it may carry */
	private Animal animal = null;

	/** how much time it has left before disappearing */
	private double timeLeft;

	/** if the user has clicked on it*/
	private boolean pickedUp;

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
		if (AnimalBookGame.SCREEN_HEIGHT < 3f) {
			width = .028f*Gdx.graphics.getWidth() * 1.5f;
			height = .044f*Gdx.graphics.getHeight() * 1.5f;
		} else {
			width = .028f*Gdx.graphics.getWidth();
			height = .044f*Gdx.graphics.getHeight();
		}
		bounds = new Rectangle(position.x - width/2,position.y - height/2,width,height);
		this.timeLeft = timeLeft;
		this.pickedUp = false;

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
		this.pickedUp = false;
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
	 *  moves the dropped to the position on screen if it has been clicked on
	 * @param pos the position to where it needs to go
	 * @param delta the time between frames
	 */
	public void droppedMove(Vector2 pos, float delta) {
		if (pickedUp) {
			position.lerp(pos, delta);
		} else {
			return;
		}
	}

	/**
	 * when clicked on, change to true from the input handler of the game
	 */
	public void pickUp() {
		pickedUp = true;
	}

	/**
	 * getter for the picked up
	 * @return
	 */
	public boolean isPickedUp() {
		return pickedUp;
	}

	/** 
	 * draw calls super on ABDrawables and ticks the time down.
	 * @param batch batch is used for ABDrawables
	 */
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
	}

	/**
	 * returns the time left for this object used in the world
	 * @return timeLeft
	 */
	public double getTimeLeft() {
		return timeLeft;
	}

	public void decreaseTimeLeft() {
		timeLeft--;
	}

	/**
	 * overrides the variable in ABDrawables for the world to pick out all dropped objects.
	 */
	@Override
	public boolean isDropping() {
		return true;
	}

	/**
	 * resets the approprtatie texture when changing screens
	 */
	public void resetTexture() {
		if(consume != null){
			super.resetTexture(consume.getType().getTexturePath());
		}else{
			super.resetTexture(animal.getDropType().getTexturePath());
		}

	}
}
