package com.tgco.animalBook.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundHandler {
	
	private static final Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/farmNoise.wav"));
	private static final Music storyBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/storyNoise.wav"));
	private static final Music marketBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/marketNoise.mp3"));
	private static final Sound buttonClick = Gdx.audio.newSound(Gdx.files.internal("sounds/buttonclick2.wav"));
	
	public static void playBackgroundMusic(boolean isLooping) {
		backgroundMusic.setLooping(isLooping);
		backgroundMusic.setVolume((float) 0.5);
		backgroundMusic.play();
	}
	
	public static void playStoryBackgroundMusic(boolean isLooping) {
		storyBackgroundMusic.setLooping(isLooping);
		storyBackgroundMusic.setVolume((float) 0.5);
		storyBackgroundMusic.play();
	}
	
	public static void playMarketBackgroundMusic(boolean isLooping) {
		marketBackgroundMusic.setLooping(isLooping);
		marketBackgroundMusic.setVolume((float) 0.5);
		marketBackgroundMusic.play();
	}
	
	public static void pauseBackgroundMusic() {
		backgroundMusic.pause();
	}
	
	public static void pauseStoryBackgroundMusic() {
		storyBackgroundMusic.pause();
	}
	
	public static void pauseMarketBackgroundMusic() {
		marketBackgroundMusic.pause();
	}
	
	public static void playButtonClick() {
		buttonClick.play();
	}
	
	public static void dispose() {
		backgroundMusic.dispose();
		storyBackgroundMusic.dispose();
		buttonClick.dispose();
	}
	
	public static Music getBackgroundMusic() {
		return backgroundMusic;
	}

}
