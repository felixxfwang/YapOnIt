package com.yaponit.listener;

import com.yaponit.activity.LoginActivity;
import com.yaponit.view.Alerter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

public class MenuItemClickListener implements OnMenuItemClickListener {

	Context m_context;

	public MenuItemClickListener(Context context) {
		m_context = context;
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
		default:
			Alerter.show(m_context,"Logout", "Logout...");
			new LogoutTask().execute();
		}
		return false;
	}

	class LogoutTask extends AsyncTask<Object,Void,Void>{


		@Override
		protected Void doInBackground(Object... arg0) {
			SharedPreferences sp = m_context.getSharedPreferences(
					"account", Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.remove("userName");
			editor.remove("password");
			editor.commit();
			
			Intent intent = new Intent();
			intent.setClass(m_context, LoginActivity.class);
			m_context.startActivity(intent);
			return null;
		}
		
		protected void onPostExecute(Void arg){
			Alerter.dismiss();
		}
		
	}
}
