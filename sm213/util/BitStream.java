package util;

/**
 * Input stream for bit-wise read access to instruction store.
 */

import java.util.Vector;

public class BitStream {
  DataModel    memory;
  int          address;
  Vector<Byte> bytes;
  int          curBitOffset;
  int          markedBitOffset;
  
  public BitStream (DataModel aMemory, int anAddress) {
    memory          = aMemory;
    address         = anAddress;
    curBitOffset    = 0;
    markedBitOffset = 0;
    bytes           = new Vector<Byte> ();
  }
  
  public byte getByteAt (int offset) {
    if (bytes.size () <= offset || bytes.get (offset) == null) 
      bytes.add (offset, (Byte) memory.getValueAt (address + offset, 1));
    return bytes.get (offset). byteValue ();
  }
  
  public BitString getValue (int length) {
    int offsetInByte = curBitOffset%8;
    if (length==0)
      return new BitString ();
    else if (offsetInByte + length > 8)
      return getValue (8-offsetInByte).concat (getValue (length-(8-offsetInByte)));
    else {
      int curByte = getByteAt (curBitOffset/8) & 0xff;	  
      BitString bitString = new BitString (length, ((curByte << offsetInByte) >> (8-length)) & 0xff);
      curBitOffset += length;
      return bitString;	
    }
  }
  
  public void skip (int bitLength) {
    curBitOffset += bitLength;
  }
  
  public void mark () {
    markedBitOffset = curBitOffset;
  }
  
  public void rewind () {
    curBitOffset = markedBitOffset;
  }
}

