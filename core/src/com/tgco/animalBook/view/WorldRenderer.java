package com.tgco.animalBook.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.tgco.animalBook.gameObjects.ABDrawable;
import com.tgco.animalBook.gameObjects.Swipe;
import com.tgco.animalBook.handlers.Weather.WeatherType;


/**
 * Provides rendering for all drawable objects in the game world, including the health and progress bars
 * 
 * @author
 *
 * 
 */
public class WorldRenderer {

	/**
	 * Stores all swipes to be drawn on screen
	 */
	private Array<Swipe> swipes;

	/**
	 * Dimensions for the progress bar and slider
	 */
	private static final float PROGRESS_BAR_WIDTH = .02f*Gdx.graphics.getWidth();
	private static final float PROGRESS_BAR_HEIGHT = .85f*Gdx.graphics.getHeight();
	private static final float PROGRESS_SLIDER_WIDTH = .015f*Gdx.graphics.getWidth();
	private static final float PROGRESS_SLIDER_HEIGHT = .01f*Gdx.graphics.getHeight();

	private static final float TIME_TO_RAIN = 2f;

	private static final float TIME_TO_CLEAR = 2f;

	/**
	 * Textures for drawing primitive rectangles
	 */
	private Texture black = new Texture(Gdx.files.internal("primitiveTextures/black.png"));
	private Texture red = new Texture(Gdx.files.internal("primitiveTextures/red.png"));
	private Texture blue = new Texture(Gdx.files.internal("primitiveTextures/blue.png"));

	/**
	 * Texture for the progress bar
	 */
	private Texture progressBar = new Texture(Gdx.files.internal("objectTextures/progressBar.png"));

	private boolean rainy;
	private Sprite rainySprite;

	private boolean fadeToRain, steadyRain, fadeToClear;

	private float timeCounter;


	/**
	 * Default constructor
	 */
	public WorldRenderer() {
		swipes = new Array<Swipe>();
		rainySprite = new Sprite(black);
		rainySprite.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		timeCounter = 0;
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
	 * @param delta					the time between frames
	 */
	public void render(SpriteBatch batch, ArrayMap<String, Array<ABDrawable>> drawables, float playerHealth, float progressPercentage,OrthographicCamera cam,float delta) {
		//Draw all objects
		for (Array<ABDrawable> a : drawables.values()){
			for (ABDrawable drawable : a) {
				drawable.draw(batch);
				/*
				// will render all object bounds for collision detection when uncommented
				batch.end();
				ShapeRenderer render = new ShapeRenderer();
				render.setProjectionMatrix(cam.combined);
				render.begin(ShapeType.Line);
				render.rect(drawable.getBounds().x, drawable.getBounds().y, drawable.getBounds().width, drawable.getBounds().height);
				render.end();
				render.dispose();
				batch.begin();
				 */
			}
		}
		//Swipes on screen
		for (Swipe swipe : swipes) {
			if (swipe.getLifeTime() <= 1f) {
				swipes.removeValue(swipe, false);
				swipe.dispose();
			} else
				swipe.draw(batch,delta);
		}
		batch.end();

		SpriteBatch projectedBatch = new SpriteBatch();
		projectedBatch.begin();

		if (rainy){
			if (fadeToRain && timeCounter < TIME_TO_RAIN){
				rainySprite.draw(projectedBatch, .5f*timeCounter/TIME_TO_RAIN);
			}
			else{
				if (fadeToRain){
					fadeToRain = false;
					steadyRain = true;
					timeCounter = 0;
				}
			}
			if (steadyRain)
				rainySprite.draw(projectedBatch, .5f);
		}
		else{
			if (fadeToClear && timeCounter < TIME_TO_CLEAR){
				if (steadyRain){
					steadyRain = false;
				}
				rainySprite.draw(projectedBatch, .5f - .5f*timeCounter/TIME_TO_CLEAR);

			}
			else{
				if (fadeToClear){
					fadeToClear = false;
					timeCounter = 0;
				}
			}
		}
		if (!steadyRain && (fadeToRain || fadeToClear))
			timeCounter+=delta;
		
		//Progress bar
		projectedBatch.draw(progressBar,Gdx.graphics.getWidth() - 2f*(.019f)*Gdx.graphics.getWidth(), (.029f)*Gdx.graphics.getHeight(), PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
		projectedBatch.draw(black,Gdx.graphics.getWidth() - 2f*(.019f)*Gdx.graphics.getWidth() + .0025f*Gdx.graphics.getWidth(), (.029f)*Gdx.graphics.getHeight() + progressPercentage*(PROGRESS_BAR_HEIGHT), PROGRESS_SLIDER_WIDTH, PROGRESS_SLIDER_HEIGHT);

		projectedBatch.end();
		projectedBatch.dispose();


		batch.begin();
	}

	/**
	 * Renders all game objects to the screen without a progress bar
	 * 
	 * @param batch					the sprite batch used for rendering
	 * @param drawables				the array of drawable objects to render
	 * @param playerHealth			the player health to render
	 * @param cam					the camera used for converting screen and world coordinates
	 * @param delta					the time between frames
	 */
	public void render(SpriteBatch batch, ArrayMap<String, Array<ABDrawable>> drawables, float playerHealth, OrthographicCamera cam, float delta) {
		//Draw all objects
		for (Array<ABDrawable> a : drawables.values()){
			for (ABDrawable drawable : a) {
				drawable.draw(batch);
			}
		}
		//Swipes on screen
		for (Swipe swipe : swipes) {
			if (swipe.getLifeTime() <= 1f) {
				swipes.removeValue(swipe, false);
				swipe.dispose();
			} else
				swipe.draw(batch,delta);
		}
		if (rainy){
			batch.begin();

		}
	}

	/**
	 * Disposes of the texture objects to release memory
	 */
	public void dispose() {
		black.dispose();
		blue.dispose();
		red.dispose();
		progressBar.dispose();
	}

	public void setRainy(boolean incomingRain){
		if (incomingRain & !rainy){
			fadeToRain = true;
		}
		else{
			if (!incomingRain & rainy){
				fadeToClear = true;
			}
		}
		rainy = incomingRain;
	}
}
