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
		super.position = new Vector2(camera.position.x, camera.position.y);
		super.currentTarget = position;
		previousTarget = position;
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(texture, position.x, position.y - 114, 56, 114);
		move();
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
