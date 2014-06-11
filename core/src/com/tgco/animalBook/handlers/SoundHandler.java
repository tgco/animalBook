package com.tgco.animalBook.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Static class that stores all of the sound objects for the game.  Each method is static to allow any
 * class in the code to access sound methods.
 * 
 * @author
 *
 */
public class SoundHandler {

	/**
	 * Static member variables for each sound clip
	 */
	private static Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/farmNoise.wav"));
	private static Music storyBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/storyNoise.wav"));
	private static Music marketBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/marketNoise.mp3"));
	private static Music whistle = Gdx.audio.newMusic(Gdx.files.internal("sounds/whistle.wav"));
	private static Sound buttonClick = Gdx.audio.newSound(Gdx.files.internal("sounds/buttonclick2.wav"));
	private static Sound pickup = Gdx.audio.newSound(Gdx.files.internal("sounds/pickup.wav"));
	private static Sound gooseHonk = Gdx.audio.newSound(Gdx.files.internal("sounds/gooseHonk.wav"));

	/**
	 * Booleans that store if the sound or music is currently muted
	 */
	private static boolean soundMuted = false;
	private static boolean musicMuted = false;

	/**
	 * Plays the file stored as backgroundMusic at relative volume 0.5. Sets the music to loop
	 * if isLooping is <code>true</code>.
	 * <p>
	 * This method checks the status of the boolean musicMuted first. If musicMuted is <code>true</code>, 
	 * this method does nothing and simply returns. If musicMuted is <code>false</code>, this
	 * method calls the play method of the Music object.
	 * 
	 * @param isLooping a boolean telling the method whether it should loop the music
	 */
	public static void playBackgroundMusic(boolean isLooping) {
		if (musicMuted) {
			return;
		} else {
			backgroundMusic.setLooping(isLooping);
			backgroundMusic.setVolume((float) 0.3);
			backgroundMusic.play();
		}
	}

	/**
	 * Plays the file stored as storyBackgroundMusic at relative volume 0.5. Sets the music to 
	 * loop if isLooping is <code>true</code>.
	 * <p>
	 * This method checks the status of the boolean musicMuted first. If musicMuted is <code>true</code>, 
	 * this method does nothing and simply returns. If musicMuted is <code>false</code>, this
	 * method calls the play method of the Music object.
	 * 
	 * @param isLooping a boolean telling the method whether it should loop the music
	 */
	public static void playStoryBackgroundMusic(boolean isLooping) {
		if (musicMuted) {
			return;
		} else {
			storyBackgroundMusic.setLooping(isLooping);
			storyBackgroundMusic.setVolume((float) 0.35);
			storyBackgroundMusic.play();
		}
	}

	/**
	 * Plays the file stored as marketBackgroundMusic at relative volume 0.5. Sets the music to 
	 * loop if isLooping is <code>true</code>.
	 * <p>
	 * This method checks the status of the boolean musicMuted first. If musicMuted is <code>true</code>, 
	 * this method does nothing and simply returns. If musicMuted is <code>false</code>, this
	 * method calls the play method of the Music object.
	 * 
	 * @param isLooping a boolean telling the method whether it should loop the music
	 */
	public static void playMarketBackgroundMusic(boolean isLooping) {
		if (musicMuted) {
			return;
		} else {
			marketBackgroundMusic.setLooping(isLooping);
			marketBackgroundMusic.setVolume((float) 0.1);
			marketBackgroundMusic.play();
		}
	}

	/**
	 * Pauses the file stored as backgroundMusic. When unpaused, the music will resume at the
	 * exact point it was paused at.
	 */
	public static void pauseBackgroundMusic() {
		backgroundMusic.pause();
	}

	/**
	 * Pauses the file stored as storyBackgroundMusic. When unpaused, the music will resume at the
	 * exact point it was paused at.
	 */
	public static void pauseStoryBackgroundMusic() {
		storyBackgroundMusic.pause();
	}

	/**
	 * Pauses the file stored as marketBackgroundMusic. When unpaused, the music will resume at the
	 * exact point it was paused at.
	 */
	public static void pauseMarketBackgroundMusic() {
		marketBackgroundMusic.pause();
	}

