// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.
package net.dummydigit.qbranch.generic

import net.dummydigit.qbranch.*

/**
 * A collection of built-in types.
 */
object BuiltinQTypeArg {

    internal class PrimitiveT<T : Any>(private val instanceCreator : () -> T,
                                       val dataType : BondDataType) : QTypeArg<T> {
        override fun newInstance() = instanceCreator()
        override fun getGenericType(): Class<T> = newInstance().javaClass
    }

    @JvmField val BoolT : QTypeArg<Boolean> = PrimitiveT({ false }, BondDataType.BT_BOOL)
    @JvmField val Int8T : QTypeArg<Byte> = PrimitiveT<Byte>({ 0 }, BondDataType.BT_INT8)
    @JvmField val Int16T : QTypeArg<Short> = PrimitiveT<Short>({ 0 }, BondDataType.BT_INT16)
    @JvmField val Int32T : QTypeArg<Int> = PrimitiveT({ 0 }, BondDataType.BT_INT32)
    @JvmField val Int64T : QTypeArg<Long> = PrimitiveT<Long>({ 0 }, BondDataType.BT_INT64)
    @JvmField val UInt8T : QTypeArg<UnsignedByte> = PrimitiveT({ UnsignedByte() }, BondDataType.BT_UINT8)
    @JvmField val UInt16T : QTypeArg<UnsignedShort> = PrimitiveT({ UnsignedShort() }, BondDataType.BT_UINT16)
    @JvmField val UInt32T : QTypeArg<UnsignedInt> = PrimitiveT({ UnsignedInt() }, BondDataType.BT_UINT32)
    @JvmField val UInt64T : QTypeArg<UnsignedLong> = PrimitiveT({ UnsignedLong() }, BondDataType.BT_UINT64)
    @JvmField val ByteStringT : QTypeArg<ByteString> = PrimitiveT({ ByteString() }, BondDataType.BT_STRING)
    @JvmField val WStringT : QTypeArg<String> = PrimitiveT({ "" }, BondDataType.BT_WSTRING)
    @JvmField val FloatT : QTypeArg<Float> = PrimitiveT({ 0.0f }, BondDataType.BT_FLOAT)
    @JvmField val DoubleT : QTypeArg<Double> = PrimitiveT({ 0.0 }, BondDataType.BT_DOUBLE)
    // TODO: Bonded?
}