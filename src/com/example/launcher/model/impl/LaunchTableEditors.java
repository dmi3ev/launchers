package com.example.launcher.model.impl;

import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableItem;

import com.example.launcher.model.ILaunchTableEditors;
import com.example.launcher.model.IRowEditors;

public class LaunchTableEditors implements ILaunchTableEditors {

	private ItemList<IRowEditors> listRowEditors = new ItemList<IRowEditors>();

	private TableEditor editorNew;

	public TableEditor getEditorNew() {
		return editorNew;
	}

	public void setEditorNew(TableEditor editorNew) {
		this.editorNew = editorNew;
	}

	public void addRowEditors(IRowEditors rowEtitor) {
		int size = listRowEditors.size();
		if (size > 0)
			listRowEditors.get(size - 1).setEnableButtonDown();
		listRowEditors.add(rowEtitor);
	}

	public void remove(int index) {
		listRowEditors.get(index).disposeAll();

		listRowEditors.remove(index);

		for (int i = index; i < listRowEditors.size(); i++) {
			listRowEditors.get(i).updateIndex(i);
		}
	}

	private void exchangeRow(int indexUp, int indexDown) {
		TableItem[] tableItems = new TableItem[2];
		tableItems[0] = listRowEditors.get(indexDown).getTableItem();
		tableItems[1] = listRowEditors.get(indexUp).getTableItem();

		listRowEditors.move(indexUp, indexDown);

		listRowEditors.get(indexDown).updateItemAndindex(indexDown, tableItems[0]);
		listRowEditors.get(indexUp).updateItemAndindex(indexUp, tableItems[1]);
	};

	public void moveUp(int index) {
		if (index == 0)
			return;
		exchangeRow(index, index - 1);
	}

	public void moveDown(int index) {
		if (index >= listRowEditors.size() - 1)
			return;
		exchangeRow(index + 1, index);
	}

	public void updateRow(int index, String name, Image image) {
		listRowEditors.get(index).setLabels(name, image);
	}
}
