package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

//Objects that will move on screen
public abstract class Movable extends Drawable {

	protected float speed;
	protected Vector2 previousTarget;
	protected Vector2 currentTarget;
	
	public Movable(Texture texture) {
		super(texture);
	}
	
	public void move() {
		
	}
	
	@Override
	public boolean isMovable() {
		return true;
	}

}
