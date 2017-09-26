// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch

/**
 * Represent an unsigned integer value.
 *
 * @param value Given byte value.
 */
class UnsignedInt(val value : Long = 0) : QBranchSerializable{
    init {
        if (value < 0 || value > 0xFFFFFFFF) {
            throw IllegalArgumentException("NegativeArgToUnsignedInt")
        }
    }
}