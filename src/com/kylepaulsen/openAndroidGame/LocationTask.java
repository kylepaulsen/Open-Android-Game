/* LocationTask.java - Class for getting location data and doing things with it.
 * 
 * Copyright © 2011 Open-Android-Game Group
 * Please see the file COPYING in this
 * distribution for license terms.
 */

package com.kylepaulsen.openAndroidGame;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

/*
 * This class is in charge of everything location task related.
 * The first thing it does is generate a random seed from your 
 * last location.
 */

public class LocationTask {
	private Context context;
	private double latitude, longitude; 
	
	public LocationTask(Context con){
		this.context = con;
		
		//just some default values...
		this.latitude = 3000;
		this.longitude = 3000;
	}
	
	/*
	 * The init function tries to get the last known location from the best
	 * provider. These settings might be changed later.
	 * THIS MUST BE CALLED BEFORE ANY OTHER METHOD IN HERE.
	 */
	public void init(){
		//I might not be doing something right because the location 
		//data isn't as close as I expected it to be.
		
		LocationManager lm = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		Location lastLocation = null;
		
		for(int tries=0; tries<3; ++tries){
			String provider = lm.getBestProvider(criteria, true);
			lastLocation = lm.getLastKnownLocation(provider);
			if(lastLocation != null){
				break;
			}
		}
	
		if(lastLocation == null){
			//couldn't get location. send error message or something...
			Toast.makeText(this.context, "Error: Couldn't get last location!", Toast.LENGTH_LONG).show();
			//default to near Salem Oregon.
			this.latitude = 45;
			this.longitude = -123;
		}else{
			this.latitude = lastLocation.getLatitude();
			this.longitude = lastLocation.getLongitude();
		}		
	}
	
	public long makeSeedFromLocation(){
		/*
		 * Here is the basic idea for generating a seed:
		 * First add 90 to lat and 180 to long to guarantee they are positive.
		 * Then multiply both by 100 and then divide by 5. (mult by 20).
		 * We mult by 100 because we want to keep up to the hundredth precision
		 * out of the lat and long before we floor them.
		 * We divide by 5 because we want increments of 5 hundredth's to belong 
		 * to the same seed. Then we concatenate the numbers together to get the 
		 * seed. Keep in mind that we must multiply the lat_num by 10000 to avoid
		 * digit collision during concatenation. (long_num will never be more than 7200)
		 */
		if(this.latitude == 3000){
			Toast.makeText(this.context, "LocationTask was used without an init() call.", Toast.LENGTH_LONG).show();
			return 0;
		}
		
		long lat_num = (long) Math.floor((this.latitude+90)*20)*10000;
		long long_num = (long) Math.floor((this.longitude+180)*20);
		Toast.makeText(this.context, "Seed is: "+(lat_num+long_num), Toast.LENGTH_LONG).show();
		return (lat_num+long_num);
	}
	
	public double getLongitude(){
		if(this.longitude == 3000){
			Toast.makeText(this.context, "LocationTask was used without an init() call.", Toast.LENGTH_LONG).show();
			return 0;
		}
		return this.longitude;		
	}
	
	public double getLatitude(){
		if(this.latitude == 3000){
			Toast.makeText(this.context, "LocationTask was used without an init() call.", Toast.LENGTH_LONG).show();
			return 0;
		}
		return this.latitude;		
	}
		
}
