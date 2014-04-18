package com.example.launcher;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

import com.example.launcher.model.IConfigList;
import com.example.launcher.model.ILaunchItem;
import com.example.launcher.service.IConfigListService;
import com.example.launcher.service.impl.ConfigListServiceImpl;

public class LaunchDelegate extends LaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {

		IConfigListService configurationService = new ConfigListServiceImpl();
		IConfigList configList = configurationService.load(configuration);

		monitor.beginTask("Start", configList.getLaunchItems().size());
		List<ILaunch> launched = new ArrayList<ILaunch>(configList.getLaunchItems().size());
		for (ILaunchItem item : configList.getLaunchItems()) {
			if (monitor.isCanceled()) {
				break;
			}
			try {
				if (item.isValid()) {
					if (!this.preLaunchCheck(item.getLaunchConfiguration(), mode, monitor)) {
						throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, String.format(
								"Configuration %s not passed prelaunch check", item.getName())));
					}

					if (item.getDelay() > 0)
						try {
							Thread.sleep(item.getDelay() * 1000);
						} catch (InterruptedException e) {
							IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
							Activator.getDefault().getLog().log(status);
						}

					SubProgressMonitor subMonitor = new SubProgressMonitor(monitor, 0);

					launched.add(item.getLaunchConfiguration().launch(mode, subMonitor));

					monitor.subTask(String.format("Complite %s", item.getName()));

					monitor.worked(launched.size());
				}
			} catch (CoreException ex) {
				terminateLaunched(launched);
				throw ex;
			}
		}
		monitor.setCanceled(true);
	}

	private void terminateLaunched(List<ILaunch> launched) {
		try {
			for (ILaunch launch : launched) {
				for (IProcess process : launch.getProcesses()) {
					if (!process.isTerminated()) {
						process.terminate();
					}
					launch.removeProcess(process);
				}
				for (IDebugTarget debugTarget : launch.getDebugTargets()) {
					launch.removeDebugTarget(debugTarget);
				}
				if (launch.canTerminate())
					launch.terminate();
			}
		} catch (DebugException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
			Activator.getDefault().getLog().log(status);
		}
	}
}
