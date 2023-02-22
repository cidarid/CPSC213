package util;

import java.util.Observer;
import javax.swing.event.UndoableEditListener;

/**
 * Like javax.Swing.Table.TableModel, but for observer/observable instead of EventListener.
 */

public interface DataModel {
  void     addObserver             (Observer anObserver);
  Class<?> getColumnClass          (int columnIndex);
  int      getColumnCount          ();
  String   getColumnName           (int columnIndex);
  int      getRowCount             ();
  Object   getValueAt              (int rowIndex, int columnIndex);
  boolean  isCellEditable          (int rowIndex, int columnIndex);
  void     setValueAt              (Object aValue, int rowIndex, int columnIndex);
  void     setValueAtByUser        (Object aValue, int rowIndex, int columnIndex);
  void     setValueAt              (Object[] aValue, int rowIndex, int columnIndex);
  void     setValueAtByUser        (Object[] aValue, int rowIndex, int columnIndex);
  boolean  insertRow               (int rowIndex);
  boolean  deleteRow               (int rowIndex);
  boolean  canInsertRow            (int rowIndex);
  boolean  canDeleteRow            (int rowIndex);
  void     addUndoableEditListener (UndoableEditListener l);
}