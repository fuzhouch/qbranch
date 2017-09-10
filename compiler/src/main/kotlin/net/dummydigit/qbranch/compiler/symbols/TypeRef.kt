// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.symbols

import net.dummydigit.qbranch.compiler.SourceCodeInfo
import net.dummydigit.qbranch.compiler.codegen.Translator
import net.dummydigit.qbranch.compiler.exceptions.InternalCompilationStateError
import java.util.*

internal class TypeRef(sourceCodeInfo: SourceCodeInfo,
                       var refType : Symbol? = null,
                       var definedByType: Symbol? = null, // To get generic argument.
                       var genericTypeArgs : LinkedList<Symbol> = LinkedList())
    : Symbol(sourceCodeInfo, SymbolType.TypeRef, "typeRef") {
    override fun toTargetCode(gen: Translator): String {
        throw InternalCompilationStateError("TypeRefIsNotVisible", sourceCodeInfo)
    }
}