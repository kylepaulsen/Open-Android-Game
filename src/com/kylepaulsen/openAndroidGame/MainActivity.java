/* MainActivity.java - The main class that extends activity.
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
        
        //test bouncing view
        //setContentView(new BouncingViewTest(this));
    }
}