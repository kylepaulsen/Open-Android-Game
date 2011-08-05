/* Player.java -- class for describing a single player
 * 
 * Copyright © 2011 Open-Android-Game Group
 * Please see the file COPYING in this
 * distribution for license terms.
 */

package com.kylepaulsen.openAndroidGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

// some random behavior
public class CopyOfPlayer {
	private int health;
	
	
	private int x; // (x, y) position for up-left corner
	private int y ;
	private int speedX = 5; // random number
	private int speedY = 3;
	
	int xMax, yMax;
	int xMin = 0;
	int yMin = 0;
	//private RectF bounds;
	//private Paint paint;
	
	// picture icon of the player, width and height
	private Bitmap bitmap;
    private int width;
	private int height;
	
	
	/* initialization, set to simple color, 
	  we need a picture later
	 */
//	public Player(int color){
//		bounds = new RectF();
//		paint = new Paint();
//		paint.setColor(color);
//		
//	}
	public CopyOfPlayer(Bitmap bitmap, int x, int y){
		this.bitmap =bitmap;
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
		this.x =x;
		this.y =y;		
	}
	

	/* update the location,
	 * and keep the player in bound.
	*/
	public void moveWithCollisionDetection(){
		// get new (x, y) position
		x = x+ speedX;
		y = y+ speedY;
		
		// detect collision on edge of screen
		if (x+width >xMax){
			speedX = -speedX;
			x = xMax - width;
		} else if (x < xMin){
			speedX = -speedX;
			x = xMin;
		}
		
		if (y + height > yMax){
			speedY= -speedY;
			y = yMax -height;
		} else if (y < yMin) {
			speedY= -speedY;
			y = yMin;
		}		
	}
	
	// another updating method
	public void UpdateLocation(){
		x = x+speedX;
		y = y+speedY;
	}
	
	public void draw(Canvas canvas){

		canvas.drawBitmap(bitmap, x, y, null);
	}
	
	public int getWidth() {
		return width;
	}
	public int getHeight(){
		return height;
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
	
	public int getSpeedX(){
		return speedX;
	}
	public void setSpeedX(int speedX){
		this.speedX =speedX;
	}
	public int getSpeedY(){
		return speedY;
	}
	public void setSpeedY(int speedY){
		this.speedY=speedY;
	}
		
}
