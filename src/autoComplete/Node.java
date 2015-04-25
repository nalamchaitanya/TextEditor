package autoComplete;

import java.util.ArrayList;

public class Node
{
	boolean endOfWord;
	boolean terminal;
	String word;
	Node parent;
	int freq;
	char ch;
	Node[] arr;
	ArrayList<String> list;
	boolean listUpdate;
	boolean arrayInitialized;
	
	public Node(char c,Node parent)
	{
		endOfWord = false;
		terminal = false;
		freq = 0;
		ch = c;
		this.parent = parent;
		arrayInitialized = false;
		listUpdate = true;
		arr = new Node[26];
	}
	
	ArrayList<String> giveStrings()
	{
		if(listUpdate == true)
		{
			if(terminal == true)
			{
				list = new ArrayList<String>(1);
				list.add(word);
			}
			else
			{
				list = new ArrayList<String>();
				if(endOfWord == true)
					list.add(word);
				for(int i = 0;i<26;i++)
				{
					if(arr[i]!=null)
					{
						list.addAll(arr[i].giveStrings());
					}						
				}
			}
		}
		listUpdate = false;
		return list;
	}

	public void printList()
	{
		list = this.giveStrings();
		int len = list.size();
		for(int i =0;i<len;i++)
			System.out.println(list.get(i));
	}	
}
