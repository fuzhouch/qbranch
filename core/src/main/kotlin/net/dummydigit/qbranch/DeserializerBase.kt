package net.dummydigit.qbranch

import net.dummydigit.qbranch.protocols.TaggedProtocolReader

interface DeserializerBase {
    fun deserialize(preCreatedObj: Any, reader: TaggedProtocolReader)
}