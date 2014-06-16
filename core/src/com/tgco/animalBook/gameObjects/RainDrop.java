package com.tgco.animalBook.gameObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tgco.animalBook.AnimalBookGame;

public class RainDrop extends Movable {

	public RainDrop(String texturePath) {
		super(texturePath);
		//default speed
		speed = 2f;

		moveBias = 0f;

		rotation = 0;
		rotationSpeed = 0f;
		position = new Vector2(((float)(new Random().nextInt(Gdx.graphics.getWidth()))), Gdx.graphics.getHeight());
	}

	public RainDrop(String texturePath, Vector2 pos){
		super(texturePath);
		//default speed
		speed = 2f;

		moveBias = 0f;

		rotation = 0;
		rotationSpeed = 0f;
		position = pos;
		width = .011f*Gdx.graphics.getWidth();
		height = .011f*Gdx.graphics.getHeight();
		bounds = new Rectangle(position.x - width/2,position.y - height/2,width,height);
		previousTarget = position.cpy();
	}

	@Override
	public void move(float cameraSpeed, float delta) {
		position.add(0f, -speed*4f);
	}

	@Override
	public void bounce(Movable movable, Obstacle obstacle){
	}
}
