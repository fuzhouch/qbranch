// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch

/**
 * Represent an unsigned byte value.
 *
 * @param value Given byte value.
 */
class UnsignedByte(val value : Short = 0) : QBranchSerializable {
    init {
        if (value < 0 || value > 0xFF) {
            throw IllegalArgumentException("NegativeArgToUnsignedByte")
        }
    }
}