	/**
	 * Changes the relative volume of the file stored as backgroundMusic.
	 * <p>
	 * This method checks the status of the boolean musicMuted first. If musicMuted is <code>true</code>, 
	 * this method does nothing and simply returns. If musicMuted is <code>false</code>, this
	 * method calls the setVolume method of the Music object.
	 * 
	 * @param level a float from 0 to 1 giving the relative volume of the music
	 */
	public static void changeBackgroundVolume(float level) {
		if (musicMuted) {
			return;
		} else {
			backgroundMusic.setVolume(level);
		}
	}

	/**
	 * Plays the file stored as buttonClick.
	 * <p>
	 * This method checks the status of the boolean soundMuted first. If soundMuted is <code>true</code>, 
	 * this method does nothing and simply returns. If soundMuted is <code>false</code>, this
	 * method calls the play method of the Sound object.
	 */
	public static void playButtonClick() {
		if (soundMuted) {
			return;
		} else {
			buttonClick.play(.4f);
		}
	}

	/**
	 * Plays the file stored as whistle.
	 * <p>
	 * This method checks the status of the boolean soundMuted first. If soundMuted is <code>true</code>, 
	 * this method does nothing and simply returns. If soundMuted is <code>false</code>, this
	 * method calls the play method of the Sound object.
	 */
	public static void playWhistle() {
		if (!whistle.isPlaying()) {
			if (soundMuted) {
				return;
			} else {
				whistle.setVolume(.1f);
				whistle.play();
			}
		}
	} 

	/**
	 * Plays the file stored as pickup.
	 * <p>
	 * This method checks the status of the boolean soundMuted first. If soundMuted is <code>true</code>, 
	 * this method does nothing and simply returns. If soundMuted is <code>false</code>, this
	 * method calls the play method of the Sound object.
	 */
	public static void playPickup() {
		if (soundMuted) {
			return;
		} else {
			pickup.play(1);
		}
	}
	
	public static void playNewAnimalSound(int level) {
		if (soundMuted) {
			return;
		} else {
			switch(level) {
			case 1:
				gooseHonk.play(.2f);
				break;
			case 2:
				//play pig sound
				break;
			case 3:
				//play goat sound
				break;
			case 4:
				//play sheep sound
				break;
			case 5:
				//play cow sound
				break;
			}
		}
	}

	/**
	 * Changes the value of soundMuted.
	 * <p>
	 * This method sets toggles soundMuted between true and false. It always sets the boolean to
	 * the opposite of its current value.
	 */
	public static void toggleSounds() {
		soundMuted = !soundMuted;
	}

	/**
	 * Changes the value of musicMuted and changes the volume of all music.
	 * <p>
	 * This method checks the status of the boolean musicMuted first. If musicMuted is <code>true</code>, this
	 * method sets all music back to their default values (as they were initialized), and changes
	 * musicMuted to <code>false</code>. If musicMuted is <code>false</code>, this method sets the volume of 
	 * all music to 0, and changes musicMuted to <code>true</code>. 
	 */
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

	/**
	 * Reinitializes all Sound and Music variables.
	 */
	public static void resetAudio() {
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/farmNoise.wav"));
		storyBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/storyNoise.wav"));
		marketBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/marketNoise.mp3"));
		buttonClick = Gdx.audio.newSound(Gdx.files.internal("sounds/buttonclick2.wav"));
		whistle = Gdx.audio.newMusic(Gdx.files.internal("sounds/whistle.wav"));
		pickup = Gdx.audio.newSound(Gdx.files.internal("sounds/pickup.wav"));
		//soundMuted = true;
		//musicMuted = true;
	}

	/**
	 * Disposes of all Sound and Music variables.
	 */
	public static void dispose() {
		backgroundMusic.dispose();
		storyBackgroundMusic.dispose();
		marketBackgroundMusic.dispose();
		buttonClick.dispose();
		whistle.dispose();
		pickup.dispose();
		gooseHonk.dispose();
	}

	public static boolean isSoundMuted() {
		return soundMuted;
	}

	public static boolean isMusicMuted() {
		return musicMuted;
	}

	public static void setSoundMuted(boolean soundMuted) {
		SoundHandler.soundMuted = soundMuted;
	}

	public static void setMusicMuted(boolean musicMuted) {
		SoundHandler.musicMuted = musicMuted;
	}


}
