package com.example.launcher.model;

import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;

public interface IRowEditors {

	void setLaunchImage(Label launchImage);

	void setLaunchName(Label launchName);

	void disposeAll();

	void layoutAll();

	void updateItemAndindex(int i, TableItem item);

	void setLabels(String name, Image image);

	void set(ColumnNames editors, TableEditor editor);

	TableItem getTableItem();

	void setEnableButtonDown();

	void updateIndex(int i);

}