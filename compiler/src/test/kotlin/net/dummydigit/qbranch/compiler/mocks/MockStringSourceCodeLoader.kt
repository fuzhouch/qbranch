// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.mocks

import net.dummydigit.qbranch.compiler.SourceCodeLoader
import java.io.InputStream

class MockStringSourceCodeLoader(private val idlCodeMap : HashMap<String, String>)
    : SourceCodeLoader {

    override fun openStream(sourceName: String): InputStream {
        val sourceNameKey = resolvePath(sourceName)
        return idlCodeMap[sourceNameKey]!!.byteInputStream()
    }

    override fun resolvePath(sourceName: String): String {
        if (idlCodeMap[sourceName] == null) {
            throw UnsupportedOperationException("sourceName=$sourceName")
        } else {
            return sourceName
        }
    }
}