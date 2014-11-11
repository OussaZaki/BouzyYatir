/**
 * The SuperClass of every character that is controlled by the player
 * 
 * @author Zaki Oussama
 * Copyright (c) <2014> <Zaki Oussama>
 */

package com.app.bouzyyatir;

public abstract class PlayableCharacter extends Sprite {
	
	public PlayableCharacter(GameView view, Game game) {
		super(view, game);
	}
	
	/**
	 * Calls super.move
	 * Moves the character to 1/6 of the horizontal screen
	 * Manages the speed changes -> Falling
	 */
	@Override
	public void move(){
		this.x = this.view.getWidth() / 6;
		
		if(speedY < 0){
			// The character is moving up
			speedY = speedY * 2 / 3 + getSpeedTimeDecrease() / 2;
		}else{
			// the character is moving down
			this.speedY += getSpeedTimeDecrease();
		}
		
		if(this.speedY > getMaxSpeed()){
			// speed limit
			this.speedY = getMaxSpeed();
		}
		
		super.move();
	}

	/**
	 * A dead character falls slowly to the ground.
	 */
	public void dead(){
		this.speedY = getMaxSpeed()/2;
	}
	
	/**
	 * Let the character flap up.
	 */
	public void onTap(){
		this.speedY = getTabSpeed();
		this.y += getPosTabIncrease();
	}
	
	/**
	 * Falling speed limit
	 * @return
	 */
	protected float getMaxSpeed(){
		// 25 @ 720x1280 px
		return view.getHeight() / 35.0f;
	}
	
	/**
	 * Every run cycle the speed towards the ground will increase.
	 * inzil yaaa bouuzi :D
	 * @return
	 */
	protected float getSpeedTimeDecrease(){
		// 4 @ 720x1280 px
		return view.getHeight() / 100;
	}
	
	/**
	 * The character gets this speed when taped.
	 * @return
	 */
	protected float getTabSpeed(){
		// -80 @ 720x1280 px
		return - view.getHeight() / 11;
	}
	
	/**
	 * The character jumps up the pixel height of this value.
	 * is3ad yaaa bouuzi :D
	 * @return
	 */
	
	protected int getPosTabIncrease(){
		// -12 @ 720x1280 px
		return - view.getHeight() / 150;
	}
}
