/* BouncingViewTest.java -- class for testing a new view
 * 
 * @author Xiaolong Cheng
 */

package com.kylepaulsen.openAndroidGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

public class BouncingViewTest extends View{
	
	private Player player;
	
	public BouncingViewTest(Context context){
		super(context);
		
		player = new Player(Color.GREEN);
		
		//enable touch mode
		this.setFocusableInTouchMode(true);
	}
	
	   @Override
	   protected void onDraw(Canvas canvas) {
	      // Draw the components

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
	      
	      if (player.speedX>0 && currentX<player.x) {
	    	  player.speedX = - player.speedX;
	      } 
	      if (player.speedX<0 && currentX>player.x+ player.width) {
	    	  player.speedX = - player.speedX;
	      } 
	      if (player.speedY>0 && currentY<player.y) {
	    	  player.speedY = - player.speedY;
	      } 
	      if (player.speedY<0 && currentY>player.y+player.width) {
	    	  player.speedY = - player.speedY;
	      } 

	      return true;  // Event handled
	   }
	   
}
