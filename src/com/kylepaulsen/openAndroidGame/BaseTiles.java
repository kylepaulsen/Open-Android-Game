/* BaseTiles.java - Class for loading and displaying just the tiles that show up.
 * 
 * Copyright © 2011 Open-Android-Game Group
 * Please see the file COPYING in this
 * distribution for license terms.
 */

package com.kylepaulsen.openAndroidGame;

import java.util.HashMap;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

public class BaseTiles {
	private Canvas canvas;
	private HashMap<Byte, Tile> tileLib;
	private Bitmap buffer_cur, buffer_off;
	private boolean dirty;
	private Rect paint_pixel_extent;
	private Rect local_pixel_extent;
	//private int offset_x, offset_y;
	private Rect local_tile_extent;
	private Rect global_tile_extent;
	
	private Boolean load_new_tiles;
	private Thread loading;
	private volatile boolean loading_new_tiles;
	
	private byte[][] layer;
	
	//local tile extent dimension consts:
	//32
	private int TILE_WIDTH = 30, TILE_HEIGHT = 22;
	//private int TILE_WIDTH = 32, TILE_HEIGHT = 32;
	
	//private int BUFFER_PADDING = 1, BUFFER_SHIFT = 4;
	private int BUFFER_PADDING = 2, BUFFER_SHIFT = 5;

	//private Bitmap tile_bitmaps[];
	
