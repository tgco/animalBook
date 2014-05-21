package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Player extends Movable {
	
	private static final String texturePath = "objectTextures/player.png";

	public Player() {
		super(texturePath);
	}
	
	@Override
	public void move() {
		
	}

}
