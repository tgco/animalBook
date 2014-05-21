package com.tgco.animalBook.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.tgco.animalBook.AnimalBookGame;
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
		Vector2 moveTouch = new Vector2(screenX, screenY);
		//unproject touch to world coordinates
		gameScreen.getWorld().getCamera().unproject(touch);

		//Determine if drag is registered
		if (lastTouch != null) {
			if ( touch.cpy().sub(lastTouch.cpy()).len() < touchToDragTolerance ) {
				gameScreen.getWorld().setCameraTarget(touch);
				gameScreen.getWorld().setPlayerTarget(moveTouch);
			}
			else {
				//Drag gesture is detected, draw a barrier between touch and last touch
				Gdx.app.log("InputHandler", "Drag captured");
				herdWithDrag(lastTouch, touch, gameScreen.getWorld().getMovables());
			}
		}

		//Rest the lastTouch so touchDown will grab a new touch next time
		lastTouch = null;
		return false;
	}
	
	//Uses the ends of a drag line to influence all animals motion
	public void herdWithDrag(Vector3 startTouch, Vector3 endTouch, Array<Movable> movables) {
		//Find a unit vector representing the drag direction
		Vector2 dragUnitVector = new Vector2(startTouch.x - endTouch.x, startTouch.y - endTouch.y);
		dragUnitVector.nor();
		
		Vector2 perpProjection;

		for (Movable movable : movables) {
			//find perpendicular projection of position onto unit vector
			perpProjection = movable.getPosition().cpy().sub(dragUnitVector.cpy().scl(movable.getPosition().cpy().dot(dragUnitVector.cpy())));
			//Amount to move over the currentTarget of the animal
			if (perpProjection.cpy().len() != 0) {
				
				Vector2 dragAveragePosition = new Vector2((startTouch.x + endTouch.x)/2,(startTouch.y + endTouch.y)/2);
				float reactionScale = 250;
				float flip = 1;
				//if touch is closer to origin, change direction of influence
				if (movable.getPosition().cpy().len() < dragAveragePosition.cpy().len())
					flip = -1;
				
				movable.addToCurrentTarget(perpProjection.cpy().nor().scl(flip*reactionScale));
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
