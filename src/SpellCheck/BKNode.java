package SpellCheck;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

//THe BKNode class

public class BKNode implements Serializable{

	//Each Node has a String a HashMap associated with it.
	String value;
	ConcurrentHashMap<Integer, BKNode> mapping;
	
	//costructor
	public BKNode(String str){
		value= str;
		//create a mapping
		mapping= new ConcurrentHashMap<Integer, BKNode>();
	}
	
	//methods needed are add, search
	/********************************************************************************/
	//add a new element to the Map
	public void addElement(String str){
		//this method is called on the root
		//distis the edit distance
		int dist= EditDistance(this.value, str);
		
		//if the particular key is not found, add to the map
		if(mapping.containsKey(dist)){
			//recurse
			if(mapping.get(dist).value.equals(str) == false)
				mapping.get(dist).addElement(str);
		}
		else{
			//add to the map and return
			BKNode n= new BKNode(str);
			mapping.put(dist, n);
		}
	}
	
	/********************************************************************************/
	//search all the strings
	public LinkedList<String> possibleWords(String str, int n){
		
		LinkedList<String> words= new LinkedList<String>();
		
		//find the editDistance
		int distance= EditDistance(str, this.value);

		if(distance <= n){
			//add to the linkedList
			words.add(this.value);
		}
		
		int i;
		if(distance > n) i= distance- n;
		else i=1;
		//we put i as 1 because we do not want the same word
		//run on the whole map of the node
		while(i <= distance + n){
			//search for i in the map
			if(mapping.containsKey(i) == true){
				//if the key is present, add to the linkedList
				words.addAll(mapping.get(i).possibleWords(str, n));
			}
			
			i++;
		}
		
		return words;
	}
	
	/********************************************************************************/
	//editDistance function takes in two Strings as input and return the Edit-Distance
	public static int EditDistance(String str1, String str2){
		
		//Using the Levenstein Distance
		//Deletion, substitutions, insertion
		int i=str1.length();
		int j= str2.length();
		
		//create a table
		int[][] editTable= new int[i+1][j+1];
		
		//fill the initial values
		for(int k=0; k< j+1;k++){
			editTable[0][k]= k;
		}
		for(int k=0; k<i+1;k++){
			editTable[k][0]= k;
		}
		
		//filling the table based on the Levenstein algorithm
		int temp;
		for(int l=1; l<i+1; l++){
			for(int m=1; m<j+1; m++){
				if(str1.charAt(l-1) != str2.charAt(m-1)){
					temp= editTable[l-1][m-1]+1;
				}
				else{
					temp= editTable[l-1][m-1];
				}
				editTable[l][m]= min(editTable[l-1][m] +1, editTable[l][m-1] +1, temp);
			}
		}
		//after filling the editTable, return the last element
		return editTable[i][j];
	}
	
	/********************************************************************************/
	//min function
	public static int min(int i1, int i2, int i3){
		if(i1 < i2){
			if(i1 < i3) return i1;
			else return i3;
		}
		else{
			if(i2 < i3) return i2;
			else return i3;
		}
	}
}
