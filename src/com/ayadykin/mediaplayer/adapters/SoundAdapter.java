package com.ayadykin.mediaplayer.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ayadykin.mediaplayer.R;
import com.ayadykin.mediaplayer.model.Sound;

public class SoundAdapter extends ArrayAdapter<Sound> {

	private Context context;
	private int resourceId;
	private List<Sound> soundList;

	public SoundAdapter(Context context, int resourceId, List<Sound> soundList) {
		super(context, resourceId, soundList);
		this.soundList = soundList;
		this.context = context;
		this.resourceId = resourceId;
	}

	@Override
	public Sound getItem(int i) {
		return soundList.get(i);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		if (view == null) {
			LayoutInflater layout = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layout.inflate(resourceId, null);
		}
		final Sound sound = soundList.get(position);
		if (soundList != null) {
			((TextView) view.findViewById(R.id.soundTitleText)).setText(sound.getTitle());
			((TextView) view.findViewById(R.id.soundArtistText)).setText(sound.getArtist());
			((TextView) view.findViewById(R.id.soundAlbumText)).setText(sound.getAlbum());
		}

		return view;
	}
	public void setSoundList(List<Sound> soundList) {
		this.soundList = soundList;
	}
}
