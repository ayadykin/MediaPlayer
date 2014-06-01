package com.ayadykin.mediaplayer.player;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import android.media.MediaPlayer;
import android.util.Log;

import com.ayadykin.mediaplayer.fragments.PlayerFragment;
import com.ayadykin.mediaplayer.model.Sound;

public class SoundPlayer extends MediaPlayer implements MediaPlayer.OnCompletionListener {

	private String title;
	private int duration;
	private int currentSongIndex;
	private List<Sound> soundList;
	private OnCompletionListener completionListener;
	private boolean isRepeat;
	private boolean isShuffle;

	public void playSound(int soundId) {
		try {
			if (soundList.size() > soundId) {
				reset();
				setDataSource(soundList.get(soundId).getPath());
				prepare();
				start();
				currentSongIndex = soundId;
				duration = getDuration() / 1000;
				title = soundList.get(soundId).getTitle();
				setOnCompletionListener(this);
			}
		} catch (IllegalArgumentException e) {
			Log.e("Error playSound", "IllegalArgumentException");
		} catch (IllegalStateException e) {
			Log.e("Error playSound", "IllegalStateException");
		} catch (IOException e) {
			Log.e("Error playSound", "IOException");
		}
	}

	public void playerStartOrPause() {
		if (!isPlaying()) {
			start();
		} else {
			pause();
		}
	}

	public void playNext() {
		if (isShuffle | isRepeat) {
			onCompletion(this);
		} else {
			if (currentSongIndex == soundList.size() - 1) {
				currentSongIndex = 0;
			} else {
				++currentSongIndex;
			}
			playSound(currentSongIndex);
		}
	}

	public void playPreview() {
		if (isShuffle | isRepeat) {
			onCompletion(this);
		} else {
			if (currentSongIndex == 0) {
				currentSongIndex = soundList.size() - 1;
			} else {
				--currentSongIndex;
			}
			playSound(currentSongIndex);
		}
	}

	public void playerStop() {
		if (isPlaying()) {
			stop();
			try {
				prepare();
			} catch (IllegalStateException e) {
				Log.e("Error playerStop", "IllegalStateException");
			} catch (IOException e) {
				Log.e("Error playerStop", "IOException");
			}
			seekTo(0);
		}
	}

	public String getTitle() {
		return title;
	}

	public int getSoundLength() {
		return duration;
	}

	public int getCurrentSongIndex() {
		return currentSongIndex;
	}

	public void setSoundList(List<Sound> soundList) {
		this.soundList = soundList;
	}

	public void setPlayMode(int mode) {

		switch (mode) {
		case PlayerFragment.MODE_NONE:
			isRepeat = false;
			isShuffle = false;
			break;
		case PlayerFragment.MODE_RANDOM:
			isShuffle = true;
			isRepeat = false;
			break;
		case PlayerFragment.MODE_LOOP:
			isRepeat = true;
			isShuffle = false;
			break;
		default:
			break;
		}
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		if (isRepeat) {
			setLooping(true);
			start();
		} else if (isShuffle) {
			if (soundList.size() >= 2) {
				currentSongIndex = new Random().nextInt(soundList.size() - 1);
			}
			setLooping(false);
			playSound(currentSongIndex);
		} else {
			setLooping(false);
			playNext();
		}
		completionListener.onCompletion();
	}

	public interface OnCompletionListener {
		public void onCompletion();
	}

	public void setOnCompletionListener(OnCompletionListener completionListener) {
		this.completionListener = completionListener;
	}
}
