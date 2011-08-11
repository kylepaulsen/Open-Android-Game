/* MainActivity.java - The main class that extends activity.
*
* Copyright © 2011 Open-Android-Game Group
* Please see the file COPYING in this
* distribution for license terms.
*/

package com.kylepaulsen.openAndroidGame;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
    
GraphicsView gameView;
/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Get rid of the application title bar.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Force application full screen.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        Constants.WINDOW_HEIGHT = getWindowManager().getDefaultDisplay().getHeight();
        Constants.WINDOW_WIDTH = getWindowManager().getDefaultDisplay().getWidth();
        
        //Kyle's android screen is 480x800
        Log.d("Main", "screen dimentions: "+Constants.WINDOW_WIDTH+", "+Constants.WINDOW_HEIGHT);
        
        gameView = new GraphicsView(this);
        
        //Set the graphics view as the main content view.
        //Note that R.layout.main is now no longer used..
      
        setContentView(gameView);
        
    }
}