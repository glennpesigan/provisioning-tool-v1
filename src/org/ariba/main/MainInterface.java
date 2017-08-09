package org.ariba.main;

import javax.swing.JOptionPane;

import org.ariba.methods.parseExcel;
import org.ariba.todo.EventTemplates;
import org.ariba.todo.SourcingLibrary;
import org.ariba.todo.Templates;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class MainInterface {

	protected Shell shell;
	private Text pathBox;
	private Text serverBox;
	private Text usernameBox;
	private Text passwordBox;
	private Text text;
	Display display;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainInterface window = new MainInterface();
			window.open();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();

		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation(x, y);
		
		text = new Text(shell, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		text.setBounds(33, 450, 735, 218);

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
		shell = new Shell(SWT.NONE);
		shell.setMinimumSize(new Point(809, 720));
		shell.setSize(540, 194);
		shell.setText("SWT Application");
		shell.setBackgroundImage(SWTResourceManager.getImage(MainInterface.class, "/org/ariba/main/laribabg1.png"));
		
		Button closeBtn = new Button(shell, SWT.FLAT);
		closeBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
				System.exit(0);
			}
		});
		closeBtn.setBounds(746, 5, 34, 35);
		closeBtn.setImage(SWTResourceManager.getImage(MainInterface.class, "/org/ariba/main/closeBtn.png"));
		
		Button browseBtn = new Button(shell, SWT.FLAT);
		browseBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog lpFileDialog = new FileDialog(shell, SWT.OPEN);
				lpFileDialog.setFilterExtensions(new String[] { "*.xl*" });
				lpFileDialog.setFilterPath("");
				String filePath = lpFileDialog.open();

				if (filePath == null) {
					pathBox.setText("");
				} else {
					pathBox.setText(filePath);
				}
			}
		});
		browseBtn.setImage(SWTResourceManager.getImage(MainInterface.class, "/org/ariba/main/browseBtn.png"));
		browseBtn.setBounds(667, 274, 101, 23);
		
		Button executeBtn = new Button(shell, SWT.FLAT);
		executeBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String fileName = pathBox.getText();
				String user = usernameBox.getText();
				String pass = passwordBox.getText();
				String serverLink = serverBox.getText();
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{

						Details.path = fileName;
						Details.server = serverLink;
						Details.user = user;
						Details.pass = pass;
						Details.display = display;
						Details.logging = text;
						
						if (user.isEmpty() || pass.isEmpty() || serverLink.isEmpty()){
							JOptionPane.showMessageDialog(null,"URL, Username and Password cannot be blank!");
							return;
						}
						
						if (fileName.isEmpty()){
							JOptionPane.showMessageDialog(null,"Please select an input file.");
							return;
						}
						
						parseExcel data = new parseExcel();
						String document = data.getSpecificData(Details.path, "Configuration", "Document", "Value").trim();
						
						switch (document.trim()){
						case "Template":
							Templates temp = new Templates();
							temp.execute();
							break;
							
						case "Event Template":
							EventTemplates et = new EventTemplates();
							et.execute();
							break;
							
						case "Sourcing Library":
							SourcingLibrary sl = new SourcingLibrary();
							sl.execute();
							break;
							
						default:
							JOptionPane.showMessageDialog(null,"Please select value on 'Document' in 'Configuration' sheet.");
							break;
						}
						
						
					}
				}).start();
					
			}
		});
		executeBtn.setImage(SWTResourceManager.getImage(MainInterface.class, "/org/ariba/main/executeBtn.png"));
		executeBtn.setBounds(667, 412, 101, 23);
		
		pathBox = new Text(shell, SWT.READ_ONLY);
//		pathBox.setText("C:\\Users\\glenn.a.pesigan\\Desktop\\Liquid Ariba\\Sample RFI Design Matrix.xlsx");
		pathBox.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		pathBox.setBounds(164, 273, 486, 25);
		
		serverBox = new Text(shell, SWT.NONE);
		serverBox.setText("https://s1.ariba.com/Buyer/Main/aw?awh=r&awssk=ZV459B.t&realm=aribademoaccenture-sap-T");
		serverBox.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		serverBox.setBounds(164, 108, 591, 25);
		
		usernameBox = new Text(shell, SWT.NONE);
		usernameBox.setText("glenn.a.pesigan");
		usernameBox.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		usernameBox.setBounds(164, 145, 591, 25);
		
		passwordBox = new Text(shell, SWT.PASSWORD);
		passwordBox.setText("ariba@01");
		passwordBox.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		passwordBox.setBounds(164, 181, 591, 25);

	}
}

