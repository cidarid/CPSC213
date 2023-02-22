package arch.sm213.machine;

import machine.AbstractCPU;
import machine.AbstractMainMemory;
import machine.Register;
import machine.RegisterSet;
import util.HalfByteNumber;
import util.SixByteNumber;

/**
 * Infrastructure for executing an SM213 CPU implementation.
 */

public abstract class AbstractSM213CPU extends AbstractCPU {
  /** Internal machine registers. */
  protected RegisterSet      ps;
  /** Page table base register (for virtual memory implementstions). */
  private   Register         ptbrReg=null;
  protected Register.Port    ptbr=null;
  /** Program counter (address of next instruction). */
  protected Register.Port    pc;
  /** Value of current instruction (in its entirety). */
  protected Register.Port    instruction;  
  /** Opcode. */
  protected Register.Port    insOpCode; 
  /** Operand 0. */
  protected Register.Port    insOp0; 
  /** Operand 1. */
  protected Register.Port    insOp1; 
  /** Operand 2. */
  protected Register.Port    insOp2; 
  /** Immediate-value operand. */
  protected Register.Port    insOpImm; 
  /** Extended operand (extra 4 bytes). */
  protected Register.Port    insOpExt;
  /** Special register used to store address of current instruction for gui. */
  private Register    curInst; 
  /** Physical memory (in case mem, is virtual memory) */
  protected AbstractMainMemory.Port physMem;
  
  /**
   * Create a new CPU.
   *
   * @param name            fully-qualified name of CPU implementation.
   * @param memory          main memory used by CPU.
   */
  public AbstractSM213CPU (String name, AbstractMainMemory memory) {
    super (name, memory);
    physMem=mem;
    for (int r=0; r<8; r++)
      is.regFile.addSigned (String.format ("r%d", r), true);
    ps = new RegisterSet ("");
    is.processorState.add (ps);
    pc          = ps.addUnsigned ("PC",          Integer.class, true).getNonClockedPort();
    instruction = ps.addUnsigned ("Instruction", SixByteNumber.class).getNonClockedPort();
    insOpCode   = ps.addUnsigned ("Ins Op Code", HalfByteNumber.class).getNonClockedPort();
    insOp0      = ps.addUnsigned ("Ins Op 0",    HalfByteNumber.class).getNonClockedPort();
    insOp1      = ps.addUnsigned ("Ins Op 1",    HalfByteNumber.class).getNonClockedPort();
    insOp2      = ps.addUnsigned ("Ins Op 2",    HalfByteNumber.class).getNonClockedPort();
    insOpImm    = ps.addSigned   ("Ins Op Imm",  Byte.class).getNonClockedPort();
    insOpExt    = ps.addSigned   ("Ins Op Ext",  Integer.class).getNonClockedPort();
    curInst     = ps.add         (AbstractCPU.InternalState.CURRENT_INSTRUCTION_ADDRESS, Integer.class, true, false, false, -1);
  }
  
  /**
   * Enable Virtual Memory address translation.
   */
  public void enableVirtualMemory() {
    ptbrReg = ps.addUnsigned ("PTBR", Integer.class, true);
    ptbr    = ptbrReg.getNonClockedPort();
    mem     = is.memImp.getPort (new VirtualMemoryMMU());
  }
  
  /**
   * MMU class for Virtual Memory, used by AbstractMainMemory to create VM Port
   */
  class VirtualMemoryMMU extends AbstractMainMemory.MMU {
    @Override public int translate (int address) throws AbstractMainMemory.InvalidAddressException {
      return translateAddress (address);
    }
  }
  
  /**
   * Translate address from virtual to physical.
   * By default there is no translation.  Subclasses extend to implement virtual memory.
   *
   * @param va                                          virtual address
   * @return                                            physical address
   * @throws AbstractMainMemory.InvalidAddressException if address is not valid
   */
  public int translateAddress (int va) throws AbstractMainMemory.InvalidAddressException {
    return va;
  }

  @Override
  public void setSyscallReturn (int retVal) throws InvalidInstructionException, MachineHaltException, AbstractMainMemory.InvalidAddressException, Register.TimingException, ImplementationException {
    try {
      reg.set(0, retVal);
    } catch (RegisterSet.InvalidRegisterNumberException ire) {
      throw new InvalidInstructionException ();
    }
  }

  /**
   * Compute one cycle of the SM213 CPU
   */
  protected void cycle () throws InvalidInstructionException, MachineHaltException, AbstractMainMemory.InvalidAddressException {
    try {
      try {
	curInst.set (pc.get ());
	fetch ();
      } finally {
        if (ptbr!=null)
          ptbr.set (ptbr.get());
	tickClock ();
      }
      try {
	execute ();
      } finally {
        if (ptbr!=null)
          ptbr.set (ptbr.get());
	tickClock ();
      }
    } catch (RegisterSet.InvalidRegisterNumberException ire) {
      throw new InvalidInstructionException ();
    }
  }
  
  /**
   * Set PC.  
   *
   * @param aPC New memory address for PC.
   */
  @Override public void resetMachineToPC (int aPC) {
    int p=0;
    if (ptbr!=null)
      p = ptbr.get();
    ps.tickClock (Register.ClockTransition.BUBBLE);
    if (ptbr!=null) {
      ptbr.set          (p);
      ptbrReg.tickClock (Register.ClockTransition.NORMAL);
    }
    super.resetMachineToPC (aPC);
  }
  
  /**
   * Fetch next instruction from memory into CPU processorState registers.
   *
   * @throws AbstractMainMemory.InvalidAddressException Program counter stores an invalid memory address for fetching a new instruction.
   */
  protected abstract void fetch () throws AbstractMainMemory.InvalidAddressException;
  
  /**
   * Execute instruction currently loaded in processorState registers.
   *
   * @throws AbstractCPU.InvalidInstructionException    instruction is invalid.
   * @throws AbstractCPU.MachineHaltException           instruction is the halt instruction.
   * @throws RegisterSet.InvalidRegisterNumberException instruction encodes an invalid register number.
   * @throws AbstractMainMemory.InvalidAddressException instruction encodes an invalid memory address.
   */
  protected abstract void execute () throws InvalidInstructionException, MachineHaltException, RegisterSet.InvalidRegisterNumberException, AbstractMainMemory.InvalidAddressException ;
  
  /**
   * Implement a clock tick by telling registerFile and processorState to save their current
   * input values and to start presenting them on their outputs.
   */
  private void tickClock () {
    is.regFile.tickClock (Register.ClockTransition.NORMAL);
    if (ptbr!=null)
      ptbr.set (ptbr.get());
    ps.tickClock  (Register.ClockTransition.NORMAL);
  }
}