package com.tgco.animalBook.gameObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

//Objects that will move on screen
public abstract class Movable extends Drawable {

	protected float speed;
	protected Vector2 previousTarget;
	protected Vector2 currentTarget;
	protected Random rand= new Random();
	
	public Movable(String texturePath) {
		super(texturePath);
	}
	
	 public void move(){
		 previousTarget.lerp(currentTarget, Gdx.graphics.getDeltaTime()*(1/8f));
		 position = previousTarget;
	 }
	
	@Override
	public boolean isMovable() {
		return true;
	}

}
