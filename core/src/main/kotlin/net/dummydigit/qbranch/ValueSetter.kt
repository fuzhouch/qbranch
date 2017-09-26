package net.dummydigit.qbranch

import net.dummydigit.qbranch.protocols.TaggedProtocolReader
import java.lang.reflect.Field

internal class ValueSetter(private val field : Field, private val deserializer : DeserializerBase) {
    fun set(obj: Any, reader: TaggedProtocolReader) {
        val fieldObj = deserializer.deserialize(reader)
        field.set(obj, fieldObj)
    }
}