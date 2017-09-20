package net.dummydigit.qbranch.impl

import net.dummydigit.qbranch.protocols.TaggedProtocolReader

interface DeserializerBase {
    fun deserialize(preCreatedObj: Any, reader: TaggedProtocolReader)
}