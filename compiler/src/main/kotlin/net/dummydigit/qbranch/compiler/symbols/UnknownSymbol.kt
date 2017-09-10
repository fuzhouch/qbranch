package net.dummydigit.qbranch.compiler.symbols

import net.dummydigit.qbranch.compiler.SourceCodeInfo
import net.dummydigit.qbranch.compiler.codegen.Translator
import net.dummydigit.qbranch.compiler.exceptions.InternalCompilationStateError

internal class UnknownSymbol(sourceCodeInfo: SourceCodeInfo)
    : Symbol(sourceCodeInfo, SymbolType.Unknown, "unknown") {
    override fun toTargetCode(gen: Translator): String {
        throw InternalCompilationStateError("unknownSymbolIsNotWritable", sourceCodeInfo)
    }
}