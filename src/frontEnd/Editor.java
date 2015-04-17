package frontEnd;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;

public class Editor {
	public JFrame mainFrame;
	public JToolBar editBar;
	public JMenuBar menuBar;
	public JTabbedPane tabbedPane;
	private int k = 1;
	public ArrayList<FileTab> fileTabsList;

	public Editor() {
		fileTabsList = new ArrayList<FileTab>();
		this.prepareEditor();
		//this.showEditor();
	}

	/********************************************************************************************************/
	private void prepareEditor() {
		// Initialization of main Frame.
		mainFrame = new JFrame("TextEditor");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setMinimumSize(new Dimension(1200, 600));
		mainFrame.setLayout(null);

		tabbedPane = new JTabbedPane();

		this.createMenu();
		menuBar.setBounds(0, 0, 1200, 20);
		mainFrame.add(menuBar);

		tabbedPane.setBounds(10, 60, 1160, 500);
	    mainFrame.add(tabbedPane);
	   
	    //creating a panel for cut, copy, paste
	    editBar= new JToolBar();
		editBar.setBounds(0, 25, 1200, 30);
	    //add edit bar to frame
		mainFrame.add(editBar);
	    //add buttons to the ToolBar
	    CreateEditBar();
	
	    mainFrame.pack();
	    mainFrame.setVisible(true);
	    
	}
	
