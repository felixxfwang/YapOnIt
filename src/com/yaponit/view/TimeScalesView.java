package com.yaponit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class TimeScalesView extends HorizontalScrollView {

	public TimeScalesView(Context context) {
		super(context);
	}

	public TimeScalesView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TimeScalesView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean arrowScroll(int direction) {
		return super.arrowScroll(direction);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

}
