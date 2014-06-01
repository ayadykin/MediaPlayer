package com.ayadykin.mediaplayer.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.ayadykin.mediaplayer.R;

public class PreferencesFragment extends PreferenceFragment {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.play_mode_pref);
	}
}
