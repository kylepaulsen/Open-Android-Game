/* World.java - Class for generating worlds in a 2d array.
 * 
 * Copyright © 2011 Open-Android-Game Group
 * Please see the file COPYING in this
 * distribution for license terms.
 */

package com.kylepaulsen.openAndroidGame;

import java.util.HashMap;
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
	private byte worldArr[][];
	private long worldSeed;
	private double lon, lat;
	private Random ran;
	
	public World(Context context, long seed, double latitude, double longitude){
		//not sure what goes here yet.
		this.con = context;
		this.worldSeed = seed;
		this.ran = new Random(this.worldSeed);
		worldArr = new byte[Constants.WORLD_SIZE][Constants.WORLD_SIZE];
		
		for(int i=0; i<Constants.WORLD_SIZE; ++i){
			for(int j=0; j<Constants.WORLD_SIZE; ++j){
				//worldArr[i][j] = 1;
				worldArr[i][j] = (byte)(ran.nextInt(4)+1);
			}
		}
		
		this.lat = latitude;
		this.lon = longitude;
	}
	
	public void generateWorld(){
		//This method will use the var ran to generate a world into the
		//worldArr. I'll get to this later..
		
		Biome biome = new Biome(this.con, this.lat, this.lon);
		float hsv[] = biome.anazlyze();
		
		//access biome vars to generate map....
		HashMap<Byte, Float> chances = new HashMap<Byte, Float>();
		chances.put(Constants.TILE_AIR_ID, 0f);
		chances.put(Constants.TILE_GRASS_ID, 1.00f);
		chances.put(Constants.TILE_DIRT_ID, 1.00f);
		chances.put(Constants.TILE_SAND_ID, 0.90f);
		chances.put(Constants.TILE_WATER_ID, 0.90f);
		
		for(int t=0; t<Constants.GEN_SUFACE_ITERATIONS; ++t){
			if(t < Constants.GEN_SUFACE_ITERATIONS-3){
				worldArr = gen_population_wins(chances);
			}else{
				worldArr = gen_surv_of_fit(chances);
			}
		}
	}
	
	public byte[][] gen_population_wins(HashMap<Byte, Float> chances){
		byte next[][] = worldArr.clone();
				
		for(int j = 0; j < Constants.WORLD_SIZE-2; ++j){
			for(int i = 0; i < Constants.WORLD_SIZE-2; ++i){
				int tileCount[] = new int[chances.size()];
				
				//look at a local small square and make a histogram.
				for(int loc_j=0; loc_j<3; ++loc_j){
					for(int loc_i=0; loc_i<3; ++loc_i){
						int tile = worldArr[i+loc_i][j+loc_j];
						++tileCount[tile];
					}
				}
				
				//pick winning tile out of a hat method where the tile that showed
				//up the most in the local small square gets a higher chance.
				int tileCountTotal = 0;
				for(byte k=0; k<tileCount.length; ++k){
					//Log.d("chance", k+"");
					float tile_chance = chances.get(k);
					tileCount[k] = (int) ((100 * tileCount[k]) * tile_chance);
					tileCount[k] += tileCountTotal;
					tileCountTotal = tileCount[k];
				}
				
				//pick the winning tile number.
				int growingTile = ran.nextInt(tileCountTotal);
				byte winningTile = 1;
				
				//check to see whos the winner.
				for(byte k=0; k<tileCount.length; ++k){
					if(growingTile < tileCount[k]) {
						winningTile = k;
						break;
					}
				}
				
				//award the center square in the local small square to the 
				//winning tile.
				next[i+1][j+1] = winningTile;
			}
		}
		return next;
	}
	
	public byte[][] gen_surv_of_fit(HashMap<Byte, Float> chances){
		byte next[][] = worldArr.clone();
		
		for(int j = 0; j < Constants.WORLD_SIZE-2; ++j){
			for(int i = 0; i < Constants.WORLD_SIZE-2; ++i){
				int tileCount[] = new int[chances.size()];
				
				byte biggest_idx = 0;
				int biggest_val = 0;
				
				//look at a local small square and make a histogram.
				for(int loc_j=0; loc_j<3; ++loc_j){
					for(int loc_i=0; loc_i<3; ++loc_i){
						if(++tileCount[worldArr[i+loc_i][j+loc_j]] > biggest_val){
							//the strongest tile (most counted one) wins.
							biggest_val = tileCount[worldArr[i+loc_i][j+loc_j]];
							biggest_idx = worldArr[i+loc_i][j+loc_j];
						}
					}
				}
				
				//award the center square in the local small square to the 
				//winning tile.
				next[i+1][j+1] = biggest_idx;
			}
		}
		return next;
	}
	
	public byte[][] getWorldArr(){
		return this.worldArr;
	}
}
