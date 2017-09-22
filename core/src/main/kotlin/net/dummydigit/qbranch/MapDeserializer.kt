// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch

import net.dummydigit.qbranch.protocols.TaggedProtocolReader

internal class MapDeserializer(private val keyDeserializer : DeserializerBase,
                      private val valueDeserializer : DeserializerBase) : DeserializerBase {
    override fun deserialize(preCreatedObj: Any, reader: TaggedProtocolReader) {
    }
}