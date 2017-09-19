// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.
package net.dummydigit.qbranch.generic

import net.dummydigit.qbranch.types.*

object BuiltinTypeArg {
    @JvmField val Int8T = StructT<Byte>({ 0 })
    @JvmField val Int16T = StructT<Short>({ 0 })
    @JvmField val Int32T = StructT({ 0 })
    @JvmField val Int64T = StructT<Long>({ 0 })
    @JvmField val UInt8T = StructT(UnsignedByte::class)
    @JvmField val UInt16T = StructT(UnsignedShort::class)
    @JvmField val UInt32T = StructT(UnsignedInt::class)
    @JvmField val UInt64T = StructT(UnsignedLong::class)
    @JvmField val ByteStringT = StructT(ByteString::class)
    @JvmField val WStringT = StructT({ "" })
    @JvmField val FloatT = StructT({ 0.0f })
    @JvmField val DoubleT = StructT({ 0.0 })
    @JvmField val BlobT = StructT(Blob::class)
// TODO: Bonded?
}