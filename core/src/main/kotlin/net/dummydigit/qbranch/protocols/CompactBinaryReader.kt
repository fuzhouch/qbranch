// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.protocols

import net.dummydigit.qbranch.*
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.charset.Charset
import net.dummydigit.qbranch.exceptions.EndOfStreamException
import net.dummydigit.qbranch.exceptions.UnsupportedVersionException
import net.dummydigit.qbranch.utils.VariableLength
import net.dummydigit.qbranch.utils.ZigZag

/**
 * Reader to process CompactBinary protocols (v1 only)
 */
class CompactBinaryReader(inputStream : InputStream, version : Int, charset: Charset) : TaggedProtocolReader {
    private val input = inputStream
    private val defaultStringCharset = charset

    constructor(inputStream : InputStream) : this(inputStream, 1, Charsets.UTF_8)
    constructor(inputStream : InputStream, version : Int) : this(inputStream, version, Charsets.UTF_8)

    init {
        if (version != 1) {
            throw UnsupportedVersionException("protocol=CompactBinary:version=$version")
        }
    }

    override fun readBool() : Boolean = input.read() == 0
    override fun readInt8() : Byte = input.read().toByte()
    override fun readUInt8() : UnsignedByte = UnsignedByte(input.read().toShort())
    override fun readInt16() : Short = ZigZag.unsignedToSigned16(readUInt16())
    override fun readUInt16(): UnsignedShort = VariableLength.decodeVarUInt16(input)
    override fun readUInt32(): UnsignedInt = VariableLength.decodeVarUInt32(input)
    override fun readInt32(): Int = ZigZag.unsignedToSigned32(readUInt32())
    override fun readInt64(): Long = ZigZag.unsignedToSigned64(readUInt64())
    override fun readUInt64(): UnsignedLong = VariableLength.decodeVarUInt64(input)
    override fun readByteString(): ByteString {
        val rawBytes = readRawStringBytes(1) ?: return ByteString("", defaultStringCharset)
        return ByteString(rawBytes, defaultStringCharset)
    }
    override fun readUTF16LEString(): String {
        // Following C#/Windows convention, we assume
        // we read UTF16 bytes. However, it may not be portable
        // on non-Windows platforms.
        val rawBytes = readRawStringBytes(2) ?: return ""
        return String(rawBytes, Charsets.UTF_16LE)
    }

    override fun readFloat(): Float {
        val floatAsBytes = ByteArray(4)
        val bytesRead = input.read(floatAsBytes)
        if (bytesRead != 4) {
            throw EndOfStreamException(4, bytesRead)
        }
        floatAsBytes.reverse()
        return ByteBuffer.wrap(floatAsBytes).float
    }

    override fun readDouble(): Double {
        val doubleAsBytes = ByteArray(8)
        val bytesRead = input.read(doubleAsBytes)
        if (bytesRead != 8) {
            throw EndOfStreamException(8, bytesRead)
        }
        doubleAsBytes.reverse()
        return ByteBuffer.wrap(doubleAsBytes).double
    }

    override fun readContainerHeader() : ContainerHeaderInfo {
        return CompactBinaryFieldInfo.decodeContainerHeaderV1(input)
    }

    override fun skipField(dataType : BondDataType) {
        when (dataType) {
            BondDataType.BT_BOOL -> input.read()
            BondDataType.BT_UINT8 -> input.read()
            BondDataType.BT_UINT16 -> readUInt16()
            BondDataType.BT_UINT32 -> readUInt32()
            BondDataType.BT_UINT64 -> readUInt64()
            BondDataType.BT_FLOAT -> readFloat()
            BondDataType.BT_DOUBLE -> readDouble()
            BondDataType.BT_STRING -> readByteString()
            BondDataType.BT_INT8 -> input.read()
            BondDataType.BT_INT16 -> readInt16()
            BondDataType.BT_INT32 -> readInt32()
            BondDataType.BT_INT64 -> readInt64()
            BondDataType.BT_WSTRING -> readUTF16LEString()
            // TODO Implement container type in next version.
            // BondDataType.BT_STRUCT ->
            // BondDataType.BT_LIST ->
            // BondDataType.BT_SET ->
            // BondDataType.BT_MAP ->
            BondDataType.BT_STOP -> throw IllegalStateException("skip=BT_STOP")
            BondDataType.BT_STOP_BASE -> throw IllegalStateException("skip=BT_STOP_BASE")
            BondDataType.BT_UNAVAILABLE -> throw IllegalStateException("skip=BT_UNAVAILABLE")
            else -> throw NotImplementedError("$dataType")
        }
        // TODO
        throw NotImplementedError("skipField")
    }

    private fun readRawStringBytes(charLen : Int) : ByteArray? {
        val stringLength = readUInt32().value.toInt()
        if (stringLength == 0) {
            return null
        }

        val rawBytes = ByteArray(stringLength * charLen)
        val bytesRead = input.read(rawBytes)
        if (bytesRead != stringLength * charLen) {
            throw EndOfStreamException(stringLength * charLen, bytesRead)
        }
        return rawBytes
    }

    override fun parseNextField() = CompactBinaryFieldInfo(input)
}