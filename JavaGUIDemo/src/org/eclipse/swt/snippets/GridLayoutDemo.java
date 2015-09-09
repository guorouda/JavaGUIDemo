package org.eclipse.swt.snippets;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;

public class GridLayoutDemo {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			GridLayoutDemo window = new GridLayoutDemo();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(368, 300);
		shell.setText("SWT Application");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(shell, SWT.NONE);
		GridLayout gl_composite = new GridLayout(3, false);
		composite.setLayout(gl_composite);
		
		Button btnB = new Button(composite, SWT.NONE);
		btnB.setText("B1");
		
		Button btnLongButton = new Button(composite, SWT.NONE);
		btnLongButton.setText("Long Button 2");
		
		Button btnButton = new Button(composite, SWT.NONE);
		btnButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnButton.setText("Button 3");
		
		Button btnB_1 = new Button(composite, SWT.NONE);
		GridData gd_btnB_1 = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
		gd_btnB_1.heightHint = 60;
		gd_btnB_1.widthHint = 84;
		btnB_1.setLayoutData(gd_btnB_1);
		btnB_1.setText("B4");
		
		Button btnButton_1 = new Button(composite, SWT.NONE);
		btnButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		GridData gd_btnButton_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnButton_1.heightHint = 108;
		btnButton_1.setLayoutData(gd_btnButton_1);
		btnButton_1.setText("Button5");
		new Label(composite, SWT.NONE);

	}

}
