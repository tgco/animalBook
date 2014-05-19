package com.tgco.animalBook.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.tgco.animalBook.gameObjects.Drawable;


//Renders all of the World's drawable objects to the screen
public class WorldRenderer {

	public WorldRenderer() {
		// TODO Auto-generated constructor stub
	}
	
	public void render(SpriteBatch batch, Array<Drawable> drawables) {
		//Draw all objects
		for (Drawable drawable : drawables) {
			drawable.draw(batch);
		}
	}

}
