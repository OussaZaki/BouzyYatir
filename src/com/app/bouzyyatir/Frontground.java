/**
 * Manages the bitmap at the front
 * 
 * @author Zaki Oussama
 * Copyright (c) <2014> <Zaki Oussama>
 */

package com.app.bouzyyatir;

import com.app.bouzyyatir.R;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class Frontground extends Background {
	/**
	 * Height of the ground relative to the height of the bitmap
	 */
	public static final float GROUND_HEIGHT = (1f * /*45*/ 35) / 720;

	/**
	 * Static bitmap to reduce memory usage.
	 */
	public static Bitmap globalBitmap;
	
	public Frontground(GameView view, Game game) {
		super(view, game);
		if(globalBitmap == null){
			BitmapDrawable bd = (BitmapDrawable) game.getResources().getDrawable(R.drawable.fg);
			globalBitmap = bd.getBitmap();
		}
		this.bitmap = globalBitmap;
	}
	
}