	public BaseTiles(HashMap<Byte, Tile> tileLib, World world){
		this.tileLib = tileLib;
		this.dirty = true;
		this.buffer_cur = Bitmap.createBitmap(TILE_WIDTH * Constants.WORLD_TILE_SIZE, TILE_HEIGHT * Constants.WORLD_TILE_SIZE, Bitmap.Config.ARGB_8888);
		this.buffer_off = Bitmap.createBitmap(TILE_WIDTH * Constants.WORLD_TILE_SIZE, TILE_HEIGHT * Constants.WORLD_TILE_SIZE, Bitmap.Config.ARGB_8888);
		
		this.canvas = new Canvas(this.buffer_cur);
		this.local_pixel_extent = new Rect(0, 0, Constants.WINDOW_WIDTH * 2 / 3, Constants.WINDOW_HEIGHT * 2 / 3);
		this.paint_pixel_extent = new Rect(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		//this.local_pixel_extent.offset(320, 320);
		
		this.local_tile_extent = new Rect(0, 0, 0, 0);
		this.global_tile_extent = new Rect(0, 0, TILE_WIDTH, TILE_HEIGHT);
		//this.global_tile_extent.offsetTo(10, 10);
		this.layer = world.getWorldArr();
		
		this.load_new_tiles = false;
		//loadTileBitmaps();
		drawInitialTiles();
	}

	/*
	private void loadTileBitmaps() {
		Resources rsc = context.getResources();
		tile_bitmaps = new Bitmap[4];
		tile_bitmaps[Constants.TILE_GRASS_ID] = BitmapFactory.decodeResource(rsc, R.drawable.grass);
		tile_bitmaps[Constants.TILE_DIRT_ID] = BitmapFactory.decodeResource(rsc, R.drawable.dirt);
		tile_bitmaps[Constants.TILE_SAND_ID] = BitmapFactory.decodeResource(rsc, R.drawable.sand);
		tile_bitmaps[Constants.TILE_WATER_ID] = BitmapFactory.decodeResource(rsc, R.drawable.water1);
		
		
		
	}*/
	
	public void render(Canvas c){
		synchronized(buffer_cur) {
			c.drawBitmap(buffer_cur, local_pixel_extent, paint_pixel_extent, null);
		}
	}
	
	private void drawInitialTiles() {
		drawAllTiles(canvas, global_tile_extent);
	}
	
	private void drawAllTiles(Canvas c, Rect g_extent) {
		for(int i = 0; i < TILE_WIDTH; ++i) {
			for(int j = 0; j < TILE_HEIGHT; ++j) {
				paintLocalCell(canvas, g_extent, i, j);
			}
		}
	}
	
	public void moveLocation(int dx, int dy) {
		local_pixel_extent.offset(dx, dy);
		
		synchronized(load_new_tiles) {
			if(load_new_tiles) return;
		}
		
		// Handle updating the local_tile_extent
		int ts = Constants.WORLD_TILE_SIZE;
		
		int top = local_pixel_extent.top / ts;
		int bottom = local_pixel_extent.bottom / ts + (local_pixel_extent.bottom % ts > 0 ? 1 : 0);
		int left = local_pixel_extent.left / ts;
		int right = local_pixel_extent.right / ts + (local_pixel_extent.right % ts > 0 ? 1 : 0);
		
		local_tile_extent.set(left, top, right, bottom);
		updateGlobalExtent();
	}
	
	private void updateGlobalExtent() {
		Rect l = local_tile_extent;
		boolean left=false, top=false, bottom=false, right=false;
		
		if(l.left < BUFFER_PADDING) left = true;
		if(l.top < BUFFER_PADDING) top = true;
		if(TILE_WIDTH - l.right < BUFFER_PADDING) right = true;
		if(TILE_HEIGHT - l.bottom < BUFFER_PADDING) bottom = true;
		// Move global extent
		if(left || top || bottom || right) {
			synchronized(load_new_tiles) {
				load_new_tiles = true;
			}
			
			int dx = 0, dy = 0;
			
			if(left) dx = -1 * BUFFER_SHIFT;
			else if(right) dx = 1 * BUFFER_SHIFT;
			
			if(top) dy = -1 * BUFFER_SHIFT;
			else if(bottom) dy = 1 * BUFFER_SHIFT;
			
			loading = new TileLoader(dx, dy);
			loading.start();
			//Log.d("check"," " + left + top + bottom + right);

			
		}
	}

	public void setLocationWorld(int tile_x, int tile_y) {
		Log.d("setlocation", "start");
		synchronized(load_new_tiles) {
			if(load_new_tiles) loading.stop();
			loading = null;
		}
		
		int w_half = TILE_WIDTH / 2;
		int h_half = TILE_HEIGHT / 2;
		
		Rect new_g_extent = new Rect(global_tile_extent);
		new_g_extent.offsetTo(w_half + tile_x, h_half + tile_y);
		
		Rect new_px_extent = new Rect(local_pixel_extent);
		new_px_extent.offsetTo(w_half * 32 + (TILE_WIDTH % 2) * 16, h_half * 32 + (TILE_HEIGHT % 2) * 16);
		
		synchronized(buffer_cur) {
			canvas.drawColor(0xFF000000);
		}
		Bitmap buffer = buffer_off;
		canvas.setBitmap(buffer);
		drawAllTiles(canvas, new_g_extent);
		
		synchronized(buffer_cur) {
			buffer_off = buffer_cur;
			buffer_cur = buffer;
			global_tile_extent = new_g_extent;
			local_pixel_extent = new_px_extent;
		}
		
		synchronized(load_new_tiles) {
			load_new_tiles = false;
		}
		Log.d("setlocation", "stop");

	}
	
	public void paintLocalCell(Canvas c, Rect g_extent, int x, int y) {
		byte cell_type = getLocalCellType(g_extent, x, y);
		int ts = Constants.WORLD_TILE_SIZE;
		
		if(cell_type > 0)
			canvas.drawBitmap(tileLib.get(cell_type).getBitmap(), x*ts, y*ts, null);
		
		/*
		// TODO: call paintCell(c, cell_type) to paint actual graphics
		int color = 0;
		if(cell_type == 0) color = 0xFFFFFFFF;
		if(cell_type == 1) color = 0xFF00FF00;
		if(cell_type == 2) color = 0xFFFF0000;
		if(cell_type == 3) color = 0xFF0000FF;
		if(cell_type == 4) color = 0xFFFFFF00;
		if(cell_type == 5) color = 0xFFFF00FF;
		if(cell_type == 6) color = 0xFF00FFFF;
		if(cell_type == 7) color = 0xFF000000;

		p.setStyle(Paint.Style.FILL);
		p.setColor(color);
		canvas.drawRect(x*ts, y*ts, x*ts+ts, y*ts+ts, p);
		p.setStyle(Paint.Style.STROKE);
		p.setColor(0xFF005500);
		canvas.drawRect(x*ts, y*ts, x*ts+ts, y*ts+ts, p);
		*/
	}
	
	/*
	 * Given the tile coords of the coord views
	 */
	public byte getLocalCellType(Rect g_extent, int tile_x, int tile_y) {
		int x = tile_x + g_extent.left;
		int y = tile_y + g_extent.top;
		
		if(x > -1 && x < Constants.WORLD_SIZE && y > -1 && y < Constants.WORLD_SIZE){
			//if(layer[x][y] == 0)
				//return 1;
			
			return layer[x][y];
			//return (x+y)%8;
		}else{
			return Constants.TILE_WATER_ID;
		}
	}
	
	public Point getCenterLocation(){
		int w_half = TILE_WIDTH / 2;
		int h_half = TILE_HEIGHT / 2;
		
		synchronized(buffer_cur) {
			int x = (int)Math.floor(local_pixel_extent.exactCenterX()/Constants.WORLD_TILE_SIZE)-w_half+(int)Math.floor(global_tile_extent.exactCenterX());
			int y = (int)Math.floor(local_pixel_extent.exactCenterY()/Constants.WORLD_TILE_SIZE)-h_half+(int)Math.floor(global_tile_extent.exactCenterY());
			return new Point(x, y);
		}
	}
	
	public Point getPixelsInTile(){
		synchronized(buffer_cur) {
			int x = (int)(local_pixel_extent.exactCenterX()%Constants.WORLD_TILE_SIZE);
			int y = (int)(local_pixel_extent.exactCenterY()%Constants.WORLD_TILE_SIZE);
			return new Point(x, y);
		}
	}
	
	public Point getstuff(){
		int x = local_pixel_extent.top;
		int y = local_pixel_extent.left;
		return new Point(x, y);
	}
	
	private class TileLoader extends Thread{
		Rect new_extent;
		int dx, dy;
		
		public TileLoader(int dx, int dy){
			super();
			this.dx = dx;
			this.dy = dy;
			new_extent = new Rect(global_tile_extent);
			new_extent.offset(dx, dy);
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//super.run();
			
			int ts = Constants.WORLD_TILE_SIZE;
			
			Rect old_extent = global_tile_extent;
			Bitmap buffer = buffer_off;
			canvas.setBitmap(buffer);
			
			// Draw the old buffer on the new one, shifted
			canvas.drawColor(0xFF000000);
			canvas.drawBitmap(buffer_cur,-1 * dx * ts, -1 * dy * ts, null);
			// That is for painting the still okay tiles
			
			// TODO: Replace this
			// Instead of drawing all tiles draw just draw the new ones
			// dx is the change in x, dy in y
			//TODO: drawEdgeTiles(dx, dy, new_extent, canvas)
			//call paintlocaltile(canvas_extent, local_x, local_y); 
			
			// START
			drawAllTiles(canvas, new_extent);
			// END
			
			Rect new_px_extent = new Rect(local_pixel_extent);
			new_px_extent.offset(-1 * dx * ts, -1 * dy * ts);
			
			
			synchronized(buffer_cur) {
				buffer_off = buffer_cur;
				buffer_cur = buffer;
				global_tile_extent = new_extent;
				local_pixel_extent = new_px_extent;
			}
			
			synchronized(load_new_tiles) {
				load_new_tiles = false;
			}
		}
		
	}
}
