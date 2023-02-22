package machine;

import java.util.List;
import java.util.Vector;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import util.AbstractDataModel;
import util.DataModelEvent;
import util.TableCellIndex;
import util.UnsignedByte;
import util.UserVisibleException;

/**
 * Main Memory (DRAM).
 */

public abstract class AbstractMainMemory extends AbstractDataModel {
  
  /**
   *
   */
  public static class MMU {
    public int translate (int address) throws InvalidAddressException {
      return address;
    }
  }
  
  /**
   * Memory's port for reading and writing.  This is the interface the memory presents to the CPU.
   */
  public class Port {
    MMU mmu;
    
    public Port (MMU mmu) {
      this.mmu=mmu;
    }
    
    /**
     * Read a sequence of bytes from memory using an aligned address.
     *
     * @param address address of first byte.
     * @param length number of bytes.
     * @return array of memory value of the specified bytes.
     * @throws InvalidAddressException if any address in the range address to address+length-1 is out of range or if address % length != 0 (i.e., address is not aligned to length).
     */
    public UnsignedByte[] read (int address, int length) throws InvalidAddressException {
      return AbstractMainMemory.this.read (mmu.translate (address), length);
    }
    /**
     * Read a sequence of bytes from memory using a possibly-unaligned address.
     *
     * @param address address of first byte.
     * @param length number of bytes.
     * @return array of memory value of the specified bytes.
     * @throws InvalidAddressException if any address in the range address to address+length-1 is out of range.
     */
    public UnsignedByte[] readUnaliged (int address, int length) throws InvalidAddressException {
      return AbstractMainMemory.this.readUnaligned (mmu.translate (address), length);
    }
    /**
     * Read a 4-byte integer from memory using an aligned address.
     *
     * @param address address of first byte.
     * @return integer value of the specified bytes.
     * @throws InvalidAddressException if any address in the range address to address+3 is out of range or if address % 4 != 0 (i.e., address is not aligned to length).
     */
    public int readInteger (int address) throws InvalidAddressException {
      return AbstractMainMemory.this.readInteger (mmu.translate (address));
    }
    /**
     * Read a 4-byte integer from memory using a possibly-unaligned address.
     *
     * @param address address of first byte.
     * @return integer value of the specified bytes.
     * @throws InvalidAddressException if any address in the range address to address+3 is out of range.
     */
    public int readIntegerUnaligned (int address) throws InvalidAddressException {
      return AbstractMainMemory.this.readIntegerUnaligned (mmu.translate (address));
    }
    /**
     * Write a sequence of bytes to memory using an aligned address.
     *
     * @param address address of first byte.
     * @param value array of bytes to write into memory.
     * @throws InvalidAddressException if any address in the range address to address+value.length-1 is out of range or if address % length != 0 (i.e., address is not aligned to length).
     */
    public void write (int address, UnsignedByte[] value) throws InvalidAddressException {
      AbstractMainMemory.this.write (mmu.translate (address), value);
    }
    /**
     * Write a sequence of bytes to memory using a possibly-unaligned address.
     *
     * @param address address of first byte.
     * @param value array of bytes to write into memory.
     * @throws InvalidAddressException if any address in the range address to address+value.length-1 is out of range.
     */
    public void writeUnaligned (int address, UnsignedByte[] value) throws InvalidAddressException {
      AbstractMainMemory.this.writeUnaligned (mmu.translate (address), value);
    }
    /**
     * Write a 4-byte integer to memory using an aligned address.
     *
     * @param address address of first byte.
     * @param value integer to write into memory.
     * @throws InvalidAddressException if any address in the range address to address+3 is out of range or if address % 4 != 0 (i.e., address is not aligned to length).
     */
    public void writeInteger (int address, int value) throws InvalidAddressException {
      AbstractMainMemory.this.writeInteger (mmu.translate (address), value);
    }
    /**
     * Write a 4-byte integer to memory using a possibly-unaligned address.
     *
     * @param address address of first byte.
     * @param value integer to write into memory.
     * @throws InvalidAddressException if any address in the range address to address+3 is out of range.
     */
    public void writeIntegerUnaligned (int address, int value) throws InvalidAddressException {
      AbstractMainMemory.this.writeIntegerUnaligned (mmu.translate (address), value);
    }
  }
  
