// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.exceptions

import net.dummydigit.qbranch.compiler.SourceCodeInfo

class UndefinedNamespaceError(namespaceName : String,
                              sourceCodeInfo: SourceCodeInfo)
    : CompilationError("UndefinedNamespace:namespace=$namespaceName", sourceCodeInfo)