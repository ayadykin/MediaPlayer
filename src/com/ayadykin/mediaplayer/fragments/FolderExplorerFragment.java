package com.ayadykin.mediaplayer.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ListFragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ayadykin.mediaplayer.R;
import com.ayadykin.mediaplayer.adapters.FolderAdapter;
import com.ayadykin.mediaplayer.model.Item;
import com.ayadykin.mediaplayer.utils.PreferenceUtils;

public class FolderExplorerFragment extends ListFragment {

	private File currentDir;
	private FolderAdapter adapter;
	private TextView currentDirTitle;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		currentDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		fill(currentDir);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.folder_list_fragment, container, false);
		currentDirTitle = (TextView) rootView.findViewById(R.id.currentDirTextView);

		Button buttonPlayFolder = (Button) rootView.findViewById(R.id.buttonPlayFolder);
		buttonPlayFolder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				PreferenceUtils utils = new PreferenceUtils(getActivity().getApplicationContext());
				utils.setPlaylistFromFolderPreferences(true);
				utils.setFolderPathPreferences(currentDir.getAbsolutePath());
				getFragmentManager().popBackStack();
				getFragmentManager().popBackStack();
			}
		});
		return rootView;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Item item = adapter.getItem(position);
		if (item.isFolder() && item.getPath() != null) {
			currentDir = new File(item.getPath());
			fill(currentDir);
		}
	}

	private void fill(File f) {
		File[] dirs = f.listFiles();
		currentDirTitle.setText(getResources().getText(R.string.current_dir) + f.getName());
		List<Item> dir = new ArrayList<Item>();
		List<Item> fls = new ArrayList<Item>();
		try {
			for (File ff : dirs) {
				if (ff.isDirectory()) {
					dir.add(new Item(ff.getName(), ff.getAbsolutePath(), true));
				} else {
					fls.add(new Item(ff.getName(), ff.getAbsolutePath(), false));
				}
			}
		} catch (Exception e) {

		}
		Collections.sort(dir);
		Collections.sort(fls);
		dir.addAll(fls);
		if (!f.getName().equalsIgnoreCase(
				Environment.getExternalStorageDirectory().getAbsolutePath())) {
			dir.add(0, new Item(".. " + getResources().getText(R.string.parent_dir), f.getParent(),
					true));
		}
		adapter = new FolderAdapter(getActivity().getApplicationContext(),
				R.layout.folder_item_layout, dir);
		setListAdapter(adapter);
	}
}
