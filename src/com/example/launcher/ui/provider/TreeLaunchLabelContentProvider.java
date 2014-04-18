package com.example.launcher.ui.provider;

import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import com.example.launcher.model.ILauncerCategories;

public class TreeLaunchLabelContentProvider implements ITreeContentProvider {

	private final Set<ILauncerCategories> categories;

	public TreeLaunchLabelContentProvider(Set<ILauncerCategories> categories, TreeViewer viewer) {
		this.categories = categories;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return categories == null ? new Object[0] : categories.toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ILauncerCategories) {
			return ((ILauncerCategories) parentElement).getItems().toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof ILauncerCategories) {
			return !((ILauncerCategories) element).getItems().isEmpty();
		}
		return false;
	}

}
