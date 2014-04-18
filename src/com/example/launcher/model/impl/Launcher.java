package com.example.launcher.model.impl;

import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.swt.graphics.Image;

import com.example.launcher.model.ILauncher;

public class Launcher implements ILauncher {
	private ILaunchConfigurationType type;

	public Launcher(ILaunchConfigurationType type) {
		this.type = type;
	}

	public ILaunchConfigurationType getType() {
		return type;
	}

	public void setType(ILaunchConfigurationType type) {
		this.type = type;
	}

	@Override
	public String getName() {
		return type.getName();
	}

	@Override
	public Image getImage() {
		return DebugUITools.getImage(type.getIdentifier());
	}
}
