// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch

import java.lang.reflect.Field
import net.dummydigit.qbranch.protocols.TaggedProtocolReader

internal sealed class StructFieldSetter(structField: Field) {
    val field = structField

    abstract fun set(obj: Any, reader: TaggedProtocolReader)

    class Bool(field: Field) : StructFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readBool())
    }

    class Int8(field: Field) : StructFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readInt8())
    }

    class Int16(field: Field) : StructFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readInt16())
    }

    class Int32(field: Field) : StructFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readInt32())
    }

    class Int64(field: Field) : StructFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readInt64())
    }

    class UInt8(field: Field) : StructFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readUInt8())
    }

    class UInt16(field: Field) : StructFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readUInt16())
    }

    class UInt32(field: Field) : StructFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readUInt32())
    }

    class UInt64(field: Field) : StructFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readUInt64())
    }

    class ByteString(field: Field) : StructFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readByteString())
    }

    class UTF16LEString(field: Field) : StructFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readUTF16LEString())
    }

    class Float(field: Field) : StructFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readFloat())
    }

    class Double(field: Field) : StructFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) = field.set(obj, reader.readDouble())
    }
}