/* Home.java - The entry view of game.
 * 
 * Copyright © 2011 Open-Android-Game Group
 * Please see the file COPYING in this
 * distribution for license terms.
 */

  /* Xiaolong, added Aug 09 */

package com.kylepaulsen.openAndroidGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Home extends Activity implements OnClickListener{
    
	private Button introBtn;
	private Button startBtn;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.home);
        introBtn =(Button)findViewById(R.id.intro_btn);
        introBtn.setOnClickListener(this);
        startBtn =(Button)findViewById(R.id.start_btn);
        startBtn.setOnClickListener(this);    
    }
    
	@Override
	public void onClick(View v) {
		if(v==introBtn){ 
			//TODO do sth to show the intro page......
		}else if(v==startBtn){
			startActivity(new Intent(Home.this, MainActivity.class));	
		}		
	}
}
