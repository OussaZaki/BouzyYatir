/**
 * The pauseButton
 * 
 * @author Zaki Oussama
 * Copyright (c) <2014> <Zaki Oussama>
 */

package com.app.bouzyyatir;

import com.app.bouzyyatir.R;

public class PauseButton extends Sprite{
	public PauseButton(GameView view, Game game) {
		super(view, game);
		this.bitmap = createBitmap(game.getResources().getDrawable(R.drawable.pause_button));
		this.width = this.bitmap.getWidth();
		this.height = this.bitmap.getHeight();
	}
	
	/**
	 * Sets the button in the right upper corner.
	 */
	@Override
	public void move(){
		this.x = this.width/2;
		this.y = this.height/2;
	}
}