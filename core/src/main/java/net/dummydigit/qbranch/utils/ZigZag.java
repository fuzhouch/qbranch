// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.utils;

import net.dummydigit.qbranch.UnsignedByte;
import net.dummydigit.qbranch.UnsignedInt;
import net.dummydigit.qbranch.UnsignedLong;
import net.dummydigit.qbranch.UnsignedShort;

import java.math.BigInteger;

/**
 * Contains util functions for decoding/encoding ZigZag format.
 */
public class ZigZag {
    // Doc REF: https://developers.google.com/protocol-buffers/docs/encoding

    /**
     * Convert uint8 to signed int8 in Zigzag encoding.
     * @param uByte Unsigned int8 value.
     * @return Matched signed int8.
     */
    static byte unsignedToSigned8(UnsignedByte uByte) {
        short value = uByte.getValue();
        return (byte)((value >> 1) ^ (~( value & 1 ) + 1));
    }

    /**
     * Convert signed int8 to unsigned int8 in Zigzag encoding.
     * @param value Signed int8 value.
     * @return Matched unsigned int8.
     */
    static UnsignedByte signedToUnsigned8(byte value) {
        return new UnsignedByte((short)((value << 1) ^ (value >> 7)));
    }

    /**
     * Convert uint16 to signed int16 in Zigzag encoding.
     * @param uShort Unsigned int16 value.
     * @return Matched signed int16.
     */
    public static short unsignedToSigned16(UnsignedShort uShort) {
        int value = uShort.getValue();
        return (short)((value >> 1) ^ (~( value & 1 ) + 1));
    }

    /**
     * Convert signed int16 to unsigned int16 in Zigzag encoding.
     * @param value Signed int16 value.
     * @return Matched unsigned int16.
     */
    static UnsignedShort signedToUnsigned16(short value) {
        return new UnsignedShort(((value << 1) ^ (value >> 15)));
    }

    /**
     * Convert uint32 to signed int32 in Zigzag encoding.
     * @param uInt Unsigned int32 value.
     * @return Matched signed int32.
     */
    public static int unsignedToSigned32(UnsignedInt uInt) {
        long value = uInt.getValue();
        return (int)((value >> 1) ^ (~( value & 1 ) + 1));
    }

    /**
     * Convert signed int32 to unsigned int32 in Zigzag encoding.
     * @param value Signed int32 value.
     * @return Matched unsigned int32.
     */
    static UnsignedInt signedToUnsigned32(int value) {
        return new UnsignedInt((long)((value << 1) ^ (value >> 31)));
    }

    /**
     * Convert unsigned int64 to signed int64 in Zigzag encoding.
     * @param uLong Unsigned int64 value.
     * @return Matched signed int64.
     */
    public static long unsignedToSigned64(UnsignedLong uLong) {
        BigInteger left = uLong.getValue().shiftRight(1);
        BigInteger right = BigInteger.valueOf((~(uLong.getValue().testBit(0)? 1 : 0) + 1));
        return left.xor(right).longValue();
    }

    /**
     * Convert signed int64 to signed int64 in Zigzag encoding.
     * @param value Signed int64 value.
     * @return Matched unsigned int64.
     */
    static UnsignedLong signedToUnsigned64(long value) {
        BigInteger left = BigInteger.valueOf(value).shiftLeft(1);
        BigInteger right = BigInteger.valueOf(value >> 63);
        return new UnsignedLong(left.xor(right));
    }
}
