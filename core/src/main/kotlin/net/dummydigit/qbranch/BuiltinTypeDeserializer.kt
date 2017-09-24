package net.dummydigit.qbranch

import net.dummydigit.qbranch.protocols.TaggedProtocolReader

internal object BuiltinTypeDeserializer {
    class PrimitiveTypeDeserializer(private val readerFunc : (reader : TaggedProtocolReader) -> Any) : DeserializerBase {
        override fun deserialize(reader: TaggedProtocolReader) : Any = readerFunc(reader)
    }

    val Bool = PrimitiveTypeDeserializer({ reader : TaggedProtocolReader -> reader.readBool() })
    val Int8 = PrimitiveTypeDeserializer({ reader : TaggedProtocolReader -> reader.readInt8() })
    val Int16 = PrimitiveTypeDeserializer( { reader : TaggedProtocolReader -> reader.readInt16() })
    val Int32 = PrimitiveTypeDeserializer( { reader : TaggedProtocolReader -> reader.readInt32() })
    val Int64 = PrimitiveTypeDeserializer( { reader : TaggedProtocolReader -> reader.readInt64() })
    val UInt8 = PrimitiveTypeDeserializer( { reader : TaggedProtocolReader -> reader.readUInt8() })
    val UInt16 = PrimitiveTypeDeserializer( { reader : TaggedProtocolReader -> reader.readUInt16() })
    val UInt32 = PrimitiveTypeDeserializer( { reader : TaggedProtocolReader -> reader.readUInt32() })
    val UInt64 = PrimitiveTypeDeserializer( { reader : TaggedProtocolReader -> reader.readUInt64() })
    val Float = PrimitiveTypeDeserializer( { reader : TaggedProtocolReader -> reader.readFloat() })
    val Double = PrimitiveTypeDeserializer( { reader : TaggedProtocolReader -> reader.readDouble() })
    val ByteString = PrimitiveTypeDeserializer( { reader : TaggedProtocolReader -> reader.readByteString() })
    val WString = PrimitiveTypeDeserializer( { reader : TaggedProtocolReader -> reader.readUTF16LEString() })
}