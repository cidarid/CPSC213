package machine;

import java.util.HashMap;
import java.lang.reflect.Constructor;
import util.AbstractDataModel;
import util.DataModelEvent;
import util.HalfByteNumber;
import util.SixByteNumber;
import util.UserVisibleException;

public class Register extends AbstractDataModel {
  private static HashMap <Class<?>, Integer> classBitLengths = new HashMap <Class<?>, Integer> ();
  private String                          name;
  private boolean                         isUserEditable;
  private boolean                         isUnsigned;
  private long                            outputValue;
  private long                            inputValue;
  private boolean                         inputStable;
  private long                            bubbleValue;
  private Class <?>                       valueClass;
  private long                            signExtend, valueMask, signMask;

  /**
   * Specifics the type of action register takes when the clock ticks.
   */
  public enum ClockTransition {
    /** Normal clock transition: register accepts value on its input port when clock ticks. */
    NORMAL, 
    /** Pipeline stall: register rejects its input value and keeps its current value when clock ticks. */
    STALL, 
    /** Inject pipeline bubble: register takes-on bubble value when clock ticks. */
    BUBBLE
  }
  
  /**
   * Indicates that CPU implementation has read from the register's input port, but no value 
   * was written to that port.  Often indicates a deadlock in the pipelined implementation.  
   *
   * This exception is an artifact of the mapping of truely parallel hardware to the 
   * partically-sequential software model.  In software, each pipeline stage is a sequential
   * piece of code, but the stages themselves are executed in parallel, each in their own thread.
   * It is thus acceptable for one stage to read a register's input as long as that stage
   * has already written to it in this cycle or if another stage will.  
   */
  public class TimingException extends UserVisibleException {
    final static String MESSAGE = "Timing error on register %s";
    public TimingException () {
      super (String.format (MESSAGE, name));
    }
    public TimingException (int aPC) {
      super (String.format (MESSAGE, name), aPC);
    }
  }
  
  /**
   * Register's value port.  Combined input and output operations to provide view for
   * non-pipelined implementations that never read the register's input port.
   */
  
  public class Port {
    /**
     * Get the register's value (i.e., the value of on its output port).
     */
    public int get () {
      return Register.this.get();
    }
    /**
     * Get the register's value as unsigned (i.e., the value on its output port).
     */
    public int getUnsigned () {
      return Register.this.getUnsigned();
    }
    /**
     * Set a new value for this register (i.e., set the value of its input port).  This is the value the register will take in the next cycle.
     */
    public void set (long aValue) {
      Register.this.set (aValue);
    }
  }
  /**
   * Get the register's value port object.
   */
  public Port getPort () {
    return this.new Port ();
  }
  
  /**
   * Register's non-clocked port value.  Simplified view in which get returns the last value set for
   * implementions where clock behaviour is abstracted away.
   */
  
  public class NonClockedPort extends Port {
    /**
     * Get the value of this register's output port.
     */
    public int get () {
      if (inputStable) 
        try {
          return Register.this.getInput();
        } catch (TimingException te) {
          throw new AssertionError (te);
        }
      else
        return Register.this.get();
    }
    /**
     * Get the value of this register's output port and treat it as unsigned.
     */
    public int getUnsigned () {
      if (inputStable) 
        try {
          return Register.this.getInputUnsigned();
        } catch (TimingException te) {
          throw new AssertionError (te);
        }
      else
        return Register.this.getUnsigned();
    }
    /**
     * Set a new value for this register's input port.  This is the value the register will take in the next cycle.
     */
    public void set (long aValue) {
      Register.this.set (aValue);
    }
  }
  /**
   * Get the register's simplified value port object.
   */
  public NonClockedPort getNonClockedPort() {
    return this.new NonClockedPort();
  }
  
  /**
   * Register's output port.
   */
  public class OutputPort {
    /**
     * Get the value of this register's output port.
     */
    public int get () {
      return Register.this.get ();
    }
  }
  /**
   * Get the register's output-port object.
   */
  public OutputPort getOutputPort () {
    return this.new OutputPort ();
  }
  
  /**
   * Register's input port.
   */
  public class InputPort {
    /**
     * Get the value of this register's input port, blocking if necessary until port is set in current cycle.
     */
    public int getValueProduced () throws TimingException {
      return Register.this.getInput ();
    }
    /**
     * Set a new value for this register's input port.  This is the value the register will take in the next cycle.
     */
    public void set (long aValue) {
      Register.this.set (aValue);
    }
  }
  /**
   * Get the register's input-port object.
   */
  public InputPort getInputPort () {
    return this.new InputPort ();
  }
  
  static {
    classBitLengths.put (HalfByteNumber.class, 4);
    classBitLengths.put (Byte.class,           8);
    classBitLengths.put (Short.class,          16);
    classBitLengths.put (Integer.class,        32);
    classBitLengths.put (SixByteNumber.class,  48);
    classBitLengths.put (Long.class,           64);    
  }
  
  Register (String aName, Class<?> aValueClass, boolean anIsUnsigned, boolean anIsUserEditable, long aBubbleValue) {
    name           = aName;
    isUserEditable = anIsUserEditable;
    isUnsigned     = anIsUnsigned; 
    valueClass     = aValueClass;
    bubbleValue    = aBubbleValue;
    inputValue     = bubbleValue;
    outputValue    = bubbleValue;
    initTwosComplementMasks ();
  }
  
