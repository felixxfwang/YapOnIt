package com.yaponit.activity;

import com.example.yaponit.R;
import com.yaponit.app.App;
import com.yaponit.listener.MenuItemClickListener;
import com.yaponit.utils.WebApiInteractor;
import com.yaponit.view.Alerter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TextAnswerActivity extends Activity {

	int postId;

	Button submit;
	Button back;
	EditText commentEditer;

	WebApiInteractor api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
		api = new WebApiInteractor(this);

		Bundle bundle = this.getIntent().getExtras();
		postId = bundle.getInt("id");

		setContentView(R.layout.text_answer);
		((TextView) findViewById(R.id.title)).setText(bundle
				.getString("content"));
		((TextView) findViewById(R.id.fan)).setText(bundle.getString("fan"));
		commentEditer = (EditText) findViewById(R.id.text_answer_editer);

		submit = (Button) findViewById(R.id.submit_button);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (commentEditer.getText().toString().equals("")) {
					Alerter.showDialog(TextAnswerActivity.this,
							getString(R.string.no_answer_title),
							getString(R.string.no_answer_text1),
							getString(R.string.no_answer_text2));
				} else {
					if (api.preRequest()) {
						new SendTextTask().execute();
						final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								commentEditer.getWindowToken(), 0);
						Alerter.show(TextAnswerActivity.this,
								getString(R.string.submit_title),
								getString(R.string.submit_text));
					}
				}
			}
		});
		back = (Button) findViewById(R.id.back_button);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!commentEditer.getText().toString().equals("")) {
					Alerter.showDialog(TextAnswerActivity.this,
							getString(R.string.forget_submit_title),
							getString(R.string.forget_submit_text1),
							getString(R.string.forget_submit_text2));
				} else {
					TextAnswerActivity.this.finish();
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add("Log out");
		menu.getItem(0).setOnMenuItemClickListener(
				new MenuItemClickListener(this));
		return true;
	}

	class SendTextTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			boolean success = sendTextResponse();
			return success;
		}

		protected void onPostExecute(Boolean success) {
			Alerter.dismiss();
			if (success) {
				Toast.makeText(TextAnswerActivity.this, "Send Success!",
						Toast.LENGTH_LONG).show();
				Intent intent = new Intent();  
                intent.putExtra("postId", postId);
                setResult(RESULT_OK, intent); 
				finish();
			} else {
				Toast.makeText(TextAnswerActivity.this, "Send Failed!",
						Toast.LENGTH_LONG).show();
			}

		}
	}

	private boolean sendTextResponse() {
		String content = commentEditer.getText().toString();
		return api.sendTextResponse(postId, 0, content);
	}
}
