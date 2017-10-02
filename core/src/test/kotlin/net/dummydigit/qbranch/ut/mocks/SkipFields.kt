// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.ut.mocks

import net.dummydigit.qbranch.QBranchSerializable
import net.dummydigit.qbranch.annotations.FieldId
import net.dummydigit.qbranch.annotations.QBranchGeneratedCode
import net.dummydigit.qbranch.generic.StructT


/*
namespace qbranch.ut

struct StructToSkip
{
    0: int32 skippedValue;
}

struct TestStructVersion1
{
    0: int32 int32Value; <<< let's decode with this struct.
}

struct TestStructVersion2
{
    0: int32 int32Value;
    1: string stringValue;
    2: StructToSkip structValue;
}
*/


@QBranchGeneratedCode("gbc", "version.mock")
open class SkipFields : QBranchSerializable {
    @FieldId(0) var int32Value : Int = 0

    companion object QTypeDef {
        class SkipFieldsT : StructT<SkipFields>() {
            override val baseClassT = null
            override fun newInstance() = SkipFields()
            private val refObj = newInstance()
            override fun getGenericType() = refObj.javaClass
        }

        @JvmStatic
        fun asQTypeArg() : SkipFieldsT {
            return SkipFieldsT()
        }
    }

}