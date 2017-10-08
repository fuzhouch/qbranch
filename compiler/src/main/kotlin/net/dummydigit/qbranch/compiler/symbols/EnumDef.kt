// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.symbols

import net.dummydigit.qbranch.compiler.SourceCodeInfo
import net.dummydigit.qbranch.compiler.Translator

internal class EnumDef(sourceCodeInfo : SourceCodeInfo,
                       name : String,
                       val valueList : MutableList<Pair<String, Int>>)
    : Symbol(sourceCodeInfo, SymbolType.Enum, name) {

    var currentDefaultValue = 0

    override fun toTargetCode(gen: Translator): String = gen.generate(this)
}