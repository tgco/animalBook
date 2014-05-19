package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

//Objects that will be rendered to the screen
public abstract class Drawable {
	
	protected Texture texture;
	
	protected Vector2 position;

	public Drawable() {
		// TODO Auto-generated constructor stub
	}
	
	//determines if this object can be moved
	public boolean isMovable() {
		return false;
	}
	
	public void dispose() {
		texture.dispose();
	}

}
