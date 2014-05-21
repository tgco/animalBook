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
	
	
	public Movable(String texturePath) {
		super(texturePath);
		
	}
	
	 public void move() {
		 //Lerp the position to the target
		 position.lerp(previousTarget, speed*Gdx.graphics.getDeltaTime());
		 
		 //Lerp the previous target to the current
		 previousTarget.lerp(currentTarget, 30*speed*Gdx.graphics.getDeltaTime());
	 }
	 
	 public void addToCurrentTarget(Vector2 addition) {
		 currentTarget.add(addition);
	 }
	
	 public void setCurrentTarget(Vector2 target) {
		 currentTarget = target;
	 }
	 
	@Override
	public boolean isMovable() {
		return true;
	}

}
