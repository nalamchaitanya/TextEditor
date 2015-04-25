package frontEnd;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import SpellCheck.*;

public class Editor {
	
	//Fields
	public JFrame mainFrame;
	public JToolBar editBar;
	public JMenuBar menuBar;
	public JTabbedPane tabbedPane;
	public ArrayList<FileTab> fileTabsList;

	//BKTree
	BKTree tree;
	
	//Connstructor
	public Editor() throws IOException {
		fileTabsList = new ArrayList<FileTab>();
		this.prepareEditor();
		//this.showEditor();

		//initializing a tree
		tree= new BKTree("start");
		// add the whole text big.txt
		BufferedReader in = new BufferedReader(new FileReader("Big.txt"));

		String str;
		while ((str = in.readLine()) != null) {
			tree.addToBKTree(str);
		}

	}

	/**
	 * @throws IOException ******************************************************************************************************/
	private void prepareEditor() throws IOException {
		//initialize the tree
		tree = new BKTree("start");

		// add the whole text big.txt
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader("Big.txt"));
			String str;
			while ((str = in.readLine()) != null) {
				tree.addToBKTree(str);
			}
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Initialization of main Frame.
		mainFrame = new JFrame("TextEditor");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setMinimumSize(new Dimension(1200, 600));
		mainFrame.setLayout(null);

		//Tabbed Pane
		tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(10, 60, 1160, 500);
	    mainFrame.add(tabbedPane);

		//MenuBar
		this.createMenu();
		menuBar.setBounds(0, 0, 1200, 20);
		mainFrame.add(menuBar);
	   
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
		//Buttons for New, Open, Save, SaveAs
		JButton newDoc= makeButton("New.jpg", "Open a New Document", "New");
		JButton open=makeButton("Open.jpg", "Open existing Document", "Open");
		JButton save= makeButton("Save.jpg", "Save The Document", "Save");
		JButton saveAs= makeButton("SaveAs.jpg","Save The Document With a Different Name", "SaveAs");
		
		//writing cut, cpoy, paste
		JButton cut=makeButton("Cut.png", "cut selected text", "Cut");
		//cut.setPreferredSize(new Dimension(25, 25));
		JButton copy= makeButton("Copy.jpg", "copy selected text", "Copy");
		//copy.setPreferredSize(new Dimension(25, 25));
		JButton paste= makeButton("Paste.png", "paste selected text",  "Paste");
		//paste.setPreferredSize(new Dimension(25, 25));
		
		//adding Undo, Redo, Find, Find&Replace Buttons
		JButton undo=makeButton("Undo.jpg", "Undo Previous Action", "Undo");
		JButton redo=makeButton("Redo.jpg", "Redo Action", "Redo");
		JButton find=makeButton("Find.jpg", "Find A String", "Find");
		JButton findAndReplace=makeButton("FindAndReplace.jpg", "Find and Replace", "FindAndReplace");
		
