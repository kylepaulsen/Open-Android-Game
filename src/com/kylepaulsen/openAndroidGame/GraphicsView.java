package com.kylepaulsen.openAndroidGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * 
 * @author Kyle
 * This class is the primary graphics class that is 
 * responsible for all drawing routines. It takes the MainActivity
 * as its context.
 * 
 * I guess it will eventually listen for input.
 */
public class GraphicsView extends SurfaceView {

	//Create a buffer bitmap so that static things can be redrawn fast and easy.
	private Bitmap cBuffer;
	//Create a current frame canvas for animated changes in the graphics.
	private Canvas cFrame;

	private SurfaceHolder graphicsHolder;
	
	private GameProgram prog;
	private int x = 0;
	
	public GraphicsView(Context context) {
		super(context);
		prog = new GameProgram(this);
		
		graphicsHolder = this.getHolder();
		graphicsHolder.addCallback(new Callback(){

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				prog.setRunning(true);
				prog.start();
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				prog.setRunning(false);
				while(prog.isAlive()){
					try{
						prog.join();
						
					}catch(InterruptedException e){}
				}
			}
		});
		
		cFrame = new Canvas();
	   

	}

	//Here is the main drawing method. For now, 
	//it just draws a white rectangle to fill the screen.
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(cBuffer == null){
			//if the buffer is new, set it up and set it to draw with the cFrame.
			//ARGB_8888 means each pixel uses 4 bytes.
			cBuffer = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
	        cFrame.setBitmap(cBuffer);
		}
		
		Paint p = new Paint();
		p.setColor(Color.RED);
		p.setStyle(Paint.Style.FILL);
		
		cFrame.drawRect(x, 0, x+50, 50, p);
		x++;
		//draw the current frame buffer.
		canvas.drawBitmap(cBuffer, 0, 0, null);				
	}
	
}
