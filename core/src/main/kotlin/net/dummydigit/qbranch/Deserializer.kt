// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch

import kotlin.reflect.KClass
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import net.dummydigit.qbranch.protocols.TaggedProtocolReader
import net.dummydigit.qbranch.generic.StructT
import net.dummydigit.qbranch.generic.TypeArg

/**
 * Deserialize objects of given type.
 */
class Deserializer<T : QBranchSerializable>(private val instanceCreator: TypeArg<T>,
                                            private val charset: Charset) {
    constructor(instanceCreator: TypeArg<T>) : this(instanceCreator, StandardCharsets.UTF_8)
    constructor(targetCls: Class<T>) : this(StructT(targetCls))
    constructor(targetCls: KClass<T>) : this(StructT(targetCls.java))

    // Note: So the creation of deserializer can be expensive
    private val cls = instanceCreator.newInstance().javaClass

    init {
        buildDeserializerInternal()
    }

    /**
     * Main deserialization function.
     */
    fun deserialize(reader: TaggedProtocolReader): T {
        // TODO: Will be changed when implementing full logic.
        val obj : T = instanceCreator.newInstance()
        val impl = StructDeserializer(obj.javaClass, charset)
        return impl.deserialize(obj, reader, false) as T
    }

    private fun buildDeserializerInternal() {
        // Step 1: Analyze each field of given T so we know
        //         how many fields need to be deserialized.
        //         build deserialization table.
        // Step 2: Build each deserializer, which should be
        //         a recursive structure.
    }
}