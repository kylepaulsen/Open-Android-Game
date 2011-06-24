package com.kylepaulsen.openAndroidGame;

import android.graphics.Canvas;

/*
 * This class will be in charge of loading worlds and writing to the
 * frame buffer (i think). It will also be in charge of world scrolling
 * and rendering other entities.
 */

public class Camera {
	private GraphicsView gv;
	
	public Camera(GraphicsView view){
		this.gv = view;
	}
	
	public void loadWorld(World world){
		//This method will tile the world with 
		//sprites using the world's 2d array as a reference.
		//Also any other entities should be loaded and rendered here.
		
		Canvas can = gv.getBuffer();
	}
}
