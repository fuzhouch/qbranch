// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import net.dummydigit.qbranch.protocols.TaggedProtocolReader

/**
 * Deserialize objects of given type.
 */
class Deserializer<T>(targetCls: Class<T>, charset: Charset) {
    constructor(targetCls: Class<T>) : this(targetCls, StandardCharsets.UTF_8)

    val cls = targetCls
    private val deserializerImpl = StructDeserializer(targetCls, charset)

    fun deserialize(reader: TaggedProtocolReader): T {
        val obj = cls.newInstance()
        return cls.cast(deserializerImpl.deserialize(obj as Any, reader, false))
    }
}