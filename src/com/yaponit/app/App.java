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

	// 单例模式中获取唯一的App实例
	public static App getInstance() {
		if (null == instance) {
			instance = new App();
		}
		return instance;

	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity并finish

	public void exit(Context context) {
		// 停止网络监听服务
//		Intent intent = new Intent(context, ListenNetStateService.class);
//		stopService(intent);

		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
}
