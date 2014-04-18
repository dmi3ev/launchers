package com.example.launcher.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.example.launcher.model.IConfigList;
import com.example.launcher.ui.component.LaunchTable;
import com.example.launcher.ui.listener.ILaunchItemChangedListener;

public class LaunchPage extends Composite {
	private LaunchTable table;

	private ILaunchItemChangedListener changedListener;

	public LaunchPage(Composite parent) {
		super(parent, SWT.MULTI);
		this.setLayout(new FillLayout());
		table = new LaunchTable(this, SWT.BORDER | SWT.MULTI);
	}

	public IConfigList getConfigQueue() {
		return table.getConfigQueue();
	}

	public void setConfigList(IConfigList configList) {
		table.setConfigList(configList);
	}

	public ILaunchItemChangedListener getChangeListener() {
		return changedListener;
	}

	public void setChangedListener(ILaunchItemChangedListener changeListener) {
		this.changedListener = changeListener;
	}

	public void changedConfig() {
		if (changedListener != null) {
			changedListener.handler();
		}
	}
}