// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.
package net.dummydigit.qbranch.generic

import net.dummydigit.qbranch.types.*

/**
 * A collection of built-in types.
 */
object BuiltinTypeArg {

    private class PrimitiveTypeArg<out T : Any>(private val instanceCreator : () -> T) : TypeArg<T> {
        override fun newInstance() = instanceCreator()
    }

    @JvmField val Int8T : TypeArg<Byte> = PrimitiveTypeArg<Byte>({ 0 })
    @JvmField val Int16T : TypeArg<Short> = PrimitiveTypeArg<Short>({ 0 })
    @JvmField val Int32T : TypeArg<Int> = PrimitiveTypeArg({ 0 })
    @JvmField val Int64T : TypeArg<Long> = PrimitiveTypeArg<Long>({ 0 })
    @JvmField val UInt8T : TypeArg<UnsignedByte> = PrimitiveTypeArg({ UnsignedByte() })
    @JvmField val UInt16T : TypeArg<UnsignedShort> = PrimitiveTypeArg({ UnsignedShort() })
    @JvmField val UInt32T : TypeArg<UnsignedInt> = PrimitiveTypeArg({ UnsignedInt() })
    @JvmField val UInt64T : TypeArg<UnsignedLong> = PrimitiveTypeArg({ UnsignedLong() })
    @JvmField val ByteStringT : TypeArg<ByteString> = PrimitiveTypeArg({ ByteString() })
    @JvmField val WStringT : TypeArg<String> = PrimitiveTypeArg({ "" })
    @JvmField val FloatT : TypeArg<Float> = PrimitiveTypeArg({ 0.0f })
    @JvmField val DoubleT : TypeArg<Double> = PrimitiveTypeArg({ 0.0 })
    @JvmField val BlobT : TypeArg<Blob> = PrimitiveTypeArg( { Blob() })
    // TODO: Bonded?
}