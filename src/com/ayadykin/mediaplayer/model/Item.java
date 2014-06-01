package com.ayadykin.mediaplayer.model;

public class Item implements Comparable<Item> {
	private String name;
	private String path;
	private boolean isFolder;

	public Item(String name, String path, boolean isFolder) {
		this.name = name;
		this.path = path;
		this.isFolder = isFolder;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public boolean isFolder() {
		return isFolder;
	}

	public int compareTo(Item o) {
		if (this.name != null)
			return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
		else
			throw new IllegalArgumentException();
	}
}
