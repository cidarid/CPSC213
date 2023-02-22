package util;

/**
 * String of bit values; used for access to instruction fields when decoding instruction.
 */

public class BitString {
  int  length;
  long value;
  
  public BitString (int aLength, long aValue) {
    length = aLength;
    value  = 0;
    for (int i=0; i<length; i++)
      value = (value<<1) | ((aValue>>(length-1-i)) & 0x1);
  }
  
  public BitString (BitString bs) {
    this (bs.length, bs.value);
  }
  
  public BitString () {
    this (0,0);
  }
  
  public int length () {
    return length;
  }
  
  public long getValue () {
    return value;
  }
  
  public int getValueAt (int startBit, int numBits) {
    int v = 0;
    for (int i=startBit; i<startBit+numBits; i++)
      v = (v << 1) | ((int) ((value >> (length-i-1)) & 0x1));
    return v;
  }
  
  public BitString concat (BitString aBitString) {
    value  = (value << aBitString.length) | aBitString.value;
    length += aBitString.length;
    return this;
  }
  
  public Byte[] toBytes () {
    int len = byteLength ();
    Byte[] byteValue = new Byte[len];
    for (int i=0; i<len; i++)
      byteValue[i] = new Byte ((byte) (value >> (len-1-i)*8));
    return byteValue;
  }
  
  public void writeToByUser (DataModel model, int address) {
    model.setValueAtByUser (toBytes (), address, 1);
  }
  
  public int byteLength () {
    return (length+7)/8;
  }
  
  public boolean equals (Object o) {
    if (o instanceof BitString) {
      BitString b = (BitString) o;
      return length == b.length && value == b.value;
    } else 
      return false;
  }
  
  public int hashCode () {
    return length + ((int) value);
  }
}

