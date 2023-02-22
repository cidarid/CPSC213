package util;

/**
 * A stream of integer values; use for access to instruction field values when encoding into instruction.
 */

public class IntStream {
  int[] values;
  int   curOffset;
  int   markedOffset;
  
  public IntStream (int[] anIntArray) {
    values       = anIntArray;
    curOffset    = 0;
    markedOffset = 0;
  }
  
  public IntStream (int anInt) {
    values       = new int[] {anInt};
    curOffset    = 0;
    markedOffset = 0;
  }
  
  public int getValue () {
    curOffset += 1;
    return values[curOffset-1];
  }
  
  public void mark () {
    markedOffset = curOffset;
  }
  
  public void rewind () {
    curOffset = markedOffset;
  }
  
  public IntStream concat (IntStream anIntStream) {
    int[] newValues = new int [values.length + anIntStream.values.length];
    for (int i=0; i<values.length; i++)
      newValues[i] = values[i];
    for (int i=0, j=values.length; i<anIntStream.values.length; i++, j++)
      newValues[j] = anIntStream.values[i];
    values = newValues;
    return this;
  }
}

