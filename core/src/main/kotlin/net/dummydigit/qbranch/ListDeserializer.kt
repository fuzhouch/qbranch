// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch

import net.dummydigit.qbranch.exceptions.SchemaMismatchException
import net.dummydigit.qbranch.protocols.TaggedProtocolReader

internal class ListDeserializer(private val elementDeserializer : DeserializerBase) : DeserializerBase {
    override fun deserialize(reader: TaggedProtocolReader) : Any {
        val header = reader.readContainerHeader()
        if (header.containerType != BondDataType.BT_LIST) {
            throw SchemaMismatchException(header.containerType, "TODO")
        }
        val newList = mutableListOf<Any>()
        for (i in 0 until header.length) {
            val newElement = elementDeserializer.deserialize(reader)
            newList.add(newElement)
        }
        return newList
    }
}