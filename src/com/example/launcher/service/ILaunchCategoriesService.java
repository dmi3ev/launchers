package com.example.launcher.service;

import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import com.example.launcher.model.ILauncerCategories;

public interface ILaunchCategoriesService {
	Set<ILauncerCategories> getCategories() throws CoreException;
}
