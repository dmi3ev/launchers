package com.example.launcher.model;

import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Image;

public interface ILaunchTableEditors {

	TableEditor getEditorNew();

	void setEditorNew(TableEditor editorNew);

	void addRowEditors(IRowEditors rowEtitor);

	void remove(int index);

	void moveUp(int index);

	void moveDown(int index);

	void updateRow(int index, String name, Image image);

}