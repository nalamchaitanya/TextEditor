package frontEnd;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

public class Editor
{
	public JFrame mainFrame;
	public JMenuBar menuBar;
	public JTabbedPane tabbedPane;
	private int k = 1;
	private ArrayList<FileTab> fileTabsList;
	
	public Editor()
	{
		fileTabsList = new ArrayList<FileTab>();
		this.prepareEditor();
		this.showEditor();
	}

	private void prepareEditor()
	{
		//Initialization of main Frame.
		mainFrame = new JFrame("TextEditor");
		mainFrame.setMinimumSize(new Dimension(1200,600));
		mainFrame.setLayout(null);
	    
	    tabbedPane = new JTabbedPane();
	    
	    this.createMenu();
	    menuBar.setBounds(0, 0, 1200,20);
		mainFrame.add(menuBar);
		
		tabbedPane.setBounds(10, 60, 1160, 500);
	    mainFrame.add(tabbedPane);
	    
	    mainFrame.pack();
	    mainFrame.setVisible(true);
	    
	}

	private void createMenu()
	{
		menuBar = new JMenuBar();
		
		JMenu menu = new JMenu("File");
		
		JMenuItem menuItem = menu.add("Open");
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser jfc = new JFileChooser();
				jfc.setDialogType(JFileChooser.OPEN_DIALOG);
				jfc.setDialogTitle("Open the file to be edited");
				jfc.showOpenDialog(mainFrame.getContentPane());
				File file = jfc.getSelectedFile();
				
				if(file!=null)
				{
					openFileTab(file);
				}
				else
					JOptionPane.showMessageDialog(tabbedPane, "File Not Found!");
			}
		});
		
		menuItem = menu.add("New");
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				openFileTab(null);
			}			
		});

		
		menuItem = menu.add("Save");
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int t = tabbedPane.getSelectedIndex();
				if(t!=-1)
				{
					FileTab temp = fileTabsList.get(t);
					if("Untitled".compareTo(temp.name.split("//s+")[0])==0)
					{	
						JFileChooser jfc = new JFileChooser();
						jfc.setDialogType(JFileChooser.OPEN_DIALOG);
						jfc.setDialogTitle("Save the File");
						jfc.showOpenDialog(mainFrame.getContentPane());
						File file = jfc.getSelectedFile();
						if(file!=null)
						{
							saveFile(file,fileTabsList.get(t).editorPane);
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
					else
						saveFile(temp.tabFile, temp.editorPane);
				}
			}			
		});
		
		menuItem = menu.add("Save As");
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int t = tabbedPane.getSelectedIndex();
				if(t!=-1)
				{
					FileTab temp = fileTabsList.get(t);
					JFileChooser jfc = new JFileChooser();
					jfc.setDialogType(JFileChooser.OPEN_DIALOG);
					jfc.setDialogTitle("Save the File");
					jfc.showOpenDialog(mainFrame.getContentPane());
					File file = jfc.getSelectedFile();
					if(file!=null)
					{
						saveFile(file,fileTabsList.get(t).editorPane);
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
			}			
		});
		
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
	}

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
	
	private void openFileTab(File file)
	{
		String str;
		if(file!=null)		
		{
			str = file.getPath();
			FileTab fileTab = new FileTab(file,tabbedPane,fileTabsList);
			tabbedPane.addTab(str,fileTab.scrollPane);
			tabbedPane.setTabComponentAt(tabbedPane.indexOfTab(str), fileTab.panelTab);
			fileTabsList.add(fileTab);
			tabbedPane.setTitleAt(fileTabsList.indexOf(fileTab),fileTab.name);			
		}
		else
		{
			str = "Untitled " + k;
			k++;
			FileTab fileTab = new FileTab(str,tabbedPane,fileTabsList);
			fileTabsList.add(fileTab);
			tabbedPane.addTab(str,fileTab.scrollPane);
			tabbedPane.setTabComponentAt(tabbedPane.indexOfTab(str), fileTab.panelTab);
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
