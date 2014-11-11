/**
 * A yummy toast
 * 
 * @author Zaki Oussama
 * Copyright (c) <2014> <Zaki Oussama>
 */

package com.app.bouzyyatir;

import com.app.bouzyyatir.R;

import android.graphics.Bitmap;

public class Toast extends PowerUp {
	
	/**
	 * Static bitmap to reduce memory usage.
	 */
	public static Bitmap globalBitmap;

	public Toast(GameView view, Game game) {
		super(view, game);
		if(globalBitmap == null){
			globalBitmap = createBitmap(game.getResources().getDrawable(R.drawable.toast));
		}
		this.bitmap = globalBitmap;
		this.width = this.bitmap.getWidth();
		this.height = this.bitmap.getHeight();
	}

	/**
	 * When eaten the player will turn into nyan cat.
	 */
	@Override
	public void onCollision() {
		super.onCollision();
		view.changeToNyanCat();
	}
	
	
}
