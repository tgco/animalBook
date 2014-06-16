package com.tgco.animalBook.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.tgco.animalBook.gameObjects.ABDrawable;
import com.tgco.animalBook.gameObjects.Dog;
import com.tgco.animalBook.gameObjects.RainDrop;
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
	private static final float TIME_TO_WIND = .75f;
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

	private Texture compass = new Texture(Gdx.files.internal("objectTextures/compass.png"));
	private Texture windy1 = new Texture(Gdx.files.internal("objectTextures/wind1.png"));
	private Texture windy2 = new Texture(Gdx.files.internal("objectTextures/wind2.png"));
	private TextureRegion compassRegion, windyRegion1, windyRegion2;
	private Animation windyAnimation;

	private boolean rainy;
	private Sprite rainySprite;

	private boolean fadeToRain, steadyRain, fadeToClearR;
	private boolean fadeToWind, steadyWind, fadeToClearW;

	private float timeCounterR, timeCounterW, animationTimer;

	private boolean windy;

	private float compassAngle;



	/**
	 * Default constructor
	 */
	public WorldRenderer() {
		swipes = new Array<Swipe>();
		rainySprite = new Sprite(black);
		rainySprite.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		timeCounterR = 0;
		timeCounterW = 0;

		compassRegion = new TextureRegion();
		compassRegion.setTexture(compass);
		compassRegion.setRegionHeight(compass.getHeight());
		compassRegion.setRegionWidth(compass.getWidth());
		
		windyRegion1 = new TextureRegion(windy1);
		windyRegion1.setRegionHeight(windy1.getHeight());
		windyRegion1.setRegionWidth(windy1.getWidth());
		windyRegion2 = new TextureRegion(windy2);
		windyRegion2.setRegionHeight(windy2.getHeight());
		windyRegion2.setRegionWidth(windy2.getWidth());
		Array<TextureRegion> tArray = new Array<TextureRegion>();
		tArray.add(windyRegion1);
		tArray.add(windyRegion2);
		
		windyAnimation = new Animation(.5f,
				tArray,
				PlayMode.LOOP);
		animationTimer = 0f;
		

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
		for (Array<ABDrawable> drawable : drawables.values()){
			for (ABDrawable d : drawable){
				if (!(d.getClass().equals("RainDrop")))
					d.draw(batch);
				if (d instanceof Dog) {
					Gdx.app.log("wat", d.getPosition().toString() + ", cam: " + cam.position.toString());
				}
			}
			/*
		for (Array<ABDrawable> a : drawables.values()){
			for (ABDrawable drawable : a) {
				if (!(drawable instanceof RainDrop))
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
			if (fadeToRain && timeCounterR < TIME_TO_RAIN){
				rainySprite.draw(projectedBatch, .5f*timeCounterR/TIME_TO_RAIN);
			}
			else{
				if (fadeToRain){
					fadeToRain = false;
					steadyRain = true;
					timeCounterR = 0;
				}
			}
			if (steadyRain)
				rainySprite.draw(projectedBatch, .5f);
		}
		else{
			if (fadeToClearR && timeCounterR < TIME_TO_CLEAR){
				if (steadyRain){
					steadyRain = false;
				}
				rainySprite.draw(projectedBatch, .5f - .5f*timeCounterR/TIME_TO_CLEAR);
			}
			else{
				if (fadeToClearR){
					fadeToClearR = false;
					timeCounterR = 0;
				}
			}
		}
		if (!steadyRain && (fadeToRain || fadeToClearR))
			timeCounterR+=delta;
		//if windy

		if (windy){
			animationTimer+=delta;
			if (fadeToWind && timeCounterW < TIME_TO_WIND){
				//fade to wind
				projectedBatch.setColor(1f, 1f, 1f, timeCounterW/TIME_TO_WIND);
				projectedBatch.draw(compassRegion, 0f, 0f, compass.getWidth()/2f, compass.getHeight()/2f, compass.getWidth(), compass.getHeight(), 1f, 1f, -90f + compassAngle*360f/((float)(2f*Math.PI)));
				projectedBatch.draw(windyAnimation.getKeyFrame(animationTimer), ((float)(compassRegion.getRegionWidth())) + 30f, 0f);
				projectedBatch.setColor(Color.WHITE);
			}
			else{
				if (fadeToWind){
					fadeToWind = false;
					steadyWind = true;
					timeCounterW = 0;
				}
			}
			if (steadyWind)
				//steady wind
				projectedBatch.draw(compassRegion, 0f, 0f, compass.getWidth()/2f, compass.getHeight()/2f, compass.getWidth(), compass.getHeight(), 1f, 1f, -90f + compassAngle*360f/((float)(2f*Math.PI)));
			projectedBatch.draw(windyAnimation.getKeyFrame(animationTimer), ((float)(compassRegion.getRegionWidth())) + 30f, 0f);
		}
		else{
			if (fadeToClearW && timeCounterW < TIME_TO_WIND){
				//back to clear
				if (steadyWind){
					steadyWind = false;
				}
				projectedBatch.setColor(1f, 1f, 1f, 1f - timeCounterW/TIME_TO_WIND);
				projectedBatch.draw(compassRegion, 0f, 0f, compass.getWidth()/2f, compass.getHeight()/2f, compass.getWidth(), compass.getHeight(), 1f, 1f, -90f + compassAngle*360f/((float)(2f*Math.PI)));
				projectedBatch.draw(windyAnimation.getKeyFrame(animationTimer), ((float)(compassRegion.getRegionWidth())) + 30f, 0f);
				projectedBatch.setColor(Color.WHITE);
			}
			else{
				if (fadeToClearW){
					fadeToClearW = false;
					timeCounterW = 0;
				}
			}
		}
		if (!steadyWind && (fadeToWind || fadeToClearW))
			timeCounterW+=delta;



		//Progress bar
		projectedBatch.draw(progressBar,Gdx.graphics.getWidth() - 2f*(.019f)*Gdx.graphics.getWidth(), (.029f)*Gdx.graphics.getHeight(), PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
		projectedBatch.draw(black,Gdx.graphics.getWidth() - 2f*(.019f)*Gdx.graphics.getWidth() + .0025f*Gdx.graphics.getWidth(), (.029f)*Gdx.graphics.getHeight() + progressPercentage*(PROGRESS_BAR_HEIGHT), PROGRESS_SLIDER_WIDTH, PROGRESS_SLIDER_HEIGHT);
		for (ABDrawable a : drawables.get("WeatherDrop")){
			a.draw(projectedBatch);
		}

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
				fadeToClearR = true;
			}
		}
		rainy = incomingRain;
	}

	public void setWindy(boolean incomingWind, Float angRad){
		if (incomingWind & !windy){
			fadeToWind = true;
		}
		else{
			if (!incomingWind & windy){
				fadeToClearW = true;
			}
		}

		windy = incomingWind;
		if (angRad != null){
			compassAngle = angRad;
			animationTimer = 0f;
		}
	}
}
