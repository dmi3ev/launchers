package com.example.launcher.ui.component;

import java.lang.reflect.Method;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.example.launcher.Activator;
import com.example.launcher.model.ColumnNames;
import com.example.launcher.model.IConfigList;
import com.example.launcher.model.ILauncerCategories;
import com.example.launcher.model.ILaunchItem;
import com.example.launcher.model.ILaunchTableEditors;
import com.example.launcher.model.impl.LaunchTableEditors;
import com.example.launcher.model.impl.RowEditors;
import com.example.launcher.service.ILaunchCategoriesService;
import com.example.launcher.service.impl.LaunchCategoriesServicerImpl;
import com.example.launcher.ui.LaunchPage;
import com.example.launcher.ui.listener.ISelectedLauncherListener;

public class LaunchTable {
	private final ILaunchCategoriesService launchItemService = new LaunchCategoriesServicerImpl();

	private LaunchPage parent;

	private IConfigList configList;

	private Table table;

	private final int countColumn = 4;
	private final int[] columnWidth = { 28, 300, 80, 100 };
	private final String[] columnTitles = { "", "Launcher Name", "Delay, sec.", "Move Up/Down" };

	private final ILaunchTableEditors launchTableEditors = new LaunchTableEditors();

	private final SelectionListener buttonDelListener = new ButtonDelListener();
	private final SelectionListener buttonEditListener = new ButtonEditListener();
	private final SelectionListener buttonUpListener = new ButtonUpListener();
	private final SelectionListener buttonDownListener = new ButtonDownListener();

	private final AddLauncher addLauncher = new AddLauncher();

	private final Image imageUp = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/up.ico")
			.createImage();
	private final Image imageDown = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/down.ico")
			.createImage();
	private final Image imageDel = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/del.ico")
			.createImage();
	private final Image imageNew = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/new.ico")
			.createImage();

	private AddLauncherDialog dialog;

	public LaunchTable(LaunchPage parent, int style) {
		this.parent = parent;

		Set<ILauncerCategories> categories = null;
		try {
			categories = launchItemService.getCategories();
		} catch (CoreException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
			Activator.getDefault().getLog().log(status);
		}
		dialog = new AddLauncherDialog(parent.getShell(), categories);

		table = new Table(parent, style);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.pack();
		try {
			Method setItemHeightMetod = table.getClass().getDeclaredMethod("setItemHeight", int.class);
			setItemHeightMetod.setAccessible(true);
			setItemHeightMetod.invoke(table, 26);
		} catch (Exception e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
			Activator.getDefault().getLog().log(status);
		}

		for (int i = 0; i < countColumn; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(columnTitles[i]);
			column.setWidth(columnWidth[i]);
			column.setResizable(i == 1);
		}

		launchTableEditors.setEditorNew(newButtonEditor());
	}

	public IConfigList getConfigQueue() {
		return configList;
	}

	public void setConfigList(IConfigList configList) {
		this.configList = configList;
		setNewTableItem(configList);
	}

	private void setNewTableItem(IConfigList configList) {
		int end = table.getItemCount() - 2;
		if (end >= 0)
			table.remove(0, end);
		for (ILaunchItem launchItem : configList.getLaunchItems()) {
			addTableItem(launchItem);
		}
	}

	private void addTableItem(ILaunchItem launchItem) {
		int oldLastIndex = table.getItemCount() - 1;
		TableItem item = new TableItem(table, SWT.NONE);
		launchTableEditors.getEditorNew().setItem(item);
		createRowEditors(table.getItem(oldLastIndex));
	}

	private TableEditor newButtonEditor() {
		TableItem item = new TableItem(table, SWT.NONE);

		TableEditor editor = new TableEditor(table);
		Button button = new Button(table, SWT.FLAT);
		button.setImage(imageNew);
		button.addSelectionListener(new ButtonAddListener());

		editor.minimumWidth = columnWidth[0];
		editor.grabHorizontal = true;
		editor.setEditor(button, item, 0);

		return editor;
	}

