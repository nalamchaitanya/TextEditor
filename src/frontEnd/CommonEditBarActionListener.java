package frontEnd;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

//common action listener for a few functions like find etc
public class CommonEditBarActionListener implements ActionListener{

	//Fields
	JTabbedPane tabbedPane;
	ArrayList<FileTab> fileTabsList;
	String action;
	
	//constructor
	public CommonEditBarActionListener(JTabbedPane tabbedPane, ArrayList<FileTab> fileTabsList, String action){
		this.tabbedPane= tabbedPane;
		this.fileTabsList= fileTabsList;
		this.action= action;
	}
	
	//function
	@Override
	public void actionPerformed(ActionEvent e) {

		if(action.equals("Find")){
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
		else if(action.equals("Undo")){
			//run undo code 
			int t = tabbedPane.getSelectedIndex();
			if (t!= -1) {
				CleanList();
				FileTab currTab = fileTabsList.get(t);
				currTab.undoListener.undoAction.actionPerformed(e);
			}
		}
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
