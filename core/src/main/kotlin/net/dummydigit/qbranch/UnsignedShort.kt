// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch

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