package com.app.bouzyyatir;

import com.app.bouzyyatir.MainActivity;
import com.app.bouzyyatir.R;
import com.app.bouzyyatir.Sprite;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends Activity {

	/** Name of the SharedPreference that saves the medals */
	public static final String medaille_save = "medaille_save";

	/** Key that saves the medal */
	public static final String medaille_key = "medaille_key";
	public static Typeface typeFace;
	public static final float DEFAULT_VOLUME = 0.3f;

	/** Volume for sound and music */
	public static float volume = DEFAULT_VOLUME;

	private ImageButton muteButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		((Button) findViewById(R.id.play_button))
				.setBackgroundResource(R.drawable.play_button);
		((Button) findViewById(R.id.play_button))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						((Button) findViewById(R.id.play_button))
								.setBackgroundResource(R.drawable.play_buttonclicked);
						startActivity(new Intent("com.app.bouzyyatir.Game"));
					}
				});

		muteButton = ((ImageButton) findViewById(R.id.mute_button));
		muteButton.setImageBitmap(Sprite.createBitmap(getResources()
				.getDrawable(R.drawable.speaker), this));
		muteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (volume != 0) {
					volume = 0;
					muteButton.setImageBitmap(Sprite
							.createBitmap(
									getResources().getDrawable(
											R.drawable.speaker_mute),
									MainActivity.this));
				} else {
					volume = DEFAULT_VOLUME;
					muteButton.setImageBitmap(Sprite.createBitmap(
							getResources().getDrawable(R.drawable.speaker),
							MainActivity.this));
				}
			}
		});

		((Button) findViewById(R.id.about_button))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						((Button) v)
								.setBackgroundResource(R.drawable.proposclicked);
						startActivity(new Intent("com.app.bouzyyatir.About"));
					}
				});
		typeFace = Typeface.createFromAsset(getAssets(), "fonts/04B_19__.ttf");
		setSocket();
	}

	/**
	 * Fills the socket with the medals that have already been collected.
	 */
	private void setSocket() {
		SharedPreferences saves = this.getSharedPreferences(medaille_save, 0);
		switch (saves.getInt(medaille_key, 0)) {
		case 1:
			((ImageView) findViewById(R.id.medaille_socket))
					.setImageBitmap(Sprite.createBitmap(getResources()
							.getDrawable(R.drawable.socket_bronce), this));
			break;
		case 2:
			((ImageView) findViewById(R.id.medaille_socket))
					.setImageBitmap(Sprite.createBitmap(getResources()
							.getDrawable(R.drawable.socket_silver), this));
			break;
		case 3:
			((ImageView) findViewById(R.id.medaille_socket))
					.setImageBitmap(Sprite.createBitmap(getResources()
							.getDrawable(R.drawable.socket_gold), this));
			break;
		}
	}

	/**
	 * Updates the socket for the medals.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		((Button) findViewById(R.id.play_button))
				.setBackgroundResource(R.drawable.play_button);
		setSocket();
	}

}
