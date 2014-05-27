package com.tgco.animalBook.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.tgco.animalBook.gameObjects.ABDrawable;
import com.tgco.animalBook.gameObjects.Player;
import com.tgco.animalBook.gameObjects.Swipe;


//Renders all of the World's drawable objects to the screen
public class WorldRenderer {

	private BitmapFont font;
	private ShapeRenderer shapeRender;
	
	private Array<Swipe> swipes;
	
	private static final float PROGRESS_BAR_WIDTH = 20;
	private static final float PROGRESS_BAR_HEIGHT = 210;
	private static final float PROGRESS_SLIDER_WIDTH = 20;
	private static final float PROGRESS_SLIDER_HEIGHT = 10;
	

	public WorldRenderer() {
		font = new BitmapFont();
		shapeRender = new ShapeRenderer();
		
		swipes = new Array<Swipe>();
	}
	
	public void addSwipe(Vector2 begin, Vector2 end) {
		swipes.add(new Swipe(begin,end));
	}
	
	public void render(SpriteBatch batch, Array<ABDrawable> drawables, Player player, float progressPercentage,OrthographicCamera cam) {
		//Draw all objects
		player.draw(batch);
		
		for (ABDrawable drawable : drawables) {
			drawable.draw(batch);
		}
		
		//Draw all objects
		player.draw(batch);
		
		batch.end();
		
		shapeRender.begin(ShapeType.Filled);
		
		//Health bar
		shapeRender.setColor(Color.BLACK);
		shapeRender.rect(Gdx.graphics.getWidth()/2 - 50, Gdx.graphics.getHeight() - 50, 100, 25);
		shapeRender.setColor(Color.RED);
		shapeRender.rect(Gdx.graphics.getWidth()/2 - 47, Gdx.graphics.getHeight() - 47, 94*(player.getHealth()/100), 19);

		//Progress bar
		shapeRender.setColor(Color.CYAN);
		shapeRender.rect((.019f)*Gdx.graphics.getWidth(), (.029f)*Gdx.graphics.getHeight(), PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
		shapeRender.setColor(Color.BLACK);
		shapeRender.rect((.019f)*Gdx.graphics.getWidth(), (.029f)*Gdx.graphics.getHeight() + progressPercentage*(PROGRESS_BAR_HEIGHT - PROGRESS_SLIDER_HEIGHT), PROGRESS_SLIDER_WIDTH, PROGRESS_SLIDER_HEIGHT);
		
		shapeRender.end();
		
		//Swipes on screen
		for (Swipe swipe : swipes) {
			if (swipe.getLifeTime() == 0) {
				swipes.removeValue(swipe, false);
			} else
				swipe.draw(cam);
		}
		
		batch.begin();

	}
	
	public void dispose() {
		shapeRender.dispose();
	}

}
