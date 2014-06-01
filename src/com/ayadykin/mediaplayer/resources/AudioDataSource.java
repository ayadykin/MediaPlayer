package com.ayadykin.mediaplayer.resources;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.ayadykin.mediaplayer.model.Sound;

public class AudioDataSource {

	private final Uri externalUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	private final Uri internalUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
	private List<Sound> soundList = new ArrayList<Sound>();;
	private Context context;
	public AudioDataSource(Context context) {
		this.context = context;
	}

	public List<Sound> getAllMedia(boolean searchFilter, String searchSelection,
			String arg) {
		soundList.clear();
		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
		String[] selectionArgs = null;

		if (searchFilter) {
			selection += searchSelection;
			selectionArgs = new String[] { arg };
		}

		Cursor cursor = context.getContentResolver().query(externalUri, null, selection,
				selectionArgs, null);
		fillSoundList(cursor);
		cursor = context.getContentResolver().query(internalUri, null, selection, selectionArgs,
				null);
		fillSoundList(cursor);
		return soundList;
	}

	public List<Sound> getMediaFromFolder(String path, boolean searchFilter,
			String searchSelection, String arg) {

		soundList.clear();

		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0" + " AND "
				+ MediaStore.Audio.Media.DATA + " LIKE ?" + " AND " + MediaStore.Audio.Media.DATA
				+ " NOT LIKE ?";
		String[] selectionArgs = new String[] { path + "/%", path + "/%/%" };

		if (searchFilter) {
			selection += searchSelection;
			selectionArgs = new String[] { path + "/%", path + "/%/%", arg };
		}
		Uri uri = getUri(path);
		Cursor cursor = context.getContentResolver().query(uri, null, selection, selectionArgs,
				null);
		fillSoundList(cursor);
		return soundList;
	}

	public List<Sound> getMediaFromFile(String path) {
		soundList.clear();
		Uri uri = getUri(path);
		Cursor cursor = context.getContentResolver().query(
				uri,
				null,
				MediaStore.Audio.Media.IS_MUSIC + " != 0" + " AND " + MediaStore.Audio.Media.DATA
						+ "=?", new String[] { path }, null);
		fillSoundList(cursor);
		return soundList;
	}

	public List<Sound> getSoundList() {
		return soundList;
	}

	private void fillSoundList(Cursor cursor) {
		int Id = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
		int path = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
		int title = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
		int artist = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
		int album = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
		if (cursor.moveToFirst())
			do {
				Sound sound = new Sound();
				sound.setId(cursor.getString(Id));
				sound.setPath(cursor.getString(path));
				sound.setTitle(cursor.getString(title));
				sound.setArtist(cursor.getString(artist));
				sound.setAlbum(cursor.getString(album));
				soundList.add(sound);
			} while (cursor.moveToNext());
		cursor.close();
	}

	private Uri getUri(String path) {
		if (path.contains(Environment.getExternalStorageDirectory().getAbsolutePath())) {
			return externalUri;
		} else {
			return internalUri;
		}
	}
}
