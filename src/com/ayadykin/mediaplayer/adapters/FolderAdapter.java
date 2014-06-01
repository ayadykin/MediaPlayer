package com.ayadykin.mediaplayer.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayadykin.mediaplayer.R;
import com.ayadykin.mediaplayer.model.Item;

public class FolderAdapter extends ArrayAdapter<Item> {

	private Context context;
	private int resourceId;
	private List<Item> folderList;

	public FolderAdapter(Context context, int textViewResourceId, List<Item> folderList) {
		super(context, textViewResourceId, folderList);
		this.context = context;
		resourceId = textViewResourceId;
		this.folderList = folderList;
	}

	public Item getItem(int i) {
		return folderList.get(i);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(resourceId, null);
		}

		final Item item = folderList.get(position);
		if (item != null) {
			TextView t1 = (TextView) view.findViewById(R.id.TextViewName);
			ImageView imageCity = (ImageView) view.findViewById(R.id.fd_Icon1);
			Drawable image;
			if (position == 0) {
				image = context.getResources().getDrawable(R.drawable.directory_up);
			} else if(item.isFolder()){
				image = context.getResources().getDrawable(R.drawable.directory_icon);
			}else{
				image = context.getResources().getDrawable(R.drawable.file_icon);
			}
			imageCity.setImageDrawable(image);

			t1.setText(item.getName());

		}
		return view;
	}
}
