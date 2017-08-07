// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.BitSet;
import net.dummydigit.qbranch.types.UnsignedInt;
import net.dummydigit.qbranch.types.UnsignedLong;
import net.dummydigit.qbranch.types.UnsignedShort;

// By May 2017 Kotlin does not support bit operations yet.
// We have to implement bit ops related functions in Java.

/**
 * Variable int encoder/decoder helper functions.
 */
public class VariableLength {

    static final int MAX_VAR_UINT16_BYTES = 3;
    static final int MAX_VAR_UINT32_BYTES = 5;
    static final int MAX_VAR_UINT64_BYTES = 10;

    /**
     * Decode uint16 from input stream, returned as UnsignedShort.
     * @param input Byte stream input.
     * @return An UnsignedShort object as return value.
     * @throws IOException Throws IOException when input can't read enough bytes.
     */
    public static UnsignedShort decodeVarUInt16(InputStream input) throws IOException {
        int value = 0, shift = 0, readBytes = 0;
        byte oneByte;
        do {
            oneByte = (byte)input.read();
            value |= ((oneByte & 0x7F) << shift);
            shift += 7;
            readBytes += 1;
        } while (!isHighBitZero((oneByte)) && readBytes < MAX_VAR_UINT16_BYTES);
        return new UnsignedShort(value);
    }

    /**
     * Decode uint32 from intput stream, returned as UnsignedInt
     * @param input Byte stream input.
     * @return An UnsignedInt object as returned value.
     * @throws IOException Throws IOException when input can't read enough bytes.
     */
    public static UnsignedInt decodeVarUInt32(InputStream input) throws IOException {
        long value = 0;
        int shift = 0, readBytes = 0;
        byte oneByte;
        do {
            oneByte = (byte)input.read();
            value |= ((oneByte & 0x7F) << shift);
            shift += 7;
            readBytes += 1;
        } while (!isHighBitZero((oneByte)) && readBytes < MAX_VAR_UINT32_BYTES);
        return new UnsignedInt(value);
    }

    /**
     * Decode uint64 from intput stream, returned as UnsignedLong
     * @param input Byte stream input.
     * @return An UnsignedLong object as returned value.
     * @throws IOException Throws IOException when input can't read enough bytes.
     */
    public static UnsignedLong decodeVarUInt64(InputStream input) throws IOException {
        BitSet payload = new BitSet(MAX_VAR_UINT64_BYTES * 8);
        byte[] payloadAsBitSet = decodeVarUInt64(input, payload).toByteArray();
        return new UnsignedLong(new BigInteger(payloadAsBitSet));
    }

    /**
     * Decode uint64 from input stream, returned as Bitset.
     * @param input Byte stream input.
     * @param payload A Bitset object as return value. Must call .clear() by caller.
     * @return Reference of passed payload parameter.
     * @throws IOException Throws IOException when input can't read enough bytes.
     */
    private static BitSet decodeVarUInt64(InputStream input, BitSet payload) throws IOException {
        int bytesRead = 0;
        byte oneByte;
        int shift = 0;
        BitSet bigEndianPayload = new BitSet(payload.length());
        do {
            oneByte = (byte)input.read();
            int perBytePayload = oneByte & 0x7F;
            bytesRead += 1;
            for (int i = 0; i < 7; ++i) {
                if ((perBytePayload >> i & 0x1) == 1) {
                    bigEndianPayload.set(shift + i);
                }
            }
            shift += 7;
        } while (!isHighBitZero((oneByte)) && bytesRead < MAX_VAR_UINT64_BYTES);

        // Need to revert to little endian, or we won't get correct number
        for (int nBytes = 0; nBytes < bytesRead; ++nBytes) {
            for (int inByteOffset = 0; inByteOffset < 8; ++inByteOffset) {
                if (bigEndianPayload.get(nBytes * 8 + inByteOffset)) {
                    int reverseOffset = (bytesRead - (nBytes + 1)) * 8 + inByteOffset;
                    payload.set(reverseOffset);
                }
            }
        }

        return payload;
    }

    private static Boolean isHighBitZero(byte value) {
        return (value & 0x80) == 0;
    }
}
