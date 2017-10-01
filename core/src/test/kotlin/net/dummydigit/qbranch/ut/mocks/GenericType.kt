// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.ut.mocks

import net.dummydigit.qbranch.annotations.*
import net.dummydigit.qbranch.generic.*

@QBranchGeneratedCode("mock", "version.mock")
open class GenericType<T1 : Any, T2 : Any>(qTypeArgs: GenericTypeT<T1, T2>) : GenericTypeBase<T2, T1>(qTypeArgs.baseClassT) {

    companion object QTypeDef {

        class GenericTypeT<T1 : Any, T2 : Any>(T1_QTypeArg : QTypeArg<T1>, T2_QTypeArg : QTypeArg<T2>)
            : StructT<GenericType<T1, T2>>() {
            override val baseClassT = GenericTypeBase.QTypeDef.GenericTypeBaseT(T2_QTypeArg, T1_QTypeArg)

            val fieldT1 = T1_QTypeArg
            val fieldT2 = T2_QTypeArg
            val vectorT1 = VectorT(T1_QTypeArg)
            val mapT1T2 = MapT(T1_QTypeArg, T2_QTypeArg)
            val setIntField = SetT(BuiltinQTypeArg.Int32T)

            override fun newInstance() = GenericType(this)
            private val refObj = newInstance()
            override fun getGenericType() = refObj.javaClass
        }

        @JvmStatic
        fun <T1 : Any, T2 : Any> asQTypeArg(T1_QTypeArg : QTypeArg<T1>, T2_QTypeArg : QTypeArg<T2>) : GenericTypeT<T1, T2> {
            return GenericTypeT(T1_QTypeArg, T2_QTypeArg)
        }
    }

    @FieldId(0) @JvmField var fieldT1 : T1 = qTypeArgs.fieldT1.newInstance()
    @FieldId(1) @JvmField var fieldT2 : T2 = qTypeArgs.fieldT2.newInstance()
    @FieldId(2) @JvmField var vectorT1 : ArrayList<T1> = qTypeArgs.vectorT1.newInstance()
    @FieldId(3) @JvmField var mapT1T2 : MutableMap<T1, T2> = qTypeArgs.mapT1T2.newInstance()
    @FieldId(4) @JvmField var setIntField : MutableSet<Int> = qTypeArgs.setIntField.newInstance()
}

// Case of generic class:
// 1. Inheritance
// 2. container
// 3. container of container
// 4. Generic struct with type arguments
// 5. Generic struct with type arguments, and type arguments are generic
// 6. field is object of type argument
// 7. field is a generic class with type argument filled as type argument.