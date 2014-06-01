package com.ayadykin.mediaplayer.fragments;

import java.util.List;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.ayadykin.mediaplayer.R;
import com.ayadykin.mediaplayer.activity.MainActivity;
import com.ayadykin.mediaplayer.adapters.SoundAdapter;
import com.ayadykin.mediaplayer.model.Sound;
import com.ayadykin.mediaplayer.resources.DataSourceManager;
import com.ayadykin.mediaplayer.utils.PreferenceUtils;

public class SoundListFragment extends ListFragment implements OnClickListener {

	private final String SOUND_ID = "soundId";
	private final String SOUND_PATH = "soundPath";
	private SoundAdapter adapter;
	private PlayerFragment playerFragment = new PlayerFragment();
	private Context context;
	private SearchView titltSearchView;
	private SearchView artistSearchView;
	private SearchView albumSearchView;


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		context = getActivity().getApplicationContext();
		PreferenceUtils utils = new PreferenceUtils(context);
		List<Sound> soundList = null;
		Bundle extras = getArguments();

		if (utils.getPlayerSourcePreferences().equals(MainActivity.FROM_NOTIFICATION)) {
			Integer soundId = extras.getInt(SOUND_ID);
			soundList = DataSourceManager.getInstance(context).getSoundList();
			goToPlayerFragment(soundId);
		} else if (utils.getPlayerSourcePreferences().equals(MainActivity.FROM_FILE)) {
			String filePath = extras.getString(SOUND_PATH);
			soundList = DataSourceManager.getInstance(context).getMediaFromFile(filePath);
			goToPlayerFragment(0);
		} else if (utils.getPlayerSourcePreferences().equals(MainActivity.NONE)) {
			soundList = DataSourceManager.getInstance(context).getSoundList();
		}
		utils.setPlayerSourcePreferences(MainActivity.NONE);

		adapter = new SoundAdapter(context, R.layout.sound_item_layout, soundList);
		setListAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.sound_list_fragment, container, false);

		titltSearchView = (SearchView) rootView.findViewById(R.id.searchViewTitle);
		artistSearchView = (SearchView) rootView.findViewById(R.id.SearchViewArtist);
		albumSearchView = (SearchView) rootView.findViewById(R.id.SearchViewAlbum);
		initSearch();

		Button resetSearch = (Button) rootView.findViewById(R.id.buttonResetSearch);
		Button resetFolder = (Button) rootView.findViewById(R.id.buttonResetFolder);
		resetSearch.setOnClickListener(this);
		resetFolder.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonResetSearch:
			List<Sound> soundList = DataSourceManager.getInstance(context).resetSearch();
			updateAdapter(soundList);
			break;
		case R.id.buttonResetFolder:
			List<Sound> fullSoundList = DataSourceManager.getInstance(context).getAllMedia();
			updateAdapter(fullSoundList);
			break;
		default:
			break;
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		Bundle args = new Bundle();
		args.putInt(SOUND_ID, position);
		playerFragment.setArguments(args);

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container, playerFragment, "Player");
		transaction.addToBackStack(null);
		transaction.commit();
	}

	private void goToPlayerFragment(int soundId) {
		Bundle args = new Bundle();
		args.putInt(SOUND_ID, soundId);
		playerFragment.setArguments(args);

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container, playerFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	private void updateAdapter(List<Sound> soundList) {
		adapter.setSoundList(soundList);
		adapter.notifyDataSetChanged();
	}

	private void initSearch() {
		titltSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				List<Sound> soundList = DataSourceManager.getInstance(context).searchFilter(
						MediaStore.Audio.Media.TITLE, query);
				titltSearchView.clearFocus();
				updateAdapter(soundList);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
		artistSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				List<Sound> soundList = DataSourceManager.getInstance(context).searchFilter(
						MediaStore.Audio.Media.ARTIST, query);
				titltSearchView.clearFocus();
				updateAdapter(soundList);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
		albumSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				List<Sound> soundList = DataSourceManager.getInstance(context).searchFilter(
						MediaStore.Audio.Media.ALBUM, query);
				titltSearchView.clearFocus();
				updateAdapter(soundList);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
	}
}
