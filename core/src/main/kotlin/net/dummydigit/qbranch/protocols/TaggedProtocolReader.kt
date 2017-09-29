// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.protocols

import net.dummydigit.qbranch.*

/**
 * Reader interface for tagged protocols.
 */
interface TaggedProtocolReader {
    fun readBool() : Boolean
    fun readInt8() : Byte
    fun readInt16() : Short
    fun readInt32() : Int
    fun readInt64() : Long
    fun readUInt8() : UnsignedByte
    fun readUInt16() : UnsignedShort
    fun readUInt32() : UnsignedInt
    fun readUInt64() : UnsignedLong
    fun readByteString() : ByteString
    fun readUTF16LEString() : String
    fun readFloat() : Float
    fun readDouble() : Double
    fun readContainerHeader() : ContainerHeaderInfo
    fun parseNextField(): FieldInfo
    fun skipField(dataType : BondDataType)
}
