package frontEnd;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter;
import SpellCheck.*;

import autoComplete.AutoCompleteRun;
import autoComplete.Trie;

public class FileTab 
{
	JTextPane editorPane;
	JScrollPane scrollPane;
	JTabbedPane tabbedPane;
	JPanel panelTab;
	File tabFile;
	String name;
	boolean find;
	boolean closed;
	AbstractDocument document;
	
	//adding SpellChecker
	SpellChecker spelling;
	
	//passing tree to theFile tab
	BKTree tree;
	
	public Font font;
	boolean bold;
	boolean italic;
	boolean underLine;

	public CustomUndoListener undoListener;
	public AutoCompleteRun autoComplete;
	
	// public ArrayList<FileTab> fileTabsList;

	/**
	 * @throws IOException ******************************************************************************************************/
	public FileTab(File file,JTabbedPane tabbedPane, BKTree t) throws IOException
	{
		tabFile = file;
		
		undoListener = new CustomUndoListener();
		
		closed = false;
		editorPane = new JTextPane();
		font = editorPane.getFont();
		bold = font.isBold();
		italic = font.isItalic();
		underLine = false;
		
		scrollPane = new JScrollPane(editorPane);
		this.tabbedPane = tabbedPane;
		name = file.getPath();
		// put find as false
		find = false;
		
		boolean fileOpened = this.openFile(file);
		
		document = (AbstractDocument) editorPane.getDocument();
		document.addUndoableEditListener(undoListener);
		document.addDocumentListener(new customDocumentListener());
		this.setTab();	
		//set tree
		tree= t;
		
		//editing text		
		editorPane.getCaret().setVisible(true);
		editorPane.setCaretPosition(0);
		this.setTab();

		try {
			autoComplete = new AutoCompleteRun(editorPane);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(autoComplete).start();
		//addKeyBindings function
		AddKeyBindings();
		
		//initialize spellChecker
		spelling= new SpellChecker(editorPane,tree);
	}

	/**
	 * @throws IOException ******************************************************************************************************/
	public FileTab(String str,JTabbedPane tabbedPane, BKTree t) throws IOException
	{
		tabFile = null;
		undoListener = new CustomUndoListener();
		name = str;
		// this.fileTabsList = fileTabsList;
		closed = false;
		this.tabbedPane = tabbedPane;
		editorPane = new JTextPane();
		
		font = editorPane.getFont();
		bold = font.isBold();
		italic = font.isItalic();
		underLine = false;
		
		scrollPane = new JScrollPane(editorPane);
		// set find as false
		find = false;
		
		document = (AbstractDocument) editorPane.getDocument();
		document.addUndoableEditListener(undoListener);
		
		editorPane.getCaret().setVisible(true);
		editorPane.setCaretPosition(0);
		this.setTab();
		//set tree
		tree= t;
		
		try {
			autoComplete = new AutoCompleteRun(editorPane);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(autoComplete).start();
		
		//add keyBinding function
		AddKeyBindings();
		
		//initializingspell Checker
		spelling= new SpellChecker(editorPane,tree);
	}


	
	
	/**
	 * @return ******************************************************************************************************/
	private boolean openFile(File file)
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
			return true;
		} catch (IOException e) {
			// Dialog box saying that such a file does not exist
			JOptionPane.showMessageDialog(tabbedPane, "File Not Found!");
			return false;
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
	
	/**
	 * All keybinding functions are to be added from here
	 * Function to add KeyBindings
	 */
	
	public void AddKeyBindings(){
		//add an InputMap and ActionMap on the JEditorPane
		InputMap iMap= editorPane.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap aMap= editorPane.getActionMap();
		
		String binding= "escape";
		iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), binding);
		aMap.put(binding, new AbstractAction() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//remove the highlighting in the text
				if(find == true){
					RemoveHighlighting();
					find= false;
				}
			}
		});
		
		//adding Key Bindings to delete tab
		binding= "remove";
		iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_DOWN_MASK ), binding);
		aMap.put(binding, new AbstractAction() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				//remove the FileTab
				Component selected= tabbedPane.getSelectedComponent();
				tabbedPane.remove(selected);
				//the fileTabsList still contains it 
				closed=true;
			}
		});
		
		//for switching between tabs
		binding= "switch";
		iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,  java.awt.event.InputEvent.SHIFT_DOWN_MASK), binding);
		aMap.put(binding, new AbstractAction() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int t= tabbedPane.getSelectedIndex();
				if(t != -1 ){
					try{
						tabbedPane.setSelectedIndex(t+1);
					}
					catch(IndexOutOfBoundsException e){
						;
					}
				}
			}
		});
		
		//for switching between tabs
		binding= "backSwitch";
		iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, java.awt.event.InputEvent.SHIFT_DOWN_MASK+java.awt.event.InputEvent.ALT_DOWN_MASK), binding);
		aMap.put(binding, new AbstractAction() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int t= tabbedPane.getSelectedIndex();
				if(t != -1 ){
					try{
						if(t >= 1)
							tabbedPane.setSelectedIndex(t-1);
					}
					catch(IndexOutOfBoundsException e){
						System.out.println("boom");
						;
					}
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


