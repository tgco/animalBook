package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Player extends Movable {
	
	private static final String texturePath = "objectTextures/player.png";

	public Player(OrthographicCamera camera) {
		super(texturePath);
		position = new Vector2(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/4);
		previousTarget = position.cpy();
		currentTarget = previousTarget;
		
		speed = 10;
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(texture, position.x, position.y, 56,114);
	}
	
	public void changeTarget(Vector2 target) {
		previousTarget = currentTarget;
		currentTarget = target;
	}
	
	@Override
	public void move() {
		previousTarget.lerp(currentTarget, Gdx.graphics.getDeltaTime()*(1/4f));
	}

}
