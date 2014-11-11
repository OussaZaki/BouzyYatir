/**
 * Rainbow tail for the nyan cat
 * 
 * @author Zaki Oussama
 * Copyright (c) <2014> <Zaki Oussama>
 */

package com.app.bouzyyatir;

import com.app.bouzyyatir.R;

import android.graphics.Bitmap;

public class Rainbow extends Sprite {
	
	/**
	 * Static bitmap to reduce memory usage.
	 */
	public static Bitmap globalBitmap;
	
	public Rainbow(GameView view, Game game) {
		super(view, game);
		if(globalBitmap == null){
			globalBitmap = createBitmap(game.getResources().getDrawable(R.drawable.rainbow));
		}
		this.bitmap = globalBitmap;
		this.width = this.bitmap.getWidth()/(colNr = 4);
		this.height = this.bitmap.getHeight()/3;
		this.y = game.getResources().getDisplayMetrics().heightPixels / 2;
	}
}
