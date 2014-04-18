package com.example.launcher.model.impl;

import java.util.Set;

import org.eclipse.debug.core.ILaunchConfigurationType;

import com.example.launcher.model.ILauncerCategories;
import com.example.launcher.model.ILaunchItem;

public class LauncherCategories extends Launcher implements ILauncerCategories {

	private Set<ILaunchItem> items;

	public LauncherCategories(ILaunchConfigurationType type, Set<ILaunchItem> items) {
		super(type);
		this.items = items;
	}

	public Set<ILaunchItem> getItems() {
		return items;
	}

	public void setItems(Set<ILaunchItem> items) {
		this.items = items;
	}

}
