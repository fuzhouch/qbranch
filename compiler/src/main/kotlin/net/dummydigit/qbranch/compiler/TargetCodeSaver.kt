// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler

import java.io.OutputStream

interface TargetCodeSaver {
    fun openStream(sourceName : String) : OutputStream
    fun onSaveDone()
    fun resolvePath(sourceName : String) : String
}