package com.ayadykin.mediaplayer.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ayadykin.mediaplayer.R;
import com.ayadykin.mediaplayer.fragments.FolderExplorerFragment;
import com.ayadykin.mediaplayer.fragments.PreferencesFragment;
import com.ayadykin.mediaplayer.fragments.SoundListFragment;
import com.ayadykin.mediaplayer.utils.NotificationUtils;
import com.ayadykin.mediaplayer.utils.PreferenceUtils;

public class MainActivity extends Activity {

	private final String SOUND_ID = "soundId";
	private final String SOUND_PATH = "soundPath";
	public static final String FROM_NOTIFICATION = "fromNotification";
	public static final String FROM_FILE = "fromFile";
	public static final String NONE = "none";

	private SoundListFragment soundListFragment = new SoundListFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Choose source for activity start
		PreferenceUtils utils = new PreferenceUtils(this);
		Bundle args = new Bundle();
		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.getBoolean(NotificationUtils.NOTIFY)) {
			utils.setPlayerSourcePreferences(FROM_NOTIFICATION);
			int soundId = extras.getInt(SOUND_ID);
			args.putInt(SOUND_ID, soundId);
			Log.d("MainActivity", FROM_NOTIFICATION);
		} else if (getIntent().getData() != null) {
			utils.setPlayerSourcePreferences(FROM_FILE);
			String path = getIntent().getData().getEncodedPath();
			args.putString(SOUND_PATH, path);
			Log.d("MainActivity", FROM_FILE);
		} else {
			utils.setPlayerSourcePreferences(NONE);
			Log.d("MainActivity", NONE);
		}
		soundListFragment.setArguments(args);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.add(R.id.container, soundListFragment);
		transaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.open_folder:
			FragmentTransaction openFolderTransaction = getFragmentManager().beginTransaction();
			openFolderTransaction.replace(R.id.container, new FolderExplorerFragment());
			openFolderTransaction.addToBackStack(null);
			openFolderTransaction.commit();
			break;
		case R.id.change_orientation:
			int orientation = getResources().getConfiguration().orientation;
			switch (orientation) {
			case Configuration.ORIENTATION_PORTRAIT:
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				break;
			case Configuration.ORIENTATION_LANDSCAPE:
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				break;
			}
			break;
		case R.id.settings:
			FragmentTransaction playModeTransaction = getFragmentManager().beginTransaction();
			playModeTransaction.replace(R.id.container, new PreferencesFragment());
			playModeTransaction.addToBackStack(null);
			playModeTransaction.commit();
			break;
		}
		return true;
	}
}
