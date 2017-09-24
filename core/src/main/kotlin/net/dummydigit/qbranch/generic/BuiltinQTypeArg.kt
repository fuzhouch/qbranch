// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.
package net.dummydigit.qbranch.generic

import net.dummydigit.qbranch.*

/**
 * A collection of built-in types.
 */
object BuiltinQTypeArg {

    internal class PrimitiveQTypeArg<T : Any>(private val instanceCreator : () -> T,
                                                 private val cls : Class<T>) : QTypeArg<T> {
        override fun newInstance() = instanceCreator()
        override fun getGenericType() = cls
    }

    @JvmField val BoolT : QTypeArg<Boolean> = PrimitiveQTypeArg({ false }, Boolean::class.java)
    @JvmField val Int8T : QTypeArg<Byte> = PrimitiveQTypeArg({ 0 }, Byte::class.java)
    @JvmField val Int16T : QTypeArg<Short> = PrimitiveQTypeArg({ 0 }, Short::class.java)
    @JvmField val Int32T : QTypeArg<Int> = PrimitiveQTypeArg({ 0 }, Int::class.java)
    @JvmField val Int64T : QTypeArg<Long> = PrimitiveQTypeArg({ 0 }, Long::class.java)
    @JvmField val UInt8T : QTypeArg<UnsignedByte> = PrimitiveQTypeArg({ UnsignedByte() }, UnsignedByte::class.java)
    @JvmField val UInt16T : QTypeArg<UnsignedShort> = PrimitiveQTypeArg({ UnsignedShort() }, UnsignedShort::class.java)
    @JvmField val UInt32T : QTypeArg<UnsignedInt> = PrimitiveQTypeArg({ UnsignedInt() }, UnsignedInt::class.java)
    @JvmField val UInt64T : QTypeArg<UnsignedLong> = PrimitiveQTypeArg({ UnsignedLong() }, UnsignedLong::class.java)
    @JvmField val ByteStringT : QTypeArg<ByteString> = PrimitiveQTypeArg({ ByteString() }, ByteString::class.java)
    @JvmField val WStringT : QTypeArg<String> = PrimitiveQTypeArg({ "" }, String::class.java)
    @JvmField val FloatT : QTypeArg<Float> = PrimitiveQTypeArg({ 0.0f }, Float::class.java)
    @JvmField val DoubleT : QTypeArg<Double> = PrimitiveQTypeArg({ 0.0 }, Double::class.java)
    @JvmField val BlobT : QTypeArg<Blob> = PrimitiveQTypeArg( { Blob() }, Blob::class.java)
    // TODO: Bonded?
}