/* Camera.java - Class for loading and tiling worlds.
 * 
 * Copyright © 2011 Kyle Paulsen
 * Please see the file COPYING in this
 * distribution for license terms.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 */

package com.kylepaulsen.openAndroidGame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/*
 * This class will be in charge of loading worlds and writing to the
 * frame buffer (i think). It will also be in charge of world scrolling
 * and rendering other entities.
 */

public class Camera {
	private GraphicsView gv;
	private Pair view_rectangle;
	//private Pair load_rectangle;
	private Pair location;
	private Pair locationDest;
	private Pair destination;
	private int tile_width;
	private int tile_height;
	private double tile_offset_y;
	private double tile_offset_x;
	private double screen_height_offset;
	private double movement_speed;
	//private Canvas canvas;
	
	private int[][] worldArr;
	
	public Camera(GraphicsView view, World world, double xpos, double ypos){
		//this.gv = view;
		this.worldArr = world.getWorldArr();
		//this.canvas = gv.getBuffer();
		
		this.tile_height = Constants.WORLD_TILE_SIZE;
		this.tile_width = Constants.WORLD_TILE_SIZE;
		
		//this.view_rectangle = new Pair(Math.ceil(this.canvas.getWidth()/this.tile_width), Math.ceil(this.canvas.getHeight()/this.tile_height));
		this.location = new Pair(xpos, ypos);
		this.locationDest = new Pair(0,0);
		this.destination = new Pair(xpos, ypos);
		
		this.tile_offset_x = this.location.x*this.tile_width - Math.floor(this.location.x*this.tile_width);
		this.tile_offset_y = this.location.y*this.tile_height - Math.floor(this.location.y*this.tile_height);
		
		this.view_rectangle = new Pair(Math.ceil(Constants.WINDOW_WIDTH/this.tile_width), Math.ceil(Constants.WINDOW_HEIGHT/this.tile_height));
		this.screen_height_offset = (Constants.WINDOW_WIDTH - Constants.WINDOW_HEIGHT)/2;
		
		//this.screen_height_offset = (this.canvas.getWidth() - this.canvas.getHeight())/2;
		this.movement_speed = Constants.DEFAULT_CAM_MOVE_SPEED;
		
	}
	
	public void renderFrame(Canvas canvas){
		//load all objects within load_rectangle and display them.
		//only objects in the view_rectangle should be completely visible.
		//System.out.println(this.location.y+" , "+this.location.x);
		
		int top_view_bound = (int)Math.floor(this.location.y - (this.view_rectangle.y/2));
		int bottom_view_bound = top_view_bound + (int)Math.ceil(this.view_rectangle.y);
		int left_view_bound = (int)Math.floor(this.location.x - (this.view_rectangle.x/2));
		int right_view_bound = left_view_bound + (int)Math.ceil(this.view_rectangle.x);
		
		Paint p = new Paint();
		p.setColor(0xFF005500);
		
		//System.out.println("i got this far.");
		
		//for(int row=top_view_bound+5; row<=bottom_view_bound-5; ++row){
		//	for(int col=left_view_bound+8; col<=right_view_bound-7; ++col){
		for(int row=top_view_bound; row<=bottom_view_bound; ++row){
			for(int col=left_view_bound; col<=right_view_bound; ++col){
				
				boolean in_rows = row > -1 && row < Constants.WORLD_SIZE;
				boolean in_cols = col > -1 && col < Constants.WORLD_SIZE;
				int tile_x = (int) ((col - left_view_bound - this.tile_offset_x) * this.tile_width);
				int tile_y = (int) ((row - top_view_bound - this.tile_offset_y) * this.tile_height);// - this.screen_height_offset);
				
				///Log.d("Camera", "col:"+col+", lvb:"+left_view_bound+", tox:"+this.tile_offset_x+", tw:"+this.tile_width);
				
				if(in_rows && in_cols){
					short block = (short)this.worldArr[row][col];
					//System.out.println("row: "+row+" col: "+col+" tile_x: "+tile_x+" tile_y: "+tile_y+" tile_width: "+this.tile_width+" tile: "+block);
					
					if(block == 0){
						p.setStyle(Paint.Style.FILL);
						p.setColor(0xFF00FF00);
						canvas.drawRect(tile_x, tile_y, tile_x+tile_width, tile_y+tile_height, p);
						p.setStyle(Paint.Style.STROKE);
						p.setColor(0xFF005500);
						canvas.drawRect(tile_x, tile_y, tile_x+tile_width, tile_y+tile_height, p);
						//Log.d("Camera", "x:"+(tile_x+tile_height)+", y:"+(tile_y+tile_height));
					}
				}
			}
		}
	}
	
	public void setLocation(double xx, double yy){
		//this.location.x = xx;
		//this.location.y = yy;
		this.location = new Pair(xx,yy);
		this.tile_offset_x = this.location.x*this.tile_width - Math.floor(this.location.x*this.tile_width);
		this.tile_offset_y = this.location.y*this.tile_height - Math.floor(this.location.y*this.tile_height);
	}
	
	public void loadWorld(World world){
		//This method will tile the world with 
		//sprites using the world's 2d array as a reference.
		//Also any other entities should be loaded and rendered here.
		
		
		//loop through world map 2d array, paint the tiles.
		/*
		for(int i=0; i<Constants.WORLD_SIZE; ++i){
			for(int j=0; j<Constants.WORLD_SIZE; ++j){
				p.setColor(0xFF005500);
				p.setStyle(Paint.Style.STROKE);
				//can.drawRect(left, top, right, bottom, paint)
			}
		}*/
	}
}