	private void createRowEditors(TableItem item) {
		final Integer index = table.indexOf(item);
		final ILaunchItem launchItem = configList.getLaunchItem(index);
		final Table table = item.getParent();

		RowEditors rowEditors = new RowEditors(table);

		TableEditor editor = new TableEditor(table);

		Button button = new Button(table, SWT.FLAT);
		button.setImage(imageDel);
		button.setData(index);
		button.addSelectionListener(buttonDelListener);
		editor.horizontalAlignment = SWT.CENTER;
		editor.grabHorizontal = true;
		editor.setEditor(button, item, 0);
		rowEditors.set(ColumnNames.delete, editor);

		editor = new TableEditor(table);
		editor.grabHorizontal = true;

		Composite composite = new Composite(table, SWT.NONE);
		composite.setBackground(table.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 0;
		composite.setLayout(gridLayout);
		composite.setData(index);

		Label label = new Label(composite, SWT.NONE);
		label.setImage(launchItem.getImage());
		rowEditors.setLaunchImage(label);

		label = new Label(composite, SWT.NONE);
		label.setText(launchItem.getName());
		label.setBackground(composite.getBackground());
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		label.setLayoutData(gridData);
		rowEditors.setLaunchName(label);

		button = new Button(composite, SWT.PUSH);
		button.setText("...");
		button.addSelectionListener(buttonEditListener);

		EditColumnMouseListener editColumnMouseListener = new EditColumnMouseListener(button);
		label.addMouseListener(editColumnMouseListener);
		composite.addMouseListener(editColumnMouseListener);

		editor.setEditor(composite, item, 1);
		rowEditors.set(ColumnNames.launch, editor);

		editor = new TableEditor(table);
		editor.grabHorizontal = true;

		Spinner spinner = new Spinner(table, SWT.BORDER);
		spinner.setMinimum(0);
		spinner.setMaximum(60);
		spinner.setSelection(launchItem.getDelay());
		spinner.setIncrement(5);
		spinner.setPageIncrement(100);
		spinner.setData(index);
		spinner.addModifyListener(new SpinnerModifyListener(spinner));
		editor.setEditor(spinner, item, 2);
		rowEditors.set(ColumnNames.delay, editor);

		editor = new TableEditor(table);
		editor.grabHorizontal = true;
		editor.grabVertical = true;

		composite = new Composite(table, SWT.NONE);
		composite.setBackground(table.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		RowLayout rowLayout = new RowLayout();
		rowLayout.center = true;
		rowLayout.spacing = 10;
		rowLayout.marginTop = 0;
		rowLayout.marginLeft = 16;
		composite.setLayout(rowLayout);
		composite.setData(index);

		button = new Button(composite, SWT.PUSH);
		button.setImage(imageUp);
		button.addSelectionListener(buttonUpListener);
		button.setEnabled(index > 0);
		rowEditors.setButtonUp(button);

		button = new Button(composite, SWT.PUSH);
		button.setImage(imageDown);
		button.addSelectionListener(buttonDownListener);
		button.setEnabled(index != configList.getSize() - 1);
		rowEditors.setButtonDown(button);

		editor.setEditor(composite, item, 3);
		rowEditors.set(ColumnNames.move, editor);

		launchTableEditors.addRowEditors(rowEditors);
	}

	public LaunchPage getParent() {
		return parent;
	}

	public void setParent(LaunchPage parent) {
		this.parent = parent;
	}

	private abstract class ButtonListener implements SelectionListener {
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	}

	private class ButtonAddListener extends ButtonListener {
		@Override
		public void widgetSelected(SelectionEvent e) {
			dialog.addSelectedLauncherListener(addLauncher);
			dialog.open();
		}
	}

	private class ButtonEditListener extends ButtonListener {
		@Override
		public void widgetSelected(SelectionEvent e) {
			int index = (Integer) ((Button) e.widget).getParent().getData();
			dialog.addSelectedLauncherListener(new EditLauncher(index));
			dialog.open();
		}
	}

	private class ButtonDelListener extends ButtonListener {
		@Override
		public void widgetSelected(SelectionEvent e) {
			int index = (Integer) e.widget.getData();
			configList.remove(index);
			launchTableEditors.remove(index);
			table.remove(index);
			table.pack(true);
			parent.changedConfig();
		}
	}

	private class ButtonUpListener extends ButtonListener {
		@Override
		public void widgetSelected(SelectionEvent e) {
			int index = (Integer) ((Button) e.widget).getParent().getData();
			configList.moveUp(index);
			launchTableEditors.moveUp(index);
			parent.changedConfig();
		}
	}

	private class ButtonDownListener extends ButtonListener {
		@Override
		public void widgetSelected(SelectionEvent e) {
			int index = (Integer) ((Button) e.widget).getParent().getData();
			configList.moveDown(index);
			launchTableEditors.moveDown(index);
			parent.changedConfig();
		}

	}

	private class AddLauncher implements ISelectedLauncherListener {
		@Override
		public void handler(ILaunchItem launchItem) {
			configList.addItem(launchItem);
			addTableItem(launchItem);
			parent.changedConfig();
		}
	}

	private class EditLauncher implements ISelectedLauncherListener {
		private int editIndex;

		public EditLauncher(int index) {
			editIndex = index;
		}

		@Override
		public void handler(ILaunchItem launchItem) {
			configList.setItem(editIndex, launchItem);
			launchTableEditors.updateRow(editIndex, launchItem.getName(), launchItem.getImage());
			parent.changedConfig();
		}
	}

	private class SpinnerModifyListener implements ModifyListener {
		private Spinner spinner;
		final ToolTip toolTip;

		SpinnerModifyListener(Spinner spinner) {
			this.spinner = spinner;
			toolTip = new ToolTip(spinner.getShell(), SWT.BALLOON | SWT.ICON_WARNING);
		}

		@Override
		public void modifyText(ModifyEvent e) {
			String string = spinner.getText();
			String message = null;
			int value = 0;
			try {
				value = Integer.parseInt(string);
				int maximum = spinner.getMaximum();
				int minimum = spinner.getMinimum();
				if (value > maximum) {
					message = "Current input is greater than the maximum limit (" + maximum + ")";
				} else if (value < minimum) {
					message = "Current input is less than the minimum limit (" + minimum + ")";
				}
			} catch (Exception ex) {
				message = "Current input is not numeric";
			}
			if (message != null) {
				spinner.setForeground(spinner.getDisplay().getSystemColor(SWT.COLOR_RED));
				Rectangle rect = spinner.getBounds();
				GC gc = new GC(spinner);
				Point pt = gc.textExtent(string);
				gc.dispose();
				toolTip.setLocation(spinner.getDisplay().map(table, null, rect.x + pt.x, rect.y + rect.height));
				toolTip.setMessage(message);
				toolTip.setVisible(true);
			} else {
				toolTip.setVisible(false);
				spinner.setForeground(null);

				Integer index = (Integer) spinner.getData();
				configList.getLaunchItem(index).setDelay(value);
			}
			parent.changedConfig();
		}
	}

	private class EditColumnMouseListener implements MouseListener {
		private Button editButton;

		EditColumnMouseListener(Button button) {
			editButton = button;
		}

		@Override
		public void mouseUp(MouseEvent e) {
		}

		@Override
		public void mouseDown(MouseEvent e) {
		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {
			editButton.notifyListeners(SWT.Selection, new Event());
		}
	}
}
