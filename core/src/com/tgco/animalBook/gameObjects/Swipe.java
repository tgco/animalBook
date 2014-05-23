package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.math.Vector2;


//A swipe on the screen
public class Swipe {
	
	private Vector2 begin;
	private Vector2 end;

	public Swipe(Vector2 begin, Vector2 end) {
		this.begin = begin;
		this.end = end;
	}

}
