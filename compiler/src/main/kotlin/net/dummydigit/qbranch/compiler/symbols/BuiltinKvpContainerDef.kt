// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.symbols

import net.dummydigit.qbranch.compiler.SourceCodeInfo
import net.dummydigit.qbranch.compiler.Translator

internal class BuiltinKvpContainerDef(sourceCodeInfo: SourceCodeInfo,
                                      typeName : String,
                                      var keyTypeRef : TypeRef? = null,
                                      var valueTypeRef : TypeRef? = null)
    : Symbol(sourceCodeInfo, SymbolType.BuiltinKvpContainer, typeName) {
    override fun toTargetCode(gen: Translator): String = gen.generate(this)
}