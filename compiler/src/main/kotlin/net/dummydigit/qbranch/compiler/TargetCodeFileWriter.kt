// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler

import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class TargetCodeFileWriter(settings: Settings) : TargetCodeWriter {

    private val outputRootPath = Paths.get(settings.outputRootPath).toAbsolutePath()

    override fun onSaveDone() { /* no need to do anything for now */ }

    override fun openTargetCodeAsStream(namespace : String, symbolName : String) : OutputStream {
        val packagePath = createPackagePathByNamespace(namespace)
        return Files.newOutputStream(packagePath.resolve(symbolName))
    }

    private fun createPackagePathByNamespace(namespace : String) : Path {
        val packagePath = namespace.split('.')
                .fold(outputRootPath, { cur, component -> cur.resolve(component) })
        if (Files.notExists(packagePath)) {
            Files.createDirectory(packagePath)
        }
        return packagePath
    }
}