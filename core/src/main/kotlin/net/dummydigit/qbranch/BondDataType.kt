// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch

/**
 * A manually created enum as replacement of built-in BondDataType definition.
 */
enum class BondDataType(val num : Int) {
    BT_STOP(0),
    BT_STOP_BASE(1),
    BT_BOOL(2),
    BT_UINT8(3),
    BT_UINT16(4),
    BT_UINT32(5),
    BT_UINT64(6),
    BT_FLOAT(7),
    BT_DOUBLE(8),
    BT_STRING(9),
    BT_STRUCT(10),
    BT_LIST(11),
    BT_SET(12),
    BT_MAP(13),
    BT_INT8(14),
    BT_INT16(15),
    BT_INT32(16),
    BT_INT64(17),
    BT_WSTRING(18),
    BT_UNAVAILABLE(127),
}