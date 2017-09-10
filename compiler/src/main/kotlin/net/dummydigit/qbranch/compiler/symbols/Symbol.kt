// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.symbols

import net.dummydigit.qbranch.compiler.SourceCodeInfo
import net.dummydigit.qbranch.compiler.codegen.Translator

internal abstract class Symbol(val sourceCodeInfo : SourceCodeInfo,
                               val type : SymbolType,
                               var name : String = "") {
    abstract fun toTargetCode(gen : Translator) : String
}