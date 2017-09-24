package net.dummydigit.qbranch

import net.dummydigit.qbranch.protocols.TaggedProtocolReader

internal interface DeserializerBase {
    fun deserialize(reader: TaggedProtocolReader) : Any
}