package com.app.bouzyyatir;

import com.app.bouzyyatir.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class About extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        
        // Backbutton
        ((Button)findViewById(R.id.back_button)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
