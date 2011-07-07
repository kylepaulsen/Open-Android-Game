package com.kylepaulsen.openAndroidGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Tile {
	
	
	private Bitmap bitmap;
	private int x;
	private int y;

	
	public Tile(Bitmap bitmap, int x, int y){
		this.bitmap =bitmap;
		this.x =x;
		this.y =y;		
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	public void setBitmap(Bitmap bitmap){
		this.bitmap =bitmap;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setX(int x){
		this.x =x;
	}
	public void setY(int y){
		this.y =y;
	}
	
	public void draw(Canvas canvas){
		canvas.drawBitmap(bitmap, x ,y, null);
	}
}
