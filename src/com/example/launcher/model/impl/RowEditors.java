package com.example.launcher.model.impl;

import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.example.launcher.model.ColumnNames;
import com.example.launcher.model.IRowEditors;

public class RowEditors implements IRowEditors {

	private static final int COUNT_EDITORS = ColumnNames.values().length;

	private TableEditor[] tableEditors = new TableEditor[COUNT_EDITORS];

	private Label launchImage;
	private Label launchName;

	private Button up;
	private Button down;

	private Table table;

	public RowEditors(Table table) {
		super();
		this.table = table;
	}

	@Override
	public void setLaunchImage(Label launchImage) {
		this.launchImage = launchImage;
	}

	@Override
	public void setLaunchName(Label launchName) {
		this.launchName = launchName;
	}

	@Override
	public TableItem getTableItem() {
		return tableEditors[ColumnNames.delete.getIndex()].getItem();
	}

	@Override
	public void disposeAll() {
		for (int i = 0; i < tableEditors.length; i++) {
			tableEditors[i].getEditor().dispose();
		}
	}

	@Override
	public void layoutAll() {
		for (TableEditor tableEditor : tableEditors) {
			tableEditor.layout();
		}
	}

	private void updateStateButton(int i) {
		int last = table.getItemCount() - 2;
		up.setEnabled(i > 0);
		down.setEnabled(i != last);
	}

	@Override
	public void updateIndex(int i) {
		for (TableEditor tableEditor : tableEditors) {
			tableEditor.getEditor().setData(i);
		}
		updateStateButton(i);
	}

	@Override
	public void updateItemAndindex(int i, TableItem item) {
		for (TableEditor tableEditor : tableEditors) {
			tableEditor.setItem(item);
			tableEditor.getEditor().setData(i);
		}
		updateStateButton(i);
	}

	@Override
	public void setLabels(String name, Image image) {
		launchName.setText(name);
		launchImage.setImage(image);
	}

	@Override
	public void set(ColumnNames editors, TableEditor editor) {
		tableEditors[editors.getIndex()] = editor;
	}

	public void setButtonUp(Button button) {
		up = button;
	}

	public void setButtonDown(Button button) {
		down = button;
	}

	@Override
	public void setEnableButtonDown() {
		down.setEnabled(true);
	}

}
