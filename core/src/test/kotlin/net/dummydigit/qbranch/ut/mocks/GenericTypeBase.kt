// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.ut.mocks

import net.dummydigit.qbranch.QBranchSerializable
import net.dummydigit.qbranch.annotations.FieldId
import net.dummydigit.qbranch.annotations.QBranchGeneratedCode
import net.dummydigit.qbranch.generic.QTypeArg
import net.dummydigit.qbranch.generic.StructT

@QBranchGeneratedCode("mock", "version.mock")
open class GenericTypeBase<T1 : Any, T2 : Any>(_qTypeArgs: GenericTypeBaseT<T1, T2>) : QBranchSerializable {

    companion object QTypeDef {
        class GenericTypeBaseT<T1 : Any, T2 : Any> (T1_QTypeArg : QTypeArg<T1>, T2_QTypeArg : QTypeArg<T2>)
            : StructT<GenericTypeBase<T1, T2>>() {
            override val baseClassT = null

            val baseFieldT1 = T1_QTypeArg
            val baseFieldT2 = T2_QTypeArg

            override fun newInstance() = GenericTypeBase(this)
            private val refObj = newInstance()
            override fun getGenericType() = refObj.javaClass
        }

        @JvmStatic
        fun <T1 : Any, T2 : Any> asQTypeArg(T1_QTypeArg: QTypeArg<T1>, T2_QTypeArg: QTypeArg<T2>): GenericTypeBaseT<T1, T2> {
            return GenericTypeBaseT(T1_QTypeArg, T2_QTypeArg)
        }
    }

    @FieldId(0) @JvmField var baseFieldT1 : T1 = _qTypeArgs.baseFieldT1.newInstance()
    @FieldId(1) @JvmField var baseFieldT2 : T2 = _qTypeArgs.baseFieldT2.newInstance()
}
