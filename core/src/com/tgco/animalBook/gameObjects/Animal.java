package com.tgco.animalBook.gameObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Animal extends Movable {
	
	//rate in percent that a child animal is spawned instead of a consumable
	private double fertilityRate;
	
	//interval between drop chances
	private double dropInterval;
	private double timeOnGround;
	private int changeTargetCount = 0;
	
	protected Random rand;

	public Animal(String texturePath, Vector2 position) {
		super(texturePath);
		//Animals start in the middle of the screen
		this.position = position;
		previousTarget = position.cpy();
		currentTarget = previousTarget.cpy();
		
		//initialize the rates
		fertilityRate = 5;
		dropInterval = 100;
		timeOnGround = 120;
		
		rand = new Random();
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		
		batch.draw(texture, position.x, position.y, 100,100);
		
		
		if(changeTargetCount % 120 == 0 && rand.nextInt(100) < 20){

			changeTarget();	
		}
		
		changeTargetCount++;
	}
	public void changeTarget(){
		currentTarget = new Vector2(position.x + rand.nextInt(400) -200, position.y +rand.nextInt(400)-200);
	}
	
	
	//Create a consumable or new animal
	public void drop() {
		if(changeTargetCount % dropInterval ==0){
			if(rand.nextInt(100) < fertilityRate){
				//drop animal
			}
			else{
				//drop egg
			}
		}
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
	
}
