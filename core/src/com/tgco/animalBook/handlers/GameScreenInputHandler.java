package com.tgco.animalBook.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.screens.GameScreen;

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
		// TODO Auto-generated method stub
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
			if ( touch.cpy().sub(lastTouch.cpy()).len() < touchToDragTolerance )
				gameScreen.getWorld().setCameraTarget(touch);
			else {
				//Drag gesture is detected, draw a barrier between touch and last touch
				Gdx.app.log("InputHandler", "Drag captured");
			}
		}

		//Rest the lastTouch so touchDown will grab a new touch next time
		lastTouch = null;
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
