package util;

import java.util.List;
import java.util.Vector;
import java.util.Observer;
import java.util.Observable;
import util.AbstractDataModel;
import util.DataModel;
import util.DataModelEvent;
import util.TableCellIndex;

public class PickModel extends AbstractDataModel implements Observer {
  DataModel       base;
  List<DataModel> pickers;
  List<Integer>   pickerRowIndices;
  List<Integer>   pickerColumnIndices;
  public PickModel (DataModel aBase, List<DataModel> aPickers, List<Integer> aPickerRowIndices, List<Integer> aPickerColumnIndices) {
    base                = aBase;
    pickers             = aPickers;
    pickerRowIndices    = aPickerRowIndices;
    pickerColumnIndices = aPickerColumnIndices;
    base.addObserver (this);
    for (DataModel picker : pickers)
      picker.addObserver (this);
  }
  public void update (Observable o, Object arg) {
    DataModelEvent event = (DataModelEvent) arg;
    if (event.getType () == DataModelEvent.Type.WRITE || event.getType () == DataModelEvent.Type.WRITE_BY_USER) 
 	if (o == base) {
	  int baseRow = event.getRowIndex ();
	  for (int row = 0; row < pickers.size (); row++) 
	    if (((Integer) pickers.get (row).getValueAt (pickerRowIndices.get (row), pickerColumnIndices.get (row))) == event.getRowIndex ()) {
	      Vector<TableCellIndex> pickCells = new Vector<TableCellIndex> ();
	      for (TableCellIndex baseCell : event.getCells ())
		pickCells.add (new TableCellIndex (row, baseCell.columnIndex));
	      tellObservers (new DataModelEvent (event.getType (), pickCells));
	    }
	} else {
	  int row = pickers.indexOf (o);
	  assert row != -1;
	  Vector<TableCellIndex> cells = new Vector<TableCellIndex> ();
	  for (int col=0; col<base.getColumnCount(); col++)
	    cells.add (new TableCellIndex (row, col));
	  tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, cells));
	}
  }
  public Class <?> getColumnClass (int col) {
    return base.getColumnClass (col);
  }
  public int getColumnCount () {
    return base.getColumnCount ();
  }
  public String getColumnName (int col) {
    return base.getColumnName (col);
  }
  public int getRowCount () {
    return pickers.size ();
  }
  public Object getValueAt (int row, int col) {
    return base.getValueAt ( (Integer) pickers.get (row).getValueAt (pickerRowIndices.get (row), pickerColumnIndices.get (row)), col);
  }
  public boolean isCellEditable (int row, int col) {
    return base.isCellEditable ( (Integer) pickers.get (row).getValueAt (pickerRowIndices.get (row), pickerColumnIndices.get (row)), col);
  }
  public void setValueAt (Object value, int row, int col) {
    base.setValueAt (value, (Integer) pickers.get (row).getValueAt (pickerRowIndices.get (row), pickerColumnIndices.get (row)), col);
  }
}
