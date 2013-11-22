package com.yaponit.activity;

import com.example.yaponit.R;
import com.yaponit.app.App;
import com.yaponit.app.AppConfig;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends Activity {

	TextView firstNameText;
	Button noThanksButton;
	Button tutorialButton;

	boolean fromQuestions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		App.getInstance().addActivity(this);

		fromQuestions = getIntent().getBooleanExtra("fromQuestions", false);
		setContentView(R.layout.welcome);
		firstNameText = (TextView) findViewById(R.id.firstname);
		firstNameText
				.setText(AppConfig.FirstName + getString(R.string.welcome));
		noThanksButton = (Button) findViewById(R.id.no_thanks_button);
		noThanksButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences sp = WelcomeActivity.this
						.getSharedPreferences("welcome", MODE_PRIVATE);
				boolean needTutorial = sp.getBoolean("needTutorial", true);
				if(needTutorial){
					Editor editor = sp.edit();
					editor.putBoolean("needTutorial", false);
					editor.commit();
				}
				if (fromQuestions) {
					finish();
				} else {
					Intent intent = new Intent();
					intent.setClass(WelcomeActivity.this, MainActivity.class);
					startActivity(intent);
				}
			}
		});

		tutorialButton = (Button) findViewById(R.id.tutorial_button);
		tutorialButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("fromQuestions", fromQuestions);
				intent.setClass(WelcomeActivity.this, TutorialActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

}
