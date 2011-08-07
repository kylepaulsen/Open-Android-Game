/* GameProgram.java - Class for game logic and main loop.
 * 
 * Copyright © 2011 Open-Android-Game Group
 * Please see the file COPYING in this
 * distribution for license terms.
 */

package com.kylepaulsen.openAndroidGame;

import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

/*
 * A lot of this code was inspired from 
 * http://www.edu4java.com/androidgame/androidgame3.html
 * 
 * This class will perform game logic and 
 * call the onDraw method for each frame.
 */
public class GameProgram extends Thread {
	private GraphicsView gv;
	private boolean running = false;
	//private Camera cam;
	private BaseTiles base_tiles;
	private World world;
	private LocationTask locTask; 
	private long currentSeed;
	private Context context;
	private Resources res;
	
	private HashMap<Byte, Tile> tileLib;
	
	private double latitude, longitude; 
	
	public GameProgram(GraphicsView view, Context context){
		this.gv = view;
		this.context = context;
		this.res = this.context.getResources();
		this.locTask = new LocationTask(context);
		this.locTask.init();
		
		this.currentSeed = this.locTask.makeSeedFromLocation();
		
		tileLib = new HashMap<Byte, Tile>();
		tileLib.put(Constants.TILE_GRASS_ID, new Tile(Constants.TILE_GRASS_ID, BitmapFactory.decodeResource(this.res, R.drawable.grass), true));
		tileLib.put(Constants.TILE_DIRT_ID, new Tile(Constants.TILE_DIRT_ID, BitmapFactory.decodeResource(this.res, R.drawable.dirt), true));
		tileLib.put(Constants.TILE_SAND_ID, new Tile(Constants.TILE_SAND_ID, BitmapFactory.decodeResource(this.res, R.drawable.sand), true));
		tileLib.put(Constants.TILE_WATER_ID, new Tile(Constants.TILE_WATER_ID, BitmapFactory.decodeResource(this.res, R.drawable.water1), false));
		
		//generate a world in 2d array
		world = new World(context, this.currentSeed, this.locTask.getLatitude(), this.locTask.getLongitude());
		world.generateWorld();
		
		//tile the world with sprites
		this.base_tiles = new BaseTiles(tileLib, world);
	}

	@Override
	public void run() {
		long ticks = 1000 / Constants.GAME_TARGET_FPS;
		long startTime;
		long sleepFor;
		
		while(running){
			
			//Next frames' Game Logic and stuff goes here.
			startTime = System.currentTimeMillis();
			
			//Try to draw the next frame
			Canvas frame = null;
			try{
				frame = gv.getHolder().lockCanvas();
				synchronized(gv.getHolder()){
					gv.onDraw(frame);
				}
			}finally{
				if(frame != null){
					gv.getHolder().unlockCanvasAndPost(frame);
				}
			}
			
			//Sleep between frames...
			sleepFor = ticks - (System.currentTimeMillis() - startTime);
			try{
				if(sleepFor > 0){
					sleep(sleepFor);
				}else{
					//The game is lagging here...
					sleep(10);
				}
			}catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setRunning(boolean s){
		this.running = s;
	}
	
	public void draw(Canvas canvas, int x, int y){
		//this method is called by the draw method in graphics view. 
		//the x and y vars are the movement vars for the player.
		base_tiles.render(canvas);
		
		boolean go = true;
		
		while(checkCollision(base_tiles.getCenterLocation(), base_tiles.getPixelsInTile(), x, y)){
			--x;
			--y;
			if(x < 0){
				x = 0;
			}
			if(y < 0){
				y = 0;
			}
			if(x == 0 && y == 0){
				go = false;
				break;
			}
		}
		
		if(go){
			base_tiles.moveLocation(x, y);
		}
		
		/*
		if(!checkCollision(base_tiles.getCenterLocation(), base_tiles.getPixelsInTile(), x, y)){
			base_tiles.moveLocation(x, y);
		}
		*/
		
		//Point p = base_tiles.getPixelsInTile();
		//Log.d("gameprog", p.x+" "+p.y);
	}
	
	//returns true if player is colliding with something.
	public boolean checkCollision(Point world_loc, Point tile_loc, int dx, int dy){
		Point top_l = new Point(world_loc.x, world_loc.y);
		Point top_r = new Point(world_loc.x, world_loc.y);
		Point bot_l = new Point(world_loc.x, world_loc.y);
		Point bot_r = new Point(world_loc.x, world_loc.y);
		
		if(((tile_loc.x+dx)-Constants.PLAYER_BOUNDING_BOX_RADIUS) < 0){
			top_l.x -= 1;
			bot_l.x -= 1;
		}else if(((tile_loc.x+dx)+Constants.PLAYER_BOUNDING_BOX_RADIUS) >= Constants.WORLD_TILE_SIZE){
			top_r.x += 1;
			bot_r.x += 1;
		}
		
		if(((tile_loc.y+dy)-Constants.PLAYER_BOUNDING_BOX_RADIUS) < 0){
			top_l.y -= 1;
			top_r.y -= 1;
		}else if(((tile_loc.y+dy)+Constants.PLAYER_BOUNDING_BOX_RADIUS) >= Constants.WORLD_TILE_SIZE){
			bot_r.y += 1;
			bot_l.y += 1;
		}
		
		//Log.d("gameprog", top_l.x+" "+top_l.y);
		
		if(top_l.x < 0) return true;
		if(top_l.y < 0) return true;
		
		if(top_r.x >= Constants.WORLD_SIZE) return true;
		if(top_r.y < 0) return true;
		
		if(bot_l.x < 0) return true;
		if(bot_l.y >= Constants.WORLD_SIZE) return true;
		
		if(bot_r.x >= Constants.WORLD_SIZE) return true;
		if(bot_r.y >= Constants.WORLD_SIZE) return true;
		
		if(!tileLib.get(world.getWorldArr()[top_l.x][top_l.y]).isPassible()) return true;
		if(!tileLib.get(world.getWorldArr()[top_r.x][top_r.y]).isPassible()) return true;
		if(!tileLib.get(world.getWorldArr()[bot_l.x][bot_l.y]).isPassible()) return true;
		if(!tileLib.get(world.getWorldArr()[bot_r.x][bot_r.y]).isPassible()) return true;
		
		return false;
	}
	
	public Context getContext(){
		return this.context;
	}
}