  public boolean valueEquals (Register anotherRegister) {
     return outputValue==anotherRegister.outputValue;
  }
  
  private void initTwosComplementMasks () {
    Integer bitLength = classBitLengths.get (valueClass);
    if (bitLength==null)
      throw new AssertionError ();
    if (bitLength!=64) {
      signExtend = ((long) -1) << bitLength;
      valueMask  = ~signExtend;
      signMask   = 1 << (bitLength-1);
    } else {
      signExtend = 0;
      valueMask  = (0xffffffffL << 32) | 0xffffffff;
      signMask   = 0;
    }
  }
  
  private int signExtend (int value) {
    return (int) (value | (((value & signMask) != 0)? signExtend : 0)); 
  }
  
  private Class<?> getInputValueWrapperClass () {
    if (valueClass == HalfByteNumber.class)
      return byte.class;
    else if (valueClass == Byte.class)
      return byte.class;
    else if (valueClass == Short.class)
      return short.class;
    else if (valueClass == Integer.class)
      return int.class;
    else 
      return long.class;
  }
  
  private Number castNumberToWrapper (long number) {
    if (valueClass == HalfByteNumber.class)
      return new Byte ((byte) number);
    else if (valueClass == Byte.class)
      return new Byte ((byte) number);
    else if (valueClass == Short.class)
      return new Short ((short) number);
    else if (valueClass == Integer.class)
      return new Integer ((int) number);
    else 
      return new Long (number);
  }
  
  public String getName () {
    return name;
  }
  
  private synchronized void setSilently (long aValue) {
    inputValue  = aValue & valueMask;
    inputStable = true;
    notifyAll ();
  }
  
  public void set (long aValue) {
    setSilently (aValue);
    tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE, 0, 1));
  }
  
  public int get () {
    return isUnsigned? getUnsigned () : signExtend (getUnsigned ());
  }
  
  public int getUnsigned () {
    tellObservers (new DataModelEvent (DataModelEvent.Type.READ, 0, 1));
    return (int) outputValue;
  }
  
  public synchronized int getInput () throws TimingException {
    return isUnsigned? getInputUnsigned () : signExtend (getInputUnsigned ());
  }
  
  public synchronized int getInputUnsigned () throws TimingException {
    try {
      if (! inputStable)
	wait (2000);
      if (! inputStable)
	throw new InterruptedException ();
      tellObservers (new DataModelEvent (DataModelEvent.Type.READ, 0, 1));
      return (int) inputValue;
    } catch (InterruptedException e) {
      throw new TimingException ();
    }
  }
  
  public synchronized void tickClock (ClockTransition transition) {
    switch (transition) {
      case NORMAL:
	outputValue = inputValue;
	break;
      case STALL:
	inputValue  = outputValue;
	break;
      case BUBBLE:
	outputValue = bubbleValue;
	inputValue  = bubbleValue;
	break;
    }
    inputStable = false;
    tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, 0, 1));
  }
  
  @Override public int getColumnCount () {
    return 2;
  }
  
  @Override public Class<?> getColumnClass (int columnIndex) {
    if (columnIndex==0)
      return String.class;
    else if (columnIndex==1)
      return valueClass;
    else
      throw new AssertionError ();
  }
  
  @Override public String getColumnName (int columnIndex) {
    if (columnIndex==0)
      return "Reg";
    else if (columnIndex==1)
      return "Value";
    else
      throw new AssertionError ();
  }
  
  @Override public int getRowCount () {
    return 1;
  }
  
  @Override public Object getValueAt (int rowIndex, int columnIndex) {
    if (columnIndex==0)
      return name;
    else if (columnIndex==1) {
      try {
	Constructor<?> constructor = valueClass.getConstructor (getInputValueWrapperClass ());
	return constructor.newInstance (castNumberToWrapper (inputValue));
      } catch (Exception e) {
	throw new AssertionError (e);
      }
    } else
      throw new AssertionError ();
  }
  
  @Override public boolean isCellEditable (int rowIndex, int columnIndex) {
    if (columnIndex==0)
      return false;
    else if (columnIndex==1)
      return isUserEditable;
    else
      throw new AssertionError ();
  }
  
  @Override public void setValueAt (Object aValue, int rowIndex, int columnIndex) {
    if (columnIndex==1) {
      if (!(aValue instanceof Number))
	throw new ClassCastException ();
      setSilently (((Number)aValue).longValue ());
      tickClock (ClockTransition.NORMAL);
      tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE, 0, 1));
    } else
      throw new AssertionError ();
  }  
  
  @Override public void setValueAtByUser (Object aValue, int rowIndex, int columnIndex) {
    if (columnIndex==1) {
      if (!(aValue instanceof Number))
	throw new ClassCastException ();
      setSilently (((Number)aValue).longValue ());
      tickClock (ClockTransition.NORMAL);
      tellObservers (new DataModelEvent (DataModelEvent.Type.WRITE_BY_USER, 0, 1));
    } else
      throw new AssertionError ();
  }  
}