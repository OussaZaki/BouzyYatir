/**
 * The bouzy that is controlled by the player
 * 
 * @author Zaki Oussama
 * Copyright (c) <2014> <Zaki Oussama>
 */

package com.app.bouzyyatir;

import java.util.HashMap;

import com.app.bouzyyatir.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;

public class Bouzy extends PlayableCharacter {
	
	/** Static bitmap to reduce memory usage. */
	public static Bitmap globalBitmap;
	
	/** The bouzy sound */
	public static final int SOUND_1 = 1;
	public static final int SOUND_2 = 2;

	SoundPool mSoundPool;
	HashMap<Integer, Integer> mSoundMap;


	public Bouzy(GameView view, Game game) {
		super(view, game);
		if(globalBitmap == null){
			globalBitmap = createBitmap(game.getResources().getDrawable(R.drawable.bouzy));
		}
		this.bitmap = globalBitmap;
		this.width = this.bitmap.getWidth()/(colNr = 8);	// The image has 8 frames in a row
		this.height = this.bitmap.getHeight()/4;			// and 4 in a column
		this.frameTime = 3;		// the frame will change every 3 runs
		this.y = game.getResources().getDisplayMetrics().heightPixels / 2;	// Startposition in in the middle of the screen
		
		mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
		mSoundMap = new HashMap<Integer, Integer>();
		  
		if(mSoundPool != null){
		    mSoundMap.put(SOUND_1, mSoundPool.load(game, R.raw.bouzy, 1));
		    mSoundMap.put(SOUND_2, mSoundPool.load(game, R.raw.fail, 1));
		}
	}
	
	/*
	*Call this function from code with the sound you want e.g. playSound(SOUND_1);
	*/
	public void playSound(int sound) {
	    if(MainActivity.volume != 0){
	    	AudioManager mgr = (AudioManager)game.getSystemService(Context.AUDIO_SERVICE);
	    	float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
	    	float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	    	float volume = streamVolumeCurrent / streamVolumeMax;  
	    
	    	if(mSoundPool != null){
	    		mSoundPool.play(mSoundMap.get(sound), volume, volume, 1, 0, 1.0f);
	    	}
	    }
	}
	
	@Override
	public void onTap(){
		super.onTap();
		playSound(SOUND_1);
	}
	
	/**
	 * Calls super.move
	 * and manages the frames. (flattering cape)
	 */
	@Override
	public void move(){
		super.move();
		
		// manage frames
		if(row != 3){
			// not dead
			if(speedY > getTabSpeed() / 3 && speedY < getMaxSpeed() * 1/3){
				row = 0;
			}else if(speedY > 0){
				row = 1;
			}else{
				row = 2;
			}
		}
	}

	/**
	 * Calls super.dead
	 * And changes the frame to a dead bouzy -.-
	 */
	@Override
	public void dead() {
		this.row = 3;
		this.frameTime = 3;
		super.dead();
	}
}
