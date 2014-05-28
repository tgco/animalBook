package com.tgco.animalBook.gameObjects;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.gameObjects.Consumable.DropType;

public abstract class Animal extends Movable {
	
	//rate in percent that a child animal is spawned instead of a consumable
	private double fertilityRate;
	
	//interval between drop chances
	private double dropInterval;
	private double timeOnGround;
	private int changeTargetCount = 0;
	
	private DropType dropType;
	
	protected Random rand;

	public Animal(String texturePath, Vector2 position) {
		super(texturePath);
		//Animals start in the middle of the screen
		this.position = position;
		previousTarget = position.cpy();
		currentTarget = previousTarget.cpy();
		
		//bounds
		bounds = new Rectangle(position.x - width/2,position.y - height/2,width,height);

		//initialize the rates
		fertilityRate = 15;
		dropInterval = 400;
		timeOnGround = 120;

		
		rand = new Random();
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		
		
		if(changeTargetCount % 50 == 0 && rand.nextInt(100) < 75){

			changeTarget();	
		}
		
		changeTargetCount++;
	}
	public void changeTarget(){
		currentTarget = new Vector2(position.x + rand.nextInt(400) - 200, position.y + rand.nextInt(400) - 200);
	}
	
	
	//Create a consumable or new animal
	public ABDrawable drop() {
		
		if(changeTargetCount % dropInterval ==0){
			if(rand.nextInt(100) < fertilityRate){
				if (this.getClass().equals(Goose.class)) 
					return new Dropped(new Goose(this.position.cpy()),this.position.cpy(), timeOnGround);
				else if (this.getClass().equals(Pig.class))
					return new Dropped(new Pig(this.position.cpy()),this.position.cpy(), timeOnGround);
				else if (this.getClass().equals(Goat.class))
					return new Dropped(new Goat(this.position.cpy()),this.position.cpy(), timeOnGround);
				else if (this.getClass().equals(Sheep.class))
					return new Dropped(new Sheep(this.position.cpy()),this.position.cpy(), timeOnGround);
				else if (this.getClass().equals(Cow.class))
					return new Dropped(new Cow(this.position.cpy()),this.position.cpy(), timeOnGround);
				else
					return new Dropped(new Goose(this.position.cpy()),this.position.cpy(), timeOnGround);
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
	
	
	public double getFertilityRate() {
		return fertilityRate;
	}

	public double getDropInterval() {
		return dropInterval/60.0;
	}

	public double getTimeOnGround() {
		return timeOnGround /60.0;
	}

	public void upgradeFertilityRate(double fertilityRate) {
		this.fertilityRate += fertilityRate;
	}

	public void upgradeDropInterval(double dropInterval) {
		this.dropInterval -= dropInterval;
	}

	public void upgradeTimeOnGround(double timeOnGround) {
		this.timeOnGround += timeOnGround;
	}
	
	public abstract DropType getDropType();
}
