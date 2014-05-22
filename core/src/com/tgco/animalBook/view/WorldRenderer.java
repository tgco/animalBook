package com.tgco.animalBook.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tgco.animalBook.gameObjects.Drawable;
import com.tgco.animalBook.gameObjects.Player;


//Renders all of the World's drawable objects to the screen
public class WorldRenderer {

	private BitmapFont font;
	private ShapeRenderer shapeRender;
	
	public WorldRenderer() {
		font = new BitmapFont();
		shapeRender = new ShapeRenderer();
	}
	
	public void render(SpriteBatch batch, Array<Drawable> drawables, Player player, float health) {
		//Draw all objects
		player.draw(batch);
		
		for (Drawable drawable : drawables) {
			drawable.draw(batch);
		}
		
		font.draw(batch, String.valueOf((int) health), player.getPosition().x - 50, player.getPosition().y - 50);
		
		shapeRender.begin(ShapeType.Filled);
		shapeRender.setColor(Color.BLACK);
		shapeRender.rect(Gdx.graphics.getWidth()/2 - 50, Gdx.graphics.getHeight() - 50, 100, 25);
		shapeRender.setColor(Color.RED);
		shapeRender.rect(Gdx.graphics.getWidth()/2 - 47, Gdx.graphics.getHeight() - 47, 94*(health/100), 19);
		shapeRender.end();
	}

}
