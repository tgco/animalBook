package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Animal extends Movable {
	
	//rate in percent that a child animal is spawned instead of a consumable
	private float fertilityRate;
	
	//interval between drop chances
	private float dropInterval;
	private int moveRate =0;
	

	public Animal(Texture texture) {
		super(texture);
		
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		
		batch.draw(texture, position.x, position.y, 75,75);
		move();
		if(moveRate % 60 == 0){
			changeTarget();	
		}
		moveRate++;
	}
	public void changeTarget(){
		currentTarget = new Vector2(position.x + rand.nextInt(100) -50, position.y +rand.nextInt(100)-50);
	}
	//Create a consumable or new animal
	public void drop() {
		
	}

}
