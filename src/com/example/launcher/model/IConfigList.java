package com.example.launcher.model;

import java.util.List;

public interface IConfigList {
	List<ILaunchItem> getLaunchItems();

	void addItem(ILaunchItem item);

	void moveUp(int index);

	void moveDown(int index);

	void remove(int index);

	void setItem(int index, ILaunchItem launchItem);

	ILaunchItem getLaunchItem(int index);

	int getSize();

}