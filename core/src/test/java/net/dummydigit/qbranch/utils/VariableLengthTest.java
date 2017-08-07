// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import net.dummydigit.qbranch.types.UnsignedInt;
import net.dummydigit.qbranch.types.UnsignedLong;
import net.dummydigit.qbranch.types.UnsignedShort;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

public class VariableLengthTest {
    final static short UINT16_FULL_LENGTH = 21845; // 0x5555 = b(0101010101010101)
    final static int UINT32_FULL_LENGTH = 1431655765; // 0x55555555
    final static long UINT64_FULL_LENGTH = 6148914691236517205L; // 0x5555555555555555
    final static byte[] UINT16_FULL_LENGTH_BYTES = new byte[VariableLength.MAX_VAR_UINT16_BYTES];
    final static byte[] UINT32_FULL_LENGTH_BYTES = new byte[VariableLength.MAX_VAR_UINT32_BYTES];
    final static byte[] UINT64_FULL_LENGTH_BYTES = new byte[VariableLength.MAX_VAR_UINT64_BYTES];

    final static short UINT16_ZERO = 0;
    final static byte[] UINT16_ZERO_BYTES = new byte[1];
    @BeforeClass
    public static void createTestIntegers() {
        // Convert UInt16 to VarUInt16 bytes, in little endian:
        // Raw value    = 0b 01010101 01010101 (high -> low)
        // Add flag bit -> 0b "0"01 "1"0101010 "1"1010101
        //              -> 0x01 0xaa 0xd5
        UINT16_FULL_LENGTH_BYTES[0] = (byte)0xd5;
        UINT16_FULL_LENGTH_BYTES[1] = (byte)0xaa;
        UINT16_FULL_LENGTH_BYTES[2] = 0x1;
        UINT16_ZERO_BYTES[0] = 0;

        // 32-bit number to VarUint32 bytes, in little endian
        // Raw value    = 0b 01010101 01010101 01010101 01010101 (high -> low)
        // Add flag bit -> 0b "0"0101 "1"0101010  "1"1010101  "1"0101010   "1"1010101
        //              -> 0x5 0xaa 0xd5 0xaa 0xd5
        UINT32_FULL_LENGTH_BYTES[0] = (byte)0xd5;
        UINT32_FULL_LENGTH_BYTES[1] = (byte)0xaa;
        UINT32_FULL_LENGTH_BYTES[2] = (byte)0xd5;
        UINT32_FULL_LENGTH_BYTES[3] = (byte)0xaa;
        UINT32_FULL_LENGTH_BYTES[4] = (byte)0x5;

        // 64-bit number to VarUint64 bytes, in little endian
        // Raw value    = 0b 01010101 01010101 01010101 01010101 01010101 01010101 01010101 01010101
        // (high -> low)
        // Add flag bit -> "0"0 "1"1010101 "1"0101010  "1"1010101  "1"0101010 (high)
        //                 "1"1010101  "1"0101010  "1"1010101 "1"0101010   "1"1010101 (low)
        UINT64_FULL_LENGTH_BYTES[0] = (byte)0xd5;
        UINT64_FULL_LENGTH_BYTES[1] = (byte)0xaa;
        UINT64_FULL_LENGTH_BYTES[2] = (byte)0xd5;
        UINT64_FULL_LENGTH_BYTES[3] = (byte)0xaa;
        UINT64_FULL_LENGTH_BYTES[4] = (byte)0xd5;
        UINT64_FULL_LENGTH_BYTES[5] = (byte)0xaa;
        UINT64_FULL_LENGTH_BYTES[6] = (byte)0xd5;
        UINT64_FULL_LENGTH_BYTES[7] = (byte)0xaa;
        UINT64_FULL_LENGTH_BYTES[8] = (byte)0xd5;
        UINT64_FULL_LENGTH_BYTES[9] = (byte)0x0;
    }

    @Test
    public void testValidDecodeVarUInt16() {
        try {
            UnsignedShort decoded16 = VariableLength.decodeVarUInt16(new ByteArrayInputStream(UINT16_FULL_LENGTH_BYTES));
            Assert.assertEquals(decoded16.getValue(), UINT16_FULL_LENGTH);

            decoded16 = VariableLength.decodeVarUInt16(new ByteArrayInputStream(UINT16_ZERO_BYTES));
            Assert.assertEquals(decoded16.getValue(), UINT16_ZERO);
        } catch (IOException e) {
            Assert.fail("UnexpectedIOExceptionFromTest:Uint16");
        }
    }

    @Test
    public void testValidDecodeVarUInt32() {
        try {
            UnsignedInt decoded32 = VariableLength.decodeVarUInt32(new ByteArrayInputStream(UINT32_FULL_LENGTH_BYTES));
            Assert.assertEquals(decoded32.getValue(), UINT32_FULL_LENGTH);
        } catch (IOException e) {
            Assert.fail("UnexpectedIOExceptionFromTest:UInt32");
        }
    }

    @Test
    public void testValidDecodeVarUInt64() {
        try {
            UnsignedLong decoded64 = VariableLength.decodeVarUInt64(new ByteArrayInputStream(UINT64_FULL_LENGTH_BYTES));
            Assert.assertTrue(decoded64.getValue().equals(BigInteger.valueOf(UINT64_FULL_LENGTH)));
        } catch (IOException e) {
            Assert.fail("UnexpectedIOExceptionFromTest:UInt64");
        }
    }
}
