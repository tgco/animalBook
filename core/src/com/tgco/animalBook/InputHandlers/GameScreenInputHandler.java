package com.tgco.animalBook.InputHandlers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.tgco.animalBook.AnimalBookGame;
import com.tgco.animalBook.screens.GameScreen;

public class GameScreenInputHandler implements InputProcessor {

	private AnimalBookGame gameInstance;
	private GameScreen gameScreen;
	
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 touch = new Vector3(screenX,screenY,0);
		//unproject touch to world coordinates
		gameScreen.getCamera().unproject(touch);
		gameScreen.setCameraTarget(touch);
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
