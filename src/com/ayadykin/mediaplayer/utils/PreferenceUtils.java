package com.ayadykin.mediaplayer.utils;

import com.ayadykin.mediaplayer.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

	private final String PLAYER_SOURCE = "playerSource";
	private final String PLAYLIST_SOURCE = "playlistSource";
	private final String FOLDER_PATH = "folderPath";
	private SharedPreferences sharedPreferences;
	private Context context;

	public PreferenceUtils(Context context) {
		this.context = context;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public void setPlaylistFromFolderPreferences(boolean isFolder) {
		sharedPreferences.edit().putBoolean(PLAYLIST_SOURCE, isFolder).commit();
	}

	public boolean isPlaylistFromFolderPreferences() {
		return sharedPreferences.getBoolean(PLAYLIST_SOURCE, false);
	}

	public void setFolderPathPreferences(String path) {
		sharedPreferences.edit().putString(FOLDER_PATH, path).commit();
	}

	public String getFolderPathPreferences() {
		return sharedPreferences.getString(FOLDER_PATH, "");
	}

	public void setPlayerSourcePreferences(String source) {
		sharedPreferences.edit().putString(PLAYER_SOURCE, source).commit();
	}

	public String getPlayerSourcePreferences() {
		return sharedPreferences.getString(PLAYER_SOURCE, "");
	}
	public String getPlayModePreferences() {
		return sharedPreferences.getString(context.getResources().getString(R.string.play_mode), "1");
	}
}
