// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch

import kotlin.reflect.KClass
import net.dummydigit.qbranch.protocols.TaggedProtocolReader
import net.dummydigit.qbranch.generic.StructT
import net.dummydigit.qbranch.StructDeserializer

/**
 * Deserialize objects of given type.
 */
class Deserializer<T : QBranchSerializable>(private val instanceCreator: StructT<T>) {
    constructor(targetCls: Class<T>) : this(StructT(targetCls))
    constructor(targetCls: KClass<T>) : this(StructT(targetCls.java))

    // Note: So the creation of deserializer can be expensive.
    // Don't try to create it all the time.
    private val cls = instanceCreator.newInstance().javaClass
    private val deserializers = buildDeserializerInternal()

    /**
     * Deserialize from specified protocol
     * @param reader Passed tagged protocol reader
     * @return Created object
     */
    fun deserialize(reader: TaggedProtocolReader): T {
        val obj : T = instanceCreator.newInstance()
        deserializers.forEach {
            it.deserialize(it.cls.cast(obj), reader)
        }
        return obj
    }

    private fun buildDeserializerInternal() : List<net.dummydigit.qbranch.StructDeserializer> {
        val deserializers = arrayListOf(net.dummydigit.qbranch.StructDeserializer(cls, false))
        var superClass = cls.superclass
        while (superClass != Object::class.java) {
            deserializers.add(net.dummydigit.qbranch.StructDeserializer(superClass, true))
            superClass = cls.superclass
        }
        return deserializers.reversed() // Start from base class
    }
}