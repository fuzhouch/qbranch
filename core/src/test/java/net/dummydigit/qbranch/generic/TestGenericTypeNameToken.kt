// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

import net.dummydigit.qbranch.ut.mocks.GenericType
import org.junit.Test

class TestGenericTypeNameToken {

    @Test
    fun testNameCreation() {
        val typeArg = GenericType.asQTypeArg(BuiltinQTypeArg.WStringT, BuiltinQTypeArg.Int32T)
        println(typeArg.getGenericType().typeName)
        typeArg.getGenericType().declaredFields.forEach {
            println(it.genericType.typeName)
        }
    }
}