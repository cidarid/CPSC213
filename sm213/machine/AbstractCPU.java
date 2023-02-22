package machine;

import java.util.Vector;
import java.util.Observable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import util.DataModel;
import util.UserVisibleException;

public abstract class AbstractCPU extends Observable {
  
  /** Fully-qualified name */
  final private String name;
  /** Interrupt flag.*/
  private boolean isInterrupt;
  
  /**
   * AbstractCPU internal state.  Some of this is public for other parts of application, but
   * this state should be treated as private by the implementors of concrete CPU subclasses.
   */
  public static class InternalState {
    /** Main memory. */
    public final AbstractMainMemory   memImp;
    /** General purpose register file.  */
    public final RegisterSet          regFile = new RegisterSet  ("Register File");
    /** Processor state registers. */
    public final Vector <RegisterSet> processorState = new Vector <RegisterSet> ();
    /** Name of program-counter register (i.e., storing next instruction's address). */
    public final static String        PC                          = "PC";
    /** Name of register that stores the current instruction's address. */
    public final static String        CURRENT_INSTRUCTION_ADDRESS = "CurrentInstructionAddress";
    InternalState (AbstractMainMemory aMemImp) {
      memImp = aMemImp;
    }
  }
  
  /** Internal state not accessed by concerete sub-classes. */
  public final InternalState is;
  
  /** General purpose register file. */
  protected RegisterSet.Ports reg;
  
  /** Link to CPU's main memory. */
  protected AbstractMainMemory.Port mem;
  
  /**
   * Create a new CPU.
   *
   * @param aName fully-qualified name of CPU implementation.
   * @param aMem  main memory used by CPU.
   */
  public AbstractCPU (String aName, AbstractMainMemory aMem) {
    is   = new InternalState (aMem);
    name = aName;
    mem  = is.memImp.getPort();
    reg  = is.regFile.getPorts();
  }
  
  /**
   * Create a new instance of the same underlying type and with the same constructor parameters
   * as existing cpu.  
   *
   * @param aCPU CPU object who's class and value is used as the basis of the new CPU.
   */
  public static AbstractCPU newInstance (AbstractCPU aCPU) {
    AbstractMainMemory newMemory = AbstractMainMemory.newInstance (aCPU.is.memImp);
    Class <?> cpuClass           = aCPU.getClass();
    try {
      Constructor<?> cpuCtor = cpuClass.getConstructor (String.class, AbstractMainMemory.class);
      return (AbstractCPU) cpuCtor.newInstance (aCPU.getName(), newMemory);
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
  
  /** 
   * Get value of CPU's main memory.
   */
  public AbstractMainMemory   getMainMemory ()     { return is.memImp; }
  
  /**
   * Get the CPU's register file.
   */
  public RegisterSet          getRegisterFile ()   { return is.regFile; }
  
  /**
   * Get the CPU's processor state.
   */
  public Vector <RegisterSet> getProcessorState () { return is.processorState;  }
  
  /**
   * Get fully-qualified name of this implementation.
   */
  public String getName () {
    return name;
  }
  
  /**
   * Get the program-counter value.
   */
  public DataModel getPC () {
     return is.processorState.get(0).getRegister (InternalState.PC);
  }
  
  /**
   * Reset Machine to specified PC, clearing other machine state
   *
   */
  public void resetMachineToPC (int aPC) {
    Register pc = is.processorState.get(0).getRegister (InternalState.PC);
    pc.set (aPC);
    pc.tickClock (Register.ClockTransition.NORMAL);
  }
  
  /**
   * Exception indicating that an invalid instruction was just detected by the CPU.
   */
  public class InvalidInstructionException extends UserVisibleException {
    final static String MESSAGE = "Invalid instruction";
    public InvalidInstructionException () {
      super (MESSAGE);
    }
    public InvalidInstructionException (int aPC) {
      super (MESSAGE, aPC);
    }
    public InvalidInstructionException (Exception aCause) {
      super (MESSAGE, aCause);
    }
    public InvalidInstructionException (Exception aCause, int aPC) {
      super (MESSAGE, aPC, aCause);
    }
  }
  
  /** 
   * Exception indicating that the CPU just retired a halt instruction.
   */
  public class MachineHaltException extends UserVisibleException {
    final static String MESSAGE = "HALT instruction executed";
    public MachineHaltException () {
      super (MESSAGE);
    }
    public MachineHaltException (int aPC) {
      super (MESSAGE, aPC, null);
    }
  }
  
  /**
   * Exception indicating an internal error in CPU implementation.  Usually a student bug.
   */
  public class ImplementationException extends UserVisibleException {
    final static String MESSAGE = "Implementation error, %s";
    public ImplementationException () {
      super ("");
    }
    public ImplementationException (RuntimeException aCause) {
      super (String.format (MESSAGE, aCause.getMessage()), aCause);
    }
    public ImplementationException (RuntimeException aCause, int aPC) {
      super (String.format (MESSAGE, aCause.getMessage()), aPC, aCause);
    }
  }

  /**
   * Receive the result of a system call.
   */
  public void setSyscallReturn(int retVal) throws InvalidInstructionException, MachineHaltException, AbstractMainMemory.InvalidAddressException, Register.TimingException, ImplementationException {
    throw new ImplementationException();
  }

  /**
   * Compute one cycle of CPU.
   *
   * @throws AbstractCPU.InvalidInstructionException    attempted to execute an invalid instruction.
   * @throws AbstractCPU.MachineHaltException           halt instruction executed.
   * @throws AbstractMainMemory.InvalidAddressException attempted to execute an instruction that accesses an invalid memory address.
   * @throws Register.TimingException                   if instruction stalls on reading register input port.
   * @throws AbstractCPU.ImplementationException        wrapper for all other exception for reporting to UI.
   */
  protected abstract void cycle () throws InvalidInstructionException, MachineHaltException, AbstractMainMemory.InvalidAddressException, Register.TimingException, ImplementationException;
  
  /**
   * Trigger an interrupt on CPU.  If CPU is executing in start() it stops by returning from start().
   */
  public synchronized void triggerInterrupt () {
    isInterrupt = true;
  }
  
  /**
   * Determing whether CPU has a pending interrupt.
   *
   * @return true iff triggerInterrupt has been called, but start() has not yet stopped as a result of that
   * interrupt.
   */
  public boolean isInterrupt () {
    return isInterrupt;
  }
  
  /**
   * Start processor execution.  Continues executing until an exception is thrown or an interrupt is
   * triggered.  If start() returns without throwing an exception then an interrupt has occured.
   *
   * @throws AbstractCPU.InvalidInstructionException    attempted to execute an invalid instruction.
   * @throws AbstractCPU.MachineHaltException           halt instruction executed.
   * @throws AbstractMainMemory.InvalidAddressException attempted to execute an instruction that accesses an invalid memory address.
   * @throws Register.TimingException                   if instruction stalls on reading register input port.
   * @throws AbstractCPU.ImplementationException        wrapper for all other exception for reporting to UI.
   */
  public void start () throws InvalidInstructionException, MachineHaltException, AbstractMainMemory.InvalidAddressException, Register.TimingException, ImplementationException {
    isInterrupt = false;
    
    while (true) {      
      
      setChanged      ();
      notifyObservers ();
      
      if (isInterrupt) {
        isInterrupt = false;
        return;
      }

      cycle ();
      
    }
  }
}

