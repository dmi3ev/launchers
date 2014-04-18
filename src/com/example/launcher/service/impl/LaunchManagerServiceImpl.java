package com.example.launcher.service.impl;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;

import com.example.launcher.service.ILaunchManagerService;

public class LaunchManagerServiceImpl implements ILaunchManagerService {
	private final ILaunchManager manager;

	public LaunchManagerServiceImpl() {
		this.manager = DebugPlugin.getDefault().getLaunchManager();
	}

	@Override
	public ILaunchConfiguration[] getLaunchConfigurations() throws CoreException {
		return manager.getLaunchConfigurations();
	}
}