		//adding the buttons
		editBar.add(newDoc);
		editBar.add(open);
		editBar.add(save);
		editBar.add(saveAs);
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
		if(action.equals("New")){
			button.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "New", tree));
		}
		
		else if(action.equals("Open")){
			button.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "Open", tree));
		}
		
		else if(action.equals("Save")){
			button.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "Save", tree));
		}
		
		else if(action.equals("SaveAs")){
			button.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "SaveAs",tree));
		}
		
		else if(action.equals("Cut")){
			button.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "Cut", tree));
		}

		else if(action.equals("Copy")){
			button.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "Copy", tree));
		}
		
		else if(action.equals("Paste")){
			button.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "Paste",tree));
		}

		else if(action.equals("Find")){
			button.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "Find", tree));				
		}
		else if(action.equals("FindAndReplace")){
			button.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "FindAndReplace", tree));
		}
		else if(action.equals("Undo")){
			button.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "Undo", tree));
		}
		else if(action.equals("Redo")){
			button.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "Redo", tree));
		}

		
		return button;
	}
	
	
	/********************************************************************************************************/
	private void createMenu() {
		menuBar = new JMenuBar();

		JMenu menu = new JMenu("File");
		//setMnemonic
		menu.setMnemonic(KeyEvent.VK_F);
		
		/*********************************************************/
		// Open a File in the new tab
		JMenuItem menuItem = menu.add("Open");
		//add Accelerators
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK , true));
		//set Mnemonic
		menuItem.setMnemonic(KeyEvent.VK_O);
		menuItem.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "Open", tree));

		/*********************************************************/
		// create a new Tab
		menuItem = menu.add("New");
		//add Accelerator
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK , true));
		//set Mnemonic
		menuItem.setMnemonic(KeyEvent.VK_N);
		menuItem.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "New", tree));

		menu.addSeparator();
		/*********************************************************/
		// save a tab
		menuItem = menu.add("Save");
		//add Accelerator
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK , true));
		//set Mnemonic
		menuItem.setMnemonic(KeyEvent.VK_S);
		menuItem.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "Save", tree));

		/*********************************************************/
		// save a tab given file names
		menuItem = menu.add("Save As");
		//set Mnemonic
		menuItem.setMnemonic(KeyEvent.VK_A);
		menuItem.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "SaveAs", tree));
		
		menu.addSeparator();
		/*********************************************************/
		// exit from the Editor
		menuItem = menu.add("Quit");
		//set Mnemonic
		menuItem.setMnemonic(KeyEvent.VK_Q);
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();
			}
		});

		menuBar.add(menu);

		// Creating a new Edit Menu

		menu = new JMenu("Edit");
		//set Mnemonic
		menu.setMnemonic(KeyEvent.VK_E);
				
		// Adding MenuItems to the Edit Menu
		/*********************************************************/
		//Cut
		menuItem = menu.add("Cut");
		//set Mnemonic
		menuItem.setMnemonic(KeyEvent.VK_C);
		menuItem.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "Cut", tree));
		/*********************************************************/
		//Copy
		menuItem = menu.add("Copy");
		//set Mnemonic
		menuItem.setMnemonic(KeyEvent.VK_O);		
		menuItem.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "Copy", tree));
		/*********************************************************/
		// Paste
		menuItem = menu.add("Paste");
		//set Mnemonic
		menuItem.setMnemonic(KeyEvent.VK_P);
		menuItem.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "Paste", tree));
		
		menu.addSeparator();
		/*********************************************************/
		// Undo
		menuItem = menu.add("Undo");
		//set Accelerator
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_DOWN_MASK , true));
		//set Mnemonic
		menuItem.setMnemonic(KeyEvent.VK_U);
		menuItem.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "Undo", tree));

		/*********************************************************/
		// Redo
		menuItem = menu.add("Redo");
		//set Accelerator
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_DOWN_MASK , true));
		//set Mnemonic
		menuItem.setMnemonic(KeyEvent.VK_R);
		menuItem.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "Redo", tree));
		
		menu.addSeparator();
		/*********************************************************/
		//Find
		//Everytime you press find, have to check if find is already open in the tab, else open it
		//The find function is useless and very weak. Modify.
		menuItem = menu.add("Find");
		//add Accelerator
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_DOWN_MASK , true));
		//set Mnemonic
		menuItem.setMnemonic(KeyEvent.VK_F);
		menuItem.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "Find", tree));
		
		/*********************************************************/
		//find and replace
		//Pressing this button will open a Dialog box, where the string will be found and highlighted
		//Pressing replace will replace the string in order
		//pressing escape, will as usual close the dialog box and remove the highlights
		menuItem= menu.add("Find&Replace");
		//set accelerator
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_DOWN_MASK , true));
		//set Mnemonic
		menuItem.setMnemonic(KeyEvent.VK_R);
		menuItem.addActionListener(new CommonEditBarActionListener(tabbedPane, fileTabsList, "FindAndReplace", tree));
		
		menuBar.add(menu);
	}


	private void showEditor() {

	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		/*//add the whole dictionary to BKTree
		// create and serialize a BKTree

		//FileInputStream in= new FileInputStream("Tree.ser");
		//ObjectInputStream objIn= new ObjectInputStream(in);
		
		//get the tree, search from tree
		//tree= (BKTree)objIn.readObject();
		// create and serialize a BKTree
		BKTree tree = new BKTree("start");

		// add the whole text big.txt
		BufferedReader in = new BufferedReader(new FileReader("Big.txt"));

		String str;
		while ((str = in.readLine()) != null) {
			tree.addToBKTree(str);
		}

		LinkedList<String> l= null;
		
		if(tree.BKTreeHasWord("tak") == false){
			//search for words
			l= tree.searchBKTree("tak", 1);
		}
		
		if(l !=null){
			//print the list
			for(int i=0; i<l.size(); i++){
				System.out.println(l.get(i));
			}
		}
		//forsome fricking reason,it takes much longer to actually read from the serialized file than
		//simply contructing the tree on demand!!
		*/
		Editor edt = new Editor();
	}
}
