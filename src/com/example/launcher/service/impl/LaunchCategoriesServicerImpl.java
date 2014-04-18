package com.example.launcher.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;

import com.example.launcher.Activator;
import com.example.launcher.model.ILauncerCategories;
import com.example.launcher.model.ILaunchItem;
import com.example.launcher.model.impl.LaunchItem;
import com.example.launcher.model.impl.LauncherCategories;
import com.example.launcher.service.ILaunchCategoriesService;
import com.example.launcher.service.ILaunchManagerService;

public class LaunchCategoriesServicerImpl implements ILaunchCategoriesService {

	private final ILaunchManagerService launchManagerService = new LaunchManagerServiceImpl();

	private boolean isCurrentType(ILaunchConfigurationType type) {
		return type.getIdentifier().equals(Activator.PLUGIN_ID);
	}

	@Override
	public Set<ILauncerCategories> getCategories() throws CoreException {
		HashMap<ILaunchConfigurationType, Set<ILaunchItem>> map = new HashMap<ILaunchConfigurationType, Set<ILaunchItem>>();
		for (ILaunchConfiguration launchConfig : launchManagerService.getLaunchConfigurations()) {
			if (isCurrentType(launchConfig.getType()))
				continue;
			Set<ILaunchItem> items = map.get(launchConfig.getType());
			if (items == null) {
				items = new HashSet<ILaunchItem>();
				map.put(launchConfig.getType(), items);
			}
			items.add(new LaunchItem(launchConfig));
		}
		Set<ILauncerCategories> categories = new HashSet<ILauncerCategories>(map.size());
		for (Entry<ILaunchConfigurationType, Set<ILaunchItem>> entry : map.entrySet()) {
			categories.add(new LauncherCategories(entry.getKey(), entry.getValue()));
		}
		return categories;
	}
}
