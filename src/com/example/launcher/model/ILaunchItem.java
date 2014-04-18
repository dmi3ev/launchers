package com.example.launcher.model;

import org.eclipse.debug.core.ILaunchConfiguration;

public interface ILaunchItem extends ILauncher {
	void setDelay(int delay);

	int getDelay();

	ILaunchConfiguration getLaunchConfiguration();

	boolean isValid();
}
