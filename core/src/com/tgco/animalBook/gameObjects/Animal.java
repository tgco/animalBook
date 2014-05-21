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
	

	public Animal(String texturePath) {
		super(texturePath);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		
		batch.draw(texture, position.x, position.y, 125,125);
		move();
		if(moveRate % 120 == 0 && rand.nextInt(100) <20){
			
			changeTarget();	
		}
		moveRate++;
	}
	public void changeTarget(){
		currentTarget = new Vector2(position.x + rand.nextInt(400) -200, position.y +rand.nextInt(400)-200);
	}
	//Create a consumable or new animal
	public void drop() {
		
	}

}
