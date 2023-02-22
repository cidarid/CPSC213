package util;

import java.util.Observer;
import java.util.Observable;
import java.util.Vector;
import util.TableCellIndex;
import util.DataModel;
import util.AbstractDataModel;
import util.DataModelEvent;

/**
 * Join two Data Models into a single model.  Model A's columns come first
 * in the new model followed by model B's.  The order of models matters for
 * delete and insert; model A's delete/insert is performed before model B's.
 */
public class CompoundModel extends AbstractDataModel implements Observer {
  private DataModel modelA;
  private DataModel modelB;
  public CompoundModel (DataModel aModelA, DataModel aModelB) {
    modelA = aModelA;
    modelB = aModelB;
    modelA.addObserver (this);
    modelB.addObserver (this);
  }
  public boolean insertRow (int row) {
    boolean inserted;
    if (canInsertRow (row)) {
      inserted = modelA.insertRow (row);
      assert inserted;
      inserted = modelB.insertRow (row);
      assert inserted;
      return true;
    } else
      return false;
  }
  public boolean deleteRow (int row) {
    boolean deleted;
    if (canDeleteRow (row)) {
      deleted = modelA.deleteRow (row);
      assert deleted;
      deleted = modelB.deleteRow (row);
      assert deleted;
      return true;
    } else
      return false;
  }
  public boolean canInsertRow (int row) {
    return modelA.canInsertRow (row) && modelB.canInsertRow (row);
  }
  public boolean canDeleteRow (int row) {
    return modelA.canDeleteRow (row) && modelB.canInsertRow (row);
  }
  public void update (Observable o, Object arg) {
    DataModelEvent event = (DataModelEvent) arg;
    if (o == modelA)
      tellObservers (event);
    else {
      Vector<TableCellIndex> fireCells = new Vector<TableCellIndex> ();
      for (TableCellIndex cell : event.getCells ())
	fireCells.add (new TableCellIndex (cell.rowIndex,cell.columnIndex + modelA.getColumnCount ()));
      tellObservers (new DataModelEvent (event.getType (), fireCells));
    }
  }
  private DataModel baseModel (int columnIndex) {
    return columnIndex < modelA.getColumnCount ()? modelA : modelB;
  }
  private int baseColumnIndex (int columnIndex) {
    return columnIndex - (columnIndex < modelA.getColumnCount ()? 0 : modelA.getColumnCount ());
  }
  public Class <?> getColumnClass (int columnIndex) {
    return baseModel (columnIndex).getColumnClass (baseColumnIndex (columnIndex));
  }
  public int     getColumnCount () {
    return modelA.getColumnCount () + modelB.getColumnCount ();
  }
  public String  getColumnName  (int columnIndex) {
    return baseModel (columnIndex).getColumnName (baseColumnIndex (columnIndex));
  }
  public int     getRowCount    () {
    return Math.max (modelA.getRowCount (), modelB.getRowCount ());
  }
  public Object  getValueAt     (int rowIndex, int columnIndex) {
    return rowIndex < baseModel (columnIndex).getRowCount ()? baseModel (columnIndex).getValueAt (rowIndex, baseColumnIndex (columnIndex)) : null;
  }
  public boolean isCellEditable (int rowIndex, int columnIndex) {
    return rowIndex < baseModel (columnIndex).getRowCount ()? baseModel (columnIndex).isCellEditable (rowIndex, baseColumnIndex (columnIndex)) : null;
  }
  public void    setValueAt     (Object aValue, int rowIndex, int columnIndex) {
    if (rowIndex < baseModel (columnIndex).getRowCount ())
      baseModel (columnIndex).setValueAt (aValue, rowIndex, baseColumnIndex (columnIndex));
  } 
}