/* Biome.java - Class for describing biomes.
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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.Toast;

/*
 * This class is for describing biomes with lots of public vars...
 * It gets information from the world_map.jpg pixel data.
 */
public class Biome {
	Context con;
	double lat, lon;
	int lonPxl, latPxl;
	
	
	public Biome(Context context, double latitude, double longitude){
		this.con = context;
		this.lat = latitude;
		this.lon = longitude;
	}
	
	//This function will set a bunch of public vars to describe 
	//the current biome for the world generator. 	
	public void anazlyze(){
		//get the corresponding pixel.
		this.lonPxl = lonToPxlX(this.lon);
		this.latPxl = latToPxlY(this.lat);
		
		//The options code is very important so make sure we read a correct pixel value.
		BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		decodeOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
		
		//get the pixel color in the world map.
		//Bitmap world = BitmapFactory.decodeResource(con.getResources(), R.drawable.world_map, decodeOptions);
		//int landColor = world.getPixel(this.lonPxl, this.latPxl);
		//int red = Color.red(landColor);
		//int green = Color.green(landColor);
		//int blue = Color.blue(landColor);
		
		
		Toast.makeText(con, "looking for pxl: "+this.lonPxl+", "+this.latPxl, Toast.LENGTH_LONG).show();
		//Toast.makeText(con, "Pxl rgb is: "+Color.red(landColor)+", "+Color.green(landColor)+", "+Color.blue(landColor), Toast.LENGTH_LONG).show();
		
		//get hsv
		float hsv[] = new float[3];
		//Color.RGBToHSV(red, green, blue, hsv);
		
		//now if the h in hsv is between certain ranges, we have a biome.
		//we could also use r, g, b, s or v for something.
	}
	
	public int latToPxlY(double lat){
		//formula found in worldMapLatLongToPixel.txt
		return (int) (Math.floor(lat/-0.06698049772278683227848101265823) + 1351);
	}
	
	public int lonToPxlX(double lon){
		//formula found in worldMapLatLongToPixel.txt
		return (int) (Math.floor(lon/0.06657861654370824053452115812918) + 2697);
	}
}
