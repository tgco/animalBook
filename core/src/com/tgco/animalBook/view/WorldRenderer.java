package com.tgco.animalBook.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.tgco.animalBook.gameObjects.ABDrawable;
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

	private static final float WEATHER_TIME = 1.75f;

	private static final float ICON_FRAME_RATE = .5f;
	
	private static final float RENDER_UNIT = Gdx.graphics.getWidth()*1f/6f;

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

	private Texture compass = new Texture(Gdx.files.internal("weather/compass.png"));

	private float animationTimer;
	private TextureAtlas iconAtlas = new TextureAtlas(Gdx.files.internal("weather/weatherIconSprites.txt"));
	private Animation clearAnimation, rainyAnimation, snowyAnimation, windyAnimation;
	private Sprite rainySprite;

	private TextureRegion compassRegion;

	private boolean rainy, windy, clear, snowy;

	private boolean fadeToRain, steadyRain, fadeToReturnR;
	private boolean fadeToWind, steadyWind, fadeToReturnW;
	private boolean fadeToClear, steadyClear, fadeToReturnC;
	private boolean fadeToSnow, steadySnow, fadeToReturnS;

	private float timeCounterR, timeCounterW, timeCounterC, timeCounterS;

	private float compassAngle;


	/**
	 * Default constructor
	 */
	public WorldRenderer() {
		swipes = new Array<Swipe>();
		timeCounterR = 0;
		timeCounterW = 0;

		compassRegion = new TextureRegion();
		compassRegion.setTexture(compass);
		compassRegion.setRegionHeight(compass.getHeight());
		compassRegion.setRegionWidth(compass.getWidth());

		animationTimer = 0f;

		Array<TextureRegion> tempRegion = new Array<TextureRegion>();

		tempRegion.add(iconAtlas.findRegion("clear1"));
		clearAnimation = new Animation(ICON_FRAME_RATE, tempRegion, PlayMode.LOOP);
		tempRegion.clear();

		tempRegion.add(iconAtlas.findRegion("rain1"));
		tempRegion.add(iconAtlas.findRegion("rain2"));
		tempRegion.add(iconAtlas.findRegion("rain3"));
		rainyAnimation = new Animation(ICON_FRAME_RATE, tempRegion, PlayMode.LOOP);
		tempRegion.clear();

		tempRegion.add(iconAtlas.findRegion("snow1"));
		tempRegion.add(iconAtlas.findRegion("snow2"));
		tempRegion.add(iconAtlas.findRegion("snow3"));
		snowyAnimation = new Animation(ICON_FRAME_RATE, tempRegion, PlayMode.LOOP);
		tempRegion.clear();

		tempRegion.add(iconAtlas.findRegion("wind1"));
		tempRegion.add(iconAtlas.findRegion("wind2"));
		windyAnimation = new Animation(ICON_FRAME_RATE, tempRegion, PlayMode.LOOP);
		tempRegion.clear();
		
		rainySprite = new Sprite(black);
		rainySprite.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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

		//if rainy
		if (rainy){
			if (fadeToRain && timeCounterR < WEATHER_TIME){
				projectedBatch.setColor(1f, 1f, 1f, timeCounterR/WEATHER_TIME);
				projectedBatch.draw(rainyAnimation.getKeyFrame(animationTimer), Gdx.graphics.getWidth() - 1f/8f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight() -2f/20f*Gdx.graphics.getHeight(), 1f/2f*RENDER_UNIT, 1f/4f*RENDER_UNIT);
				rainySprite.draw(projectedBatch, .333f*timeCounterR/WEATHER_TIME);
				projectedBatch.setColor(Color.WHITE);
			}
			else{
				if (fadeToRain){
					fadeToRain = false;
					steadyRain = true;
					timeCounterR = 0;
				}
			}
			if (steadyRain){
				projectedBatch.draw(rainyAnimation.getKeyFrame(animationTimer), Gdx.graphics.getWidth() - 1f/8f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight() -2f/20f*Gdx.graphics.getHeight(), 1f/2f*RENDER_UNIT, 1f/4f*RENDER_UNIT);
				rainySprite.draw(projectedBatch, .333f);
			}
		}
		else{
			if (fadeToReturnR && timeCounterR < WEATHER_TIME){
				if (steadyRain){
					steadyRain = false;
				}
				projectedBatch.setColor(1f, 1f, 1f, 1f - timeCounterR/WEATHER_TIME);
				projectedBatch.draw(rainyAnimation.getKeyFrame(animationTimer), Gdx.graphics.getWidth() - 1f/8f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight() -2f/20f*Gdx.graphics.getHeight(), 1f/2f*RENDER_UNIT, 1f/4f*RENDER_UNIT);
				rainySprite.draw(projectedBatch, .333f - .333f*timeCounterR/WEATHER_TIME);
				projectedBatch.setColor(Color.WHITE);
			}
			else{
				if (fadeToReturnR){
					fadeToReturnR = false;
					timeCounterR = 0;
					animationTimer = 0;
				}
			}
		}
		if (!steadyRain && (fadeToRain || fadeToReturnR))
			timeCounterR+=delta;

		//if windy
		if (windy){

			if (fadeToWind && timeCounterW < WEATHER_TIME){
				//fade to wind
				projectedBatch.setColor(1f, 1f, 1f, timeCounterW/WEATHER_TIME);
				projectedBatch.draw(compassRegion, 0f, 0f, RENDER_UNIT/2f, RENDER_UNIT/2f, RENDER_UNIT, RENDER_UNIT, 1f, 1f, -90f + compassAngle*360f/((float)(2f*Math.PI)));
				projectedBatch.draw(windyAnimation.getKeyFrame(animationTimer), Gdx.graphics.getWidth() - 1f/8f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight() -2f/20f*Gdx.graphics.getHeight(), 1f/2f*RENDER_UNIT, 1f/4f*RENDER_UNIT);
				projectedBatch.setColor(Color.WHITE);
			}
			else{
				if (fadeToWind){
					fadeToWind = false;
					steadyWind = true;
					timeCounterW = 0;
				}
			}
			if (steadyWind){
				projectedBatch.draw(compassRegion, 0f, 0f, RENDER_UNIT/2f, RENDER_UNIT/2f, RENDER_UNIT, RENDER_UNIT, 1f, 1f, -90f + compassAngle*360f/((float)(2f*Math.PI)));
				projectedBatch.draw(windyAnimation.getKeyFrame(animationTimer), Gdx.graphics.getWidth() - 1f/8f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight() -2f/20f*Gdx.graphics.getHeight(), 1f/2f*RENDER_UNIT, 1f/4f*RENDER_UNIT);
				}
		}
		else{
			if (fadeToReturnW && timeCounterW < WEATHER_TIME){
				//back to clear
				if (steadyWind){
					steadyWind = false;
				}
				projectedBatch.setColor(1f, 1f, 1f, 1f - timeCounterW/WEATHER_TIME);
				projectedBatch.draw(compassRegion, 0f, 0f, RENDER_UNIT/2f, RENDER_UNIT/2f, RENDER_UNIT, RENDER_UNIT, 1f, 1f, -90f + compassAngle*360f/((float)(2f*Math.PI)));
				projectedBatch.draw(windyAnimation.getKeyFrame(animationTimer), Gdx.graphics.getWidth() - 1f/8f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight() -2f/20f*Gdx.graphics.getHeight(), 1f/2f*RENDER_UNIT, 1f/4f*RENDER_UNIT);
				projectedBatch.setColor(Color.WHITE);
			}
			else{
				if (fadeToReturnW){
					fadeToReturnW = false;
					timeCounterW = 0;
					animationTimer = 0;
				}
			}
		}
		if (!steadyWind && (fadeToWind || fadeToReturnW))
			timeCounterW+=delta;

		//if clear
		if (clear){

			if (fadeToClear && timeCounterC < WEATHER_TIME){
				//fade to clear
				projectedBatch.setColor(1f, 1f, 1f, timeCounterC/WEATHER_TIME);
				projectedBatch.draw(clearAnimation.getKeyFrame(animationTimer), Gdx.graphics.getWidth() - 1f/8f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight() -2f/20f*Gdx.graphics.getHeight(), 1f/2f*RENDER_UNIT, 1f/4f*RENDER_UNIT);
				projectedBatch.setColor(Color.WHITE);
			}
			else{
				if (fadeToClear){
					fadeToClear = false;
					steadyClear = true;
					timeCounterC = 0;
				}
			}
			if (steadyClear)
				projectedBatch.draw(clearAnimation.getKeyFrame(animationTimer), Gdx.graphics.getWidth() - 1f/8f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight() -2f/20f*Gdx.graphics.getHeight(), 1f/2f*RENDER_UNIT, 1f/4f*RENDER_UNIT);
			}
		else{
			if (fadeToReturnC && timeCounterC < WEATHER_TIME){
				//back to clear
				if (steadyClear){
					steadyClear = false;
				}
				projectedBatch.setColor(1f, 1f, 1f, 1f - timeCounterC/WEATHER_TIME);
				projectedBatch.draw(clearAnimation.getKeyFrame(animationTimer), Gdx.graphics.getWidth() - 1f/8f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight() -2f/20f*Gdx.graphics.getHeight(), 1f/2f*RENDER_UNIT, 1f/4f*RENDER_UNIT);
				projectedBatch.setColor(Color.WHITE);
			}
			else{
				if (fadeToReturnC){
					fadeToReturnC = false;
					timeCounterC = 0;
					animationTimer = 0;
				}
			}
		}
		if (!steadyClear && (fadeToClear || fadeToReturnC))
			timeCounterC+=delta;

		//if snow
		if (snowy){

			if (fadeToSnow && timeCounterS < WEATHER_TIME){
				//fade to clear
				projectedBatch.setColor(1f, 1f, 1f, timeCounterS/WEATHER_TIME);
				projectedBatch.draw(snowyAnimation.getKeyFrame(animationTimer), Gdx.graphics.getWidth() - 1f/8f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight() -2f/20f*Gdx.graphics.getHeight(), 1f/2f*RENDER_UNIT, 1f/4f*RENDER_UNIT);
				projectedBatch.setColor(Color.WHITE);
			}
			else{
				if (fadeToSnow){
					fadeToSnow = false;
					steadySnow = true;
					timeCounterS = 0;
				}
			}
			if (steadySnow)
				projectedBatch.draw(snowyAnimation.getKeyFrame(animationTimer), Gdx.graphics.getWidth() - 1f/8f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight() -2f/20f*Gdx.graphics.getHeight(), 1f/2f*RENDER_UNIT, 1f/4f*RENDER_UNIT);
			}
		else{
			if (fadeToReturnS && timeCounterS < WEATHER_TIME){
				//back to clear
				if (steadySnow){
					steadySnow = false;
				}
				projectedBatch.setColor(1f, 1f, 1f, 1f - timeCounterS/WEATHER_TIME);
				projectedBatch.draw(snowyAnimation.getKeyFrame(animationTimer), Gdx.graphics.getWidth() - 1f/8f*Gdx.graphics.getWidth(), Gdx.graphics.getHeight() -2f/20f*Gdx.graphics.getHeight(), 1f/2f*RENDER_UNIT, 1f/4f*RENDER_UNIT);
				projectedBatch.setColor(Color.WHITE);
			}
			else{
				if (fadeToReturnS){
					fadeToReturnS = false;
					timeCounterS = 0;
					animationTimer = 0;
				}
			}
		}
		if (!steadySnow && (fadeToSnow || fadeToReturnS))
			timeCounterS+=delta;

		animationTimer+=delta;
		//weather icon



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
		compass.dispose();
		iconAtlas.dispose();
	}

	public void setRainy(boolean incomingRain){
		if (incomingRain & !rainy){
			fadeToRain = true;
		}
		else{
			if (!incomingRain & rainy){
				fadeToReturnR = true;
			}
		}
		rainy = incomingRain;
	}

	public void setWindy(boolean incomingWind, Float angRad, Float mag){
		if (incomingWind & !windy){
			fadeToWind = true;
		}
		else{
			if (!incomingWind & windy){
				fadeToReturnW = true;
			}
		}

		windy = incomingWind;
		if (angRad != null && mag != null){
			compassAngle = angRad;
			//animationTimer = 0f;
		}
	}

	public void setSnowy(boolean b) {

		if (b & !snowy){
			fadeToSnow = true;
		}
		else{
			if (!b & snowy){
				fadeToReturnS = true;
			}
		}
		snowy = b;

	}

	public void setClear(boolean b) {

		if (b & !clear){
			fadeToClear = true;
		}
		else{
			if (!b & clear){
				fadeToReturnC = true;
			}
		}
		clear = b;
	}
}