	/********************************************************************************************************/
	public void CreateEditBar(){
		//writing cut, cpoy, paste for now
		
		JButton cut=makeButton("Cut.png", "cut selected text", "Cut");
		//cut.setPreferredSize(new Dimension(25, 25));
		JButton copy= makeButton("Copy.png", "copy selected text", "Copy");
		//copy.setPreferredSize(new Dimension(25, 25));
		JButton paste= makeButton("Paste.png", "paste selected text",  "Paste");
		//paste.setPreferredSize(new Dimension(25, 25));
		
		//adding Undo, Redo, Find, Find&Replace Buttons
		JButton undo=makeButton("Undo.png", "Undo Previous Action", "Undo");
		JButton redo=makeButton("Redo.png", "Redo Action", "Redo");
		JButton find=makeButton("Find.png", "Find A String", "Find");
		JButton findAndReplace=makeButton("FindAndReplace.png", "Find and Replace", "FindAndReplace");
		
		//adding the buttons
		editBar.add(cut);
		editBar.add(copy);
		editBar.add(paste);
		editBar.add(undo);
		editBar.add(redo);
		editBar.add(find);
		editBar.add(findAndReplace);
		
	}
	//make the button
	public JButton makeButton(String imagePath, String toolTipText, String action)
	{
		
		//create new JButton
		JButton button= new JButton();
		//set the tool tip text
		button.setToolTipText(toolTipText);
		//set the Icon
		try{
			Image img= ImageIO.read(new FileInputStream(imagePath));
			img= img.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
			button.setIcon(new ImageIcon(img));			
		}
		catch(IOException e){
			;
		}

		//if action listener is common
		if(action.equals("Find")){
			button.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "Find"));				
		}
		else if(action.equals("FindAndReplace")){
			button.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "FindAndReplace"));
		}
		else{
			//set Action Listener
			button.addActionListener(new EditBarActionListener(action, tabbedPane));
		}
		
		return button;
	}
	
	
	/********************************************************************************************************/
	private void createMenu() {
		menuBar = new JMenuBar();

		JMenu menu = new JMenu("File");

		/*********************************************************/
		// Open a File in the new tab
		JMenuItem menuItem = menu.add("Open");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Open the fileChooser
				JFileChooser jfc = new JFileChooser();
				jfc.setDialogType(JFileChooser.OPEN_DIALOG);
				jfc.setDialogTitle("Open the file to be edited");
				jfc.showOpenDialog(mainFrame.getContentPane());
				File file = jfc.getSelectedFile();
				// if a file is selected, or it existx
				if (file != null) {
					openFileTab(file);
				} else
					JOptionPane.showMessageDialog(tabbedPane, "File Not Found!");
			}
		});

		/*********************************************************/
		// create a new Tab
		menuItem = menu.add("New");
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				openFileTab(null);
			}
		});

		menu.addSeparator();
		/*********************************************************/
		// save a tab
		menuItem = menu.add("Save");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int t = tabbedPane.getSelectedIndex();
				if (t != -1) 
				{
					CleanList();
					FileTab temp = fileTabsList.get(t);

					// for the first save call FileChooser
					if ("Untitled".compareTo(temp.name.split(" ")[0]) == 0) {
						// call the File Chooser
						JFileChooser jfc = new JFileChooser();
						jfc.setDialogType(JFileChooser.OPEN_DIALOG);
						jfc.setDialogTitle("Save the File");
						jfc.showOpenDialog(mainFrame.getContentPane());
						File file = jfc.getSelectedFile();
						if (file != null)
						{
							saveFile(file, temp.editorPane);
							// update the tab name;
							temp.name = file.getPath();
							temp.tabFile = file;
							temp.setTab();
							tabbedPane.setTabComponentAt(t, temp.panelTab);
							tabbedPane.setTitleAt(t, temp.name);
						} else
							JOptionPane.showMessageDialog(tabbedPane,"File Not Found!");
					}
					// for all subsequent saves, just use the name of the
					// FileTab
					else
						saveFile(temp.tabFile, temp.editorPane);
				}
			}
		});

		/*********************************************************/
		// save a tab given file names
		menuItem = menu.add("Save As");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int t = tabbedPane.getSelectedIndex();
				if (t != -1)
				{
					// remove all the FileTabs which are closed in the
					// FileTabList
					CleanList();
					FileTab temp = fileTabsList.get(t);

					// opening the FileChooser
					JFileChooser jfc = new JFileChooser();
					jfc.setDialogType(JFileChooser.OPEN_DIALOG);
					jfc.setDialogTitle("Save the File");
					jfc.showOpenDialog(mainFrame.getContentPane());
					File file = jfc.getSelectedFile();
					// If a file is selected
					if (file != null)
					{
						saveFile(file, temp.editorPane);
						// update the tab name;
						temp.name = file.getPath();
						temp.tabFile = file;
						temp.setTab();
						tabbedPane.setTabComponentAt(t, temp.panelTab);
						tabbedPane.setTitleAt(t, temp.name);
					}
					// if no file is selected
					else
						JOptionPane.showMessageDialog(tabbedPane,"File Not Found!");
				}
			}
		});
		
		menu.addSeparator();
		/*********************************************************/
		// exit from the Editor
		menuItem = menu.add("Quit");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();
			}
		});

		menuBar.add(menu);

		// Creating a new Edit Menu

		menu = new JMenu("Edit");

		// Adding MenuItems to the Edit Menu
		/*********************************************************/
		//Cut
		menuItem = menu.add("Cut");
		menuItem.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				//to the action on the highlighted text
				JScrollPane curr= (JScrollPane)tabbedPane.getSelectedComponent();
				if(curr != null){
					JViewport view= (JViewport)curr.getViewport();
					JEditorPane editor= (JEditorPane)view.getComponent(0);
					//cut the selected text
					try{
						editor.cut();
					}
					catch(NullPointerException exp){
						;
					}
				}
			}

		});
		/*********************************************************/
		//Copy
		menuItem = menu.add("Copy");
		menuItem.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				//to the action on the highlighted text
				JScrollPane curr= (JScrollPane)tabbedPane.getSelectedComponent();
				if(curr != null){
					JViewport view= (JViewport)curr.getViewport();
					JEditorPane editor= (JEditorPane)view.getComponent(0);
					//cut the selected text
					try{
						editor.copy();
					}
					catch(NullPointerException exp){
						;
					}
				}
			}

		});
		/*********************************************************/
		// Paste
		menuItem = menu.add("Paste");
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//to the action on the highlighted text
				JScrollPane curr= (JScrollPane)tabbedPane.getSelectedComponent();
				if(curr != null)
				{
					JViewport view= (JViewport)curr.getViewport();
					JEditorPane editor= (JEditorPane)view.getComponent(0);
					//cut the selected text
					try
					{
						editor.paste();
					}
					catch(NullPointerException exp)
					{
						;
					}
				}
			}
		});
		
		menu.addSeparator();
		/*********************************************************/
		// Undo
		menuItem = menu.add("Undo");
		// TODO for now exception of t = -1 is not handled.
		menuItem.addActionListener(new ActionListener() {
			int t = tabbedPane.getSelectedIndex();
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(t);
				if (t == -1) {
					CleanList();
					FileTab currTab = fileTabsList.get(0);
					//currTab.undoListener.undoAction.actionPerformed(e);
				}
			}
		});

		/*********************************************************/
		// Redo
		menuItem = menu.add("Redo");
		// TODO for now exception of t = -1 is not handled.
		menuItem.addActionListener(new ActionListener() {
			int t = tabbedPane.getSelectedIndex();

			@Override
			public void actionPerformed(ActionEvent e) {
				if (t == -1) {
					System.out.println("i am here");
					CleanList();
					FileTab currTab = fileTabsList.get(0);
					//currTab.undoListener.redoAction.actionPerformed(e);
				}
			}
		});
		
		menu.addSeparator();
		/*********************************************************/
		//Find
		//Everytime you press find, have to check if find is already open in the tab, else open it
		//The find function is useless and very weak. Modify.
		menuItem = menu.add("Find");
		menuItem.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "Find"));
		
		/*********************************************************/
		//find and replace
		//Pressing this button will open a Dialog box, where the string will be found and highlighted
		//Pressing replace will replace the string in order
		//pressing escape, will as usual close the dialog box and remove the highlights
		menuItem= menu.add("Find&Replace");
		menuItem.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "FindAndReplace"));
		
		menuBar.add(menu);
	}

	/********************************************************************************************************/
	private void saveFile(File file, JEditorPane selected) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					file.getPath()));
			out.write(selected.getText());
			// flush and close the writer
			out.flush();
			out.close();
		} catch (IOException e) {
			;
		}
	}

	/********************************************************************************************************/
	// code for opening existing files or new files
	private void openFileTab(File file) {
		// remove all the FileTabs which are closed in the FileTabList
		CleanList();

		String str;
		if (file != null) {
			str = file.getPath();
			//create a new FileTab
			FileTab fileTab = new FileTab(file,tabbedPane);
			//add the Scroll pane to the TabbedPane
			tabbedPane.addTab(str,fileTab.scrollPane);
			//setting up the button at the tab
			tabbedPane.setTabComponentAt(tabbedPane.indexOfTab(str), fileTab.panelTab);
			fileTabsList.add(fileTab);
			tabbedPane.setTitleAt(fileTabsList.indexOf(fileTab), fileTab.name);
		}
		// this part is for opening new tabs
		else {
			// if there are no tabs in the ScrollPane, set k to 1
			if (fileTabsList.size() == 0) {
				// the window is empty
				k = 1;
			}
			str = "Untitled " + k;
			k++;
			//creating a new Tab
			FileTab fileTab = new FileTab(str,tabbedPane);
			fileTabsList.add(fileTab);
			tabbedPane.addTab(str, fileTab.scrollPane);
			tabbedPane.setTabComponentAt(tabbedPane.indexOfTab(str),
					fileTab.panelTab);
		}
	}
	
	/********************************************************************************************************/
	// cleaning the filetab
	public void CleanList() {
		int len = fileTabsList.size();
		for (int i = 0; i < len; i++) {
			if (i < fileTabsList.size()) {
				if (fileTabsList.get(i).closed) {
					fileTabsList.remove(i);
					i--;
				}
			}
		}
	}

	private void showEditor() {

	}

	public static void main(String[] args) {
		Editor edt = new Editor();
	}
}