  /**
   * Get memory's port object.
   */
  public Port getPort (MMU mmu) {
    return new Port (mmu);
  }
  
  /**
   * Get memory's port object.
   */
  Port getPort () {
    return getPort (new MMU());
  }
  
  /**
   * Exception indicating an invalid address was used in read or write operation.
   */
  
  public static class InvalidAddressException extends UserVisibleException {
    static final String MESSAGE = "Invalid address issued";
    public InvalidAddressException () {
      super (MESSAGE);
    }
    public InvalidAddressException (int aPC) {
      super (MESSAGE, aPC);
    }
  }
  
  //////////////////
  // RE-IMPLEMENT THESE METHODS IN CONCRETE MAIN MEMORY FILE CLASS
  //
  
  /**
   * Read a sequence of bytes from memory.  Protected method called by public read method. 
   * 
   * @param  address  byte address of first byte to read.
   * @param  length   number of bytes to read.
   *
   * @throws InvalidAddressException if address is out of range.
   */
  protected abstract byte[] get (int address, int length) throws InvalidAddressException;
  
  /**
   * Write a sequence of bytes to memory.  Protected method called by public write method. 
   *
   * @param  address  byte address of first byte to write.
   * @param  value    array of unsigned bytes to write to memory at this address.
   *
   * @throws InvalidAddressException if address is out of range.
   */
  protected abstract void set (int address, byte[] value) throws InvalidAddressException;
  
  /**
   * Determine whether specified address and length represent an ALIGNED access.  Protected method called 
   * by public read and write methods.
   *
   * An address is aligned if and only if the address modulo value.length is 0 (i.e., the low order log 2 
   * (length) bits are 0. Aligned memory access is faster than unaligned access and so compilers should 
   * attempt to used aligned access whenever possible.  It is sometimes not possible, however, particularlly 
   * for reading instructions in architectures that support variable instruction lengths such as SM213 and Y86,
   * for example.
   *
   * @return true iff access to address of length bytes is an aligned access.
   */
  protected abstract boolean isAccessAligned (int address, int length);
  
  /**
   * Convert a byte array to an integer.
   *
   * @param byteAtAddrPlus0  value of byte at some memory address addr.
   * @param byteAtAddrPlus1  value of byte at some memory address addr + 1.
   * @param byteAtAddrPlus2  value of byte at some memory address addr + 2.
   * @param byteAtAddrPlus3  value of byte at some memory address addr + 3.
   * @return integer comprised of this four bytes organized according to the Endianness of the target ISA.
   */
  public abstract int bytesToInteger (byte byteAtAddrPlus0, byte byteAtAddrPlus1, byte byteAtAddrPlus2, byte byteAtAddrPlus3);
  
  /**
   * Convert an integer to a byte array.
   *
   * @param i an 32-bit integer value.
   * @return an array of bytes that comprise the integer in address order according to the Endianness of the target ISA.
   */
  public abstract byte[] integerToBytes (int i);
  
  /*
   * Byte capacity of memory.
   */
  public abstract int length ();
  
  /////////////////////
  // For Machine
  //

  /**
   * READ a sequence of bytes from memory starting at specified ALIGNED.  An address is aligned
   * if and only if the address modulo length is 0 (i.e., the low order log 2 (length) bits are 0.
   *
   * @throws InvalidAddressException if address is out of range or is not aligned.
   */
  final public UnsignedByte[] read (int address, int length) throws InvalidAddressException {
    if (isAccessAligned (address,length))
      return readUnaligned (address, length);
    else
      throw new InvalidAddressException ();
  }
  
