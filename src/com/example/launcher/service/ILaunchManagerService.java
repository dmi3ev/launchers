package com.example.launcher.service;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

public interface ILaunchManagerService {
	ILaunchConfiguration[] getLaunchConfigurations() throws CoreException;
}
