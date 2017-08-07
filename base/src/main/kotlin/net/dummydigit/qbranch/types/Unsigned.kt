// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.types

import java.math.BigInteger

// NOTE
// As we know, JVM does not define unsigned types. We can't simply
// map them to signed integers with same length for obvious reason:
// it can cause incorrect data representation.
//
// The implementation tries to address the problem by two steps:
//   1. Apply a marker class to explicitly mark unsigned types.
//   2. Assign a `value' field with larger room to contain given input.
//
// Another limitation

/**
 * Represent an unsigned byte value.
 *
 * @param value Given byte value.
 */
class UnsignedByte(val value : Short = 0) {
    init {
        if (value < 0 || value > 0xFF) {
            throw IllegalArgumentException("NegativeArgToUnsignedByte")
        }
    }
}

/**
 * Represent an unsigned short value.
 *
 * @param value Given byte value.
 */
class UnsignedShort(val value : Int = 0) {
    init {
        if (value < 0 || value > 0xFFFF) {
            throw IllegalArgumentException("NegativeArgToUnsignedShort")
        }
    }
}

/**
 * Represent an unsigned integer value.
 *
 * @param value Given byte value.
 */
class UnsignedInt(val value : Long = 0) {
    init {
        if (value < 0 || value > 0xFFFFFFFF) {
            throw IllegalArgumentException("NegativeArgToUnsignedInt")
        }
    }
}

/**
 * Represent an unsigned long value.
 *
 * @param value Given byte value.
 */
class UnsignedLong(val value : BigInteger = BigInteger("0")) {
    companion object {
        val ZERO : BigInteger = BigInteger("0")
        val MAX_UNSIGNED_LONG_VALUE : BigInteger = BigInteger("18446744073709551615")
    }
    init {
        if (value < ZERO || value > MAX_UNSIGNED_LONG_VALUE) {
            throw IllegalArgumentException("NegativeArgToUnsignedLong")
        }
    }
}

