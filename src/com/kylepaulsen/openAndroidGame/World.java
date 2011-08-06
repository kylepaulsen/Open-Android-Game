/* World.java - Class for generating worlds in a 2d array.
 * 
 * Copyright © 2011 Open-Android-Game Group
 * Please see the file COPYING in this
 * distribution for license terms.
 */

package com.kylepaulsen.openAndroidGame;

import java.util.Random;

import android.content.Context;
import android.util.Log;

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
		worldArr = new int[Constants.WORLD_SIZE][Constants.WORLD_SIZE];
		
		this.lat = latitude;
		this.lon = longitude;
	}
	
	public void generateWorld(){
		//This method will use the var ran to generate a world into the
		//worldArr. I'll get to this later..
		
		//Biome biome = new Biome(this.con, this.lat, this.lon);
		//biome.anazlyze();
		
		//access biome vars to generate map....
		
		for(int x=0; x<1000; ++x){
			int r = this.ran.nextInt(Constants.WORLD_SIZE);
			int c = this.ran.nextInt(Constants.WORLD_SIZE);
			this.worldArr[r][c] = this.ran.nextInt(4);
		}
		
		
	}
	
	public int[][] getWorldArr(){
		return this.worldArr;
	}
}
