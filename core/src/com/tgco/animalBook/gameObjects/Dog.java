package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Dog extends Animal {

	public Dog(String texturePath, Vector2 position) {
		super(texturePath, position);
		
		width = 100;
		height = 100;
		
		bounds = new Rectangle(position.x,position.y,width,height);
	}

}
