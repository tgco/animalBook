package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.math.Vector2;

public class Dropped extends ABDrawable{
	private static final String texturePath = "objectTextures/dropEgg.jpg";
	private Consumable consume = null;
	private Animal animal = null;
	public Dropped(Consumable consume, Vector2 pos) {
		super(texturePath);
		this.consume = consume;
		this.position = pos;
		width = 25;
		height = 25;
	}
	
	public Dropped(Animal animal, Vector2 pos) {
		super(texturePath);
		this.animal = animal;
		this.position = pos;
		width = 25;
		height = 25;
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
	public boolean isDropping() {
		return true;
	}
}
