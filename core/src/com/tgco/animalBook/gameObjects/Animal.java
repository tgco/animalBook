/**
 * File : Animal.java
 * The animal class is an abstract class that has the basic functionality of all animals in the game.
 * it draws itself, drops the proper dropped items
 * 
 */
package com.tgco.animalBook.gameObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.gameObjects.Consumable.DropType;

public abstract class Animal extends Movable {

	/**
	 * rate in percent that a child animal is spawned instead of a consumable
	 */
	private double fertilityRate;

	/** interval between drop chances */
	private double dropInterval;
	
	/** the time it stays on the ground until it disappears */
	private double timeOnGround;

	/** a counter to reflect the amount of frames have gone by to determine when to move the animal */
	private float changeTargetCount = 0f;
	
	/** a counter to reflect the amount of frames before an animal will drop an item */
	private int dropCount = 0;

	/** every animal has a different item that is dropped */
	private DropType dropType;

	/** rand is used to created random movement of the animal */
	protected Random rand;

	/** the animal will have different target changes for different levels */
	private int  animalLevel;

	private int animalX;

	private int animalY;

	/**
	 * animal construct which every child must have a string to it's picture which is needed for ABDrawbles
	 *  along with a position
	 * @param texturePath the string path to the picture
	 * @param position the starting position when the animal is created
	 * @param animalLevel the level the animal is on
	 */
	public Animal(String texturePath, Vector2 position, int animalX, int animalY) {
		super(texturePath);
		this.position = position;
		previousTarget = position.cpy();
		currentTarget = previousTarget.cpy();
		this.animalX = animalX;
		this.animalY = animalY;

		//leeway for collection
		bounds = new Rectangle(position.x - width/2,position.y - height/2,width,height);

		//initialize the rates
		fertilityRate = 15;
		dropInterval = 400;
		timeOnGround = 120;

		dropCount = 0;

		rand = new Random();
	}

	/**
	 * Overridden to allow animal to possibly choose a new target
	 */
	@Override
	public void move(float cameraSpeed, float delta) {
		super.move(cameraSpeed, delta);

		if (changeTargetCount > 30) {
			changeTargetCount = 0f;
			if (rand.nextInt(100) < 75)
				changeTarget();
		}

		changeTargetCount += AnimalBookGame.TARGET_FRAME_RATE*delta;
		dropCount++;
	}

	private void changeTarget(){
		int xChangeDistance = animalX;
		int yChangeDistance = animalY;

		currentTarget = new Vector2(position.x + rand.nextInt(xChangeDistance) - xChangeDistance/2, position.y + rand.nextInt(yChangeDistance) - yChangeDistance/2);
	}


	/**
	 * Create a consumable or new animal based on which animal the parent is.
	 * 
	 * @return ABDrawable which is either an animal or consumable to the Dropped class
	 */
	public ABDrawable drop(int animalX, int animalY) {
		
		
		if(dropCount  % dropInterval == 0){
			if(rand.nextInt(100) < fertilityRate){
				if (this.getClass().equals(Goose.class)) 
					return new Dropped(new Goose(this.position.cpy(), animalX, animalY),this.position.cpy(), timeOnGround);
				else if (this.getClass().equals(Pig.class))
					return new Dropped(new Pig(this.position.cpy(), animalX, animalY),this.position.cpy(), timeOnGround);
				else if (this.getClass().equals(Goat.class))
					return new Dropped(new Goat(this.position.cpy(), animalX, animalY),this.position.cpy(), timeOnGround);
				else if (this.getClass().equals(Sheep.class))
					return new Dropped(new Sheep(this.position.cpy(), animalX, animalY),this.position.cpy(), timeOnGround);
				else if (this.getClass().equals(Cow.class))
					return new Dropped(new Cow(this.position.cpy(), animalX, animalY),this.position.cpy(), timeOnGround);
				else
					return new Dropped(new Goose(this.position.cpy(), animalX, animalY),this.position.cpy(), timeOnGround);
			}
			else{
				if (this.getClass().equals(Goose.class))
					return new Dropped(new Consumable(Consumable.DropType.values()[0]), this.position.cpy(), timeOnGround);
				else if (this.getClass().equals(Pig.class))
					return new Dropped(new Consumable(Consumable.DropType.values()[1]), this.position.cpy(), timeOnGround);
				else if (this.getClass().equals(Goat.class))
					return new Dropped(new Consumable(Consumable.DropType.values()[2]), this.position.cpy(), timeOnGround);
				else if (this.getClass().equals(Sheep.class))
					return new Dropped(new Consumable(Consumable.DropType.values()[3]), this.position.cpy(), timeOnGround);
				else if (this.getClass().equals(Cow.class))
					return new Dropped(new Consumable(Consumable.DropType.values()[4]), this.position.cpy(), timeOnGround);
				else
					return new Dropped(new Consumable(Consumable.DropType.values()[0]), this.position.cpy(), timeOnGround);
			}
		}
		else{
			return null;
		}
	}

	/**
	 * Create a consumable every time for tutorial purposes
	 * 
	 * @return ABDrawable which is a consumable to the Dropped class
	 */
	public ABDrawable forceDropConsumable() {

		if (this.getClass().equals(Goose.class))
			return new Dropped(new Consumable(Consumable.DropType.values()[0]), this.position.cpy(), timeOnGround);
		else if (this.getClass().equals(Pig.class))
			return new Dropped(new Consumable(Consumable.DropType.values()[1]), this.position.cpy(), timeOnGround);
		else if (this.getClass().equals(Goat.class))
			return new Dropped(new Consumable(Consumable.DropType.values()[2]), this.position.cpy(), timeOnGround);
		else if (this.getClass().equals(Sheep.class))
			return new Dropped(new Consumable(Consumable.DropType.values()[3]), this.position.cpy(), timeOnGround);
		else if (this.getClass().equals(Cow.class))
			return new Dropped(new Consumable(Consumable.DropType.values()[4]), this.position.cpy(), timeOnGround);
		else
			return new Dropped(new Consumable(Consumable.DropType.values()[0]), this.position.cpy(), timeOnGround);


	}

	/**
	 * used for upgradesScreen
	 * @return fertilityRate
	 */
	public double getFertilityRate() {
		return fertilityRate;
	}

	/**
	 * used for the upgradesScreen in terms of seconds
	 * @return getDroppedInterval
	 */
	public double getDropInterval() {
		return dropInterval/60.0;
	}

	/**
	 * used for the upgradesScreen in terms of seconds
	 * @return getTimeOnGround
	 */
	public double getTimeOnGround() {
		return timeOnGround /60.0;
	}

	/**
	 *  used with the upgradesScreen to increase the fertility of the animal
	 * @param fertilityRate the amount to add to the fertilityRate of the animal
	 */
	public void upgradeFertilityRate(double fertilityRate) {
		this.fertilityRate += fertilityRate;
	}

	/**
	 * used with the upgradesScreen to increase the drop rate of the animal
	 * @param dropInterval the amount to add to the dropInterval of the animal
	 */
	public void upgradeDropInterval(double dropInterval) {
		this.dropInterval -= dropInterval;
	}

	/**
	 * used with the upgradesScreen to increase how long it will stay on the ground
	 * @param timeOnGround the amount to add to the timeOnGround of the animal
	 */
	public void upgradeTimeOnGround(double timeOnGround) {
		this.timeOnGround += timeOnGround;
	}

	/** every animal will be able to get the dropType that it has */
	public abstract DropType getDropType();

	/** every animal must have a resetTexture for switching screens */
	public abstract void resetTexture();
}
