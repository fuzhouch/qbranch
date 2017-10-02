// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.utils

import net.dummydigit.qbranch.BondDataType
import net.dummydigit.qbranch.protocols.CompactBinaryReader
import net.dummydigit.qbranch.protocols.TaggedProtocolReader

class CompactBinaryReaderWithMarker(private val readerImpl : CompactBinaryReader) : TaggedProtocolReader by readerImpl {

    var skippedStringCount = 0
    var skippedStructCount = 0

    override fun skipField(dataType: BondDataType) {
        if (dataType == BondDataType.BT_STRUCT) {
            skippedStructCount += 1;
        }

        if (dataType == BondDataType.BT_STRING) {
            skippedStringCount += 1;
        }
        readerImpl.skipField(dataType)
    }
}