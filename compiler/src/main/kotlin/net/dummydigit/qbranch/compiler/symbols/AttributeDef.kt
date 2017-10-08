// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.symbols

import net.dummydigit.qbranch.compiler.SourceCodeInfo
import net.dummydigit.qbranch.compiler.Translator

internal class AttributeDef(sourceCodeInfo : SourceCodeInfo,
                            val key: String,
                            val value: String)
    : Symbol(sourceCodeInfo, SymbolType.Attribute) {
    override fun toTargetCode(gen: Translator): String = gen.generate(this)
}
