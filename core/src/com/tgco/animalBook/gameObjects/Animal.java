package com.tgco.animalBook.gameObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Animal extends Movable {
	
	//rate in percent that a child animal is spawned instead of a consumable
	private float fertilityRate;
	
	//interval between drop chances
	private float dropInterval;
	private int changeTargetCount = 0;
	
	protected Random rand;

	public Animal(String texturePath, Vector2 position) {
		super(texturePath);
		//Animals start in the middle of the screen
		this.position = position;
		previousTarget = position.cpy();
		currentTarget = previousTarget.cpy();
		
		//bounds
		bounds = new Rectangle(position.x,position.y,width,height);
		
		rand = new Random();
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		
		
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
		
	}

}
