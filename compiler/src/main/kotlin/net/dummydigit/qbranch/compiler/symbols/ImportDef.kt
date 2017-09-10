// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.symbols

import net.dummydigit.qbranch.compiler.SourceCodeInfo
import net.dummydigit.qbranch.compiler.codegen.Translator

internal class ImportDef(sourceCodeInfo : SourceCodeInfo, val importPath : String)
    : Symbol(sourceCodeInfo, SymbolType.Import, importPath) {
    override fun toTargetCode(gen: Translator): String = gen.generate(this)
}