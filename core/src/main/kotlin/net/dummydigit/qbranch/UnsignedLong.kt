// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.package net.dummydigit.qbranch
package net.dummydigit.qbranch

import java.math.BigInteger

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