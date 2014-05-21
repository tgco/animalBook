package com.tgco.animalBook.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundHandler {
	
	private static Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/farmNoise.wav"));
	private static Music storyBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/storyNoise.wav"));
	private static Music marketBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/marketNoise.mp3"));
	private static Sound buttonClick = Gdx.audio.newSound(Gdx.files.internal("sounds/buttonclick2.wav"));
	
	private static boolean soundMuted = false;
	private static boolean musicMuted = false;
	
	public static void playBackgroundMusic(boolean isLooping) {
		if (musicMuted) {
			return;
		} else {
			backgroundMusic.setLooping(isLooping);
			backgroundMusic.setVolume((float) 0.5);
			backgroundMusic.play();
		}
	}
	
	public static void playStoryBackgroundMusic(boolean isLooping) {
		if (musicMuted) {
			return;
		} else {
			storyBackgroundMusic.setLooping(isLooping);
			storyBackgroundMusic.setVolume((float) 0.5);
			storyBackgroundMusic.play();
		}
	}
	
	public static void playMarketBackgroundMusic(boolean isLooping) {
		if (musicMuted) {
			return;
		} else {
			marketBackgroundMusic.setLooping(isLooping);
			marketBackgroundMusic.setVolume((float) 0.5);
			marketBackgroundMusic.play();
		}
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
	
	public static void changeBackgroundVolume(float level) {
		if (musicMuted) {
			return;
		} else {
			backgroundMusic.setVolume(level);
		}
	}
	
	public static void playButtonClick() {
		if (soundMuted) {
			return;
		} else {
			buttonClick.play();
		}
	}
	
	public static void toggleSounds() {
		if (soundMuted) {
			soundMuted = false;
		} else {
			soundMuted = true;
		}
	}
	
	public static void toggleMusic() {
		if (musicMuted) {
			backgroundMusic.setVolume((float) 0.5);
			storyBackgroundMusic.setVolume((float) 0.3);
			marketBackgroundMusic.setVolume((float) 0.2);
			musicMuted = false;
		} else {
			backgroundMusic.setVolume(0);
			storyBackgroundMusic.setVolume(0);
			marketBackgroundMusic.setVolume(0);
			musicMuted = true;
		}
	}
	
	public static void resetAudio() {
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/farmNoise.wav"));
		 storyBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/storyNoise.wav"));
		 marketBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/marketNoise.mp3"));
		buttonClick = Gdx.audio.newSound(Gdx.files.internal("sounds/buttonclick2.wav"));
		//soundMuted = true;
		//musicMuted = true;
	}
	
	public static void dispose() {
		backgroundMusic.dispose();
		storyBackgroundMusic.dispose();
		marketBackgroundMusic.dispose();
		buttonClick.dispose();
	}
}
