package com.tgco.animalBook.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tgco.animalBook.gameObjects.ABDrawable;
import com.tgco.animalBook.gameObjects.Cow;
import com.tgco.animalBook.gameObjects.Goat;
import com.tgco.animalBook.gameObjects.Goose;
import com.tgco.animalBook.gameObjects.Pig;
import com.tgco.animalBook.gameObjects.Sheep;

//instantiates correct object for the given level
public class LevelHandler {

	private int level;

	public LevelHandler(int level) {
		this.level = level;
	}

	public Array<ABDrawable> addAnimals(int level, int numAnimals) {

		Array<ABDrawable> animals = new Array<ABDrawable>();
		
		int x;
		for(int i = 0; i < numAnimals; i++){
			if(i < .5*numAnimals){
				x = -i;
				
				switch(level) {
				case 1 :
					animals.add(new Goose(new Vector2(Gdx.graphics.getWidth()/2 + x*40 -50, (float) (Gdx.graphics.getHeight()/2 -x*x*25 + 10*x -50))));
					break;
				case 2 :
					animals.add(new Pig(new Vector2(Gdx.graphics.getWidth()/2 + x*40 -50, (float) (Gdx.graphics.getHeight()/2 -x*x*25 + 10*x -50))));
					break;
				case 3 :
					animals.add(new Goat(new Vector2(Gdx.graphics.getWidth()/2 + x*40 -50, (float) (Gdx.graphics.getHeight()/2 -x*x*25 + 10*x -50))));
					break;
				case 4 :
					animals.add(new Sheep(new Vector2(Gdx.graphics.getWidth()/2 + x*40 -50, (float) (Gdx.graphics.getHeight()/2 -x*x*25 + 10*x -50))));
					break;
				case 5 :
					animals.add(new Cow(new Vector2(Gdx.graphics.getWidth()/2 + x*40 -50, (float) (Gdx.graphics.getHeight()/2 -x*x*25 + 10*x -50))));
					break;
				}
				
			}
			else {
				x = (i - (int)Math.floor(.5*numAnimals));
				
				switch(level) {
				case 1 :
					animals.add(new Goose(new Vector2(Gdx.graphics.getWidth()/2 + x*40, (float) (Gdx.graphics.getHeight()/2 -x*x*30 + 15*x -50))));
					break;
				case 2 :
					animals.add(new Pig(new Vector2(Gdx.graphics.getWidth()/2 + x*40, (float) (Gdx.graphics.getHeight()/2 -x*x*30 + 15*x -50))));
					break;
				case 3 :
					animals.add(new Goat(new Vector2(Gdx.graphics.getWidth()/2 + x*40, (float) (Gdx.graphics.getHeight()/2 -x*x*30 + 15*x -50))));
					break;
				case 4 :
					animals.add(new Sheep(new Vector2(Gdx.graphics.getWidth()/2 + x*40, (float) (Gdx.graphics.getHeight()/2 -x*x*30 + 15*x -50))));
					break;
				case 5 :
					animals.add(new Cow(new Vector2(Gdx.graphics.getWidth()/2 + x*40, (float) (Gdx.graphics.getHeight()/2 -x*x*30 + 15*x -50))));
					break;
				}
				
			}
		}
		
		return animals;


	}

}
