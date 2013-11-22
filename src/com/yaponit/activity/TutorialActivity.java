package com.yaponit.activity;

import com.example.yaponit.R;
import com.yaponit.app.App;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class TutorialActivity extends Activity {

	ImageView tutorialImg;
	Button tutorialDone;
	Button tutorialDone2;

	int width;
	final int TUTORIAL_WIDTH = 640;
	final int TUTORIAL_HEIGHT = 1525;

	boolean fromQuestions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		App.getInstance().addActivity(this);

		setContentView(R.layout.tutorial);

		DisplayMetrics dMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		width = dMetrics.widthPixels;

		fromQuestions = getIntent().getBooleanExtra("fromQuestions", false);

		tutorialImg = (ImageView) findViewById(R.id.tutorial_img);
		LayoutParams imgParams = new LayoutParams(width, TUTORIAL_HEIGHT
				* width / TUTORIAL_WIDTH);
		tutorialImg.setLayoutParams(imgParams);

		OnClickListener doneListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (fromQuestions) {
					finish();
				} else {
					Intent intent = new Intent();
					intent.setClass(TutorialActivity.this, MainActivity.class);
					startActivity(intent);
				}
			}
		};
		tutorialDone = (Button) findViewById(R.id.tutorial_done);
		tutorialDone2 = (Button) findViewById(R.id.tutorial_done2);
		tutorialDone.setOnClickListener(doneListener);
		tutorialDone2.setOnClickListener(doneListener);
	}

}
