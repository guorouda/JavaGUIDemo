package org.eclipse.swt.snippets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class SwtApp1 {

	protected Shell shell;
	JavaLineStyler lineStyler = new JavaLineStyler();
	FileDialog fileDialog;
	StyledText text;
	
	static ResourceBundle resources = ResourceBundle.getBundle("examples_javaviewer");

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		Display display =  new Display();
		SwtApp1 window = new SwtApp1();
		Shell shell = window.open(display);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	/**
	 * Open the window.
	 */
	public Shell open(Display display) {
		createShell(display);
		createMenuBar ();
		createStyledText ();
		shell.open();
		return shell;
	}

	/**
	 * Create contents of the window.
	 */
	void createShell (Display display) {
		shell = new Shell (display);
		shell.setSize(500, 400);
		shell.setText (resources.getString("Window_title"));	
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		shell.setLayout(layout);
		shell.addShellListener (new ShellAdapter () {
			public void shellClosed (ShellEvent e) {
				lineStyler.disposeColors();
				text.removeLineStyleListener(lineStyler);
			}
		});
	}
	
	void createMenuBar () {
		Menu bar = new Menu (shell, SWT.BAR);
		shell.setMenuBar (bar);

		MenuItem fileItem = new MenuItem (bar, SWT.CASCADE);
		fileItem.setText (resources.getString("File_menuitem"));
		fileItem.setMenu (createFileMenu ());

	}
	
	Menu createFileMenu() {
		Menu bar = shell.getMenuBar ();
		Menu menu = new Menu (bar);
		MenuItem item;

		// Open 
		item = new MenuItem (menu, SWT.PUSH);
		item.setText (resources.getString("Open_menuitem"));
		item.setAccelerator(SWT.MOD1 + 'O');
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				openFile();
			}
		});

		// Exit
		item = new MenuItem (menu, SWT.PUSH);
		item.setText (resources.getString("Exit_menuitem"));
		item.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				menuFileExit ();
			}
		});
		return menu;
	}
	
	void openFile() {	
		if (fileDialog == null) {
			fileDialog = new FileDialog(shell, SWT.OPEN);
		}

		fileDialog.setFilterExtensions(new String[] {"*.java", "*.*"});
		String name = fileDialog.open();
		
		open(name);
	}
	
	void open(String name) {
		final String textString;
		
		if ((name == null) || (name.length() == 0)) return;

		File file = new File(name);
		if (!file.exists()) {
			String message = MessageFormat.format(resources.getString("Err_file_no_exist"), new String[] {file.getName()});
			displayError(message);
			return;
		}

		try {
			FileInputStream stream= new FileInputStream(file.getPath());
			try {
				Reader in = new BufferedReader(new InputStreamReader(stream));
				char[] readBuffer= new char[2048];
				StringBuffer buffer= new StringBuffer((int) file.length());
				int n;
				while ((n = in.read(readBuffer)) > 0) {
					buffer.append(readBuffer, 0, n);
				}
				textString = buffer.toString();
				stream.close();
			}
			catch (IOException e) {
				// Err_file_io
				String message = MessageFormat.format(resources.getString("Err_file_io"), new String[] {file.getName()});
				displayError(message);
				return;
			}
		}
		catch (FileNotFoundException e) {
			String message = MessageFormat.format(resources.getString("Err_not_found"), new String[] {file.getName()});
			displayError(message);
			return;
		}
		// Guard against superfluous mouse move events -- defer action until later
		Display display = text.getDisplay();
		display.asyncExec(new Runnable() {
			public void run() {
				text.setText(textString);
			}
		});	
		
		// parse the block comments up front since block comments can go across
		// lines - inefficient way of doing this
		lineStyler.parseBlockComments(textString);
	}
	
	void createStyledText() {
		text = new StyledText (shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData spec = new GridData();
		spec.horizontalAlignment = GridData.FILL;
		spec.grabExcessHorizontalSpace = true;
		spec.verticalAlignment = GridData.FILL;
		spec.grabExcessVerticalSpace = true;
		text.setLayoutData(spec);
		text.addLineStyleListener(lineStyler);
		text.setEditable(false);
		Color bg = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
		text.setBackground(bg);
	}
	
	void displayError(String msg) {
		MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
		box.setMessage(msg);
		box.open();
	}

	void menuFileExit () {
		shell.close ();
	}
}
