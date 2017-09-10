// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.exceptions

import net.dummydigit.qbranch.compiler.SourceCodeInfo
import net.dummydigit.qbranch.compiler.symbols.SymbolType

class TypeMismatchError(symbolName : String,
                        expectedType : SymbolType,
                        actualType : SymbolType,
                        sourceCodeInfo: SourceCodeInfo)
    : CompilationError("UnmatchedType:name=$symbolName,"+
                       "expectedType=$expectedType," +
                       "actualType=$actualType", sourceCodeInfo)