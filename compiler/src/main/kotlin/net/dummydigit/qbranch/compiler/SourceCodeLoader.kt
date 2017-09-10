// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler

import java.io.InputStream

interface SourceCodeLoader {
    fun openStream(sourceName : String) : InputStream
    fun resolvePath(sourceName : String) : String
}