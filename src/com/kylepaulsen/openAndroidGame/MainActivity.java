package com.kylepaulsen.openAndroidGame;
//hoobey doobey
import android.app.Activity;
import android.os.Bundle;
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
        
        gameView = new GraphicsView(this);
        
        //Set the graphics view as the main content view.
        //Note that R.layout.main is now no longer used..
        setContentView(gameView);
    }
}