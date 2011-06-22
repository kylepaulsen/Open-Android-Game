package com.kylepaulsen.openAndroidGame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * 
 * @author Kyle
 * This class is the primary graphics class that is 
 * responsible for all drawing routines. It takes the MainActivity
 * as its context.
 */
public class GraphicsView extends View {

	public GraphicsView(Context context) {
		super(context);
	}

	//Here is the main drawing method. For now, 
	//it just draws a white rectangle to fill the screen.
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		Paint p = new Paint();
		p.setColor(Color.WHITE);
		p.setStyle(Paint.Style.FILL);
		
		//Fill the screen with white.
		canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), p);
	}
	
}
