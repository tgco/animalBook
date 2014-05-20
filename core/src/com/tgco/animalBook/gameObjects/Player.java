package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Player extends Movable {
	
	private static final Texture texture = new Texture(Gdx.files.internal("objectTextures/player.png"));

	public Player() {
		super(texture);
	}
	
	@Override
	public void move(Vector2 target, Vector2 prevTarget) {
		
	}

}
