package frontEnd;

import java.awt.Color;
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
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class Editor
{
	public JFrame mainFrame;
	public JToolBar editBar;
	public JMenuBar menuBar;
	public JTabbedPane tabbedPane;
	private int k = 1;
	public ArrayList<FileTab> fileTabsList;
	
	public Editor()
	{
		fileTabsList = new ArrayList<FileTab>();
		this.prepareEditor();
		this.showEditor();
	}

	/********************************************************************************************************/
	private void prepareEditor()
	{
		//Initialization of main Frame.
		mainFrame = new JFrame("TextEditor");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setMinimumSize(new Dimension(1200,600));
		mainFrame.setLayout(null);
	    
	    tabbedPane = new JTabbedPane();
	    
	    this.createMenu();
	    menuBar.setBounds(0, 0, 1200,20);
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
		
		//adding the buttons
		editBar.add(cut);
		editBar.add(copy);
		editBar.add(paste);
	}
	//make the button
	public JButton makeButton(String imagePath, String toolTipText, String action){
		
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

		//set Action Listener
		button.addActionListener(new EditBarActionListener(action, tabbedPane));
		
		return button;
	}
	
	
	/********************************************************************************************************/
	private void createMenu()
	{
		menuBar = new JMenuBar();
		
		JMenu menu = new JMenu("File");
		
		/*********************************************************/
		//Open a File in the new tab
		JMenuItem menuItem = menu.add("Open");
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//Open the fileChooser
				JFileChooser jfc = new JFileChooser();
				jfc.setDialogType(JFileChooser.OPEN_DIALOG);
				jfc.setDialogTitle("Open the file to be edited");
				jfc.showOpenDialog(mainFrame.getContentPane());
				File file = jfc.getSelectedFile();
				//if a file is selected, or it existx
				if(file!=null)
				{
					openFileTab(file);
				}
				else
					JOptionPane.showMessageDialog(tabbedPane, "File Not Found!");
			}
		});
		
		/*********************************************************/
		//create a new Tab
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
		//save a tab
		menuItem = menu.add("Save");
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int t = tabbedPane.getSelectedIndex();
				if(t!=-1)
				{
					CleanList();
					FileTab temp = fileTabsList.get(t);

					//for the first save call FileChooser
					if("Untitled".compareTo(temp.name.split(" ")[0])==0)
					{	
						//call the File Chooser
						JFileChooser jfc = new JFileChooser();
						jfc.setDialogType(JFileChooser.OPEN_DIALOG);
						jfc.setDialogTitle("Save the File");
						jfc.showOpenDialog(mainFrame.getContentPane());
						File file = jfc.getSelectedFile();
						if(file!=null)
						{
							saveFile(file,temp.editorPane);
							//update the tab name;
							temp.name = file.getPath();
							temp.tabFile = file;
							temp.setTab();
							tabbedPane.setTabComponentAt(t, temp.panelTab);
							tabbedPane.setTitleAt(t,temp.name);	
						}
						else
							JOptionPane.showMessageDialog(tabbedPane, "File Not Found!");
					}
					//for all subsequent saves, just use the name of the FileTab
					else
						saveFile(temp.tabFile, temp.editorPane);
				}
			}			
		});
		
		/*********************************************************/
		//save a tab given file names
		menuItem = menu.add("Save As");
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int t = tabbedPane.getSelectedIndex();
				if(t!=-1)
				{
					//remove all the FileTabs which are closed in the FileTabList
					CleanList();
					FileTab temp = fileTabsList.get(t);
					
					//opening the FileChooser
					JFileChooser jfc = new JFileChooser();
					jfc.setDialogType(JFileChooser.OPEN_DIALOG);
					jfc.setDialogTitle("Save the File");
					jfc.showOpenDialog(mainFrame.getContentPane());
					File file = jfc.getSelectedFile();
					//If a file is selected
					if(file!=null)
					{
						saveFile(file,temp.editorPane);
						//update the tab name;
						temp.name = file.getPath();
						temp.tabFile = file;
						temp.setTab();
						tabbedPane.setTabComponentAt(t, temp.panelTab);
						tabbedPane.setTitleAt(t,temp.name);	
					}
					//if no file is selected
					else
						JOptionPane.showMessageDialog(tabbedPane, "File Not Found!");
				}
			}			
		});
		
		menu.addSeparator();
		/*********************************************************/
		//esit from the Editor
		menuItem = menu.add("Quit");
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				mainFrame.dispose();
			}			
		});

		menuBar.add(menu);
		
		//Creating a new Edit Menu
		
		menu= new JMenu("Edit");
		
		//Adding MenuItems to the Edit Menu
		/*********************************************************/
		//Copy
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
		//Cut
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
		//Paste
		menuItem = menu.add("Paste");
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
						editor.paste();
					}
					catch(NullPointerException exp){
						;
					}
				}
			}
			
		});
		
		menu.addSeparator();
		/*********************************************************/
		//Undo
		menuItem = menu.add("Undo");
		menuItem.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				//TODO the undo
			}
			
		});
		/*********************************************************/
		//Redo
		menuItem = menu.add("Redo");
		menuItem.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				//TODO the redo
			}
			
		});
		
		menu.addSeparator();
		/*********************************************************/
		//Find
		//Everytime you press find, have to check if find is already open in the tab, else open it
		menuItem = menu.add("Find");
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//see the current tab
				int t= tabbedPane.getSelectedIndex();
				if(t != -1){
					//if the FileTab is closed, go to the next one
					CleanList();
					
					FileTab currTab=fileTabsList.get(t);
					currTab.find= true;
					
					//create a new Pop up asking for text to be found
					String str= JOptionPane.showInputDialog(currTab.scrollPane, "Enter the String");
					//use the String in the Find function
					if(str != null)
						Find(currTab, str);
					//if user presses enter, find becomes false and highlights are removed
				}
			}
		});
		
		//add edit menu to menuBar
		menuBar.add(menu);
	}
	
	/********************************************************************************************************/
	private void saveFile(File file,JEditorPane selected)
	{
		try
		{
			BufferedWriter out= new BufferedWriter(new FileWriter(file.getPath()));
			out.write(selected.getText());
			//flush and close the writer
			out.flush();
			out.close();			
		} 
		catch (IOException e)
		{
			;
		}
	}

	/********************************************************************************************************/
	//code for opening existing files or new files
	private void openFileTab(File file)
	{
		//remove all the FileTabs which are closed in the FileTabList
		CleanList();
		
		String str;
		if(file!=null)		
		{
			str = file.getPath();
			//create a new FileTab
			FileTab fileTab = new FileTab(file,tabbedPane,fileTabsList);
			//add the Scroll pane to the TabbedPane
			tabbedPane.addTab(str,fileTab.scrollPane);
			//setting up the button at the tab
			tabbedPane.setTabComponentAt(tabbedPane.indexOfTab(str), fileTab.panelTab);
			fileTabsList.add(fileTab);
			tabbedPane.setTitleAt(fileTabsList.indexOf(fileTab),fileTab.name);		
		}
		//this part is for opening new tabs
		else
		{
			//if there are no tabs in the ScrollPane, set k to 1
			if(fileTabsList.size() == 0){
				//the window is empty
				k =1;
			}
			str = "Untitled " + k;
			k++;
			//creating a new Tab
			FileTab fileTab = new FileTab(str,tabbedPane,fileTabsList);
			fileTabsList.add(fileTab);
			tabbedPane.addTab(str,fileTab.scrollPane);
			tabbedPane.setTabComponentAt(tabbedPane.indexOfTab(str), fileTab.panelTab);	
		}
	}

	/********************************************************************************************************/
	//Code for Find and Replace
	public void Find(FileTab currTab, String str){
		Document doc= currTab.editorPane.getDocument();
		
		int len= str.length();
		
		for(int i=0; i + len <= doc.getLength() ; i++){
			//for every index i, compare the string and Highlight
			try {
				String temp= doc.getText(i, len);
				//compare temp with str
				if(str.equals(temp)){
					//highlight
					javax.swing.text.DefaultHighlighter.DefaultHighlightPainter highlightPainter =
                            new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(new Color(255, 255, 0));
					currTab.editorPane.getHighlighter().addHighlight(i, i+len, highlightPainter);
				}
				
			} 
			catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/********************************************************************************************************/
	//cleaning the filetab
	public void CleanList(){
		int len= fileTabsList.size();
		for(int i=0; i<len; i++){
			if(i < fileTabsList.size()){
				if(fileTabsList.get(i).closed){
					fileTabsList.remove(i);
					i--;
				}
			}
		}
	}
	
	
	private void showEditor()
	{
		
	}
	
	public static void main(String[] args)
	{
		Editor edt = new Editor();
	}
}
