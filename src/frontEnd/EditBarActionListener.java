package frontEnd;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;

//An action Listener for Edit Bar buttons
public class EditBarActionListener implements ActionListener{

	//fields
	String action;
	JTabbedPane tabbedPane;
	
	//constructor
	public EditBarActionListener(String action, JTabbedPane tabbedPane){
		this.action= action;
		this.tabbedPane= tabbedPane;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//based on String action, perform operations
		//If the button is clicked
		//to the action on the highlighted text
		JScrollPane curr= (JScrollPane)tabbedPane.getSelectedComponent();
		if(curr != null){
			JViewport view= (JViewport)curr.getViewport();
			JEditorPane editor= (JEditorPane)view.getComponent(0);
			
			//now we can take selected text and cut it
			
			if(action.equals("Cut")){
				//cut the selected text
				try{
					editor.cut();
				}
				catch(NullPointerException e){
					;
				}
			}
			else if(action.equals("Copy")){
				//copy the selected text
				try{
					editor.copy();
				}
				catch(NullPointerException e){
					;
				}
			}
			else if(action.equals("Paste")){
				//paste the selected text
				try{
					editor.paste();
				}
				catch(NullPointerException e){
					;
				}
			}
			//TODO
			else if(action.equals("Undo")){
				//Call for Undo
			}
			//TODO
			else if(action.equals("Redo")){
				//call for Redo
			}
			//TODO
			else if(action.equals("Find")){
				//call find function
			}
			//TODO
			else if(action.equals("FindAndReplace")){
				//call function
			}
		}
	}

	
}
