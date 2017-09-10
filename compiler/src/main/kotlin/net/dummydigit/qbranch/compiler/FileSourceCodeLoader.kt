// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler

import java.nio.file.Paths
import java.io.*
import java.nio.file.Path

class FileSourceCodeLoader(settings: Settings) : SourceCodeLoader {
    private val includePaths = settings.includePaths.map {
        Paths.get(it).toAbsolutePath()
    }

    override fun openStream(sourceName : String) : InputStream {
        return FileInputStream(resolvePathInternal(sourceName).toFile())
    }

    override fun resolvePath(sourceName: String): String {
        return this.resolvePathInternal(sourceName).toString()
    }

    private fun resolvePathInternal(sourceName: String) : Path {
        val inputFile = Paths.get(sourceName)
        return when {
            inputFile.isAbsolute -> inputFile
            includePaths.isEmpty() -> inputFile.toRealPath()
            else -> {
                for (eachPath in includePaths) {
                    try {
                        return eachPath.resolve(sourceName).toRealPath()
                    } catch (e : IOException) {
                        // Try next include path on failure.
                    }
                }
                // If all visited but still didn't resolve a path,
                // then it's an error.
                throw FileNotFoundException(sourceName)
            }
        }
    }
}