// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.ut.mocks

import net.dummydigit.qbranch.annotations.*
import net.dummydigit.qbranch.generic.*
import net.dummydigit.qbranch.QBranchSerializable

@QBranchGeneratedCode("mock", "version.mock")
class GenericType<T1 : Any, T2 : Any>(@Transient private val T1_QTypeArg : QTypeArg<T1>,
                                      @Transient private val T2_QTypeArg : QTypeArg<T2>) : QBranchSerializable{
    @Transient private val fieldT1_QTypeArg = T1_QTypeArg
    @Transient private val fieldT2_QTypeArg = T2_QTypeArg
    @Transient private val vectorT1_QTypeArg = VectorT(T1_QTypeArg)
    @Transient private val mapT1T2_QTypeArg = MapT(T1_QTypeArg, T2_QTypeArg)
    @Transient private val setIntField_QTypeArg = SetT(BuiltinQTypeArg.Int32T)

    companion object {
        @JvmStatic
        fun <T1 : Any, T2 : Any> asQTypeArg(tArgT1: QTypeArg<T1>, tArgT2: QTypeArg<T2>): StructT<GenericType<T1, T2>> {
            return StructT({ GenericType(tArgT1, tArgT2) })
        }
    }

    @FieldId(0) @JvmField var fieldT1 : T1 = fieldT1_QTypeArg.newInstance()
    @FieldId(1) @JvmField var fieldT2 : T2 = fieldT2_QTypeArg.newInstance()
    @FieldId(2) @JvmField var vectorT1 : ArrayList<T1> = vectorT1_QTypeArg.newInstance()
    @FieldId(3) @JvmField var mapT1T2 : MutableMap<T1, T2> = mapT1T2_QTypeArg.newInstance()
    @FieldId(4) @JvmField var setIntField : MutableSet<Int> = setIntField_QTypeArg.newInstance()
}