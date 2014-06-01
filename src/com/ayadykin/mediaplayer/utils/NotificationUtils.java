package com.ayadykin.mediaplayer.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.ayadykin.mediaplayer.R;
import com.ayadykin.mediaplayer.activity.MainActivity;

public class NotificationUtils {

	private static final String SOUND_ID = "soundId";
	public static final int NOTIFY_ID = 1;
	public static final String NOTIFY = "Notification";
	private static NotificationUtils instance;
	private NotificationManager manager;
	private NotificationCompat.Builder nBuilder;
	private Context context;

	private NotificationUtils(Context context) {
		this.context = context;
		manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public static NotificationUtils getInstance(Context context) {
		if (instance == null) {
			instance = new NotificationUtils(context);
		} else {
			instance.context = context;
		}
		return instance;
	}

	public void setNotification(String title, int soundId) {

		nBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.directory_up)
				.setAutoCancel(true).setContentText(title)
				.setContentIntent(createPendingIntent(soundId));
		Notification notification = nBuilder.build();
		notification.flags = Notification.FLAG_NO_CLEAR;
		manager.notify(NOTIFY_ID, notification);
	}

	public void updateNotification(String title, int soundId) {
		nBuilder.setContentText(title).setContentIntent(createPendingIntent(soundId));
		Notification notification = nBuilder.build();
		notification.flags = Notification.FLAG_NO_CLEAR;
		manager.notify(NOTIFY_ID, notification);
	}

	public void deleteNotification() {
		manager.cancel(NOTIFY_ID);
	}

	private PendingIntent createPendingIntent(int soundId) {
		Intent property = new Intent(context, MainActivity.class);
		property.putExtra(SOUND_ID, soundId);
		property.putExtra(NOTIFY, true);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, property, 0);
		return contentIntent;
	}
}
