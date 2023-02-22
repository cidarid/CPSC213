package util;

public class HalfByteNumber extends Number {
  byte value;
  public HalfByteNumber (byte aValue) {
    value = aValue;
  }
  public byte byteValue () {
    return value;
  }
  public short shortValue () {
    return (short) value;
  }
  public int intValue () {
    return (int) value;
  }
  public long longValue () {
    return (long) value;
  }
  public double doubleValue () {
    return (double) value;
  }
  public float floatValue () {
    return (float) value;
  }
  public Number toNumber () {
    return new Byte (value);
  }
}

