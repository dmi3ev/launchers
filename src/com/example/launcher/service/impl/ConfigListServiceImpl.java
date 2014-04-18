package com.example.launcher.service.impl;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import com.example.launcher.Activator;
import com.example.launcher.model.IConfigList;
import com.example.launcher.model.ILaunchItem;
import com.example.launcher.model.impl.ConfigList;
import com.example.launcher.model.impl.LaunchItem;
import com.example.launcher.service.IConfigListService;
import com.example.launcher.service.ILaunchManagerService;

public class ConfigListServiceImpl implements IConfigListService {

	private ILaunchManagerService launchManagerService = new LaunchManagerServiceImpl();

	private static final String ATTR_NAME = Activator.PLUGIN_ID + ".NAME[%s]";
	private static final String ATTR_DELAY = Activator.PLUGIN_ID + ".DELAY[%s]";

	@Override
	public IConfigList load(ILaunchConfiguration launchConfig) {
		try {
			ILaunchConfiguration[] items = launchManagerService.getLaunchConfigurations();

			IConfigList configuration = new ConfigList();

			int i = 0;
			while (launchConfig.hasAttribute(String.format(ATTR_NAME, i))) {

				String name = launchConfig.getAttribute(String.format(ATTR_NAME, i), "");
				int delay = launchConfig.getAttribute(String.format(ATTR_DELAY, i), 0);

				ILaunchConfiguration launchConfiguration = null;
				for (ILaunchConfiguration item : items) {
					if (item.getName().equals(name)) {
						launchConfiguration = item;
						break;
					}
				}

				configuration.addItem(new LaunchItem(launchConfiguration, delay));
				i++;
			}
			return configuration;
		} catch (CoreException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
			Activator.getDefault().getLog().log(status);
		}
		return null;
	}

	@Override
	public void save(IConfigList configuration, ILaunchConfigurationWorkingCopy launchConfig) {
		try {
			removeAttibutes(launchConfig);
			int i = 0;
			for (ILaunchItem item : configuration.getLaunchItems()) {
				launchConfig.setAttribute(String.format(ATTR_NAME, i), item.getName());
				launchConfig.setAttribute(String.format(ATTR_DELAY, i), item.getDelay());
				i++;
			}
		} catch (CoreException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
			Activator.getDefault().getLog().log(status);
		}
	}

	private void removeAttibutes(ILaunchConfigurationWorkingCopy configuration) throws CoreException {
		for (Object attr : configuration.getAttributes().keySet()) {
			configuration.removeAttribute(attr.toString());
		}
	}

}
