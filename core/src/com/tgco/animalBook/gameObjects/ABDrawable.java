package com.tgco.animalBook.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

//Objects that will be rendered to the screen
public abstract class ABDrawable {
	
	protected Texture texture;
	protected float width, height;
	protected Rectangle bounds;
	
	protected Vector2 position;

	public ABDrawable(String texturePath) {
		this.texture = new Texture(Gdx.files.internal(texturePath));
		//default values
		position = new Vector2();
		width = 100;
		height = 100;
		bounds = new Rectangle(position.x - width/2,position.y - height/2,width,height);
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(texture, position.x - width/2, position.y - height/2, width, height);
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public void setPosition(Vector2 position) {
		this.position = position;
		bounds.setX(position.x - width/2);
		bounds.setY(position.y - height/2);
	}
	
	//determines if this object can be moved
	public boolean isMovable() {
		return false;
	}
	
	public boolean isMarket() {
		return false;
	}
	
	public boolean isDropping() {
		return false;
	}
	public void dispose() {
		texture.dispose();
	}
	
	public Texture getTexture(){
		return texture;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getWidth() {
		return width;
	}

}
