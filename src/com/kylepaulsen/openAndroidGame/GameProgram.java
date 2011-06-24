package com.kylepaulsen.openAndroidGame;

import android.graphics.Canvas;

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
	private Camera cam;
	private World world;
	
	public GameProgram(GraphicsView view){
		this.gv = view;
		
		//generate a world in 2d array
		world = new World(0); 
		world.generateWorld();
		
		//tile the world with sprites
		cam = new Camera(gv);
		cam.loadWorld(world); 
		
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
}
