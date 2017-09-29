// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch

import net.dummydigit.qbranch.protocols.TaggedProtocolReader

internal class VectorDeserializer(private val elementDeserializer : DeserializerBase) : DeserializerBase {
    override fun deserialize(reader: TaggedProtocolReader) : Any {
        val header = reader.readContainerHeader()
        val newVector = arrayListOf<Any>()
        for (i in 0 until header.length) {
            val newElement = elementDeserializer.deserialize(reader)
            newVector.add(newElement)
        }
        return newVector
    }
}