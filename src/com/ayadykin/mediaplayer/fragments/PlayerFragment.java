package com.ayadykin.mediaplayer.fragments;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ayadykin.mediaplayer.R;
import com.ayadykin.mediaplayer.player.SoundPlayer.OnCompletionListener;
import com.ayadykin.mediaplayer.service.PlayerService;
import com.ayadykin.mediaplayer.utils.NotificationUtils;
import com.ayadykin.mediaplayer.utils.PreferenceUtils;

public class PlayerFragment extends Fragment implements OnClickListener {

	public static final int MODE_NONE = 1;
	public static final int MODE_RANDOM = 2;
	public static final int MODE_LOOP = 3;
	private final String SOUND_ID = "soundId";
	private TextView songTitle;
	private TextView spendTime;
	private TextView elapsedTime;
	private Button buttonPlay;
	private Button buttonPlayNext;
	private Button buttonPlayPrev;
	private Button buttonStop;
	private SeekBar seekBar;
	private Handler mHandler = new Handler();
	private PlayerService playerService;
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getActivity().getApplicationContext();
		int soundId = getArguments().getInt(SOUND_ID);
		Intent playerServiceIntent = new Intent(context, PlayerService.class);

		// Create and bind service
		if (PlayerService.playerServiceCreated) {
			if (playerService != null
					&& playerService.getSoundPlayer().getCurrentSongIndex() != soundId) {
				playerService.getSoundPlayer().playSound(soundId);
			}
		} else {
			playerServiceIntent.putExtra(SOUND_ID, soundId);
			context.startService(playerServiceIntent);
		}
		context.bindService(playerServiceIntent, mConnection, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.player_fragment, container, false);

		buttonPlay = (Button) rootView.findViewById(R.id.buttonPlay);
		buttonPlayNext = (Button) rootView.findViewById(R.id.buttonNext);
		buttonPlayPrev = (Button) rootView.findViewById(R.id.buttonPrev);
		buttonStop = (Button) rootView.findViewById(R.id.buttonStop);
		seekBar = (SeekBar) rootView.findViewById(R.id.seekBar1);
		songTitle = (TextView) rootView.findViewById(R.id.textViewSong);
		spendTime = (TextView) rootView.findViewById(R.id.textViewTimeSpend);
		elapsedTime = (TextView) rootView.findViewById(R.id.textViewTimeElapsed);
		buttonPlay.setOnClickListener(this);
		buttonPlayNext.setOnClickListener(this);
		buttonPlayPrev.setOnClickListener(this);
		buttonStop.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacks(updateUI);
		context.unbindService(mConnection);
		if (!playerService.getSoundPlayer().isPlaying()) {
			context.stopService(new Intent(context, PlayerService.class));
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.buttonPlay:
			playerStartOrPause();
			break;
		case R.id.buttonNext:
			buttonPlay.setBackgroundResource(R.drawable.button_pause);
			playerService.getSoundPlayer().playNext();
			updateTitle();
			break;
		case R.id.buttonPrev:
			buttonPlay.setBackgroundResource(R.drawable.button_pause);
			playerService.getSoundPlayer().playPreview();
			updateTitle();
			break;
		case R.id.buttonStop:
			buttonPlay.setBackgroundResource(R.drawable.button_play);
			playerService.getSoundPlayer().playerStop();
			break;
		default:
			break;
		}
	}

	private void playerStartOrPause() {
		if (!playerService.getSoundPlayer().isPlaying()) {
			buttonPlay.setBackgroundResource(R.drawable.button_pause);
			updateProgressBar();
		} else {
			buttonPlay.setBackgroundResource(R.drawable.button_play);
		}
		playerService.getSoundPlayer().playerStartOrPause();
	}

	private void updateProgressBar() {
		mHandler.postDelayed(updateUI, 100);
	}

	private void updateTitle() {
		String title = playerService.getSoundPlayer().getTitle();
		songTitle.setText(title);
		NotificationUtils.getInstance(context).updateNotification(title,
				playerService.getSoundPlayer().getCurrentSongIndex());
	}

	private OnCompletionListener completionListener = new OnCompletionListener() {
		@Override
		public void onCompletion() {
			updateTitle();
		}
	};
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			playerService = ((PlayerService.MyBinder) service).getService();
			seekBar.setMax(playerService.getSoundPlayer().getSoundLength());
			playerService.getSoundPlayer().setOnCompletionListener(completionListener);
			loadPlayModePreferences();
			updateTitle();
			updateProgressBar();
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			playerService = null;
		}
	};

	private void loadPlayModePreferences() {
		PreferenceUtils utils = new PreferenceUtils(context);
		String playMode = utils.getPlayModePreferences();

		switch (Integer.valueOf(playMode)) {
		case MODE_NONE:
			playerService.getSoundPlayer().setPlayMode(MODE_NONE);
			break;
		case MODE_RANDOM:
			playerService.getSoundPlayer().setPlayMode(MODE_RANDOM);
			break;
		case MODE_LOOP:
			playerService.getSoundPlayer().setPlayMode(MODE_LOOP);
			break;
		default:
			break;
		}
	}

	Runnable updateUI = new Runnable() {

		@Override
		public void run() {
			int currentPosition = playerService.getSoundPlayer().getCurrentPosition() / 1000;
			seekBar.setProgress(currentPosition);
			spendTime.setText(secondsToTime(currentPosition));
			elapsedTime.setText(secondsToTime(playerService.getSoundPlayer().getSoundLength()
					- currentPosition));
			mHandler.postDelayed(this, 100);
		}
	};

	private String secondsToTime(int seconds) {
		return String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
	}
}
