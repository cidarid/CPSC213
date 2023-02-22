package util;

import java.util.List;
import java.util.Vector;
import java.util.Arrays;

/**
 * Observer update event for DataModel, presented as argument to Observer.update ().
 */

public class DataModelEvent {
  private Type                 type;
  private List<TableCellIndex> cells;
  
  public DataModelEvent (Type aType, List<TableCellIndex> aCells) {
    type  = aType;
    cells = aCells;
  }
  
  public DataModelEvent (Type aType, int rowIndex, int columnIndex) {
    this (aType, Arrays.asList (new TableCellIndex (rowIndex, columnIndex)));;
  }
  
  public enum Type { READ, WRITE, CURSOR_SET, CURSOR_CLEAR, WRITE_BY_USER, CHANGING, ROWS_INSERTED, ROWS_DELETED };
  
  public Type getType () { 
    return type; 
  }

  public List<TableCellIndex> getCells () {
    return new Vector<TableCellIndex> (cells);
  }
  
  public int getRowIndex () {
    int rowIndex = cells.get (0).rowIndex;
    for (TableCellIndex cell : cells)
      if (cell.rowIndex != rowIndex)
	throw new AssertionError ();
    return rowIndex;
  }
}
