package com.tgco.animalBook.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.tgco.animalBook.gameObjects.ABDrawable;
import com.tgco.animalBook.gameObjects.Player;
import com.tgco.animalBook.gameObjects.Swipe;


/**
 * Provides rendering for all drawable objects in the game world, including the health and progress bars
 * 
 * @author
 *
 * 
 */
public class WorldRenderer {

	/**
	 * Object that draws basic shapes used for the progress bar and health bar
	 */
	private ShapeRenderer shapeRender;

	/**
	 * Stores all swipes to be drawn on screen
	 */
	private Array<Swipe> swipes;

	/**
	 * Dimensions for the progress bar and slider
	 */
	private static final float PROGRESS_BAR_WIDTH = 20;
	private static final float PROGRESS_BAR_HEIGHT = 210;
	private static final float PROGRESS_SLIDER_WIDTH = 20;
	private static final float PROGRESS_SLIDER_HEIGHT = 10;


	/**
	 * Default constructor
	 */
	public WorldRenderer() {
		shapeRender = new ShapeRenderer();

		swipes = new Array<Swipe>();
	}

	/**
	 * Adds a swipe to the array using the end points
	 * 
	 * @param begin where the swipe begins
	 * @param end	where the swipe ends
	 */
	public void addSwipe(Vector2 begin, Vector2 end) {
		swipes.add(new Swipe(begin,end));
	}

	/**
	 * Renders all game objects to the screen
	 * 
	 * @param batch					the sprite batch used for rendering
	 * @param drawables				the array of drawable objects to render
	 * @param player				the player to render
	 * @param progressPercentage	the percentage of the game lane covered to render progress
	 * @param cam					the camera used for converting screen and world coordinates
	 */
	public void render(SpriteBatch batch, ArrayMap<String, Array<ABDrawable>> drawables, float playerHealth, float progressPercentage,OrthographicCamera cam) {
		//Draw all objects
		for (Array<ABDrawable> a : drawables.values()){
			for (ABDrawable drawable : a) {
				drawable.draw(batch);
			}
		}
		batch.end();


		shapeRender.begin(ShapeType.Filled);
		shapeRender.setColor(Color.BLACK);
		shapeRender.rect(Gdx.graphics.getWidth()/2 - .046f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - .073f*Gdx.graphics.getHeight(), .093f*Gdx.graphics.getWidth(), .037f*Gdx.graphics.getHeight());
		shapeRender.setColor(Color.RED);
		shapeRender.rect(Gdx.graphics.getWidth()/2 - .0435f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - .069f*Gdx.graphics.getHeight(), .087f*Gdx.graphics.getWidth()*(playerHealth/100), .028f*Gdx.graphics.getHeight());

		//Progress bar
		shapeRender.setColor(Color.CYAN);
		shapeRender.rect((.019f)*Gdx.graphics.getWidth(), (.029f)*Gdx.graphics.getHeight(), PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
		shapeRender.setColor(Color.BLACK);
		shapeRender.rect((.019f)*Gdx.graphics.getWidth(), (.029f)*Gdx.graphics.getHeight() + progressPercentage*(PROGRESS_BAR_HEIGHT - PROGRESS_SLIDER_HEIGHT), PROGRESS_SLIDER_WIDTH, PROGRESS_SLIDER_HEIGHT);

		shapeRender.end();

		//Swipes on screen
		/*
		for (Swipe swipe : swipes) {
			if (swipe.getLifeTime() == 0) {
				swipes.removeValue(swipe, false);
			} else
				swipe.draw(cam);
		}
		 */
		batch.begin();

	}

	/**
	 * Disposes of the shape render object to release memory
	 */
	public void dispose() {
		shapeRender.dispose();
	}

}
