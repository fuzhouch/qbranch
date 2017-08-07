// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.ut.mocks

import net.dummydigit.qbranch.annotations.FieldId
import net.dummydigit.qbranch.annotations.QBranchGeneratedCode
import net.dummydigit.qbranch.types.*

// A mock class to test primitive types
@QBranchGeneratedCode("gbc", "version.mock")
open class AllPrimitiveTypes {
    @FieldId(0) var fieldByte : Byte = 1
    @FieldId(1) var fieldShort : Short = 2
    @FieldId(2) var fieldInt : Int = 3
    @FieldId(3) var fieldLong : Long = 4L

    @FieldId(4) var fieldUnsignedByte : UnsignedByte = UnsignedByte()
    @FieldId(5) var fieldUnsignedShort : UnsignedShort = UnsignedShort()
    @FieldId(6) var fieldUnsignedInt : UnsignedInt = UnsignedInt()
    @FieldId(7) var fieldUnsignedLong : UnsignedLong = UnsignedLong()

    @FieldId(8) var fieldByteString : ByteString = ByteString()
    @FieldId(9) var fieldString : String = ""
}
