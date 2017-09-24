// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.ut.mocks

import net.dummydigit.qbranch.QBranchSerializable
import net.dummydigit.qbranch.annotations.FieldId
import net.dummydigit.qbranch.annotations.QBranchGeneratedCode
import net.dummydigit.qbranch.generic.QTypeArg
import net.dummydigit.qbranch.generic.StructT

@QBranchGeneratedCode("mock", "version.mock")
open class GenericTypeBase<T1 : Any, T2 : Any>(@Transient private val T1_QTypeArg : QTypeArg<T1>,
                                               @Transient private val T2_QTypeArg : QTypeArg<T2>) : QBranchSerializable {
    @Transient private val baseFieldT1_QTypeArg = T1_QTypeArg
    @Transient private val baseFieldT2_QTypeArg = T2_QTypeArg

    companion object {
        @JvmStatic
        fun <T1 : Any, T2 : Any> asQTypeArg(T1_QTypeArg: QTypeArg<T1>, T2_QTypeArg: QTypeArg<T2>): StructT<GenericTypeBase<T1, T2>> {
            return StructT({ GenericTypeBase(T1_QTypeArg, T2_QTypeArg) }, baseClassT = null)
        }
    }

    @FieldId(0) @JvmField var baseFieldT1 : T1 = baseFieldT1_QTypeArg.newInstance()
    @FieldId(1) @JvmField var baseFieldT2 : T2 = baseFieldT2_QTypeArg.newInstance()
}
