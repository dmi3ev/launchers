package com.example.launcher.ui.component;

import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

import com.example.launcher.model.ILauncerCategories;
import com.example.launcher.model.ILaunchItem;
import com.example.launcher.ui.listener.ISelectedLauncherListener;
import com.example.launcher.ui.provider.TreeLaunchItemLabelProvider;
import com.example.launcher.ui.provider.TreeLaunchLabelContentProvider;

public class AddLauncherDialog extends Dialog {

	private final Set<ILauncerCategories> categories;
	private FilteredTree tree;
	private ISelectedLauncherListener selectedLauncherListener;
	private TreeViewer viewer;
	private Button buttonOK;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public AddLauncherDialog(Shell parentShell, Set<ILauncerCategories> category) {
		super(parentShell);
		this.categories = category;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));

		PatternFilter pattern = new PatternFilter();
		tree = new FilteredTree(container, SWT.BORDER, pattern, true);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		viewer = tree.getViewer();
		viewer.setContentProvider(new TreeLaunchLabelContentProvider(categories, viewer));
		viewer.setInput(this);
		viewer.setAutoExpandLevel(0);
		viewer.setLabelProvider(new TreeLaunchItemLabelProvider());
		viewer.getTree().addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				TreeItem item = (TreeItem) event.item;
				buttonOK.setEnabled(item.getParentItem() != null);
			}
		});
		viewer.getTree().addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				Point point = new Point(e.x, e.y);
				TreeItem item = viewer.getTree().getItem(point);
				if (item == null)
					return;
				if (item.getParentItem() != null)
					okPressed();
			}
		});
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection().isEmpty())
					buttonOK.setEnabled(false);
			}
		});

		viewer.expandAll();
		return container;
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	protected void okPressed() {
		Tree tree = viewer.getTree();
		if (tree.getSelectionCount() == 1) {
			TreeItem treeItem = tree.getSelection()[0];
			ILaunchItem selected = (ILaunchItem) treeItem.getData();
			if (selectedLauncherListener != null)
				selectedLauncherListener.handler(selected);
			super.okPressed();
		}
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		buttonOK = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		buttonOK.setEnabled(false);

		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	public void addSelectedLauncherListener(ISelectedLauncherListener listener) {
		this.selectedLauncherListener = listener;
	}

}
