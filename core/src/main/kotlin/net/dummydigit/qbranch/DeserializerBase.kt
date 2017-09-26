package net.dummydigit.qbranch

import net.dummydigit.qbranch.generic.*
import net.dummydigit.qbranch.protocols.TaggedProtocolReader

internal interface DeserializerBase {
    fun deserialize(reader: TaggedProtocolReader) : Any

    companion object {
        fun createDeserializerByTypeArg(typeArg : QTypeArg<*>) : DeserializerBase {
            return when (typeArg) {
                is VectorT<*> -> {
                    val elementDeserializer = createDeserializerByTypeArg(typeArg.elementT)
                    VectorDeserializer(elementDeserializer)
                }

                is SetT<*> -> {
                    val elementDeserializer = createDeserializerByTypeArg(typeArg.elementT)
                    SetDeserializer(elementDeserializer)
                }

                is ListT<*> -> {
                    val elementDeserializer = createDeserializerByTypeArg(typeArg.elementT)
                    ListDeserializer(elementDeserializer)
                }

                is MapT<*, *> -> {
                    val keyDeserializer = createDeserializerByTypeArg(typeArg.keyT)
                    val valueDeserializer = createDeserializerByTypeArg(typeArg.valueT)
                    MapDeserializer(keyDeserializer, valueDeserializer)
                }

                is StructT<*> -> {
                    StructDeserializer(typeArg, isBaseClass = false)
                }

                is BuiltinQTypeArg.PrimitiveT<*> -> {
                    when (typeArg.dataType) {
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

                else -> { throw NotImplementedError() }
            }
        }
    }
}