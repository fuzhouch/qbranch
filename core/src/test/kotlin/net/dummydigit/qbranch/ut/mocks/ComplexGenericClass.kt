// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.ut.mocks

import net.dummydigit.qbranch.annotations.FieldId
import net.dummydigit.qbranch.annotations.QBranchGeneratedCode
import net.dummydigit.qbranch.generic.*

// This file demonstrates the case that how a generic class is referenced
// when creating instances.

@QBranchGeneratedCode("gbc", "version.mock")
open class C1<T1 : Any, T2 : Any>(typeArgs : Array<ObjectCreatorAsAny>) {
    @FieldId(0) var valueT1 : T1 = cast(typeArgs[0].newInstanceAsAny())
    @FieldId(1) var valueT2 : T2 = cast(typeArgs[1].newInstanceAsAny())
}

@QBranchGeneratedCode("gbc", "version.mock")
open class C2<T : Any>(typeArgs : Array<ObjectCreatorAsAny>) {
    @FieldId(0) var valueT : T = cast(typeArgs[0].newInstanceAsAny())
}


// Case 1: Generic class used as base class
@QBranchGeneratedCode("gbc", "version.mock")
open class D1 : C2<C1<Int, String>>(arrayOf(mkCreator(C1::class, toKTypeArgsV(Int::class, String::class)))) {
}

// Case 2: Generic class used as field
// D2 can either Bond generated class, or handwritten
// class when reference C2.
class D2 {
    val value = C2<C1<Int, String>>(arrayOf(mkCreator(C1::class, toKTypeArgsV(Int::class, String::class))))
}