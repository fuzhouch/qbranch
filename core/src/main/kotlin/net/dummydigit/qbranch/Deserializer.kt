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
        return typeArg.getGenericType().cast(deserializerImpl.deserialize(reader))
    }

    private fun createDeserializer(typeArg : QTypeArg<*>) : DeserializerBase {
        return when (typeArg) {
            is StructT -> StructDeserializer(typeArg.getGenericType(), isBaseClass = false)
            is VectorT<*> -> VectorDeserializer(createDeserializer(typeArg.elementT))
            is ListT<*> -> ListDeserializer(createDeserializer(typeArg.elementT))
            is SetT<*> -> SetDeserializer(createDeserializer(typeArg.elementT))
            is MapT<*,*> -> MapDeserializer(createDeserializer(typeArg.keyT), createDeserializer(typeArg.valueT))
            is BuiltinQTypeArg.PrimitiveQTypeArg<*> -> createBuiltinDeserializer(typeArg.getGenericType())
            else -> throw UnsupportedBondTypeException(typeArg.getGenericType())
        }
    }

    private fun createBuiltinDeserializer(builtinType : Class<*>) : DeserializerBase {
        return when (builtinType) {
            Boolean::class.java -> BuiltinTypeDeserializer.Bool
            Byte::class.java -> BuiltinTypeDeserializer.Int8
            Short::class.java -> BuiltinTypeDeserializer.Int16
            Int::class.java -> BuiltinTypeDeserializer.Int32
            Long::class.java -> BuiltinTypeDeserializer.Int64
            UnsignedByte::class.java -> BuiltinTypeDeserializer.UInt8
            UnsignedShort::class.java -> BuiltinTypeDeserializer.UInt16
            UnsignedInt::class.java -> BuiltinTypeDeserializer.UInt32
            UnsignedLong::class.java -> BuiltinTypeDeserializer.UInt64
            Float::class.java -> BuiltinTypeDeserializer.Float
            Double::class.java -> BuiltinTypeDeserializer.Double
            ByteString::class.java -> BuiltinTypeDeserializer.ByteString
            String::class.java -> BuiltinTypeDeserializer.WString
            else -> throw UnsupportedBondTypeException(builtinType)
        }
    }
}