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
		//base_tiles.setLocationWorld(-35, -27);
		
		/*boolean changed = false;
		boolean try_again = false;
		Point loc;
		do{
			loc = this.base_tiles.getCenterLocation();
			try_again = false;
			if(!tileLib.get(world.getWorldArr()[loc.x][loc.y]).isPassible()){
				try_again = true;
				changed = true;
				++loc.x;
			}
			
		}while(try_again);*/
		//if(changed) base_tiles.setLocationWorld(loc.x, loc.y);
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
		
		Point newDxDy;
		
		newDxDy = checkCollision(base_tiles.getCenterLocation(), base_tiles.getPixelsInTile(), x, y);
		
		//Point lol = base_tiles.getstuff();
		//Log.d("stuff", lol.x+" "+lol.y);
		
		if(newDxDy.x != 0 || newDxDy.y != 0){
			base_tiles.moveLocation(newDxDy.x, newDxDy.y);
		}
	}
	
	//returns a new dx dy in a point that is a safe movement that wont intersect
	//something that is un-passable. THIS FUNCTION IS WAY TOO COMPLICATED!!! D:
	public Point checkCollision(Point world_loc, Point tile_loc, int dx, int dy){
		if(dx == 0 && dy == 0) return new Point(0, 0);
		
		Point top_l = new Point(world_loc.x, world_loc.y);
		Point top_r = new Point(world_loc.x, world_loc.y);
		Point bot_l = new Point(world_loc.x, world_loc.y);
		Point bot_r = new Point(world_loc.x, world_loc.y);
		
		Point newDxDy = new Point(dx, dy);
		
		boolean limitX = false;
		boolean limitY = false;
		
		//get block coords of bounding box points of player.
		if(((tile_loc.x+dx)-Constants.PLAYER_BOUNDING_BOX_RADIUS) < 0){
			top_l.x -= 1;
			bot_l.x -= 1;
			//This is the value needed to JUST touch the side of the adjacent tile.
			newDxDy.x = Constants.PLAYER_BOUNDING_BOX_RADIUS-tile_loc.x;
		}else if(((tile_loc.x+dx)+Constants.PLAYER_BOUNDING_BOX_RADIUS) >= Constants.WORLD_TILE_SIZE){
			top_r.x += 1;
			bot_r.x += 1;
			//This is the value needed to JUST touch the side of the adjacent tile.
			newDxDy.x = -tile_loc.x-Constants.PLAYER_BOUNDING_BOX_RADIUS+Constants.WORLD_TILE_SIZE;
		}

		if(((tile_loc.y+dy)-Constants.PLAYER_BOUNDING_BOX_RADIUS) < 0){
			top_l.y -= 1;
			top_r.y -= 1;
			//This is the value needed to JUST touch the side of the adjacent tile.
			newDxDy.y = Constants.PLAYER_BOUNDING_BOX_RADIUS-tile_loc.y;
		}else if(((tile_loc.y+dy)+Constants.PLAYER_BOUNDING_BOX_RADIUS) > Constants.WORLD_TILE_SIZE){
			bot_r.y += 1;
			bot_l.y += 1;
			//This is the value needed to JUST touch the side of the adjacent tile.
			newDxDy.y = -tile_loc.y-Constants.PLAYER_BOUNDING_BOX_RADIUS+Constants.WORLD_TILE_SIZE;
		}
		
		//check for map limits:
		if(top_l.x < 0){
			limitX = true;
			top_l.x = 0;
		}
		if(top_l.y < 0){
			limitY = true;
			top_l.y = 0;
		}
		
		if(top_r.x >= Constants.WORLD_SIZE){
			limitX = true;
			top_r.x = Constants.WORLD_SIZE-1;
		}
		if(top_r.y < 0){
			limitY = true;
			top_r.y = 0;
		}
		
		if(bot_l.x < 0){
			limitX = true;
			bot_l.x = 0;
		}
		if(bot_l.y >= Constants.WORLD_SIZE){
			limitY = true;
			bot_l.y = Constants.WORLD_SIZE-1;
		}
		
		if(bot_r.x >= Constants.WORLD_SIZE){
			limitX = true;
			bot_r.x = Constants.WORLD_SIZE-1;
		}
		if(bot_r.y >= Constants.WORLD_SIZE){
			limitY = true;
			bot_r.y = Constants.WORLD_SIZE-1;
		}
		
		//do collision detection for bottom left corner of bounding box.
		if(!tileLib.get(world.getWorldArr()[bot_l.x][bot_l.y]).isPassible()){
			//heading south-east
			if(dx >= 0 && dy > 0){
				limitY = true;
			}
			//heading south-west
			if(dx < 0 && dy > 0){
				if(sideDetect(tile_loc.x-Constants.PLAYER_BOUNDING_BOX_RADIUS, tile_loc.y+Constants.PLAYER_BOUNDING_BOX_RADIUS, dx, dy) == 0){
					limitY = true;
				}else{
					limitX = true;
				}
			}
			//heading north-west
			if(dx < 0 && dy <= 0){
				limitX = true;
			}
		}
		
		//do collision detection for bottom right corner of bounding box.
		if(!tileLib.get(world.getWorldArr()[bot_r.x][bot_r.y]).isPassible()){
			//heading south-west
			if(dx <= 0 && dy > 0){
				limitY = true;
			}
			//heading south-east
			if(dx > 0 && dy > 0){
				if(sideDetect(tile_loc.x+Constants.PLAYER_BOUNDING_BOX_RADIUS, tile_loc.y+Constants.PLAYER_BOUNDING_BOX_RADIUS, dx, dy) == 0){
					limitY = true;
				}else{
					limitX = true;
				}
			}
			//heading north-east
			if(dx > 0 && dy <= 0){
				limitX = true;
			}
		}
		
		//do collision detection for top right corner of bounding box.
		if(!tileLib.get(world.getWorldArr()[top_r.x][top_r.y]).isPassible()){
			//heading north-west
			if(dx <= 0 && dy < 0){
				limitY = true;
			}
			//heading north-east
			if(dx > 0 && dy < 0){
				if(sideDetect(tile_loc.x+Constants.PLAYER_BOUNDING_BOX_RADIUS, tile_loc.y-Constants.PLAYER_BOUNDING_BOX_RADIUS, dx, dy) == 0){
					limitY = true;
				}else{
					limitX = true;
				}
			}
			//heading south-east
			if(dx > 0 && dy >= 0){
				limitX = true;
			}
		}
		
		//do collision detection for top left corner of bounding box.
		if(!tileLib.get(world.getWorldArr()[top_l.x][top_l.y]).isPassible()){
			//heading north-east
			if(dx >= 0 && dy < 0){
				limitY = true;
			}
			//heading north-west
			if(dx < 0 && dy < 0){
				if(sideDetect(tile_loc.x-Constants.PLAYER_BOUNDING_BOX_RADIUS, tile_loc.y-Constants.PLAYER_BOUNDING_BOX_RADIUS, dx, dy) == 0){
					limitY = true;
				}else{
					limitX = true;
				}
			}
			//heading south-west
			if(dx < 0 && dy >= 0){
				limitX = true;
			}
		}
		
		if(limitX) dx = newDxDy.x;
		if(limitY) dy = newDxDy.y;
		
		return new Point(dx, dy);
	}
	
	/* Helper function for collision detection. It tells you if you cross a 
	// vertical tile line or a horizontal tile line first given the dy and dx
	// as your direction. This is needed to catch a corner of an unpassable tile
	// when you are running fast enough to hop over that corner between frames.
	// return 0 for horizontal pass or 1 for vertical or otherwise. */
	public int sideDetect(int sx, int sy, int dx, int dy){
		//java's % operator behaves weird. Example: -2%32 = -2 NOT 30.
		sx = sx % Constants.WORLD_TILE_SIZE;
		sy = sy % Constants.WORLD_TILE_SIZE;
		if(sx < 0) sx += Constants.WORLD_TILE_SIZE;
		if(sy < 0) sy += Constants.WORLD_TILE_SIZE;
		
		//already hitting a side.
		if(sx == 0) return 1;
		if(sy == 0) return 0;
		
		float slope = ((float)dy)/dx;
		
		//heading south-east
		if(dx >= 0 && dy >= 0){
			int wall_dist = Constants.WORLD_TILE_SIZE-sx;
			float y_diff = wall_dist*slope;
			if(sy+y_diff > Constants.WORLD_TILE_SIZE){
				return 0;
			}else{
				return 1;
			}
		}
		
		//heading north-east
		if(dx >= 0 && dy < 0){
			int wall_dist = Constants.WORLD_TILE_SIZE-sx;
			float y_diff = wall_dist*slope;
			if(sy+y_diff < 0){
				return 0;
			}else{
				return 1;
			}
		}
		
		//heading south-west
		if(dx < 0 && dy >= 0){
			int wall_dist = sx;
			float y_diff = wall_dist*slope;
			if(sy-y_diff > Constants.WORLD_TILE_SIZE){
				return 0;
			}else{
				return 1;
			}
		}
		
		//heading north-west
		if(dx < 0 && dy < 0){
			int wall_dist = sx;
			float y_diff = wall_dist*slope;
			if(sy-y_diff < 0){
				return 0;
			}else{
				return 1;
			}
		}
		
		return 0;
	}
	
	public Context getContext(){
		return this.context;
	}
}
