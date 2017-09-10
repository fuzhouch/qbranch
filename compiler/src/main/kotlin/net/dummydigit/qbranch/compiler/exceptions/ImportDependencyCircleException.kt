// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.exceptions

class ImportDependencyCircleException(importFileName : String)
    : RuntimeException("ImportDependencyCircle:$importFileName")