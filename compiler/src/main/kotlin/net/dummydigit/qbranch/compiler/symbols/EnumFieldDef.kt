// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.symbols

import net.dummydigit.qbranch.compiler.SourceCodeInfo
import net.dummydigit.qbranch.compiler.Translator
import net.dummydigit.qbranch.compiler.exceptions.InternalCompilationStateError

internal class EnumFieldDef(sourceCodeInfo : SourceCodeInfo,
                            val enumDef : EnumDef,
                            val fieldName : String,
                            var enumValue : Int) : Symbol(sourceCodeInfo, SymbolType.EnumSymbol, fieldName) {
    override fun toTargetCode(gen: Translator) : String {
        throw InternalCompilationStateError("genEnumFieldShouldNotHappen", sourceCodeInfo)
    }
}