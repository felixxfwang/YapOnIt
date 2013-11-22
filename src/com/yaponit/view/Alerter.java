package com.yaponit.view;

import com.example.yaponit.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Alerter {
	static AlertDialog alerter;

	public static void show(Context context, String title, String message) {
		alerter = new Builder(context).create();
		alerter.show();
		Window window = alerter.getWindow();
		View view = View.inflate(context, R.layout.alert_dialog, null);
		TextView titleView = (TextView) view.findViewById(R.id.alert_title);
		titleView.setText(title);
		TextView textView = (TextView) view.findViewById(R.id.alert_text);
		textView.setText(message);
		window.setContentView(view);
	}

	public static void dismiss() {
		alerter.dismiss();
	}

	public static void showDialog(Context context, String title, String text) {
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.show();
		Window window = dlg.getWindow();
		View view = View.inflate(context, R.layout.alert_dialog_ok_only, null);
		TextView titleView = (TextView) view.findViewById(R.id.alert_title);
		titleView.setText(title);
		TextView textView = (TextView) view.findViewById(R.id.alert_text);
		textView.setText(text);
		Button ok = (Button) view.findViewById(R.id.ok_button);
		ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dlg.dismiss();
			}
		});
		window.setContentView(view);
	}

	public static void showDialog(final Context context, String title, String text1,
			String text2) {
		final AlertDialog dlg = new AlertDialog.Builder(context).create();
		dlg.show();
		Window window = dlg.getWindow();
		View view = View.inflate(context, R.layout.alert_dialog_ok_and_cancel,
				null);
		TextView titleView = (TextView) view.findViewById(R.id.alert_title);
		titleView.setText(title);
		TextView textView = (TextView) view.findViewById(R.id.alert_text);
		textView.setText(text1);
		TextView textView2 = (TextView) view.findViewById(R.id.alert_text2);
		textView2.setText(text2);
		Button stay = (Button) view.findViewById(R.id.stay_button);
		stay.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dlg.dismiss();
			}
		});
		Button leave = (Button) view.findViewById(R.id.leave_button);
		leave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dlg.dismiss();
				((Activity)context).finish();
			}
		});
		window.setContentView(view);
	}
}
