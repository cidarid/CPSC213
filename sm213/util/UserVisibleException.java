package util;

import java.util.HashMap;

/**
 * The idea  is that exceptions that are thrown from the class the defines an
 * exception that extends UserVisibleException will be treated as the "natural"
 * exception of a sequence of exceptions that are thrown from elsewhere and that all
 * of these subsequent unnatrual exceptions will take their identity (i.e., stack trace
 * from that preceeding natural exception.
 */

public class UserVisibleException extends Exception {
  private static String                        VISIBLE_CLASS_PREFIX = "arch.";
  private static HashMap <Class<?>, Exception> lastNatural          = new HashMap <Class<?>, Exception> ();
  private        Exception                     naturalInstance      = null;
  private        int                           pc                   = -1;
  
  {
    if (!getStackTrace()[0].getClassName().startsWith (VISIBLE_CLASS_PREFIX)) {
      naturalInstance = this;
      lastNatural.put (getClass(), this);
    } else
      naturalInstance = lastNatural.get (getClass());
  }
  
  public UserVisibleException () {
    super ();
  }
  
  public UserVisibleException (String aMessage, int aPC, Exception aCause) {
    super (aMessage);
    pc = aPC;
    if (aCause!=null) {
      naturalInstance = aCause;
      lastNatural.put (getClass(), aCause);
    }
  }
  
  public UserVisibleException (String aMessage, Exception aCause) {
    this (aMessage, -1, aCause);
  }
  
  public UserVisibleException (String aMessage) {
    this (aMessage, -1, null);
  }
  
  public UserVisibleException (String aMessage, int aPC) {
    this (aMessage, aPC, null);
  }
  
  @Override public String getMessage () {
    String dbgMsg = "";
    if (naturalInstance!=null)
      for (StackTraceElement ste : naturalInstance.getStackTrace())
        if (ste.getClassName().startsWith (VISIBLE_CLASS_PREFIX) && ste.getLineNumber()>0) {
          String[] parts     = ste.getClassName().split ("\\.");
          String   className = parts.length>0? parts[parts.length-1]: ste.getClassName();
          dbgMsg = String.format (", in %s.%s at line %d", className, ste.getMethodName(), ste.getLineNumber());
          break;
        }
    return super.getMessage().concat (pc!=-1? String.format (" at PC 0x%x", pc): "").concat (dbgMsg).concat (".");
  }
  
  public void setPC (int aPC) {
    pc = aPC;
  }
}