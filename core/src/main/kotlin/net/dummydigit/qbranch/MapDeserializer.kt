// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch

import net.dummydigit.qbranch.exceptions.SchemaMismatchException
import net.dummydigit.qbranch.protocols.TaggedProtocolReader

internal class MapDeserializer(private val keyDeserializer : DeserializerBase,
                               private val valueDeserializer : DeserializerBase) : DeserializerBase {
    override fun deserialize(reader: TaggedProtocolReader) : Any {
        val header = reader.readKvpContainerHeader()
        val newMap = mutableMapOf<Any, Any>()
        for (i in 0 until header.kvpCount) {
            val newKey = keyDeserializer.deserialize(reader)
            val newValue = valueDeserializer.deserialize(reader)
            newMap.put(newKey, newValue)
        }
        return newMap
    }
}