package com.kylepaulsen.openAndroidGame;

import java.util.Random;

import android.content.Context;

/*
 * This class will be in charge of generating random worlds based off of
 * some random seed (maybe a modulus of last gps coords?)
 * Worlds should be a 2d array that will later be tiled with sprites by the
 * Camera class.
 */

public class World {
	private Context con;
	private int worldArr[][];
	private long worldSeed;
	private double lon, lat;
	private Random ran;
	
	public World(Context context, long seed, double latitude, double longitude){
		//not sure what goes here yet.
		this.con = context;
		this.worldSeed = seed;
		this.ran = new Random(this.worldSeed);
		worldArr = new int[500][500];
		
		//a really crappy way of getting the long, lat coords back.
		this.lat = latitude;
		this.lon = longitude;
	}
	
	public void generateWorld(){
		//This method will use the var ran to generate a world into the
		//worldArr. I'll get to this later..
		Biome biome = new Biome(this.con, this.lat, this.lon);
		biome.anazlyze();
		
		//access biome vars to generate map....
		
	}
}
