// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch

import net.dummydigit.qbranch.exceptions.UnsupportedBondTypeException
import net.dummydigit.qbranch.protocols.TaggedProtocolReader
import net.dummydigit.qbranch.generic.*

/**
 * Deserialize objects of given type.
 */
class Deserializer<out T : Any>(private val typeArg : QTypeArg<T>) {
    private val deserializerImpl = createDeserializer(typeArg)
    /**
     * Deserialize from specified protocol
     * @param reader Passed tagged protocol reader
     * @return Created object
     */
    fun deserialize(reader: TaggedProtocolReader): T {
        val obj = deserializerImpl.deserialize(reader)
        return typeArg.getGenericType().cast(obj)
    }

    private fun createDeserializer(typeArg : QTypeArg<*>) : DeserializerBase {
        return when (typeArg) {
            is StructT<*> -> StructDeserializer(typeArg, isBaseClass = false)
            is VectorT<*> -> VectorDeserializer(createDeserializer(typeArg.elementT))
            is ListT<*> -> ListDeserializer(createDeserializer(typeArg.elementT))
            is SetT<*> -> SetDeserializer(createDeserializer(typeArg.elementT))
            is MapT<*,*> -> MapDeserializer(createDeserializer(typeArg.keyT), createDeserializer(typeArg.valueT))
            is BuiltinQTypeArg.PrimitiveT<*> -> createBuiltinDeserializer(typeArg.dataType)
            else -> throw UnsupportedBondTypeException(typeArg.newInstance().javaClass)
        }
    }

    private fun createBuiltinDeserializer(dataType : BondDataType) : DeserializerBase {
        return when (dataType) {
            BondDataType.BT_BOOL -> { BuiltinTypeDeserializer.Bool }
            BondDataType.BT_INT8 -> { BuiltinTypeDeserializer.Int8 }
            BondDataType.BT_INT16 -> { BuiltinTypeDeserializer.Int16 }
            BondDataType.BT_INT32 -> { BuiltinTypeDeserializer.Int32 }
            BondDataType.BT_UINT8 -> { BuiltinTypeDeserializer.UInt8 }
            BondDataType.BT_UINT16 -> { BuiltinTypeDeserializer.UInt16 }
            BondDataType.BT_UINT32 -> { BuiltinTypeDeserializer.UInt32 }
            BondDataType.BT_UINT64 -> { BuiltinTypeDeserializer.UInt64 }
            BondDataType.BT_FLOAT -> { BuiltinTypeDeserializer.Float }
            BondDataType.BT_DOUBLE -> { BuiltinTypeDeserializer.Double }
            else -> { throw NotImplementedError() }
        }
    }
}