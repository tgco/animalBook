package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Player extends Movable {
	
	private static final String texturePath = "objectTextures/player.png";
	private static final Inventory inventory = new Inventory();
	private float playerMoney =0;
	
	private float playerHealth = 100;

	public Player(float speed) {
		super(texturePath);
		position = new Vector2(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/8);
		previousTarget = position.cpy();
		currentTarget = previousTarget.cpy();
		
		this.speed = speed;
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(texture, position.x, position.y, 70, 143);
	}
	
	public void eat(float value) {
		if ((playerHealth + value) > 100) {
			playerHealth = 100;
		} else {
			playerHealth += value;
		}
	}
	
	public void decreaseHealth(float amount) {
		playerHealth -= amount;
	}
	
	public float getHealth() {
		return playerHealth;
	}
	
	@Override
	public void move(float cameraSpeed) {
		position.y += speed;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float newSpeed) {
		speed = newSpeed;
	}
	
	public float getPlayerMoney() {
		return playerMoney;
	}

	public void setPlayerMoney(float playerMoney) {
		this.playerMoney = playerMoney;
	}
	public Inventory getInventory(){
		return inventory;
	}

}
