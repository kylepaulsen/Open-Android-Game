/* World.java - Class for generating worlds in a 2d array.
 * 
 * Copyright © 2011 Kyle Paulsen
 * Please see the file COPYING in this
 * distribution for license terms.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 */

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
		
		/*
		 * 0 = grass
		 * 1 = water
		 * 
		 * 
		 */
		
		
		
	}
	
	public int[][] getWorldArr(){
		return this.worldArr;
	}
}
