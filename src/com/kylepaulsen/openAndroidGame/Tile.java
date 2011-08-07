/* Tile.java - Class for describing tiles.
 * 
 * Copyright © 2011 Open-Android-Game Group
 * Please see the file COPYING in this
 * distribution for license terms.
 */

package com.kylepaulsen.openAndroidGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Tile {
	
	private int id;
	private Bitmap bitmap;
	private boolean passable;
		
	public Tile(int id, Bitmap bitmap, boolean passable){
		this.bitmap = bitmap;
		this.id = id;
		this.passable = passable;
	}
	
	public Bitmap getBitmap() {
		return this.bitmap;
	}
	
	public void setBitmap(Bitmap bitmap){
		this.bitmap = bitmap;
	}
	
	public boolean isPassible(){
		return passable;
	}
}
