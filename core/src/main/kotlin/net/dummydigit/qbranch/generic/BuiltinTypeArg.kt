// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.
package net.dummydigit.qbranch.generic

import net.dummydigit.qbranch.types.*

object BuiltinTypeArg {
    @JvmField val Int8T : TypeArg<Byte> = StructT<Byte>({ 0 })
    @JvmField val Int16T : TypeArg<Short> = StructT<Short>({ 0 })
    @JvmField val Int32T : TypeArg<Int> = StructT({ 0 })
    @JvmField val Int64T : TypeArg<Long> = StructT<Long>({ 0 })
    @JvmField val UInt8T : TypeArg<UnsignedByte> = StructT(UnsignedByte::class)
    @JvmField val UInt16T : TypeArg<UnsignedShort> = StructT(UnsignedShort::class)
    @JvmField val UInt32T : TypeArg<UnsignedInt> = StructT(UnsignedInt::class)
    @JvmField val UInt64T : TypeArg<UnsignedLong> = StructT(UnsignedLong::class)
    @JvmField val ByteStringT : TypeArg<ByteString> = StructT(ByteString::class)
    @JvmField val WStringT : TypeArg<String> = StructT({ "" })
    @JvmField val FloatT : TypeArg<Float> = StructT({ 0.0f })
    @JvmField val DoubleT : TypeArg<Double> = StructT({ 0.0 })
    @JvmField val BlobT : TypeArg<Blob> = StructT(Blob::class)
    // TODO: Bonded?
}