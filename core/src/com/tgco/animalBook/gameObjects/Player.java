/**
 * File: Player.java
 * The player houses the inventory, it's money and health, eating.
 */
package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player extends Movable {
	
	/** the texture is what the player looks like */
	private static final String texturePath = "objectTextures/player.png";
	/**inventory houses the consumables it's carrying */
	private final Inventory inventory = new Inventory();
	/** it starts at $0*/
	private int playerMoney = 0;
	/** it starts at full health */
	private float playerHealth = 100;

	/**
	 * the constructor which sets the variables of it's parent
	 * @param speed since it is a movable it must have some speed
	 */
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
	
	/**
	 * eat increases the player's health by a certain amount
	 * @param value how much the player adds in health
	 */
	public void eat(float value) {
		if ((playerHealth + value) > 100) {
			playerHealth = 100;
		} else {
			playerHealth += value;
		}
	}
	
	/**
	 * dereases the health as the game goes on
	 * @param amount decreases the health by this amount
	 */
	public void decreaseHealth(float amount) {
		playerHealth -= amount;
	}
	
	/**
	 * getter to get the health for displaying purposes and lossing purpose.
	 * @return playerHealth
	 */
	public float getHealth() {
		return playerHealth;
	}
	
	/**
	 * overRides the Movable move function to go with the camera in terms of on rails
	 * @param camerSpeed the speed of the camera
	 */
	@Override
	public void move(float cameraSpeed) {
		position.y += speed;
		bounds.y += speed;
	}

	/**
	 * getter for the speed();
	 * @return speed
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * setter for the speed of the person for levels
	 * @param newSpeed sets the new speed of the person
	 */
	public void setSpeed(float newSpeed) {
		speed = newSpeed;
	}
	
	/**
	 * getter for the player money for upgrades, market, and display on gameScreen
	 * @return player money
	 */
	public int getPlayerMoney() {
		return playerMoney;
	}

	/**
	 * adds the apropriate amount of money
	 * @param playerMoney amount adding to the money
	 */
	public void addPlayerMoney(float playerMoney) {
		this.playerMoney += playerMoney;
	}
	
	/**
	 * getter for the inventory used for the market and inventoryScreen
	 * @return inventory
	 */
	public Inventory getInventory(){
		return inventory;
	}

	/**
	 * Subtracts money when the player buys an upgrade
	 * @param playerMoney amount subtracted from the money of the player
	 */
	public void subtractPlayerMoney(float playerMoney) {
		this.playerMoney -= playerMoney;
	}
	public void resetPlayerPosition(){
		position = new Vector2(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/8);
		previousTarget = position.cpy();
		currentTarget = previousTarget.cpy();
		//width = .065f*Gdx.graphics.getWidth();
		//height = .21f*Gdx.graphics.getHeight();
		bounds = new Rectangle(position.x - width/2,position.y - height/2,width,height);
		super.resetTexture(texturePath);
	}
}
