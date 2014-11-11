/**
 * An obstacle: TopTube + botHead
 * 
 * @author Zaki Oussama
 * Copyright (c) <2014> <Zaki Oussama>
 */

package com.app.bouzyyatir;

import android.graphics.Canvas;

public class Obstacle extends Sprite{
	private TopTube top;
	private BotTube bot;
	
	/** Necessary so the onPass method is just called once */
	private boolean isPassed = false;

	public Obstacle(GameView view, Game game) {
		super(view, game);
		top = new TopTube(view, game);
		bot = new BotTube(view, game);
		
		initPos();
	}
	
	/**
	 * Creates a Top Tube and a bot Tube at the right of the screen.
	 * With a certain gap between them.
	 * The vertical position is in a certain area random.
	 */
	private void initPos(){
		int height = game.getResources().getDisplayMetrics().heightPixels;
		int gab = (int) (height / 4.5 - view.getSpeedX());
		if(gab < height / 5.5){
			gab = (int) (height / 5.5);
		}
		int random = (int) (Math.random() * height * 2 / 5);
		int y1 = (height / 10) + random - top.height;
		int y2 = (height / 10) + random + gab;
		
		top.init(game.getResources().getDisplayMetrics().widthPixels, y1);
		bot.init(game.getResources().getDisplayMetrics().widthPixels, y2);
	}

	/**
	 * Draws top and bot.
	 */
	@Override
	public void draw(Canvas canvas) {
		top.draw(canvas);
		bot.draw(canvas);
	}

	/**
	 * Checks whether both, top and bot, are out of range.
	 */
	@Override
	public boolean isOutOfRange() {
		return top.isOutOfRange() && bot.isOutOfRange();
	}

	/**
	 * Checks whether the top or the bot is colliding with the sprite.
	 */
	@Override
	public boolean isColliding(Sprite sprite) {
		return top.isColliding(sprite) || bot.isColliding(sprite);
	}

	/**
	 * Moves both, top and bot.
	 */
	@Override
	public void move() {
		top.move();
		bot.move();
	}
	
	public float getXMiddle(){
		return top.getX();
	}
	
	public boolean isMiddle(){
		return top.isMiddleRange();
	}

	/**
	 * Sets the speed of the top and the bot.
	 */
	@Override
	public void setSpeedX(float speedX) {
		top.setSpeedX(speedX);
		bot.setSpeedX(speedX);
	}
	
	/**
	 * Checks whether the top and the bot are passed.
	 */
	@Override
	public boolean isPassed(){
		return top.isPassed() && bot.isPassed();
	}
	
	/**
	 * Will call obstaclePassed of the game, if this is the first pass of this obstacle.
	 */
	@Override
	public void onPass(){
		if(!isPassed){
			isPassed = true;
			view.getGame().obstaclePassed();
		}
	}

}
