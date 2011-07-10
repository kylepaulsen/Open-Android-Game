/* PlayerAnimated.java - Class for animated sprite.
 * 
 * Copyright © 2011 Open-Android-Game Group
 * Please see the file COPYING in this
 * distribution for license terms.
 */

package com.kylepaulsen.openAndroidGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.lang.Math;

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
    
	//for motion use, July09
	private static int SPEED = 3;
	private int speedX, speedY;
	private float destX, destY;
	
	
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
		
		this.speedX =0;
		this.speedY =0;
	}

	public void update(long gameTime){
		
		//firstly, update the sprite image
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
		
		//then, update location
		this.updateLocation();
	}
	
	public void updateLocation(){
		
		this.x = this.x + speedX;		
		this.y = this.y + speedY;
		
		// when it's close enough, stop moving
		if (Math.abs(this.destX-this.x)<10||Math.abs(this.destY-this.y)<10) {
			this.setSpeed(0, 0);	
		}
	}
	
	// draw method of animated player
	public void draw (Canvas canvas){
		Rect destRect = new Rect(getX(),getY(), getX()+spriteWidth, getY()+spriteHeight);
		canvas.drawBitmap(bitmap, sourceRect, destRect, null);
	}
	
	// set destination and speed, July09
	public void setDest(float destX, float destY){
		this.destX = destX;
		this.destY = destY;
		
		// initial distance between destination and current location
		float distance =  Math.round(  Math.sqrt(Math.pow(destX-this.x, 2) + Math.pow(destY-this.y, 2))  );
		if (distance==0) distance = 1; //prevent dividing zero
		//calc speed
		this.speedX= Math.round((destX - this.x)* SPEED / distance);	
		this.speedY =Math.round((destY - this.y)* SPEED / distance);
	}
		
	
	public void setSpeed(int sx, int sy){
		this.speedX =sx;
		this.speedY =sy;
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
