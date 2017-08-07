// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.utils;

import net.dummydigit.qbranch.types.UnsignedByte;
import net.dummydigit.qbranch.types.UnsignedInt;
import net.dummydigit.qbranch.types.UnsignedLong;
import net.dummydigit.qbranch.types.UnsignedShort;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigInteger;

public class ZigZagTest {
    public static final byte[] INT8_VALUES = new byte[11];
    @BeforeClass
    public static void createTestIntegers() {
        INT8_VALUES[0] = 0;
        INT8_VALUES[1] = -1;
        INT8_VALUES[2] = 1;
        INT8_VALUES[3] = -2;
        INT8_VALUES[4] = 2;
        INT8_VALUES[5] = -3;
        INT8_VALUES[6] = 3;
        INT8_VALUES[7] = -4;
        INT8_VALUES[8] = 4;
        INT8_VALUES[9] = -5;
        INT8_VALUES[10] = 5;
    }

    @Test
    public void testConvertInt8() {
        for (int i = 0; i < INT8_VALUES.length; ++i) {
            byte v = INT8_VALUES[i];
            UnsignedByte u8 = ZigZag.signedToUnsigned8(v);
            byte i8 = ZigZag.unsignedToSigned8(u8);
            Assert.assertEquals(i, u8.getValue());
            Assert.assertEquals(v, i8);
        }
    }

    @Test
    public void testConvertInt16() {
        for (int i = 0; i < INT8_VALUES.length; ++i) {
            short v = INT8_VALUES[i];
            UnsignedShort u16 = ZigZag.signedToUnsigned16(v);
            short i16 = ZigZag.unsignedToSigned16(u16);
            Assert.assertEquals(i, u16.getValue());
            Assert.assertEquals(v, i16);
        }
    }

    @Test
    public void testConvertInt32() {
        for (int i = 0; i < INT8_VALUES.length; ++i) {
            int v = INT8_VALUES[i];
            UnsignedInt u32 = ZigZag.signedToUnsigned32(v);
            int i32 = ZigZag.unsignedToSigned32(u32);
            Assert.assertEquals(i, u32.getValue());
            Assert.assertEquals(v, i32);
        }
    }

    @Test
    public void testConvertInt64() {
        for (int i = 0; i < INT8_VALUES.length; ++i) {
            long v = INT8_VALUES[i];
            UnsignedLong u64 = ZigZag.signedToUnsigned64(v);
            long i64 = ZigZag.unsignedToSigned64(u64);
            Assert.assertTrue(BigInteger.valueOf(i).equals(u64.getValue()));
            Assert.assertEquals(v, i64);
        }
    }
}
