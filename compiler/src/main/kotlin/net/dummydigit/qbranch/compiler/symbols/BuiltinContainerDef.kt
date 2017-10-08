// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.symbols

import net.dummydigit.qbranch.compiler.SourceCodeInfo
import net.dummydigit.qbranch.compiler.Translator

internal class BuiltinContainerDef(sourceCodeInfo: SourceCodeInfo,
                                   typeName : String,
                                   var elemetTypeRef : Symbol? = null)
    : Symbol(sourceCodeInfo, SymbolType.BuiltinContainer, typeName) {
    override fun toTargetCode(gen: Translator): String = gen.generate(this)
}