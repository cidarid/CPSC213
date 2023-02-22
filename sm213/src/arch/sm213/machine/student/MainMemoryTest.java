package arch.sm213.machine.student;

import machine.AbstractMainMemory;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import machine.AbstractMainMemory.InvalidAddressException;

public class MainMemoryTest {
    MainMemory m;

    // Initializer beforeEach method
    @BeforeEach
    void beforeEach() {
        m = new MainMemory(1024);
    }

    // Tests the access aligned method with values that should be aligned (address is divisible by length)
    @Test
    void testCorrectAlignment() {
        assertTrue(m.isAccessAligned(16, 4));
        assertTrue(m.isAccessAligned(0, 32));
        assertTrue(m.isAccessAligned(94, 2));
        assertTrue(m.isAccessAligned(-30, 2));
    }

    // Tests the access aligned method with values that should be misaligned
    @Test
    void testIncorrectAlignment() {
        assertFalse(m.isAccessAligned(30, 4));
        assertFalse(m.isAccessAligned(71, 2));
        assertFalse(m.isAccessAligned(1, 16));
        assertFalse(m.isAccessAligned(-33, 4));
    }

    // Tests the integer to bytes method by comparing an expected byte array representation
    // of a hex value to the actual representation
    @Test
    void testIntegerToBytes() {
        byte[] expectedBytes = {0, 0, 0, 0x32};
        byte[] actualBytes = m.integerToBytes(0x32);
        byteArraysEquals(expectedBytes, actualBytes);
        expectedBytes = new byte[]{0x41, 0x29, 0x30, 0x45};
        actualBytes = m.integerToBytes(0x41293045);
        byteArraysEquals(expectedBytes, actualBytes);
    }

    // Tests the bytes to integer method by comparing an expected hex
    // representation of a byte array to the actual representation
    @Test
    void testBytesToInteger() {
        assertEquals(0x32, m.bytesToInteger((byte)0, (byte)0, (byte)0, (byte)0x32));
        assertEquals(0x12345678, m.bytesToInteger((byte)0x12, (byte)0x34, (byte)0x56, (byte)0x78));
    }

    // Tests the getter and setter methods for the memory by writing 4 bytes to memory
    // and then getting those same 4 bytes, and making sure that the bytes are the same
    // as they were at the beginning. Overwriting is also tested.
    @Test
    void testGetterAndSetter() {
        try {
            byte[] writtenBytes = {1, 2, 3, 4};
            m.set(20, writtenBytes);
            byte[] retrievedBytes = m.get(20, 4);
            byteArraysEquals(writtenBytes, retrievedBytes);
            byte[] writtenBytes2 = new byte[]{5, 6, 7, 8};
            m.set(20, writtenBytes2);
            byte[] retrievedBytes2 = m.get(20, 4);
            byteArraysEquals(writtenBytes2, retrievedBytes2);
        } catch (InvalidAddressException e) {
            fail("Invalid address");
        }
    }

    // Tests an error case for the setter method by passing an address out of scope,
    // One case attempts to access address 2048 when only 1024 addresses are available
    // The other case attempts to access address -1 when only 0-1024 should be available,
    // this makes sure that negative numbers are not allowed even if their absolute value
    // is within the scope
    @Test
    void testSetterInvalidAddresses() {
        try {
            m.set(2048, new byte[]{});
            fail("Should have thrown exception for out of range");
        } catch (InvalidAddressException e) {
            // pass
        }
        try {
            m.set(-1, new byte[]{});
            fail("Should have thrown exception for negative");
        } catch (InvalidAddressException e) {
            // pass
        }
    }

    // Tests an error case for the getter method by passing an address out of scope,
    // in this case attempting to access address 2048 when only 1024 addresses are available
    // The other case attempts to access address -1 when only 0-1024 should be available,
    // this makes sure that negative numbers are not allowed even if their absolute value
    // is within the scope
    @Test
    void testGetterInvalidAddress() {
        try {
            m.get(2048, 4);
            fail("Should have thrown exception for out of range");
        } catch (InvalidAddressException e) {
            // pass
        }
        try {
            m.get(-1, 4);
            fail("Should have thrown exception for negative");
        } catch (InvalidAddressException e) {
            // pass
        }
    }

    // Helper method to assert that the elements of two byte arrays are equal
    void byteArraysEquals(byte[] a, byte[] b) {
        if (a.length != b.length) {
            fail("Byte arrays are not equal length");
        }
        for (int i = 0; i < a.length; i++) {
            assertEquals(a[i], b[i]);
        }
    }
}
