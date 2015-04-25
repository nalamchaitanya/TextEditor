package SpellCheck;

import java.io.Serializable;
import java.util.LinkedList;

//BKTrees as used to store the dictionary
//We create a BKTree for the whole dictionary first and then serialize it.
//For every run of the Program, the BKtree object is deserialized and used

public class BKTree implements Serializable{
	
	BKNode root;
	
	//constructor
	public BKTree(String str){
		root= new BKNode(str);
	}
	
	//methods
	//add to tree
	public void addToBKTree(String str){
		root.addElement(str);
	}
	
	//search the tree
	public LinkedList<String> searchBKTree(String str, int n){
		return root.possibleWords(str, n);
	}
	
	//check if the tree has the word verbatim
	public boolean BKTreeHasWord(String str){
		//if the linkedList has only one element, then return true
		if(root.possibleWords(str, 0).size() == 0) return false;
		else return true;
	}
}
