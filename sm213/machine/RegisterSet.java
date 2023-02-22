package machine;

import java.util.List;
import java.util.Vector;
import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;
import util.AbstractDataModel;
import util.DataModelEvent;
import util.UserVisibleException;

/**
 * Set of machine registers.
 */

public class RegisterSet extends AbstractDataModel implements Observer {
  private String name;
  private Vector  <Register>         registers   = new Vector  <Register>         ();
  private HashMap <String, Register> registerMap = new HashMap <String, Register> ();
  
  public class InvalidRegisterNumberException extends Exception {}
  public class InvalidRegisterNameError       extends Error {}
  
  public RegisterSet (String aName) {
    name = aName;
  }
  
  public boolean valueEquals (RegisterSet anotherRegisterSet) {
    boolean isEqualSoFar = registers.size() == anotherRegisterSet.registers.size();
    if (isEqualSoFar)
      for (int i=0; isEqualSoFar && i<registers.size(); i++)
        isEqualSoFar &= registers.get (i).valueEquals (anotherRegisterSet.registers.get (i));
    return isEqualSoFar;
  }
  
  /**
   * Public access to register set as a indexed list of ports
   */
  public class Ports {
    /** 
     * Get the value stored by the specified register.
     */
    public int get (int regIndex) throws InvalidRegisterNumberException {
      return RegisterSet.this.getValue (regIndex);
    }
    /**
     * Set a new value for the specific register to take on in the next cycle.
     */
    public void set (int regIndex, long value) throws InvalidRegisterNumberException {
      RegisterSet.this.setValue (regIndex, value);
    }
  }
  public Ports getPorts () {
    return this.new Ports ();
  }
  
  public Register add (String regName, Class<?> regClass, boolean isUnsigned, boolean isUserEditable, boolean isVisible, long bubbleValue) {
    Register reg = new Register (regName, regClass, isUnsigned, isUserEditable, bubbleValue);
    if (isVisible)
      registers.add (reg);
    registerMap.put (regName, reg);
    reg.addObserver (this);
    return reg;
  }
  
  public Register addUnsigned (String regName, Class<?> regClass, long bubbleValue) {
    return add (regName, regClass, true, false, true, bubbleValue);
  }
  
  public Register addUnsigned (String regName, Class<?> regClass, boolean isUserEditable) {
    return add (regName, regClass, true, isUserEditable, true, 0);
  }
  
  public Register addUnsigned (String regName, Class<?> regClass) {
    return add (regName, regClass, true, false, true, 0);
  }
  
  public Register addSigned (String regName, Class<?> regClass) {
    return add (regName, regClass, false, false, true, 0);
  }
  
  public Register addUnsigned (String regName, boolean isUserEditable) {
    return add (regName, Integer.class, true, isUserEditable, true, 0);
  }
  
  public Register addSigned (String regName, boolean isUserEditable) {
    return add (regName, Integer.class, false, isUserEditable, true, 0);
  }
  
  public Register addUnsigned (String regName) {
    return add (regName, Integer.class, true, false, true, 0);
  }
  
  public Register addSigned (String regName) {
    return add (regName, Integer.class, false, false, true, 0);
  }
  
  public Register getRegister (String aName) {
    return registerMap.get (aName);
  }
  
  public List <Register> getAll () {
    return registers;
  }
  
  public String getName () {
    return name;
  }
  
  private int getValue (int regIndex) throws InvalidRegisterNumberException {
    try {
      return registers.get (regIndex).get ();
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new InvalidRegisterNumberException ();
    }
  }
  
  private void setValue (int regIndex, long value) throws InvalidRegisterNumberException {
    try {
      registers.get (regIndex).set (value);
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new InvalidRegisterNumberException ();
    }
  }
  
  public int getValue (String regName) throws InvalidRegisterNameError {
    try {
      return registerMap.get (regName).get ();
    } catch (NullPointerException e) {
      throw new InvalidRegisterNameError ();
    }
  }
  
  public int getInputValue (String regName) throws InvalidRegisterNameError, Register.TimingException {
    try {
      return registerMap.get (regName).getInput();
    } catch (NullPointerException e) {
      throw new InvalidRegisterNameError ();
    }
  }
  
  public int getInputValue (int regIndex) throws InvalidRegisterNumberException, Register.TimingException {
    try {
      return registers.get (regIndex).getInput();
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new InvalidRegisterNumberException ();
    }
  }
  
  public void setValue (String regName, long value) throws InvalidRegisterNameError {
    try {
      registerMap.get (regName).set (value);
    } catch (NullPointerException e) {
      throw new InvalidRegisterNameError ();
    }
  }
  
  public void tickClock (Register.ClockTransition transition) {
    for (Register r : registerMap.values ())
      r.tickClock (transition);
  }
  
  @Override public void update (Observable o, Object obj) {
    Register       reg   = (Register)       o;
    DataModelEvent event = (DataModelEvent) obj;
    tellObservers (new DataModelEvent (event.getType (), registers.indexOf (reg), 1));
  }
  
  @Override public int getColumnCount () {
    return 2;
  }
  
  @Override public Class<?> getColumnClass (int columnIndex) {
    if (columnIndex==0)
      return String.class;
    else if (columnIndex==1)
      return Long.class;
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
    return registers.size ();
  }
  
  @Override public Object getValueAt (int rowIndex, int columnIndex) {
    return registers.get(rowIndex).getValueAt (rowIndex, columnIndex);
  }
  
  @Override public boolean isCellEditable (int rowIndex, int columnIndex) {
    return registers.get (rowIndex).isCellEditable (0,columnIndex);
  }
  
  @Override public void setValueAt (Object aValue, int rowIndex, int columnIndex) {
    registers.get (rowIndex).setValueAt (aValue, rowIndex, columnIndex);
  }
  
  @Override public void setValueAtByUser (Object aValue, int rowIndex, int columnIndex) {
    registers.get (rowIndex).setValueAtByUser (aValue, rowIndex, columnIndex);
  }
}