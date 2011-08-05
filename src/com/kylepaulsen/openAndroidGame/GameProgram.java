/* GameProgram.java - Class for game logic and main loop.
 * 
 * Copyright © 2011 Open-Android-Game Group
 * Please see the file COPYING in this
 * distribution for license terms.
 */

package com.kylepaulsen.openAndroidGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
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
	private Context context;
	
	private double latitude, longitude; 
	
	public GameProgram(GraphicsView view, Context context){
		this.gv = view;
		this.context = context;
		this.locTask = new LocationTask(context);
		this.locTask.init();
		
		this.currentSeed = this.locTask.makeSeedFromLocation();
		
		//generate a world in 2d array
		world = new World(context, this.currentSeed, this.locTask.getLatitude(), this.locTask.getLongitude()); 
		world.generateWorld();
		
		//tile the world with sprites
		this.base_tiles = new BaseTiles(context, world);
	}

	@Override
	public void run() {
		long ticks = 1000 / Constants.GAME_TARGET_FPS;
		long startTime;
		long sleepFor;
		
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
	
	public void draw(Canvas canvas, int x, int y){
		base_tiles.render(canvas);
		base_tiles.moveLocation(x, y);
		Point p = base_tiles.getCenterLocation();
		Log.d("gameprog", p.x+" "+p.y);
	}
	
	public Context getContext(){
		return this.context;
	}
}
