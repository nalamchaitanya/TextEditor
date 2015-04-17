package frontEnd;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter;

public class FileTab 
{
	JEditorPane editorPane;
	JScrollPane scrollPane;
	JTabbedPane tabbedPane;
	JPanel panelTab;
	File tabFile;
	String name;
	boolean find;
	boolean closed;
	AbstractDocument document;
	
	public CustomUndoListener undoListener;
	
	// public ArrayList<FileTab> fileTabsList;

	/********************************************************************************************************/
	public FileTab(File file,JTabbedPane tabbedPane,ArrayList<FileTab> fileTabsList)
	{
		tabFile = file;
		undoListener = new CustomUndoListener();
		// this.fileTabsList = fileTabsList;
		closed = false;
		editorPane = new JEditorPane();
		scrollPane = new JScrollPane(editorPane);
		this.tabbedPane = tabbedPane;
		name = file.getPath();
		// put find as false
		find = false;
		this.openFile(file);
		document = (AbstractDocument) editorPane.getDocument();
		document.addUndoableEditListener(undoListener);
		document.addDocumentListener(new customDocumentListener());
		this.setTab();
		
		//addKeyBindings function
		AddKeyBindings();
	}

	/********************************************************************************************************/
	public FileTab(String str,JTabbedPane tabbedPane,ArrayList<FileTab> fileTabsList)
	{
		tabFile = null;
		undoListener = new CustomUndoListener();
		name = str;
		// this.fileTabsList = fileTabsList;
		closed = false;
		this.tabbedPane = tabbedPane;
		editorPane = new JEditorPane();
		scrollPane = new JScrollPane(editorPane);
		// set find as false
		find = false;
		document = (AbstractDocument) editorPane.getDocument();
		document.addUndoableEditListener(undoListener);
		document.addDocumentListener(new customDocumentListener());
		this.setTab();
		
		//add keyBinding function
		AddKeyBindings();
	}


	
	
	/********************************************************************************************************/
	private void openFile(File file)
	{
		String currStr="", readStr;
		
		//read from the file and write to the Pane
		try{
			BufferedReader in= new BufferedReader(new FileReader(file));
			//read the string
			readStr= in.readLine();
			while(readStr != null){
				//add the string to current string
				currStr= currStr+ readStr+"\n";
				readStr= in.readLine();
			}

			// add currStr to the current Pane
			editorPane.setText(currStr);

			// close the file
			in.close();
		} catch (IOException e) {
			// Dialog box saying that such a file does not exist
			JOptionPane.showMessageDialog(tabbedPane, "File Not Found!");
		}

	}

	/********************************************************************************************************/
	public void setTab()
	{
		panelTab = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		panelTab.setOpaque(false);

		HoverButton close = new HoverButton("x");
		close.setMargin(new Insets(0, 0, 0, 0));

		JLabel title = new JLabel(name);

		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Component selected = tabbedPane.getSelectedComponent();
				if (selected != null) {
					// fileTabsList.remove(this);
					closed = true;
					tabbedPane.remove(selected);
				}
			}
		});

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		panelTab.add(title, gbc);

		gbc.gridx++;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		gbc.weightx = 0;

		panelTab.add(close, gbc);
	}
	
	protected class customDocumentListener implements DocumentListener
	{

		@Override
		public void changedUpdate(DocumentEvent arg0) 
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void insertUpdate(DocumentEvent arg0)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeUpdate(DocumentEvent arg0)
		{
			// TODO Auto-generated method stub
			
		}
	}	
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	//All keybinding functions are to be added from here
	//Function to add KeyBindings
	public void AddKeyBindings(){
		//add an InputMap and ActionMap on the JEditorPane
		InputMap iMap= editorPane.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap aMap= editorPane.getActionMap();
		
		String escape= "escape";
		iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), escape);
		aMap.put(escape, new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//remove the highlighting in the text
				if(find == true){
					RemoveHighlighting();
					find= false;
				}
			}
		});
	}
	
	/********************************************************************************************************/
	//Function to remove Highlighting
	public void RemoveHighlighting(){
		
		Highlighter hl= editorPane.getHighlighter();
		//fetching the current list of Highlights
		Highlighter.Highlight[] hls= hl.getHighlights();
		
		for(int i=0; i<hls.length; i++){
			if(hls[i].getPainter() instanceof DefaultHighlightPainter ){
				hl.removeHighlight(hls[i]);
			}
		}
	}
}