  /**
   * WRITE a sequence of bytes to memory starting at specified ALIGNED.  An address is aligned
   * if and only if the address modulo value.length is 0 (i.e., the low order log 2 (value.length) bits are 0.
   *
   * @throws InvalidAddressException if address is out of range or is not aligned.
   */
  final public void write (int address, UnsignedByte[] value) throws InvalidAddressException {
    if (isAccessAligned (address, value.length)) 
      writeUnaligned (address, value);
    else 
      throw new InvalidAddressException ();
  }
  
  /**
   * READ a sequence of bytes from memory starting at specified possibly-UNALIGNED address.  An address is aligned
   * if and only if the address modulo length is 0 (i.e., the low order log 2 (length) bits are 0.
   *
   * Unaligned memory access is, in real hardware, slower than aligned access and so clients should use the
   * aligned read and write methods whenever possible.
   *
   * @throws InvalidAddressException if address is out of range.
   */
  final public UnsignedByte[] readUnaligned (int address, int length) throws InvalidAddressException {
    UnsignedByte[] value = UnsignedByte.toUnsignedBytes (get (address, length));
    List<TableCellIndex> cells = new Vector<TableCellIndex> ();
    for (int i=0; i<length; i++)
      cells.add (new TableCellIndex (address+i, 1));
    tellObservers (new DataModelEvent (DataModelEvent.Type.READ, cells));
    return value;
  }
  
