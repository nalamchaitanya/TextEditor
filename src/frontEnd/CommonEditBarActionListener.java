package frontEnd;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import SpellCheck.*;

//common action listener for a few functions
public class CommonEditBarActionListener implements ActionListener{

	//Fields
	JTabbedPane tabbedPane;
	ArrayList<FileTab> fileTabsList;
	String action;
	static int k;
	BKTree tree;
	
	//constructor
	public CommonEditBarActionListener(JTabbedPane tabbedPane, ArrayList<FileTab> fileTabsList, String action, BKTree t){
		this.tabbedPane= tabbedPane;
		this.fileTabsList= fileTabsList;
		this.action= action;
		tree= t;
	}
	
	//function
	@Override
	public void actionPerformed(ActionEvent e) {

		//basically for every action
		//for New
		if(action.equals("New")){
			//run code for New Document
			try {
				openFileTab(null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		//for Opening a Document
		else if(action.equals("Open")){
			//run code for opening a Document
			// Open the fileChooser
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogType(JFileChooser.OPEN_DIALOG);
			jfc.setDialogTitle("Open the file to be edited");
			//changed from mainFrame.getContentPane() to tabbedPane
			jfc.showOpenDialog(tabbedPane);
			File file = jfc.getSelectedFile();
			// if a file is selected, or it existx
			if (file != null) {
				try {
					openFileTab(file);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} 
			else
				JOptionPane.showMessageDialog(tabbedPane, "File Not Found!");
		}
		
		//for saving
		else if(action.equals("Save")){
			//run code forsaving a file
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
					//change from mainFram's contentPane to tabbedPane
					jfc.showOpenDialog(tabbedPane);
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
		
		//save as
		else if(action.equals("SaveAs")){
			//code for saving
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
				//changing from mainFram's ContentPane to tabbedPane
				jfc.showOpenDialog(tabbedPane);
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
		
		//Quitting needs a MainFrame
		
		//for Cut
		else if(action.equals("Cut")){
			//implement cut action
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
		
		//for Copy
		else if(action.equals("Copy")){
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
		
		//for Paste
		else if(action.equals("Paste")){
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
		
		//for Find
		else if(action.equals("Find")){
			//run code for find
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
				if(str != null){
					FindAndReplace(currTab, str, null);
				//if user presses enter, find becomes false and highlights are removed
				}
			}
		}
		
		//For FindAndReplace
		else if(action.equals("FindAndReplace")){
			//run code for FindAndReplace
			//get the curren tab
			int t= tabbedPane.getSelectedIndex();
			if(t!= -1){
				//clean the fileTabsList
				CleanList();
				
				FileTab currTab= fileTabsList.get(t);
				currTab.find= true;
				//the find variable indicates if highlights have to be cleaned
				
				//create a pop-up asking for text to be found
				JTextField[] fr= new JTextField[2];
				//first text field
				fr[0]= new JTextField();
				fr[0].setToolTipText("Enter Text To Be Found");
				//next text field
				fr[1]= new JTextField();
				fr[1].setToolTipText("Enter Text To Replace With");
				
				JOptionPane.showMessageDialog(currTab.scrollPane, fr, "Enter The Strings", JOptionPane.PLAIN_MESSAGE);

				//send strings to FindAndReplace Function
				if(fr[0].getText() != null){
					FindAndReplace(currTab, fr[0].getText(), fr[1].getText());
				}
			}
		}
		
		//For Undo
		else if(action.equals("Undo")){
			//run undo code 
			int t = tabbedPane.getSelectedIndex();
			if (t!= -1) {
				CleanList();
				FileTab currTab = fileTabsList.get(t);
				currTab.undoListener.undoAction.actionPerformed(e);
			}
		}
		
		//For Redo
		else if(action.equals("Redo")){
			//run Redo code
			int t = tabbedPane.getSelectedIndex();
			if (t != -1) {
				CleanList();
				FileTab currTab = fileTabsList.get(t);
				currTab.undoListener.redoAction.actionPerformed(e);
			}
		}	
	}
	

	/**
	 * @throws IOException ******************************************************************************************************/
	// code for opening existing files or new files
	private void openFileTab(File file) throws IOException {
		//make k 1
		if(k ==0)
			k++;
		
		// remove all the FileTabs which are closed in the FileTabList
		CleanList();

		String str;
		if (file != null) {
			str = file.getPath();
			//create a new FileTab
			FileTab fileTab = new FileTab(file,tabbedPane, tree);
			//add the Scroll pane to the TabbedPane
			tabbedPane.addTab(str,fileTab.scrollPane);
			//setting up the button at the tab
			//setting button by string name gives error, check this
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
			FileTab fileTab = new FileTab(str,tabbedPane, tree);
			fileTabsList.add(fileTab);
			tabbedPane.addTab(str, fileTab.scrollPane);
			tabbedPane.setTabComponentAt(tabbedPane.indexOfTab(str),
					fileTab.panelTab);
		}
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
	//Code for Find and replace
	public void FindAndReplace(FileTab currTab, String str1, String str2){
		Document doc= currTab.editorPane.getDocument();
		if(str1 != null){
			int len= str1.length();
			
			for(int i=0; i + len <= doc.getLength() ; i++){
				//for every index i, compare the string and Highlight
				try {
					String temp= doc.getText(i, len);
					//compare temp with str
					if(str1.equals(temp)){
						//replace the String with str2
						if(str2 != null){
								//add str2
								doc.insertString(i, str2, null);
								//remove str1
								doc.remove(i+str2.length(), len);
						}
						//highlight
						javax.swing.text.DefaultHighlighter.DefaultHighlightPainter highlightPainter =
	                            new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(new Color(255, 255, 0));
						if(str2 != null)
							currTab.editorPane.getHighlighter().addHighlight(i, i+str2.length(), highlightPainter);
						else
							currTab.editorPane.getHighlighter().addHighlight(i, i+len, highlightPainter);
							
					}
					
				} 
				catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	
}
