package com.kylepaulsen.openAndroidGame;

import java.util.Random;

/*
 * This class will be in charge of generating random worlds based off of
 * some random seed (maybe a modulus of last gps coords?)
 * Worlds should be a 2d array that will later be tiled with sprites by the
 * Camera class.
 */

public class World {
	private int worldArr[][];
	private int worldSeed;
	private Random ran;
	
	public World(int seed){
		//not sure what goes here yet.
		this.worldSeed = seed;
		this.ran = new Random(this.worldSeed);
		worldArr = new int[500][500];
	}
	
	public void generateWorld(){
		//This method will use the var ran to generate a world into the
		//worldArr. I'll get to this later..
	}
	
}
