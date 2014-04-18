package com.example.launcher.model.impl;

import java.util.List;

import com.example.launcher.model.IConfigList;
import com.example.launcher.model.ILaunchItem;

public class ConfigList implements IConfigList {

	private final ItemList<ILaunchItem> listLaunchItem = new ItemList<ILaunchItem>();

	@Override
	public List<ILaunchItem> getLaunchItems() {
		return listLaunchItem;
	}

	@Override
	public void addItem(ILaunchItem item) {
		listLaunchItem.add(item);
	}

	@Override
	public void moveUp(int index) {
		listLaunchItem.moveUp(index);
	}

	@Override
	public void moveDown(int index) {
		listLaunchItem.moveDown(index);
	}

	@Override
	public void remove(int index) {
		listLaunchItem.remove(index);
	}

	@Override
	public void setItem(int index, ILaunchItem launchItem) {
		listLaunchItem.set(index, launchItem);
	}

	@Override
	public ILaunchItem getLaunchItem(int index) {
		return listLaunchItem.get(index);
	}

	@Override
	public int getSize() {
		return listLaunchItem.size();
	}

}