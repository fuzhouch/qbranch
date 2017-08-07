// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch

import bond.BondDataType
import net.dummydigit.qbranch.types.*

/**
 * A mapping to allow
 */
object BondJavaTypeMapping {
    val bondTagToBuiltInType = hashMapOf(
            BondDataType.BT_BOOL to Boolean::class.java,
            BondDataType.BT_UINT8 to UnsignedByte::class.java,
            BondDataType.BT_UINT16 to UnsignedShort::class.java,
            BondDataType.BT_UINT32 to UnsignedInt::class.java,
            BondDataType.BT_UINT64 to UnsignedLong::class.java,
            BondDataType.BT_FLOAT to Float::class.java,
            BondDataType.BT_DOUBLE to Double::class.java,
            BondDataType.BT_STRING to ByteString::class.java,
            BondDataType.BT_LIST to List::class.java,
            BondDataType.BT_SET to Set::class.java,
            BondDataType.BT_MAP to Map::class.java,
            BondDataType.BT_INT8 to Byte::class.java,
            BondDataType.BT_INT16 to Short::class.java,
            BondDataType.BT_INT32 to Int::class.java,
            BondDataType.BT_INT64 to Long::class.java,
            BondDataType.BT_WSTRING to String::class.java
    )

    val builtInTypeToBondTag = hashMapOf(
            Boolean::class.java to BondDataType.BT_BOOL,
            UnsignedByte::class.java to BondDataType.BT_UINT8,
            UnsignedShort::class.java to BondDataType.BT_UINT16,
            UnsignedInt::class.java to BondDataType.BT_UINT32,
            UnsignedLong::class.java to BondDataType.BT_UINT64,
            Float::class.java to BondDataType.BT_FLOAT,
            Double::class.java to BondDataType.BT_DOUBLE,
            ByteString::class.java to BondDataType.BT_STRING,
            List::class.java to BondDataType.BT_LIST,
            Set::class.java to BondDataType.BT_SET,
            Map::class.java to BondDataType.BT_MAP,
            Byte::class.java to BondDataType.BT_INT8,
            Short::class.java to BondDataType.BT_INT16,
            Int::class.java to BondDataType.BT_INT32,
            Long::class.java to BondDataType.BT_INT64,
            String::class.java to BondDataType.BT_WSTRING
    )
}
