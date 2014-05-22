package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Player extends Movable {
	
	private static final String texturePath = "objectTextures/player.png";

	public Player(float speed) {
		super(texturePath);
		position = new Vector2(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/8);
		previousTarget = position.cpy();
		currentTarget = previousTarget.cpy();
		
		this.speed = speed;
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(texture, position.x, position.y, 70, 143);
	}
	
	@Override
	public void move(float cameraSpeed) {
		position.y += speed;
	}

}
