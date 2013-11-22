package com.yaponit.activity;

import com.example.yaponit.R;
import com.yaponit.app.App;
import com.yaponit.app.AppConfig;
import com.yaponit.entity.LoginUser;
import com.yaponit.utils.WebApiInteractor;
import com.yaponit.view.Alerter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class LoginActivity extends Activity {

	ImageView image = null;
	EditText userName = null;
	EditText password = null;
	ImageButton loginButton = null;

	WebApiInteractor api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);

		setContentView(R.layout.login);

		image = (ImageView) findViewById(R.id.login_logo);
		userName = (EditText) findViewById(R.id.user_name);
		password = (EditText) findViewById(R.id.password);
		loginButton = (ImageButton) findViewById(R.id.login_button);

		api = new WebApiInteractor(this);
		SharedPreferences sp = this.getSharedPreferences("account",
				MODE_PRIVATE);
		String uName = sp.getString("userName", "none");
		String pwd = sp.getString("password", "none");
		if (uName != null && pwd != null && !uName.equals("none")
				&& !pwd.equals("none")) {
			userName.setText(uName);
			password.setText(pwd);
			Alerter.show(LoginActivity.this, "Login", "Logging in...");
			new LoginWithoutInputTask().execute(uName, pwd);
		}

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// hide soft keyboard
				final InputMethodManager imm = (InputMethodManager) LoginActivity.this
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(userName.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(password.getWindowToken(), 0);

				String uN = userName.getText().toString();
				String p = password.getText().toString();
				// check weather the user name and password is empty
				if (uN.equals("") || p.equals("")) {
					Alerter.showDialog(LoginActivity.this, "Login",
							getString(R.string.no_username_and_password));
					return;
				}

				Alerter.show(LoginActivity.this, "Login", "Logging in...");

				if (api.preRequest()) {
					new LoginTask().execute(uN, p);
				}
				// nextActivity();
			}
		});

	}

	private void nextActivity() {
		SharedPreferences sp = this.getSharedPreferences("welcome",
				MODE_PRIVATE);
		Intent intent = new Intent();
		if (sp.getBoolean("needTutorial", true)) {
			intent.setClass(this, WelcomeActivity.class);
		} else {
			intent.setClass(this, MainActivity.class);
		}
		startActivity(intent);
	}

	class LoginTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			LoginUser user = api.login(params[0], params[1]);
			if (user != null) {
				AppConfig.FirstName = user.getFirstName();
				return user.getToken();
			}
			return null;
		}

		protected void onPostExecute(String token) {
			Alerter.dismiss();

			if (token != null) {
				AppConfig.AccessToken = token;
				SharedPreferences sp = LoginActivity.this.getSharedPreferences(
						"account", MODE_PRIVATE);
				Editor editor = sp.edit();
				editor.putString("userName", userName.getText().toString());
				editor.putString("password", password.getText().toString());
				editor.commit();
				nextActivity();
			} else {
				Alerter.showDialog(LoginActivity.this, LoginActivity.this
						.getString(R.string.login_failed_title),
						LoginActivity.this.getString(R.string.login_failed));
			}
		}

	}

	class LoginWithoutInputTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			LoginUser user = api.login(params[0], params[1]);
			if (user != null) {
				AppConfig.FirstName = user.getFirstName();
				return user.getToken();
			}
			return null;
		}

		protected void onPostExecute(String token) {
			Alerter.dismiss();
			if (token != null) {
				AppConfig.AccessToken = token;
				nextActivity();
			} else {
				Alerter.showDialog(LoginActivity.this, LoginActivity.this
						.getString(R.string.login_failed_title),
						LoginActivity.this.getString(R.string.login_failed));
			}
		}
	}
}
