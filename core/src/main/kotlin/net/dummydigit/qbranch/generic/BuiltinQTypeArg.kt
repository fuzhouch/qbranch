// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.
package net.dummydigit.qbranch.generic

import net.dummydigit.qbranch.types.*

/**
 * A collection of built-in types.
 */
object BuiltinQTypeArg {

    private class PrimitiveQTypeArg<out T : Any>(private val instanceCreator : () -> T) : QTypeArg<T> {
        override fun newInstance() = instanceCreator()
    }

    @JvmField val Int8T : QTypeArg<Byte> = PrimitiveQTypeArg<Byte>({ 0 })
    @JvmField val Int16T : QTypeArg<Short> = PrimitiveQTypeArg<Short>({ 0 })
    @JvmField val Int32T : QTypeArg<Int> = PrimitiveQTypeArg({ 0 })
    @JvmField val Int64T : QTypeArg<Long> = PrimitiveQTypeArg<Long>({ 0 })
    @JvmField val UInt8T : QTypeArg<UnsignedByte> = PrimitiveQTypeArg({ UnsignedByte() })
    @JvmField val UInt16T : QTypeArg<UnsignedShort> = PrimitiveQTypeArg({ UnsignedShort() })
    @JvmField val UInt32T : QTypeArg<UnsignedInt> = PrimitiveQTypeArg({ UnsignedInt() })
    @JvmField val UInt64T : QTypeArg<UnsignedLong> = PrimitiveQTypeArg({ UnsignedLong() })
    @JvmField val ByteStringT : QTypeArg<ByteString> = PrimitiveQTypeArg({ ByteString() })
    @JvmField val WStringT : QTypeArg<String> = PrimitiveQTypeArg({ "" })
    @JvmField val FloatT : QTypeArg<Float> = PrimitiveQTypeArg({ 0.0f })
    @JvmField val DoubleT : QTypeArg<Double> = PrimitiveQTypeArg({ 0.0 })
    @JvmField val BlobT : QTypeArg<Blob> = PrimitiveQTypeArg( { Blob() })
    // TODO: Bonded?
}