/* PlayerAnimated.java - Class for animated sprite.
 * 
 *  @author Xiaolong Cheng
 * Copyright (c) Kyle Paulsen
 * Please see the file COPYING in this
 * distribution for license terms.
 * 
 */

package com.kylepaulsen.openAndroidGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/*
 *  part of the code is inspired from an online tutorial
 *  http://obviam.net/index.php/sprite-animation-with-android/
 *  
 */

public class PlayerAnimated {
	private Bitmap bitmap;		// the animation sequence
	private Rect sourceRect;	// the rectangle to be drawn from the animation bitmap
	private int frameNr;		// number of frames in animation
	private int currentFrame;	// the current frame
	
	/*Note that this is not the game FPS but the walking FPS.*/
	private long frameTicker;	// the time of the last frame update
	private int framePeriod;	// milliseconds between each frame (1000/fps)

	private int spriteWidth;	// the width of the sprite to calculate the cut out rectangle
	private int spriteHeight;	// the height of the sprite

	private int x;				// the X coordinate of the object (top left of the image)
	private int y;				// the Y coordinate of the object (top left of the image)

	public PlayerAnimated(Bitmap bitmap, int x, int y, 
			int width, int height, int fps, int frameCount ){
		
		this.bitmap = bitmap;
		this.x =x;
		this.y =y;
		currentFrame =0;
		frameNr = frameCount;
		
		/* the cutting method depend on the 
		 * source picture used here
		*/
		spriteWidth  = bitmap.getWidth()/frameCount;
		spriteHeight = bitmap.getHeight();
		
		sourceRect = new Rect(0,0,spriteWidth,spriteHeight);
		framePeriod = 1000/fps;
		frameTicker =01;
	}

	public void update(long gameTime){
		if (gameTime > frameTicker + framePeriod){
			frameTicker = gameTime;// set current update time
			currentFrame++; //increment frame
			if (currentFrame>=frameNr){
				currentFrame =0;
			}
			
		}
		// define the rectangle to cut out sprite
		this.sourceRect.left = currentFrame*spriteWidth;
		this.sourceRect.right = this.sourceRect.left+spriteWidth;
	}
	
	// draw method of animated player
	public void draw (Canvas canvas){
		Rect destRect = new Rect(getX(),getY(), getX()+spriteWidth, getY()+spriteHeight);
		canvas.drawBitmap(bitmap, sourceRect, destRect, null);
	}
	
	public int getX(){
		return x;
	}
	public void setX(int x){
		this.x=x;
	}
	
	public int getY(){
		return y;
	}
	public void setY(int y){
		this.y =y;
	}
}
