package util;

import java.util.Observable;
import java.util.List;
import javax.swing.event.UndoableEditListener;

public abstract class AbstractDataModel extends Observable implements DataModel {
  protected void tellObservers (DataModelEvent e) {
    setChanged ();
    notifyObservers (e);
  }
  protected void tellObservers (List<DataModelEvent> el) {
    setChanged ();
    notifyObservers (el);
  }
  public void setValueAt (Object[] aValue, int rowIndex, int columnIndex) {
    for (int i=0; i<aValue.length; i++)
      setValueAt (aValue[i], rowIndex+i, columnIndex);
  }
  public boolean insertRow (int rowIndex) {
    return false;
  }
  public boolean deleteRow (int rowIndex) {
    return false;
  }
  public boolean canInsertRow (int rowIndex) {
    return false;
  }
  public boolean canDeleteRow (int rowIndex) {
    return false;
  }
  public void addUndoableEditListener (UndoableEditListener l) {
    ;
  }
  public Class <?> getColumnClass (int col) {
    return String.class;
  }
  public int getColumnCount () {
    return 1;
  }
  public String getColumnName (int col) {
    return "";
  }
  public int getRowCount () {
    return 1;
  }
  public Object getValueAt (int row, int col) {
    return null;
  }
  public boolean isCellEditable (int row, int col) {
    return false;
  }
  public void setValueAt (Object value, int row, int col) {
    ;
  }  
  public void setValueAtByUser (Object value, int row, int col) {
    setValueAt (value, row, col);
  }
  public void setValueAtByUser (Object []aValue, int rowIndex, int columnIndex) {
    for (int i=0; i<aValue.length; i++)
      setValueAt (aValue[i], rowIndex+i, columnIndex);
  }
}