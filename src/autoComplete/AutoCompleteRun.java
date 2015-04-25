package autoComplete;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public class AutoCompleteRun implements Runnable
{
	Trie trie;
	String str;
	ArrayList<String> list ;
	JEditorPane editorPane;
	Node node;
	JFrame frameTemp;
	KeyListener keyListen;
	
	public AutoCompleteRun(JEditorPane editorPane) throws BadLocationException
	{
		list = new ArrayList<String>();
		trie  = new Trie();
		this.editorPane = editorPane;
		str = editorPane.getText();
		
		//editorPane.getDocument().addDocumentListener(new customDocumentListener());
		editorPane.addKeyListener(new customKeyListener());
		//editorPane.addCaretListener(new customCaretListener());
		
		node = trie.root;
	}
	
	protected class customKeyListener implements KeyListener
	{
		String str;
		boolean neglect;
		int beforeIndex;
		String beforeStr;
		InputMap  iMap;
		ActionMap aMap;
		
		public customKeyListener()
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
						suggest.addItem(list.get(i));
					/*JOptionPane pane = new JOptionPane(suggest,JOptionPane.INFORMATION_MESSAGE,JOptionPane.CANCEL_OPTION);
					JDialog suggestion = pane.createDialog("");*/
					
					suggest.addActionListener(new autoFillActionListener());
					
					frameTemp = new JFrame();
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
		public void keyPressed(KeyEvent e)
		{
			// TODO Auto-generated method stub
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e)
		{
			boolean ctrl = false;
			char ch = e.getKeyChar();
			if((e.getModifiers()& ActionEvent.CTRL_MASK)==ActionEvent.CTRL_MASK)
				ctrl = true;
			if(ctrl == false)
			{	
				if('A'<=ch&&ch<='Z')
					ch = (char) ('a'+ ch -'A');
				if('a'<=ch&&ch<='z')
				{
					str = str + ch;
					if(node!=null)
						node = node.arr[ch-'a'];
					System.out.println("******************* "+str+" ********************");
					if(node!=null)
					{
						//node.printList();
						list = node.giveStrings();
						if(list.get(0).equals("")==false)
							list.add(0, "");
					}
				}
				else if(ch == 8)
				{
					str = str.substring(0,str.length()-1);
					if(node!=null)
						node = node.parent;
					System.out.println("******************* "+str+" ********************");
					if(node!=null)
					{
						//node.printList();
						list = node.giveStrings();
						if(list.get(0).equals("")==false)
							list.add(0, "");
					}
				}
				else
				{
					str = cleanString(str);
					trie.addString(str);
					str = "";
					node = trie.root;
					list = node.giveStrings();
					//node.printList();
				}
			}
		}
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
			iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ALT, java.awt.event.InputEvent.CTRL_DOWN_MASK), binding);
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
