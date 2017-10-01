// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch

import net.dummydigit.qbranch.protocols.TaggedProtocolReader

internal class ListDeserializer(private val elementDeserializer : DeserializerBase) : DeserializerBase {
    override fun deserialize(reader: TaggedProtocolReader) : Any {
        val header = reader.readContainerHeader()
        val newList = mutableListOf<Any>()
        for (i in 0 until header.elementCount) {
            val newElement = elementDeserializer.deserialize(reader)
            newList.add(newElement)
        }
        return newList
    }
}