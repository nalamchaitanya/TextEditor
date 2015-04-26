package autoComplete;

public class Trie
{
	Node root;
	
	public Trie()
	{
		root = new Node('@',null);
		root.word = "";
	}
	
	public void addString(String str)
	{
		Node temp;
		Node parnt = this.root;
		int len = str.length();
		boolean alreadyExists = true;
		for(int i = 0;i<len;i++)
		{
			temp = parnt.arr[str.charAt(i)-'a'];
			if(temp == null)
			{
				alreadyExists = false;
				parnt.arr[str.charAt(i)-'a'] = new Node(str.charAt(i),parnt);
				if(parnt.terminal == true)
					parnt.terminal = false;
				temp = parnt.arr[str.charAt(i)-'a'];
				parnt.listUpdate = true;
			}
			parnt = temp;
		}
		parnt.endOfWord = true;
		parnt.word = str;
		parnt.terminal = true;
		parnt.listUpdate = true;
		for(int i = 0;i<26;i++)
		{
			if(parnt.arr[i]!=null)
			{
				parnt.terminal = false;
				break;
			}
		}
		if(alreadyExists == true)
			parnt.freq++;
	}
	
	public void removeString(String str)
	{
		int len = str.length();
		Node node = this.root;
		int i = 0;
		while(!(node.isTerminal())&&i<len)
		{
			node = node.arr[str.charAt(i)-'a'];
			i++;
		}
		if(node.isTerminal()==false)
			node.endOfWord = false;
		else if(i<len)
			;
		else
		{
			char temp = node.ch;
			while(temp!='@')
			{
				if(node.endOfWord == true)
					break;
				node = node.parent;
				node.arr[temp-'a'] = null;
				temp = node.ch;
			}
		}
	}
	
}
