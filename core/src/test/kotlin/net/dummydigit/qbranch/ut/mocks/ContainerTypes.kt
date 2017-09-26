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

    class ContainerTypes_QTypeArg : StructT<ContainerTypes>(hashMapOf()) {
        override fun newInstance() = ContainerTypes()
        private val refObj = newInstance()
        override fun getGenericType() = refObj.javaClass
    }

    companion object {
        @JvmStatic
        fun asQTypeArg() : ContainerTypes_QTypeArg {
            return ContainerTypes_QTypeArg()
        }
    }

    @FieldId(0) @JvmField var vectorIntField : ArrayList<ArrayList<Int>> = vectorIntField_QTypeArg.newInstance()
}