package net.dummydigit.qbranch

import net.dummydigit.qbranch.protocols.TaggedProtocolReader

internal sealed class BuiltinTypeDeserializer(private val readerFunc : (reader : TaggedProtocolReader) -> Any) : DeserializerBase {
    override fun deserialize(preCreatedObj: Any, reader: TaggedProtocolReader) {
        readerFunc(reader)
    }

    class BoolDeserializer : BuiltinTypeDeserializer({ reader : TaggedProtocolReader -> reader.readBool() })
    class Int8Deserializer : BuiltinTypeDeserializer({ reader : TaggedProtocolReader -> reader.readInt8() })
    class Int16Deserializer : BuiltinTypeDeserializer( { reader : TaggedProtocolReader -> reader.readInt16() })
    class Int32Deserializer : BuiltinTypeDeserializer( { reader : TaggedProtocolReader -> reader.readInt32() })
    class Int64Deserializer : BuiltinTypeDeserializer( { reader : TaggedProtocolReader -> reader.readInt64() })
    class UInt8Deserializer : BuiltinTypeDeserializer( { reader : TaggedProtocolReader -> reader.readUInt8() })
    class UInt16Deserializer : BuiltinTypeDeserializer( { reader : TaggedProtocolReader -> reader.readUInt16() })
    class UInt32Deserializer : BuiltinTypeDeserializer( { reader : TaggedProtocolReader -> reader.readUInt32() })
    class UInt64Deserializer : BuiltinTypeDeserializer( { reader : TaggedProtocolReader -> reader.readUInt64() })
    class FloatDeserializer : BuiltinTypeDeserializer( { reader : TaggedProtocolReader -> reader.readFloat() })
    class DoubleDeserializer : BuiltinTypeDeserializer( { reader : TaggedProtocolReader -> reader.readDouble() })
    class ByteStringDeserializer : BuiltinTypeDeserializer( { reader : TaggedProtocolReader -> reader.readByteString() })
    class WStringDeserializer : BuiltinTypeDeserializer( { reader : TaggedProtocolReader -> reader.readUTF16LEString() })
}