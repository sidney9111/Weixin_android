package com.peiban.app;

import com.peiban.service.SnsService;

import android.app.NotificationManager;
import android.content.Context;

public abstract class AbstractNotifiy implements Notifiy{
	private NotificationManager notificationManager;
	private SnsService service;
	public AbstractNotifiy(SnsService context) {
		super();
		service = context;
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	public NotificationManager getNotificationManager() {
		return notificationManager;
	}

	public SnsService getService() {
		return service;
	}
	
	
}
