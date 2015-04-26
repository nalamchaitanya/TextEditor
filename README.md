# TextEditor
Project - TextEditor

	Project Description
		It is a Text Editor which is useful for writing text documents and saving them.
	
	Project Functionalities
		It has the following functionalities :
		• It can edit the text in the given text file.
		• It can format the text like changing the size of letters,Bold,Italic.
		• It can undo and redo the edits made by the user.
		• It can also do spell check, auto complete and search a specific word.
		• As a later part we would do the find and replace and also different format of letters
		like TimesNewRoman etc.
		
	Implementation Details
		• The editor can perform all the basic jobs like Open,Edit,Save,Save As etc.
		• Used JEditorPane to edit text and tabs were implemented using JTabbedPane.
		• We can change the font styles,sizes and make it bold,italic and underlined although
		we are unable to encode and save them. We used the java in-built GraphicsEnviron-
		ment library to do it.
		• We have implemented undo and redo by using undo Manager in java.
		• We have used the Damerau - Levenshtein Distance as the edit distance between two
		strings. We have used BKTrees to search through the Dictionary.
		• We have used Trie which is constructed dynamically from the file and gives sugges-
		tion.
		• We have also implemented Find and Replace by naive search and also added a edit
		bar.
		• We have also added KeyBindings and Accelerators. KeyBindings are added for closing
		and shifting through tabs.
		• Threading is also implemented for Dynamic Trie construction and initial BKTree
		construction from Dictionary.
