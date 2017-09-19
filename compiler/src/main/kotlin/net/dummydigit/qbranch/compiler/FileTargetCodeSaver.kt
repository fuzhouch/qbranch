// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler

import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class FileTargetCodeSaver(private val settings: Settings) : TargetCodeSaver {
    override fun openStream(sourceName: String): OutputStream {
        return Files.newOutputStream(resolvePathInternal(sourceName))
    }

    override fun resolvePath(sourceName: String): String {
        return this.resolvePathInternal(sourceName).toString()
    }

    override fun onSaveDone() { /* no need to do anything */ }

    private fun resolvePathInternal(sourceName: String) : Path {
        val outputPath = Paths.get(sourceName)
        return when {
            outputPath.isAbsolute -> outputPath
            settings.outputPathRoot.isEmpty() -> outputPath.toRealPath()
            else -> ParsingUtil.buildOutputFileName(outputPath.toString(),
                    settings.outputPathRoot)
        }
    }
}