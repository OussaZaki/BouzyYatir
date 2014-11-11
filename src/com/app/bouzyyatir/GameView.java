/**
 * GameView
 * Probably the most important class for the game
 * 
 * @author Zaki Oussama
 * Copyright (c) <2014> <Zaki Oussama>
 */

package com.app.bouzyyatir;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class GameView extends SurfaceView implements Runnable, OnTouchListener {

	/** Milliseconds the thread sleeps after drawing */
	public static final long UPDATE_INTERVAL = 30;

	/** The thread that checks, moves and draws */
	private Thread thread;

	/** The surfaceholder needed for the canvas drawing */
	private SurfaceHolder holder;

	/** Whether the thread should run or not */
	volatile private boolean shouldRun = true;

	/** Whether the tutorial was already shown */
	private boolean showedTutorial = false;

	private Game game;
	private PlayableCharacter player;
	private Background bg;
	private Frontground fg;
	private List<Obstacle> obstacles = new ArrayList<Obstacle>();
	private List<PowerUp> powerUps = new ArrayList<PowerUp>();

	private PauseButton pauseButton;
	private Tutorial tutorial;

	public GameView(Context context) {
		super(context);
		this.game = (Game) context;

		holder = getHolder();
		player = new Bouzy(this, game);
		bg = new Background(this, game);
		fg = new Frontground(this, game);
		pauseButton = new PauseButton(this, game);
		tutorial = new Tutorial(this, game);

		setOnTouchListener(this);
	}

	/**
	 * Manages the touchevent
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (!shouldRun) {
				// Start game if it's paused
				resumeAndKeepRunning();
			}

			if (pauseButton.isTouching((int) event.getX(), (int) event.getY())
					&& this.shouldRun) {
				pause();
			} else {
				// Bouzy flap
				this.player.onTap();
			}
		}

		return true;
	}

	/**
	 * The thread runs this method
	 */
	public void run() {
		draw(); // draw at least once

		while (shouldRun || !showedTutorial) {
			if (!showedTutorial) {
				showTutorial();
			} else {
				// check
				checkPasses();
				checkOutOfRange();
				checkCollision();
				createObstacle();
				move();

				draw();

				try {
					Thread.sleep(UPDATE_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Draw Tutorial
	 */
	public void showTutorial() {
		showedTutorial = true;

		player.move();
		pauseButton.move();

		while (!holder.getSurface().isValid()) {/* wait */
		}

		Canvas canvas = holder.lockCanvas();
		drawCanvas(canvas);
		drawTutorial(canvas);
		holder.unlockCanvasAndPost(canvas);
	}

	/**
	 * Joins the thread
	 */
	public void pause() {
		shouldRun = false;
		while (thread != null) {
			try {
				thread.join();
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		thread = null;
	}

	/**
	 * Activates the thread. But shouldRun will be false. This means the canvas
	 * will be drawn once. If this is the first start of a game, the tutorial
	 * will be drawn.
	 */
	public void resume() {
		pause(); // make sure the old thread isn't running
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Start the thread and let it run.
	 */
	public void resumeAndKeepRunning() {
		pause(); // make sure the old thread isn't running
		shouldRun = true;
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Draws all gameobjects on the surface
	 */
	private void draw() {
		while (!holder.getSurface().isValid()) {/* wait */
		}
		Canvas canvas = holder.lockCanvas();
		drawCanvas(canvas);
		holder.unlockCanvasAndPost(canvas);
	}

	/**
	 * Draws all gameobjects on the canvas
	 * 
	 * @param canvas
	 */
	private void drawCanvas(Canvas canvas) {
		bg.draw(canvas);
		for (Obstacle r : obstacles) {
			r.draw(canvas);
		}
		for (PowerUp p : powerUps) {
			p.draw(canvas);
		}
		player.draw(canvas);
		fg.draw(canvas);
		pauseButton.draw(canvas);

		// Score Text
		int xPos = (int) ((canvas.getWidth() / 2) - getScoreTextMetrics()/2);
		Paint paint = new Paint();
		paint.setTextSize(getScoreTextMetrics());
		paint.setColor(Color.WHITE);
		paint.setTypeface(MainActivity.typeFace);
		
		Paint stkPaint = new Paint();
		stkPaint.setTextSize(getScoreTextMetrics());
		stkPaint.setColor(Color.BLACK);
		stkPaint.setTypeface(MainActivity.typeFace);
		stkPaint.setStrokeWidth(8);
		stkPaint.setStyle(Paint.Style.STROKE);
		
		canvas.drawText(String.valueOf(game.points), xPos,
				getScoreTextMetrics() *2, paint);
		canvas.drawText(String.valueOf(game.points), xPos,
				getScoreTextMetrics() *2, stkPaint);
	
	}

	/**
	 * Draws the tutorial on the canvas
	 * 
	 * @param canvas
	 */
	private void drawTutorial(Canvas canvas) {
		tutorial.move();
		tutorial.draw(canvas);
	}

	/**
	 * Let the player fall to the ground
	 */
	private void playerDeadFall() {
		player.dead();
		do {
			player.move();
			draw();
		} while (!player.isTouchingGround());
	}

	/**
	 * Checks whether a obstacle is passed.
	 */
	private void checkPasses() {
		for (Obstacle o : obstacles) {
			if (o.isPassed()) {
				o.onPass();
				createPowerUp();
			}
		}
	}

	/**
	 * Creates a toast with a certain chance
	 */
	private void createPowerUp() {
		// Toast
		if (game.points >= 40 && powerUps.size() < 1
				&& !(player instanceof NyanCat)) {
			// If no powerUp is present and you have more than / equal 40 points
			if (Math.random() * 100 < 33) { // 33% chance
				powerUps.add(new Toast(this, game));
			}
		}
	}

	/**
	 * Checks whether the obstacles or powerUps are out of range and deletes
	 * them
	 */
	private void checkOutOfRange() {
		for (int i = 0; i < obstacles.size(); i++) {
			if (this.obstacles.get(i).isOutOfRange()) {
				this.obstacles.remove(i);
				i--;
			}
		}
		for (int i = 0; i < powerUps.size(); i++) {
			if (this.powerUps.get(i).isOutOfRange()) {
				this.powerUps.remove(i);
				i--;
			}
		}
	}

	/**
	 * Checks collisions and performs the action
	 */
	private void checkCollision() {
		for (Obstacle o : obstacles) {
			if (o.isColliding(player)) {
				o.onCollision();
				((Bouzy)player).playSound(Bouzy.SOUND_2);
				gameOver();
			}
		}
		for (int i = 0; i < powerUps.size(); i++) {
			if (this.powerUps.get(i).isColliding(player)) {
				this.powerUps.get(i).onCollision();
				this.powerUps.remove(i);
				i--;
			}
		}
		if (player.isTouchingEdge()) {
			gameOver();
		}
	}

	/**
	 * if no obstacle is present a new one is created
	 */
	private void createObstacle() {
		if (obstacles.size() >= 1 && obstacles.size() <= 2) {
			if (obstacles.get(obstacles.size() - 1).getXMiddle() < this.getWidth()/3)
				obstacles.add(new Obstacle(this, game));
		}
		else if (obstacles.size() < 1) {
			obstacles.add(new Obstacle(this, game));
		}
	}

	/**
	 * Update sprite movements
	 */
	private void move() {
		for (Obstacle o : obstacles) {
			o.setSpeedX(-getSpeedX());
			o.move();
		}
		for (PowerUp p : powerUps) {
			p.move();
		}

		bg.setSpeedX(-getSpeedX() / 2);
		bg.move();

		fg.setSpeedX(-getSpeedX() * 4 / 3);
		fg.move();

		pauseButton.move();

		player.move();
	}

	/**
	 * Changes the player to Nyan Cat
	 */
	public void changeToNyanCat() {
		PlayableCharacter tmp = this.player;
		this.player = new NyanCat(this, game);
		this.player.setX(tmp.x);
		this.player.setY(tmp.y);

		game.musicShouldPlay = true;
		Game.musicPlayer.start();
	}

	/**
	 * return the speed of the obstacles/Bouzy
	 */
	public int getSpeedX() {
		// 16 @ 720x1280 px
		int speedDefault = this.getWidth() / 50;
		// 1,2 every 4 points @ 720x1280 px
		int speedIncrease = (int) (this.getWidth() / 600f * (game.points / 4));

		int speed = speedDefault + speedIncrease;

		if (speed > 2 * speedDefault) {
			return 2 * speedDefault;
		} else {
			return speed;
		}
	}

	/**
	 * Let's the player fall down dead, makes sure the runcycle stops and
	 * invokes the next method for the dialog and stuff.
	 */
	public void gameOver() {
		this.shouldRun = false;
		playerDeadFall();
		game.gameOver();
	}

	/**
	 * A value for the position and size of the onScreen score Text
	 */
	public int getScoreTextMetrics() {
		// 106 @ 720x1280 px
		return this.getHeight() / 12;
	}

	public PlayableCharacter getPlayer() {
		return this.player;
	}

	public Game getGame() {
		return this.game;
	}

}
