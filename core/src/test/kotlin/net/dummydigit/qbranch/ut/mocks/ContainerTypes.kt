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
class ContainerTypes(typeArg : ContainerTypesT) : QBranchSerializable {

    class ContainerTypesT : StructT<ContainerTypes>() {
        // All fields definition must be put before newInstance()
        // or it will cause NullPointerException when calling asQTypeArg.
        override val baseClassT = null
        val vectorIntField = VectorT(VectorT(BuiltinQTypeArg.Int32T))

        override fun newInstance() = ContainerTypes(this)
        private val refObj = newInstance()
        override fun getGenericType() = refObj.javaClass
    }

    companion object {
        @JvmStatic
        fun asQTypeArg() : ContainerTypesT {
            return ContainerTypesT()
        }
    }

    @FieldId(0) @JvmField var vectorIntField : ArrayList<ArrayList<Int>> = typeArg.vectorIntField.newInstance()
}