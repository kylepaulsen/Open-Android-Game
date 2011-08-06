/* PlayerAnimated.java - Class for animated sprite.
 * 
 * Copyright © 2011 Open-Android-Game Group
 * Please see the file COPYING in this
 * distribution for license terms.
 */

package com.kylepaulsen.openAndroidGame;

/* @date July09
 * @author Xiaolong
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.lang.Math;


public class Player extends WorldEnt {
	private final int SPRITE_OFFSET_X = -4;
	private final int SPRITE_OFFSET_Y = 20;
	
	private Bitmap bitmap;		// the animation sequence
	private Rect sourceRect;	// the rectangle to be drawn from the animation bitmap
	private int frameNrX;		// number of frames in animation
	private int frameNrY;
	private int frameAniDir;    //frame animation direction
	private int currentFrameX;	// the current frame
	private int currentFrameY;
	
	/*Note that this is not the game FPS but the walking FPS.*/
	private long frameTicker;	// the time of the last frame update
	private int framePeriod;	// milliseconds between each frame (1000/fps)

	private int spriteWidth;	// the width of the sprite to calculate the cut out rectangle
	private int spriteHeight;	// the height of the sprite

	private int x;				// the X coordinate of the object (top left of the image)
	private int y;				// the Y coordinate of the object (top left of the image)
    
	//for motion use, July09
	//private static int SPEED = 3;
	//private int speedX, speedY;
	private float destX, destY;
	
	private GameProgram gp;
		
	public Player(GameProgram gp){
		this.gp = gp;
		this.bitmap = BitmapFactory.decodeResource(this.gp.getContext().getResources(), R.drawable.maincharacter);
		this.x = Constants.WINDOW_WIDTH/2-16; //initial location (x,y)
		this.y = Constants.WINDOW_HEIGHT/2-40;
		this.currentFrameX = 1;
		this.currentFrameY = 0;
		this.frameNrX = 3;
		this.frameNrY = 4;
		this.frameAniDir = 0;
		/* the cutting method depend on the 
		 * source picture used here
		*/
		this.spriteWidth  = bitmap.getWidth()/frameNrX;
		this.spriteHeight = bitmap.getHeight()/frameNrY;
		
		sourceRect = new Rect(0,0,spriteWidth,spriteHeight);
		framePeriod = 1000/4; //4 is the fps
		frameTicker = 1;
		
		//this.speedX = 0;
		//this.speedY = 0;
	}

	public void update(long gameTime){
		
		//firstly, update the sprite image
		if (gameTime > frameTicker + framePeriod){
			frameTicker = gameTime;// set current update time
			
			if(currentFrameX == 0){
				currentFrameX = 1;
				frameAniDir = 0;
			}else if(currentFrameX == 2){
				currentFrameX = 1;
				frameAniDir = 1;
			}else if(currentFrameX == 1 && frameAniDir == 0){
				currentFrameX = 2;
			}else /*if(currentFrameX == 1 && frameAniDir == 1)*/{
				currentFrameX = 0;
			}
		}
		/* define the rectangle to cut out sprite
		 * and update the correct frame
		*/
		/*
		if(this.speedX>0) {
			currentFrameY =2;
		}
		else if (this.speedX<0) {
			currentFrameY =1;
		}
		else { // if move vertically
			if (speedY<0) currentFrameY= 3;
			else currentFrameY =0;
		}
		*/
		this.sourceRect.top = currentFrameY*spriteHeight;
		this.sourceRect.bottom = this.sourceRect.top+spriteHeight;		
		this.sourceRect.left = currentFrameX*spriteWidth;
		this.sourceRect.right = this.sourceRect.left+spriteWidth;
		
		//then, update location
		//this.updateLocation();
	}
	
	/*
	public void updateLocation(){
		
		//this.x = this.x + speedX;		
		//this.y = this.y + speedY;
		
		// when it's close enough, stop moving
		
		if (Math.abs(this.destX-(this.x+0.5*this.spriteWidth))<10 ||
				Math.abs(this.destY-(this.y+0.5*this.spriteHeight))<10) {
			this.setSpeed(0, 0);	
		}
		
	}*/
	
	// draw method of animated player
	public void draw (Canvas canvas){
		Rect destRect = new Rect(this.x+SPRITE_OFFSET_X, this.y+SPRITE_OFFSET_Y, (int)(this.x+(spriteWidth)*1.3)+SPRITE_OFFSET_X, (int)(this.y+(spriteHeight)*1.3)+SPRITE_OFFSET_Y);
		canvas.drawBitmap(bitmap, sourceRect, destRect, null);
	}
	
	// set destination and speed, July09
	public void setDest(float destX, float destY){
		this.destX = destX;
		this.destY = destY;
		
		int xDiff = (int) (Constants.WINDOW_WIDTH/2-destX);
		int yDiff = (int) (Constants.WINDOW_HEIGHT/2-destY);
		
		if(yDiff >= xDiff && yDiff > -xDiff){
			//bottom region
			if(currentFrameY != 3){
				currentFrameY = 3;
			}
		}else if(yDiff < xDiff && yDiff >= -xDiff){
			//right region
			if(currentFrameY != 1){
				currentFrameY = 1;
			}
		}else if(yDiff < xDiff && yDiff <= -xDiff){
			//top region
			if(currentFrameY != 0){
				currentFrameY = 0;
			}
		}else if(yDiff > xDiff && yDiff <= -xDiff){
			//left region
			if(currentFrameY != 2){
				currentFrameY = 2;
			}
		}
		
		/*
		// initial distance between destination and current location
		float distance =  Math.round(  Math.sqrt(Math.pow(destX-this.x, 2) + Math.pow(destY-this.y, 2))  );
		if (distance==0) distance = 1; //prevent dividing zero
		//calc speed
		this.speedX= Math.round((destX - this.x)* SPEED / distance);	
		this.speedY =Math.round((destY - this.y)* SPEED / distance);
		*/
	}
		
	/*
	public void setSpeed(int sx, int sy){
		this.speedX =sx;
		this.speedY =sy;
	}*/
	
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