  /**
   * WRITE a sequence of bytes to memory starting at specified possibly-UNALIGNED address.  An address is aligned
   * if and only if the address modulo value.length is 0 (i.e., the low order log 2 (value.length) bits are 0.
   *
   * Unaligned memory access is, in real hardware, slower than aligned access and so clients should use the
   * aligned read and write methods whenever possible.
   *
   * @throws InvalidAddressException if address is out of range.
   */
  final public void writeUnaligned (int address, UnsignedByte[] value) throws InvalidAddressException {
    set (address, UnsignedByte.toBytes (value));
    List<TableCellIndex> cells = new Vector<TableCellIndex> ();
    for (int i=0; i<value.length; i++)
      cells.add (new TableCellIndex (address+i, 1));
    tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE, cells));
  }
  
  /**
   * Read a four-byte integer from memory at ALIGNED address.
   */
  public int readInteger (int address) throws InvalidAddressException {
    if (isAccessAligned (address, 4))
      return readIntegerUnaligned (address);
    else
      throw new InvalidAddressException ();
  }
  
  /*
   * Write a four-byte integer to memory at ALIGNED, power-of-two address.
   */
  public void writeInteger (int address, int value) throws InvalidAddressException {
    if (isAccessAligned (address, 4))
      writeIntegerUnaligned (address, value);
    else
      throw new InvalidAddressException ();
  }
  
  
  /**
   * Read a four-byte Big-Endian integer from memory at possibly UNALIGNED address.
   *
   * Unaligned memory access is, in real hardware, slower than aligned access and so clients should use the
   * aligned read and write methods whenever possible.
   *
   */
  public int readIntegerUnaligned  (int address) throws InvalidAddressException {
    byte b[] = UnsignedByte.toBytes (readUnaligned (address, 4));
    return bytesToInteger (b[0], b[1], b[2], b[3]);
  }
  
  /*
   * Write a four-byte Big-Endian integer to memory at possibly UNALIGNED address.
   *
   * Unaligned memory access is, in real hardware, slower than aligned access and so clients should use the
   * aligned read and write methods whenever possible.
   *
   */
  public void writeIntegerUnaligned (int address, int value) throws InvalidAddressException {
    writeUnaligned (address, UnsignedByte.toUnsignedBytes (integerToBytes (value)));
  }
  
  
  /////////////////////////
  // Simulator Glue
  //
  
  /**
   * Determin whether two memories store exactly the same data.
   *
   * @param anotherMemory memory object to compare this memory to.
   * @return true iff this memory and anotherMemory are identical.
   */
  public boolean valueEquals (AbstractMainMemory anotherMemory) {
    try {
      return java.util.Arrays.equals (get (0, length()), anotherMemory.get (0, anotherMemory.length ()));
    } catch (InvalidAddressException e) {
      return false;
    }
  }
  
  /**
   * Create a new main memory whose base class and configuraiton is the same as another 
   * memory object.
   *
   * @param aMem the memory object used as the basis for creating this new memory.
   */
  public static AbstractMainMemory newInstance (AbstractMainMemory aMem) {
    Class <?> memClass = aMem.getClass();
    try {
      Constructor<?> memCtor = memClass.getConstructor (int.class);
      return (AbstractMainMemory) memCtor.newInstance (aMem.length ());
    } catch (NoSuchMethodException e) {
      throw new AssertionError (e);
    } catch (InstantiationException e) {
      throw new AssertionError (e);      
    } catch (IllegalAccessException e) {
      throw new AssertionError (e);
    } catch (InvocationTargetException e) {
      throw (RuntimeException) e.getTargetException ();
    }
  }
  
  // Implements DataModel
  
  @Override public Class<?> getColumnClass (int columnIndex) {
    if (columnIndex==0)
      return Integer.class;
    else if (columnIndex==1)
      return Byte.class; 
    else
      throw new AssertionError ();
  }
  
  @Override public int getColumnCount () {
    return 2;
  }
  
  @Override public String getColumnName (int columnIndex) {
    if (columnIndex==0)
      return "Address";
    else if (columnIndex==1)
      return "Value";
    else
      throw new AssertionError ();
  }
  
  @Override public int getRowCount () {
    return length ();
  }
  
  @Override public Object getValueAt (int rowIndex, int columnIndex) {
    if (columnIndex==0)
      return new Integer (rowIndex);
    else if (columnIndex==1)
      try {
	return new Byte (get (rowIndex, 1) [0]);
      } catch (InvalidAddressException e) {
	throw new IndexOutOfBoundsException ();
      }
    else
      throw new AssertionError ();
  }
  
  @Override public boolean isCellEditable (int rowIndex, int columnIndex) {
    if (columnIndex==0)
      return false;
    else if (columnIndex==1)
      return true;
    else
      throw new AssertionError ();
  }
  
  @Override public void setValueAt (Object[] aValue, int rowIndex, int columnIndex) {
    if (columnIndex==1) {
      Vector<TableCellIndex> writtenCells = new Vector<TableCellIndex> ();
      try {
	Byte[] b = (Byte[]) aValue;
	for (int i=0; i<b.length; i++) {
	  set (rowIndex+i, new byte[] {b[i]});
	  writtenCells.add (new TableCellIndex (rowIndex+i, columnIndex));
	}
      } catch (InvalidAddressException e) {
	throw new IndexOutOfBoundsException ();
      } finally {
	tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE, writtenCells));	
      }
    } else
      throw new AssertionError ();
  }
  
  @Override public void setValueAtByUser (Object[] aValue, int rowIndex, int columnIndex) {
    if (columnIndex==1) {
      Vector<TableCellIndex> writtenCells = new Vector<TableCellIndex> ();
      try {
	Byte[] b = (Byte[]) aValue;
	for (int i=0; i<b.length; i++) {
	  set (rowIndex+i, new byte[] {b[i]});
	  writtenCells.add (new TableCellIndex (rowIndex+i, columnIndex));
	}
      } catch (InvalidAddressException e) {
	throw new IndexOutOfBoundsException ();
      } finally {
	tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, writtenCells));	
      }
    } else
      throw new AssertionError ();
  }
  
  @Override public void setValueAt (Object aValue, int rowIndex, int columnIndex) {
    setValueAt (new Byte [] { (Byte) aValue }, rowIndex, columnIndex);
  }
  
  @Override public void setValueAtByUser (Object aValue, int rowIndex, int columnIndex) {
    setValueAtByUser (new Byte[] { (Byte) aValue }, rowIndex, columnIndex);
  }
}