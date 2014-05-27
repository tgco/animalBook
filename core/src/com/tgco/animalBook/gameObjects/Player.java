package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player extends Movable {
	
	private static final String texturePath = "objectTextures/player.png";
	private static final Inventory inventory = new Inventory();
	private float playerMoney = 0;
	
	private float playerHealth = 100;

	public Player(float speed) {
		super(texturePath);
		position = new Vector2(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/8);
		previousTarget = position.cpy();
		currentTarget = previousTarget.cpy();
		
		width = .065f*Gdx.graphics.getWidth();
		height = .21f*Gdx.graphics.getHeight();
		bounds = new Rectangle(position.x - width/2,position.y - height/2,width,height);
		
		this.speed = speed;
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
		bounds.y += speed;
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

	public void addPlayerMoney(float playerMoney) {
		this.playerMoney += playerMoney;
	}
	public Inventory getInventory(){
		return inventory;
	}

	public void subtractPlayerMoney(float playerMoney) {
		this.playerMoney -= playerMoney;
		
	}

}
