// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler

import java.io.OutputStream

interface TargetCodeWriter {
    fun openTargetCodeAsStream(namespace : String, symbolName : String) : OutputStream
    fun onSaveDone()
}