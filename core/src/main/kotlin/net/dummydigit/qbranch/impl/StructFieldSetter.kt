// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.impl

import net.dummydigit.qbranch.BondDataType
import net.dummydigit.qbranch.exceptions.SchemaMismatchException
import net.dummydigit.qbranch.generic.ListT
import net.dummydigit.qbranch.generic.MapT
import net.dummydigit.qbranch.generic.SetT
import net.dummydigit.qbranch.generic.VectorT
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

    class Vector(field : Field,
                 private val creatorField : Field,
                 private val elementDeserializer : DeserializerBase) : StructFieldSetter(field) {

        override fun set(obj: Any, reader: TaggedProtocolReader) {
            val header = reader.readContainerHeader()
            if (header.containerType != BondDataType.BT_LIST) {
                throw SchemaMismatchException(header.containerType, field.name)
            }
            val containerT = creatorField.get(obj) as VectorT<*>
            val newVector = arrayListOf<Any>()
            for (i in 0 until header.length) {
                val newElement = containerT.newElement()
                elementDeserializer.deserialize(newElement, reader)
                newVector.add(newElement)
            }
            field.set(obj, newVector)
        }
    }

    class List(field : Field,
                 private val creatorField : Field,
                 private val elementDeserializer : DeserializerBase) : StructFieldSetter(field) {

        override fun set(obj: Any, reader: TaggedProtocolReader) {
            val header = reader.readContainerHeader()
            if (header.containerType != BondDataType.BT_LIST) {
                throw SchemaMismatchException(header.containerType, field.name)
            }
            val containerT = creatorField.get(obj) as ListT<*>
            val newList = mutableListOf<Any>()
            for (i in 0 until header.length) {
                val newElement = containerT.newElement()
                elementDeserializer.deserialize(newElement, reader)
                newList.add(newElement)
            }
            field.set(obj, newList)
        }
    }

    class Set(field : Field,
               private val creatorField : Field,
               private val elementDeserializer : DeserializerBase) : StructFieldSetter(field) {

        override fun set(obj: Any, reader: TaggedProtocolReader) {
            val header = reader.readContainerHeader()
            if (header.containerType != BondDataType.BT_SET) {
                throw SchemaMismatchException(header.containerType, field.name)
            }
            val containerT = creatorField.get(obj) as SetT<*>
            val newSet = mutableSetOf<Any>()
            for (i in 0 until header.length) {
                val newElement = containerT.newElement()
                elementDeserializer.deserialize(newElement, reader)
                newSet.add(newElement)
            }
            field.set(obj, newSet)
        }
    }

    class Map(field : Field,
              private val creatorField : Field,
              private val keyDeserializer : DeserializerBase,
              private val valueDeserializer : DeserializerBase) : StructFieldSetter(field) {

        override fun set(obj: Any, reader: TaggedProtocolReader) {
            val header = reader.readContainerHeader()
            if (header.containerType != BondDataType.BT_MAP) {
                throw SchemaMismatchException(header.containerType, field.name)
            }
            val containerT = creatorField.get(obj) as MapT<*,*>
            val newMap = mutableMapOf<Any, Any>()
            for (i in 0 until header.length) {
                val newKey = containerT.newKey()
                keyDeserializer.deserialize(newKey, reader)
                val newValue = containerT.newValue()
                valueDeserializer.deserialize(newValue, reader)
                newMap.put(newKey, newValue)
            }
            field.set(obj, newMap)
        }
    }

    class Struct(field : Field, private val deserializer : DeserializerBase) : StructFieldSetter(field) {
        override fun set(obj: Any, reader: TaggedProtocolReader) {
            deserializer.deserialize(field.get(obj), reader)
        }
    }
}