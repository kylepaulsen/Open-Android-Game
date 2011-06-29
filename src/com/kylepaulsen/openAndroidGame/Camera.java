/* Camera.java - Class for loading and tiling worlds.
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

import android.graphics.Canvas;

/*
 * This class will be in charge of loading worlds and writing to the
 * frame buffer (i think). It will also be in charge of world scrolling
 * and rendering other entities.
 */

public class Camera {
	private GraphicsView gv;
	
	public Camera(GraphicsView view){
		this.gv = view;
	}
	
	public void loadWorld(World world){
		//This method will tile the world with 
		//sprites using the world's 2d array as a reference.
		//Also any other entities should be loaded and rendered here.
		
		Canvas can = gv.getBuffer();
	}
}
