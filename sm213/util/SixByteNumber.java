package util;

public class SixByteNumber extends Number {
  long value;
  public SixByteNumber (long aValue) {
    value = aValue;
  }
  public byte byteValue () {
    return (byte) value;
  }
  public short shortValue () {
    return (short) value;
  }
  public int intValue () {
    return (int) value;
  }
  public long longValue () {
    return value;
  }
  public double doubleValue () {
    return (double) value;
  }
  public float floatValue () {
    return (float) value;
  }
  public Number toNumber () {
    return new Long (value);
  }
}
