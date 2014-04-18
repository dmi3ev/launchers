package com.example.launcher.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.example.launcher.Activator;
import com.example.launcher.model.IConfigList;
import com.example.launcher.service.IConfigListService;
import com.example.launcher.service.impl.ConfigListServiceImpl;
import com.example.launcher.ui.listener.ILaunchItemChangedListener;

public class LaunchTab extends AbstractLaunchConfigurationTab {

	public static final String NAME = "Configurate Launchers";

	private static final String ICON = "icons/launch.ico";

	private LaunchPage launchPage;

	private IConfigListService configQueueService = new ConfigListServiceImpl();

	@Override
	public void createControl(Composite parent) {
		launchPage = new LaunchPage(parent);
		launchPage.setChangedListener(new ILaunchItemChangedListener() {
			@Override
			public void handler() {
				setDirty(true);
				updateLaunchConfigurationDialog();
			}
		});

		IStatus status = new Status(IStatus.INFO, Activator.PLUGIN_ID, "Start");
		Activator.getDefault().getLog().log(status);
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		IConfigList configQueue = configQueueService.load(configuration);
		launchPage.setConfigList(configQueue);
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configQueueService.save(launchPage.getConfigQueue(), configuration);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Image getImage() {
		return AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, ICON).createImage();
	}

	@Override
	public Control getControl() {
		return launchPage;
	}
}
