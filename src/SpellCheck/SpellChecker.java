package SpellCheck;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class SpellChecker{

	//Fields
	BKTree tree;
	KeyListener keyListen;
	//We also need the editor pane
	JEditorPane editorPane;
	String currStr;
	//mask
	boolean mask;
	
	//constructor
	public SpellChecker(JEditorPane editorP, BKTree t) throws IOException{
		this.editorPane= editorP;
		//set initial position and final position
		currStr="";
		this.tree= t;
		
		mask = false;
		
		keyListen= new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
				//put mask as true if control was pressed
				if(e.getKeyCode() == 17){
					mask = true;
				}
				
				if(mask == false){
					//when a key is typed
					//check if the key pressed is backspace or delete
					int temp= (int)e.getKeyChar();
					//add the character to the string
					if(temp <= 122 && temp >= 65){
						currStr=currStr.concat(Character.toString((char)temp).toLowerCase());	
					}
					
					if(temp == 8){
						//a key has been deleted
						if(currStr.length() >=1){
							//remove the last element from the string
							currStr= currStr.substring(0, currStr.length()-1);
						}
					}
					
					//if a space has been pressed, generate the list of words that matchit if wrong
					if(temp == 32 || temp == 9 || temp ==10){
						//check if string is not all whitespace
						if(currStr.trim().length() >=1){
						
							LinkedList<String> l= null;
							//check in tree
							if(tree.BKTreeHasWord(currStr.trim()) == false){
								l= tree.searchBKTree(currStr.trim(), 2);
							}
							if(l != null && l.size() >= 1){
								l.set(0, "");
								
								//add the list to a pop up
								Point point=editorPane.getCaret().getMagicCaretPosition();
								JComboBox<String> box= new JComboBox<String>();
								for(int i=0; i<l.size(); i++){
									box.addItem(l.get(i));
								}
								//add actionlistener
								//add a customised actionlistener
								//box.addActionListener();
								//add the JComboBox to editorPane
								JOptionPane pane= new JOptionPane(box, JOptionPane.INFORMATION_MESSAGE, JOptionPane.CANCEL_OPTION);
								JDialog dialog= pane.createDialog("Select");
								if(point != null){
									point.x= point.x +30;
									point.y += 155;
									dialog.setLocation(point);
								}
								//dialog.setSize(new Dimension(200, 100));
								dialog.setVisible(true);
								//JDialog dialog=
								//get the selected item
								String repString= (String)box.getSelectedItem();
								//replace the curString with repString
								
								replaceSpelling(currStr, repString);						
							}
						}
						currStr="";
					}
				}
			}			
		
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == 17){
					mask = false;
				}
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
			}
		};
		
		
		editorPane.addKeyListener(keyListen);
		
	}
	
	//function for replacing String
	public void replaceSpelling(String currStr, String repString){
		Document doc= editorPane.getDocument();
		/*//replace all occurances of the String
		if(currStr != null && repString !=""){
			int len= currStr.length();
			
			for(int i=0; i + len <= doc.getLength() ; i++){
				//for every index i, compare the string and Highlight
				try {
					String temp= doc.getText(i, len);
					//compare temp with str
					if(currStr.equals(temp)){
						//replace the String with str2
						//add str2
						doc.insertString(i, repString, null);
						//remove str1
						doc.remove(i+ repString.length(), len);	
					}
					
				} 
				catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}*/
		if(currStr != null && repString != null && repString != ""){
			boolean flag= false;
			
			int num=editorPane.getCaretPosition();
			int lastIndex= editorPane.getText().lastIndexOf(" ");
			if(lastIndex == -1){
				//it means that only one word is present
				lastIndex= 0;
				flag =true;
			}
			//System.out.println(num +" "+ lastIndex);
			
			//replace the Strings in the Document
			try{
				if(flag == false){
					doc.insertString(lastIndex+1, repString, null);
					doc.remove(lastIndex + repString.length() + 1, num - lastIndex-1);
				}
				else{
					doc.insertString(lastIndex, repString, null);
					doc.remove(lastIndex + repString.length(), num - lastIndex);	
				}
			}
			catch(BadLocationException e){
				;
			}
		}
	}
	
}
