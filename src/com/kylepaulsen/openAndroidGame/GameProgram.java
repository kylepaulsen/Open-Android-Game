/* GameProgram.java - Class for game logic and main loop.
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

import android.content.Context;
import android.graphics.Canvas;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

/*
 * A lot of this code was inspired from 
 * http://www.edu4java.com/androidgame/androidgame3.html
 * 
 * This class will perform game logic and 
 * call the onDraw method for each frame.
 */
public class GameProgram extends Thread {
	private GraphicsView gv;
	private boolean running = false;
	//private Camera cam;
	private BaseTiles base_tiles;
	private World world;
	private LocationTask locTask; 
	private long currentSeed;
	
	private double latitude, longitude; 
	
	public GameProgram(GraphicsView view, Context context){
		this.gv = view;
		
		this.locTask = new LocationTask(context);
		this.locTask.init();
		
		this.currentSeed = this.locTask.makeSeedFromLocation();
		
		//generate a world in 2d array
		world = new World(context, this.currentSeed, this.locTask.getLatitude(), this.locTask.getLongitude()); 
		world.generateWorld();
		
		//tile the world with sprites
		this.base_tiles = new BaseTiles(world);
	}

	@Override
	public void run() {
		long ticks = 1000 / Constants.GAME_TARGET_FPS;
		long startTime;
		long sleepFor;
		
		int i = 0, l = 0;
		
		while(running){
			
			//Next frames' Game Logic and stuff goes here.
			startTime = System.currentTimeMillis();
			
			//Try to draw the next frame
			Canvas frame = null;
			try{
				frame = gv.getHolder().lockCanvas();
				synchronized(gv.getHolder()){
					gv.onDraw(frame);
				}
			}finally{
				if(frame != null){
					gv.getHolder().unlockCanvasAndPost(frame);
				}
			}
			
			//Sleep between frames...
			sleepFor = ticks - (System.currentTimeMillis() - startTime);
			try{
				if(sleepFor > 0){
					sleep(sleepFor);
				}else{
					//The game is lagging here...
					sleep(10);
				}
			}catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setRunning(boolean s){
		this.running = s;
	}
	
	public void draw(Canvas canvas){
		base_tiles.render(canvas);
	}
}
