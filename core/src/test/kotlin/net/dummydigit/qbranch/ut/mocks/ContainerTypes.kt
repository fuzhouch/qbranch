// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.ut.mocks

import net.dummydigit.qbranch.QBranchSerializable
import net.dummydigit.qbranch.annotations.FieldId
import net.dummydigit.qbranch.annotations.QBranchGeneratedCode
import net.dummydigit.qbranch.generic.BuiltinQTypeArg
import net.dummydigit.qbranch.generic.VectorT

@QBranchGeneratedCode("mock", "version.mock")
class ContainerTypes : QBranchSerializable {
    @FieldId(0) @JvmField var vectorIntField : ArrayList<Int> = arrayListOf()

    private val vectorIntField_QTypeArg = VectorT(BuiltinQTypeArg.Int32T)
}