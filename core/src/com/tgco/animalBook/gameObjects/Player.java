package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player extends Movable {
	
	private static final Texture texture = new Texture(Gdx.files.internal("objectTextures/player.png"));

	public Player() {
		super(texture);
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(texture, position.x, position.y, 125,125);
		move();
	}

}
