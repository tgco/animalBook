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
	
	public void render(SpriteBatch batch, Array<Drawable> drawables, Player player, float progressPercentage) {
		//Draw all objects
		player.draw(batch);
		
		for (Drawable drawable : drawables) {
			drawable.draw(batch);
		}
		
		font.draw(batch, String.valueOf((int) player.getHealth()), player.getPosition().x - 50, player.getPosition().y - 50);
		
		shapeRender.begin(ShapeType.Filled);
		
		//Health bar
		shapeRender.setColor(Color.BLACK);
		shapeRender.rect(Gdx.graphics.getWidth()/2 - 50, Gdx.graphics.getHeight() - 50, 100, 25);
		shapeRender.setColor(Color.RED);
		shapeRender.rect(Gdx.graphics.getWidth()/2 - 47, Gdx.graphics.getHeight() - 47, 94*(player.getHealth()/100), 19);
		
		//Progress bar
		shapeRender.setColor(Color.CYAN);
		shapeRender.rect(20, 20, 20, 210);
		shapeRender.setColor(Color.BLACK);
		shapeRender.rect(20, 20 + progressPercentage*200, 20, 10);
		
		shapeRender.end();
	}

}
