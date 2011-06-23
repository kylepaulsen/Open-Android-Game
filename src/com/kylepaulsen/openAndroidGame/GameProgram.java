package com.kylepaulsen.openAndroidGame;

import android.graphics.Canvas;

/*
 * A lot of this code was inspired from 
 * http://www.edu4java.com/androidgame/androidgame3.html
 */
public class GameProgram extends Thread {
	private GraphicsView gv;
	private boolean running = false;
	
	public GameProgram(GraphicsView view){
		this.gv = view;
	}

	@Override
	public void run() {
		while(running){
			Canvas window = null;
			try{
				window = gv.getHolder().lockCanvas();
				synchronized(gv.getHolder()){
					gv.onDraw(window);
				}
			}finally{
				if(window != null){
					gv.getHolder().unlockCanvasAndPost(window);
				}
			}
		}
	}
	
	public void setRunning(boolean s){
		this.running = s;
	}
}
