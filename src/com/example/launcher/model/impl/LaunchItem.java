package com.example.launcher.model.impl;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.example.launcher.Activator;
import com.example.launcher.model.ILaunchItem;

public class LaunchItem implements ILaunchItem {
	private final static Image imageError = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
			"icons/error.ico").createImage();

	private ILaunchConfiguration launchConfiguration;
	private int delay = 0;

	public LaunchItem(ILaunchConfiguration launchConfiguration) {
		this.launchConfiguration = launchConfiguration;
	}

	public LaunchItem(ILaunchConfiguration launchConfiguration, int delay) {
		this(launchConfiguration);
		setDelay(delay);
	}

	@Override
	public ILaunchConfiguration getLaunchConfiguration() {
		return launchConfiguration;
	}

	@Override
	public String getName() {
		if (isValid())
			return launchConfiguration.getName();
		else
			return "";
	}

	@Override
	public boolean equals(Object obj) {
		if (isValid() && obj instanceof ILaunchConfiguration) {
			return getLaunchConfiguration().equals(obj);
		}
		return super.equals(obj);
	}

	@Override
	public Image getImage() {
		if (isValid())
			try {
				return DebugUITools.getImage(getLaunchConfiguration().getType().getIdentifier());
			} catch (CoreException e) {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
				Activator.getDefault().getLog().log(status);
				return null;
			}
		else {
			return imageError;
		}
	}

	@Override
	public boolean isValid() {
		return getLaunchConfiguration() != null;
	}

	@Override
	public void setDelay(int delay) {
		if (delay < 0)
			delay = 0;
		this.delay = delay;
	}

	@Override
	public int getDelay() {
		return delay;
	}
}
