package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.math.Vector2;

//Objects that will move on screen
public abstract class Movable extends Drawable {

	protected float speed;
	protected Vector2 previousTarget;
	protected Vector2 currentTarget;
	
	public Movable() {
		// TODO Auto-generated constructor stub
	}
	
	public void move() {
		
	}

}
