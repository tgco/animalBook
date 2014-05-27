package com.tgco.animalBook.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class lostDialog extends Dialog{

	public lostDialog(String title, Skin skin, String windowStyleName) {
		super(title, skin, windowStyleName);
		// TODO Auto-generated constructor stub
	}
	public lostDialog(String title, WindowStyle windowStyleName) {
		super(title, windowStyleName);
		// TODO Auto-generated constructor stub
	}
	public lostDialog(String title, Skin skin) {
		super(title, skin);
		// TODO Auto-generated constructor stub
	}
	
	{
		text("You lost all your animals");
		button("Retry");
		button("Quit");
		
	}
	
	@Override
	public void result(Object object){
		
	}
}
