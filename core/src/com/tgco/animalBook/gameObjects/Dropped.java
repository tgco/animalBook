package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Dropped extends ABDrawable{

	private Consumable consume = null;
	private Animal animal = null;
	private double timeLeft;

	public Dropped(Consumable consume, Vector2 pos, double timeLeft) {
		super(consume.getType().getTexturePath());
		this.consume = consume;
		this.position = pos;
		width = 25;
		height = 25;
		this.timeLeft = timeLeft;

	}

	public Dropped(Animal animal, Vector2 pos, double timeLeft) {
		super(animal.getDropType().getTexturePath());
		this.animal = animal;
		this.position = pos;
		width = 25;
		height = 25;
		this.timeLeft = timeLeft;
	}

	public ABDrawable getDropped(){
		if(animal !=null){
			return animal;
		}
		else{
			return consume;
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		timeLeft--;
	}


	public double getTimeLeft() {
		return timeLeft;
	}

	@Override
	public boolean isDropping() {
		return true;
	}
}
