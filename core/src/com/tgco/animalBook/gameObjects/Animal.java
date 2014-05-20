package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Animal extends Movable {
	
	//rate in percent that a child animal is spawned instead of a consumable
	private float fertilityRate;
	
	//interval between drop chances
	private float dropInterval;
	

	public Animal(Texture texture) {
		super(texture);
		
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		
		batch.draw(texture, position.x, position.y, 50,50);
	}
	//Create a consumable or new animal
	public void drop() {
		
	}

}
