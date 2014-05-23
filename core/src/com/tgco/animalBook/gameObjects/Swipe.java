package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;


//A swipe on the screen
public class Swipe {
	
	private Vector2 begin;
	private Vector2 end;
	
	private int lifeTime;

	public Swipe(Vector2 begin, Vector2 end) {
		this.begin = begin;
		this.end = end;
		
		lifeTime = 100;
	}
	
	public void draw(ShapeRenderer render) {
		render.begin(ShapeType.Line);
		render.line(begin.x, begin.y, end.x, end.y, Color.ORANGE, Color.PINK);
		render.end();
		
		lifeTime -= 1;
	}
	
	public int getLifeTime() {
		return lifeTime;
	}

}
