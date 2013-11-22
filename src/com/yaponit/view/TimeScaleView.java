package com.yaponit.view;

import com.example.yaponit.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

public class TimeScaleView extends FrameLayout {
	Context context;

	TextView timeText;

	public TimeScaleView(Context context) {
		super(context);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.time_scale, this, true);
		timeText = (TextView) findViewById(R.id.time_scale_text);
	}
	
	public void setText(String time){
		timeText.setText(time);
	}

//	@Override
//	protected void onFinishInflate() {
//
//		timeText = new TextView(context);
//		timeText.setLayoutParams(new FrameLayout.LayoutParams(
//				FrameLayout.LayoutParams.WRAP_CONTENT,
//				FrameLayout.LayoutParams.WRAP_CONTENT));
//		timeText.setTextColor(Color.WHITE);
//		timeText.setSingleLine(true);
//		timeText.setText(time);
//		addView(timeText);
//	}
}
