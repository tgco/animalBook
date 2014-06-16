package com.tgco.animalBook.handlers;

import java.util.Random;


public class Weather {
	private Random rand;
	private WeatherType weather;
	private int numWeather;

	public Weather(){
		rand = new Random();
		weather = WeatherType.CLEAR;
		numWeather = 0;
	}

	public enum WeatherType{
		CLEAR ("Clear", ""),
		RAINY ("Rainy", ""),
		WINDY ("Windy", "");

		private WeatherType(String weatherName, String texturePath){
			this.weatherName = weatherName;
			this.texturePath = texturePath;
		}

		private final String weatherName;
		private final String texturePath;

		public final String getName(){
			return weatherName;
		}
		
		public final String getTexturePath(){
			return texturePath;
		}

	}
	
	public WeatherType getNewWeather(){
		/*if (rand.nextBoolean())
			return WeatherType.values()[rand.nextInt(WeatherType.values().length)];
		else
			return WeatherType.CLEAR;*/
		if (numWeather < Weather.WeatherType.values().length -1){
			numWeather++;
		}
		else
			numWeather=0;
		return WeatherType.values()[numWeather];
		
	}
	public WeatherType getWeather(){
		return weather;
	}
	public void setWeatherType(WeatherType wt){
		System.out.println(wt.getName());
		weather = wt;
	}

}
