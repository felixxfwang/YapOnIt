package com.yaponit.app;

import java.util.LinkedList;
import java.util.List;

import com.yaponit.activity.MainActivity;
import com.yaponit.service.ListenNetStateService;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class App extends Application {

	private List<Activity> activityList = new LinkedList<Activity>();
	private static App instance;

	private App() {
	}

	// ����ģʽ�л�ȡΨһ��Appʵ��
	public static App getInstance() {
		if (null == instance) {
			instance = new App();
		}
		return instance;

	}

	// ���Activity��������
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// ��������Activity��finish

	public void exit(Context context) {
		// ֹͣ�����������
//		Intent intent = new Intent(context, ListenNetStateService.class);
//		stopService(intent);

		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
}
