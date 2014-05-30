package com.tgco.animalBook.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.gameObjects.Dropped;
import com.tgco.animalBook.gameObjects.Movable;
import com.tgco.animalBook.screens.GameScreen;
import com.tgco.animalBook.screens.MainMenuScreen;

/**
 * Handles input to create touch controls for the game screen, 
 * and implements logic for influencing animals with a swipe gesture.
 * 
 * @author
 * 
 * 
 */
public class GameScreenInputHandler implements InputProcessor {

	/**
	 * Reference to the game instance in order to change the current screen
	 */
	private AnimalBookGame gameInstance;
	
	/**
	 * Reference to the game screen in order to operate on game objects
	 */
	private GameScreen gameScreen;

	/**
	 * Holds the location of the last touch on screen
	 */
	private Vector3 lastTouch;

	/**
	 * Distinguishes distance a touch must move to register as a drag
	 */
	private static float touchToDragTolerance = 50f;

	/**
	 * The distance a movable must be within in order to be influenced by a drag
	 */
	private static final float HERD_TOLERANCE = 300f;

	/**
	 * Constructor that takes the needed references
	 * 
	 * @param gameInstance	reference to the running game instance
	 * @param gameScreen	reference to the current game screen
	 */
	public GameScreenInputHandler(AnimalBookGame gameInstance, GameScreen gameScreen) {
		this.gameInstance = gameInstance;
		this.gameScreen = gameScreen;
	}

	/**
	 * Detects a key press on down
	 * 
	 * @param keycode the keycode for which key was pressed
	 * @return 		  true if the keydown event was handled
	 */
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.BACK){
			gameInstance.setScreen(new MainMenuScreen(gameInstance));
			
			//store the data in levelData of Game
			
			// spot 1 is current level
			gameInstance.addToDatalevel(gameInstance.getLevelHandler().getLevel(),0);
			
			//spot 2 is player			
			gameInstance.addToDatalevel(gameScreen.getWorld().getPlayer(),1);
			
			//spot 3 is storing movable array
			gameInstance.addToDatalevel(gameScreen.getWorld().getMovables(),2);
			
			//spot 4 is storing dropped items array
			gameInstance.addToDatalevel(gameScreen.getWorld().getDropped(), 3);

			gameScreen.dispose();
		}
		return false;
	}

	/**
	 * Detects and finds where the screen is touched every frame
	 * 
	 * @param screenX the x coordinate in screen coordinates of the touch
	 * @param screenY the y coordinate in screen coordinates of the touch
	 * @param pointer the number of the touch when multiple touches are on screen
	 * @return 		  true if the touchDown event was handled
	 */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		//Store touch if one isn't already stored
		if (lastTouch == null) {
			lastTouch = new Vector3(screenX,screenY,0);
			//unproject to world coordinates
			gameScreen.getWorld().getCamera().unproject(lastTouch);

			if (AnimalBookGame.tapControls) {
				//Influence geese to the camera center if touched
				Vector3 camCenter = new Vector3(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,0);
				gameScreen.getWorld().getCamera().unproject(camCenter);
				Vector2 camCenter2d = new Vector2(camCenter.x,camCenter.y);
				
				for(Movable movable : gameScreen.getWorld().getMovables()) {
					if (movable.getBounds().contains(new Vector2(lastTouch.x,lastTouch.y))) {
						float reactionScale = 100;
						//SoundHandler.playWhistle();
						movable.addToCurrentTarget(camCenter2d.cpy().sub(movable.getPosition()).nor().scl(reactionScale));
					}
				}
			}

		}
		
		return false;
	}

	/**
	 * Detects when a touch leaves the screen
	 * 
	 * @param screenX the x coordinate in screen coordinates of the touch release
	 * @param screenY the y coordinate in screen coordinates of the touch release
	 * @param pointer the number of the touch release when multiple releases occur
	 * @return 		  true if the touch release event was handled
	 */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 touch = new Vector3(screenX,screenY,0);
		//unproject touch to world coordinates
		gameScreen.getWorld().getCamera().unproject(touch);

		//Determine if drag is registered
		if (lastTouch != null) {
			if ( touch.cpy().sub(lastTouch.cpy()).len() > touchToDragTolerance ) {
				//Drag gesture is detected, create an influence barrier between touch and last touch
				if (!gameScreen.isPaused()) {
					gameScreen.getWorld().addSwipeToWorld(lastTouch, touch);
					SoundHandler.playWhistle();
					herdWithDrag(lastTouch, touch, gameScreen.getWorld().getMovables());
				}
			}
		}

		//Remove dropped items that were touched
		for (int i = 0; i < gameScreen.getWorld().getDropped().size ; i++){
			Dropped dropping = 	gameScreen.getWorld().getDropped().get(i);
			if (!gameScreen.isPaused()){
				Vector3 vect = new Vector3(screenX,screenY,0);
				//unproject operations
				gameScreen.getWorld().getCamera().unproject(vect);
				Vector2 vect2 = new Vector2(vect.x, vect.y);
				if(dropping.getBounds().contains(vect2)){

					gameScreen.getWorld().removeFromABDrawable(dropping);
					SoundHandler.playPickup();
					dropping.dispose();
				}
			}
		}
		
		//Rest the lastTouch so touchDown will grab a new touch next time
		lastTouch = null;
		//False so other methods can interact with the touch still (buttons)
		return false;
	}

	/**
	 * Influences the motion of all movable objects away from the line created between startTouch
	 * and endTouch
	 * 
	 * @param startTouch the point where the line begins
	 * @param endTouch	 the point where the line ends
	 * @param movables	 all movable objects to influence
	 */
	public void herdWithDrag(Vector3 startTouch, Vector3 endTouch, Array<Movable> movables) {
		//Find middle of the drag
		Vector2 dragCenter = new Vector2((startTouch.x + endTouch.x)/2,(startTouch.y + endTouch.y)/2);
		//Find a unit vector representing the drag direction
		Vector2 dragUnitVector = new Vector2(startTouch.x - endTouch.x, startTouch.y - endTouch.y);
		dragUnitVector.nor();

		Vector2 perpProjection;
		Vector2 positionCenter;

		for (Movable movable : movables) {
			//find center of the drawable
			positionCenter = movable.getPosition();
			//find perpendicular projection of the position minus center onto unit vector
			perpProjection = (positionCenter.cpy().sub(dragCenter)).cpy().sub(dragUnitVector.cpy().scl((positionCenter.cpy().sub(dragCenter)).cpy().dot(dragUnitVector.cpy())));
			
			if (perpProjection.cpy().len() != 0) {
				if (positionCenter.cpy().sub(dragCenter).len() < HERD_TOLERANCE){

					//change to adjust how much a goose reacts to a drag (should depend on distance of goose from drag center)
					float reactionScale = 70000 * 1/positionCenter.cpy().sub(dragCenter).len();

					movable.addToCurrentTarget(perpProjection.cpy().nor().scl(reactionScale));
					
					//Add a line to draw the direction the goose was influenced
					gameScreen.getWorld().addSwipeToWorld(new Vector3(positionCenter.x,positionCenter.y,0), new Vector3(positionCenter.cpy().add(perpProjection.cpy().nor().scl(reactionScale)).x,positionCenter.cpy().add(perpProjection.cpy().nor().scl(reactionScale)).y,0));
				}
			}
		}

	}
	
	/**
	 * Unused input detection functions
	 */
	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
