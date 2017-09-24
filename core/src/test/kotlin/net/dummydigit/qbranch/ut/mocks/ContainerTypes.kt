// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.ut.mocks

import net.dummydigit.qbranch.QBranchSerializable
import net.dummydigit.qbranch.annotations.FieldId
import net.dummydigit.qbranch.annotations.QBranchGeneratedCode
import net.dummydigit.qbranch.generic.BuiltinQTypeArg
import net.dummydigit.qbranch.generic.StructT
import net.dummydigit.qbranch.generic.VectorT

@QBranchGeneratedCode("mock", "version.mock")
class ContainerTypes : QBranchSerializable {
    private val vectorIntField_QTypeArg = VectorT(VectorT(BuiltinQTypeArg.Int32T))

    companion object {
        @JvmStatic
        fun asQTypeArg() : StructT<ContainerTypes> {
            return StructT({ ContainerTypes() }, baseClassT = null)
        }
    }

    @FieldId(0) @JvmField var vectorIntField : ArrayList<ArrayList<Int>> = vectorIntField_QTypeArg.newInstance()
}