package com.tgco.animalBook.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundHandler {
	
	private static final Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/farmNoise.wav"));
	private static final Sound buttonClick = Gdx.audio.newSound(Gdx.files.internal("sounds/buttonclick2.wav"));
	
	public static void playBackgroundMusic(boolean isLooping) {
		backgroundMusic.setLooping(isLooping);
		backgroundMusic.setVolume((float) 0.1);
		backgroundMusic.play();
	}
	
	public static void pauseBackgroundMusic() {
		backgroundMusic.pause();
	}
	
	public static void playButtonClick() {
		buttonClick.play();
	}
	
	public static void dispose() {
		backgroundMusic.dispose();
		buttonClick.dispose();
	}
	
	public static Music getBackgroundMusic() {
		return backgroundMusic;
	}

}
