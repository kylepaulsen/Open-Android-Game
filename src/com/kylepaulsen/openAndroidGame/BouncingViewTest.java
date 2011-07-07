/* BouncingViewTest.java -- class for testing a new view
 * 
 * @author Xiaolong Cheng
 */

package com.kylepaulsen.openAndroidGame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

public class BouncingViewTest extends View{
	
	private Player player;
	
	
	public BouncingViewTest(Context context){
		super(context);
		
		//player = new Player(Color.GREEN);
		player = new Player(BitmapFactory.decodeResource(getResources(), 
				R.drawable.mario), 50,50);
		

		
		//enable touch mode
		this.setFocusableInTouchMode(true);
	}
	
	   @Override
	   protected void onDraw(Canvas canvas) {

	      
	      //draw many Tiles
	      for (int i =0; i<10; ++i){
	    	  for (int j=0; j<8; ++j){
	    		Tile tile = new Tile(BitmapFactory.decodeResource(getResources(), 
	    				R.drawable.grass), 50*i, 50*j);
	    		tile.draw(canvas);  
	    	  }	    		  
	      }
	      
	      // Draw a single player
	      player.draw(canvas);
	      // Update the position of the ball, including collision detection and reaction.
	      player.moveWithCollisionDetection();

	  
	      // Delay
	      try {  
	         Thread.sleep(30);  
	      } catch (InterruptedException e) { }
	      
	      invalidate();  // Force a re-draw

	   }
	
	// Called back when the view is first created or its size changes.
	   @Override
	   public void onSizeChanged(int w, int h, int oldW, int oldH) {
	      // Set the movement bounds for the ball
	      player.xMax = w-1;
	      player.yMax = h-1;
	   }

	   // touch input handler
	   @Override
	   public boolean onTouchEvent(MotionEvent event) {
	      float currentX = event.getX();
	      float currentY = event.getY();
	      
	      if (player.getSpeedX()>0 && currentX<player.getX()) {
	    	  player.setSpeedX (- player.getSpeedX());
	      } 
	      if (player.getSpeedX()<0 && currentX>player.getX()+ player.getWidth()) {
	    	  player.setSpeedX(- player.getSpeedX());
	      } 
	      if (player.getSpeedY()>0 && currentY<player.getY()) {
	    	  player.setSpeedY (- player.getSpeedY());
	      } 
	      if (player.getSpeedY()<0 && currentY>player.getY()+player.getHeight()) {
	    	  player.setSpeedY (- player.getSpeedY());
	      } 

	      return true;  // Event handled
	   }
	   
}
