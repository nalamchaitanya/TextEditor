package frontEnd;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

public class CustomUndoListener implements UndoableEditListener
{		
	//fields
	public UndoableEdit undoManager;
	public RedoAction redoAction;
	public UndoAction undoAction;
	
	//constructor
	public CustomUndoListener()
	{
		undoAction = new UndoAction();
		redoAction = new RedoAction();
		undoManager = new UndoManager();
	}
	
	//if undoable edit  happens then the event is added to undo manager
	//then undoaction and redoactions are updated.
	//i.e enable or disable accordingly.
	@Override
	public void undoableEditHappened(UndoableEditEvent e)
	{
		undoManager.addEdit(e.getEdit());
		undoAction.updateUndo();
		redoAction.updateRedo();
	}	
	
	//this is an undoaction which extends abstract action
	protected class UndoAction extends AbstractAction
	{

		//this is executed when undoaction is called
		@Override
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				undoManager.undo();	//does undo
				updateUndo();		//update its state
				redoAction.updateRedo();//update redo state
			}
			catch(CannotUndoException uex)
			{
				//create a new Dialog box 
				JOptionPane.showMessageDialog(null, "Cannot Undo");
			}
		}
		
		public UndoAction()
		{
			super("Undo");	//calling super constructor
			setEnabled(false); //initially set false
		}
		
		public void updateUndo()
		{
			if(undoManager.canUndo())
			{
				setEnabled(true);			
			}
			else
			{
				setEnabled(false);
			}
		}
	}

	protected class RedoAction extends AbstractAction
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				undoManager.redo();
				updateRedo();
				undoAction.updateUndo();
			}
			catch(CannotRedoException uex)
			{
				//Create a new Dialog Box
				JOptionPane.showMessageDialog(null, "Cannot Redo");
			}
		}
		
		public RedoAction()
		{
			super("Redo");
			setEnabled(false);
		}
		
		public void updateRedo()
		{
			if(undoManager.canRedo())
			{
				setEnabled(true);			
			}
			else
			{
				setEnabled(false);
			}
		}
	}
}
