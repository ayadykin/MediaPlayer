package com.ayadykin.mediaplayer.resources;

import java.util.List;

import android.content.Context;

import com.ayadykin.mediaplayer.model.Sound;
import com.ayadykin.mediaplayer.utils.PreferenceUtils;

public class DataSourceManager {

	private static DataSourceManager dataSourceManager;
	private AudioDataSource audioDataSource;
	private PreferenceUtils utils;

	private DataSourceManager(Context context) {
		audioDataSource = new AudioDataSource(context);
		utils = new PreferenceUtils(context);
	}

	public static DataSourceManager getInstance(Context context) {
		if (dataSourceManager == null) {
			dataSourceManager = new DataSourceManager(context);
		}
		return dataSourceManager;
	}

	public List<Sound> searchFilter(String selection, String arg) {
		List<Sound> soundList = null;
		if (utils.isPlaylistFromFolderPreferences()) {
			String folderPath = utils.getFolderPathPreferences();
			soundList = audioDataSource.getMediaFromFolder(folderPath, true, " AND " + selection
					+ "=?", arg);
		} else {
			soundList = audioDataSource.getAllMedia(true, " AND " + selection + "=?", arg);
		}
		return soundList;
	}

	public List<Sound> resetSearch() {
		List<Sound> soundList = null;
		if (utils.isPlaylistFromFolderPreferences()) {
			String folderPath = utils.getFolderPathPreferences();
			soundList = audioDataSource.getMediaFromFolder(folderPath, false, null, null);
		} else {
			soundList = audioDataSource.getAllMedia(false, null, null);
		}
		return soundList;
	}

	public List<Sound> getAllMedia() {
		utils.setPlaylistFromFolderPreferences(false);
		return audioDataSource.getAllMedia(false, null, null);
	}

	public List<Sound> getMediaFromFolder() {
		utils.setPlaylistFromFolderPreferences(true);
		return audioDataSource.getMediaFromFolder(utils.getFolderPathPreferences(), false, null,
				null);
	}

	public List<Sound> getMediaFromFile(String path) {
		return audioDataSource.getMediaFromFile(path);
	}

	public List<Sound> getSoundList() {
		List<Sound> soundList = null;
		if (utils.isPlaylistFromFolderPreferences()) {
			soundList = getMediaFromFolder();
		} else {
			soundList = getAllMedia();
		}
		return soundList;
	}
	public List<Sound> getSoundListFromDataSource() {
		return audioDataSource.getSoundList();
	}
}
