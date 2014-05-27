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

public class GameScreenInputHandler implements InputProcessor {

	private AnimalBookGame gameInstance;
	private GameScreen gameScreen;

	//For creating barrier nodes
	private Vector3 lastTouch;

	//Distinguishes distance the finger must move to register a drag instead of a touch
	private static float touchToDragTolerance = 50f;
	
	//Must be under this distance to react to a swipe
	private static final float HERD_TOLERANCE = 300f;

	public GameScreenInputHandler(AnimalBookGame gameInstance, GameScreen gameScreen) {
		this.gameInstance = gameInstance;
		this.gameScreen = gameScreen;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.BACK){
			gameInstance.setScreen(new MainMenuScreen(gameInstance));
		}
		return false;
	}

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
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (lastTouch == null) {
			lastTouch = new Vector3(screenX,screenY,0);
			//unproject operations
			gameScreen.getWorld().getCamera().unproject(lastTouch);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 touch = new Vector3(screenX,screenY,0);
		//unproject touch to world coordinates
		gameScreen.getWorld().getCamera().unproject(touch);

		//Determine if drag is registered
		if (lastTouch != null) {
			if ( touch.cpy().sub(lastTouch.cpy()).len() > touchToDragTolerance ) {
				//Drag gesture is detected, draw a barrier between touch and last touch
				if (!gameScreen.isPaused()) {
					gameScreen.getWorld().addSwipeToWorld(lastTouch, touch);
					SoundHandler.playWhistle();
					herdWithDrag(lastTouch, touch, gameScreen.getWorld().getMovables());
				}
			}
		}

		for (int i =0; i < gameScreen.getWorld().getDropped().size ; i++){
			Dropped dropping = 	gameScreen.getWorld().getDropped().get(i);
			if (!gameScreen.isPaused()){
				Vector3 vect = new Vector3(screenX,screenY,0);
				//unproject operations
				gameScreen.getWorld().getCamera().unproject(vect);
				Vector2 vect2 = new Vector2(vect.x, vect.y);
				if(vect2.cpy().sub(dropping.getPosition().cpy()).len() <= 30){
					gameScreen.getWorld().removeFromABDrawable(dropping);
				}
			}
		}




		//Rest the lastTouch so touchDown will grab a new touch next time
		lastTouch = null;
		return false;
	}

	//Uses the ends of a drag line to influence all animals motion
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
			//Amount to move over the currentTarget of the animal
			if (perpProjection.cpy().len() != 0) {

				if (positionCenter.cpy().sub(dragCenter).len() < HERD_TOLERANCE){

					//change to adjust how much a goose reacts to a drag (should depend on distance of goose from drag center)
					float reactionScale = 70000 * 1/positionCenter.cpy().sub(dragCenter).len();

					movable.addToCurrentTarget(perpProjection.cpy().nor().scl(reactionScale));

					gameScreen.getWorld().addSwipeToWorld(new Vector3(positionCenter.x,positionCenter.y,0), new Vector3(positionCenter.cpy().add(perpProjection.cpy().nor().scl(reactionScale)).x,positionCenter.cpy().add(perpProjection.cpy().nor().scl(reactionScale)).y,0));
				}
			}
		}

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
