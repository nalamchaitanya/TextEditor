package autoComplete;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;

public class AutoCompleteRun implements Runnable
{
	Trie trie;
	String str;
	ArrayList<String> list ;
	JEditorPane editorPane;
	Node node;
	JFrame frameTemp;
	
	public AutoCompleteRun(JEditorPane editorPane) throws BadLocationException
	{
		list = new ArrayList<String>();
		trie  = new Trie();
		this.editorPane = editorPane;
		str = editorPane.getText();
		
		editorPane.getDocument().addDocumentListener(new customDocumentListener());
		//editorPane.addCaretListener(new customCaretListener());
		
		node = trie.root;
	}
	
	
	class autoFillActionListener implements ActionListener
	{
		
		public autoFillActionListener()
		{
			
		}
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			String str = (String) ((JComboBox<String>) e.getSource()).getSelectedItem();
			System.out.println(str);
			String temp = editorPane.getText();
			int lastIndex = temp.lastIndexOf(" ");
			try {
				editorPane.getDocument().insertString(lastIndex+1, str+" ", null);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			temp = editorPane.getText();
			lastIndex = temp.lastIndexOf(" ");
			try
			{
				editorPane.getDocument().remove(lastIndex, temp.length()-lastIndex);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			frameTemp.dispose();
		}
		
	}
		
	
	protected class customDocumentListener implements DocumentListener
	{
		String str;
		boolean neglect;
		int beforeIndex;
		String beforeStr;
		InputMap  iMap;
		ActionMap aMap;
		
		public customDocumentListener()
		{
			str = "";
			beforeIndex = 0;
			
			iMap= editorPane.getInputMap(JComponent.WHEN_FOCUSED);
			aMap= editorPane.getActionMap();
			
			String binding= "autoComplete";
			iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, java.awt.event.InputEvent.CTRL_DOWN_MASK), binding);
			aMap.put(binding, new AbstractAction()
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					Point p = editorPane.getCaret().getMagicCaretPosition();
					JComboBox<String> suggest = new JComboBox<String>();
					for(int i = 0;i<list.size();i++)
						if(list.get(i).length()!=0)
							suggest.addItem(list.get(i));
					frameTemp = new JFrame();
					suggest.addActionListener(new autoFillActionListener());
					frameTemp.add(suggest);
					if(p!=null)
						frameTemp.setBounds(p.x+10,p.y+130,200,20);
					else
						frameTemp.setBounds(500,200 , 200, 20);
					frameTemp.pack();
					frameTemp.setVisible(true);
				}
			});
			
			binding= "escape";
			iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0), binding);
			aMap.put(binding, new AbstractAction()
			{

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					frameTemp.dispose();
				}
				
			});
			
			
		}
		@Override
		public void changedUpdate(DocumentEvent e) 
		{
			
		}

		@Override
		public void insertUpdate(DocumentEvent e)
		{
			int offSet = e.getOffset();
			String tmp = editorPane.getText();
			if(tmp.charAt(tmp.length()-1)=='\n')
				tmp = tmp.substring(0, tmp.length()-1);
			if(offSet==tmp.length()-1&&offSet>beforeIndex)
			{
				char ch = tmp.charAt(offSet);
				if(ch!=' '&&ch!='@')
				{
					str = str + ch;
					if(node!=null)
						node = node.arr[ch-'a'];
					System.out.println("******************* "+str+" ********************");
					if(node!=null)
					{
						node.printList();
						list = node.giveStrings();
					}
				}
				else if(ch == '@')
					;
				else
				{
					str = cleanString(str);
					trie.addString(str);
					str = "";
					node = trie.root;
					list = node.giveStrings();
					//node.printList();
				}
				beforeIndex = offSet;
				beforeStr = str;
			}
			else
				str = beforeStr;
		}

		@Override
		public void removeUpdate(DocumentEvent arg0)
		{
			// TODO Auto-generated method stub			
		}
	}	
	
	public void run() 
	{
		String strList[] = str.split(" ");
		int size = strList.length;
		for(int i = 0;i<size;i++)
		{
			String st = cleanString(strList[i]);
			if(st.contains("'")==true)
				continue;
			trie.addString(st);
		}
	}
	
	public String cleanString(String str1)
	{
		String string = str1.toLowerCase();
		int len = string.length();
		if(len == 0)
			return "";
		if(str1.charAt(len-1)=='\n')
			return cleanString(str1.substring(0, len-1));
		char ch = string.charAt(0);
		if(!(('a'<=ch)&&(ch<='z')))
		{
			string = string.substring(1);
		}
		ch = string.charAt(len-1);
		if(!(('a'<=ch)&&(ch<='z')))
		{
			string = string.substring(0,len-1);
		}
		return string;
	}
}
