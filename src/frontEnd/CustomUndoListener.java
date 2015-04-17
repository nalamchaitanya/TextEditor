package frontEnd;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

public class CustomUndoListener implements UndoableEditListener
{		
	public UndoableEdit undoManager;
	public RedoAction redoAction;
	public UndoAction undoAction;
	
	public CustomUndoListener()
	{
		undoAction = new UndoAction();
		redoAction = new RedoAction();
		undoManager = new UndoManager();
	}
	@Override
	public void undoableEditHappened(UndoableEditEvent e)
	{
		undoManager.addEdit(e.getEdit());
		undoAction.updateUndo();
		redoAction.updateRedo();
	}	
	
	protected class UndoAction extends AbstractAction
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				undoManager.undo();
				updateUndo();
				redoAction.updateRedo();
			}
			catch(CannotUndoException uex)
			{
				System.out.println("Cannot Undo");
			}
		}
		
		public UndoAction()
		{
			super("Undo");
			setEnabled(false);
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
				System.out.println("Cannot redo");
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
