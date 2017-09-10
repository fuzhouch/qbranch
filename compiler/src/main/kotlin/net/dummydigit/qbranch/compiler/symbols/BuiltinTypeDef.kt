// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.symbols

import net.dummydigit.qbranch.compiler.ParsingUtil
import net.dummydigit.qbranch.compiler.SourceCodeInfo
import net.dummydigit.qbranch.compiler.codegen.Translator
import net.dummydigit.qbranch.compiler.exceptions.InternalCompilationStateError

internal class BuiltinTypeDef(sourceCodeInfo: SourceCodeInfo,
                     typeName : String)
    : Symbol(sourceCodeInfo, SymbolType.BuiltinType, typeName) {

    init {
        if (!ParsingUtil.bondIdlPrimitiveType.contains(typeName)) {
            throw InternalCompilationStateError("BultinTypeDef:UnknownPrimitive:type=$typeName",
                    sourceCodeInfo)
        }
    }

    override fun toTargetCode(gen: Translator): String = gen.generate(this)
}