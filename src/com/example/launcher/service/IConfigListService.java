package com.example.launcher.service;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import com.example.launcher.model.IConfigList;

public interface IConfigListService {
	IConfigList load(ILaunchConfiguration config);

	void save(IConfigList configuration, ILaunchConfigurationWorkingCopy config);
}
