package com.example.launcher.ui.provider;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.example.launcher.model.ILauncher;

public class TreeLaunchItemLabelProvider extends LabelProvider {
	@Override
	public String getText(Object element) {
		return ((ILauncher) element).getName();
	}

	@Override
	public Image getImage(Object element) {
		ILauncher name = (ILauncher) element;
		return name.getImage();
	}
}
