/**
 * A TopTube with web
 * 
 * BTW TopTubes have 8 eyes.
 * 
 * @author Zaki Oussama
 * Copyright (c) <2014> <Zaki Oussama>
 */
package com.app.bouzyyatir;

import com.app.bouzyyatir.R;

import android.graphics.Bitmap;

public class TopTube extends Sprite {
	
	/**
	 * Static bitmap to reduce memory usage.
	 */
	public static Bitmap globalBitmap;

	public TopTube(GameView view, Game game) {
		super(view, game);
		if(globalBitmap == null){
			globalBitmap = createBitmap(game.getResources().getDrawable(R.drawable.top_full));
		}
		this.bitmap = globalBitmap;
		this.width = this.bitmap.getWidth();
		this.height = this.bitmap.getHeight();
	}
	
	/**
	 * Sets the position
	 * @param x
	 * @param y
	 */
	public void init(int x, int y){
		this.x = x;
		this.y = y;
	}

}
