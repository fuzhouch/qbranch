// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.ut.mocks

import net.dummydigit.qbranch.annotations.FieldId
import net.dummydigit.qbranch.annotations.QBranchGeneratedCode
import net.dummydigit.qbranch.generic.ObjectCreatorAsAny
import net.dummydigit.qbranch.generic.cast

@QBranchGeneratedCode("gbc", "version.mock")
open class StructWithGenericField<T : Any>(typeArgs : Array<ObjectCreatorAsAny>) {
    @FieldId(0) var genericField : T = cast(typeArgs[0].newInstanceAsAny())
    @FieldId(1) var intField : Int = 0
    @FieldId(2) var containerField : List<T> = listOf()
}
