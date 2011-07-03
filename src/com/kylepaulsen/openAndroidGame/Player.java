/* Player.java -- class for describing a single player
 * 
 * Copyright (c) 2011 Xiaolong Cheng
 * Please see the file COPYING in this 
 * distribution for license terms.
 * 
 */

package com.kylepaulsen.openAndroidGame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

// some random behavior
public class Player {
	private int health;
	
	float width =40; // if not square, add length
	float x= 100; // (x, y) position for up-left corner
	float y =100;
	float speedX = 5; // random number
	float speedY = 3;
	
	float xMax, yMax;
	float xMin = 0;
	float yMin = 0;
	private RectF bounds;
	private Paint paint;
	
	
	/* initialization, set to simple color, 
	  we need a picture later
	 */
	public Player(int color){
		bounds = new RectF();
		paint = new Paint();
		paint.setColor(color);
		
	}
	

	/* function to keep the player in bound.
	 * random implementation.
	*/
	public void moveWithCollisionDetection(){
		// get new (x, y) position
		x += speedX;
		y += speedY;
		
		// detect collision on edge of screen
		if (x+width >xMax){
			speedX = -speedX;
			x = xMax - width;
		} else if (x < xMin){
			speedX = -speedX;
			x = xMin;
		}
		
		if (y + width > yMax){
			speedY= -speedY;
			y = yMax -width;
		} else if (y < yMin) {
			speedY= -speedY;
			y = yMin;
		}
		
	}
	
	public void draw(Canvas canvas){
		bounds.set(x, y, x+width, y+width);
		canvas.drawRect(bounds, paint);
	}
	
}
