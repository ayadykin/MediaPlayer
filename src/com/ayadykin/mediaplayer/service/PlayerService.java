package com.ayadykin.mediaplayer.service;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.ayadykin.mediaplayer.model.Sound;
import com.ayadykin.mediaplayer.player.SoundPlayer;
import com.ayadykin.mediaplayer.resources.DataSourceManager;
import com.ayadykin.mediaplayer.utils.NotificationUtils;

public class PlayerService extends Service {

	private static final String SOUND_ID = "soundId";
	private final IBinder binder = new MyBinder();
	private SoundPlayer soundPlayer = new SoundPlayer();
	public static boolean playerServiceCreated;

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		NotificationUtils.getInstance(this).deleteNotification();
		soundPlayer.release();
		playerServiceCreated = false;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		List<Sound> soundList = DataSourceManager.getInstance(this).getSoundListFromDataSource();
		int position = intent.getIntExtra(SOUND_ID, 0);
		soundPlayer.setSoundList(soundList);
		soundPlayer.playSound(position);
		NotificationUtils.getInstance(this).setNotification(soundList.get(position).getTitle(),
				position);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		playerServiceCreated = true;
	}

	public class MyBinder extends Binder {
		public PlayerService getService() {
			return PlayerService.this;
		}
	}

	public SoundPlayer getSoundPlayer() {
		return soundPlayer;
	}
}
