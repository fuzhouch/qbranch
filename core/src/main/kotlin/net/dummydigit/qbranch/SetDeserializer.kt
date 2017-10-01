// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch

import net.dummydigit.qbranch.protocols.TaggedProtocolReader

internal class SetDeserializer(private val elementDeserializer : DeserializerBase) : DeserializerBase {
    override fun deserialize(reader: TaggedProtocolReader) : Any {
        val header = reader.readContainerHeader()
        val newSet = mutableSetOf<Any>()
        for (i in 0 until header.elementCount) {
            val newElement = elementDeserializer.deserialize(reader)
            newSet.add(newElement)
        }
        return newSet
    }
}