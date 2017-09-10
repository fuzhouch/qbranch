// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.symbols

import net.dummydigit.qbranch.compiler.SourceCodeInfo
import net.dummydigit.qbranch.compiler.codegen.Translator

internal class GenericTypeParamDef(sourceCodeInfo : SourceCodeInfo,
                                   paramName: String)
    : Symbol(sourceCodeInfo, SymbolType.GenericTypeParam, paramName) {
    override fun toTargetCode(gen: Translator): String = gen.generate(this)
}