// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.parser

import net.dummydigit.qbranch.compiler.FileSourceCodeLoader
import java.io.FileNotFoundException
import java.nio.file.Path
import java.nio.file.Paths
import net.dummydigit.qbranch.compiler.Settings
import org.junit.Assert
import org.junit.Test

class SourceFileLoaderTest {

    private fun isMicrosoftWindows() : Boolean {
        return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0
    }

    private fun getAbsoluteHomePath() : Path {
        val envName = if (isMicrosoftWindows()) { "USERPROFILE" } else { "HOME" }
        val homePath = Paths.get(System.getenv()[envName]).toRealPath()
        Assert.assertTrue(homePath.isAbsolute)
        return homePath
    }

    private fun getTestAbsolutePath(parentPath : Path, createFile : Boolean) : Path {
        val milli = System.currentTimeMillis()
        val fileName = "qbranch_compiler_ut_$milli.bond"
        val filePath = parentPath.resolve(fileName)
        if (createFile) {
            Assert.assertEquals(true, filePath.toFile().createNewFile())
        }
        return filePath
    }

    private fun getTestAbsolutePath(createFile : Boolean) : Path {
        return getTestAbsolutePath(getAbsoluteHomePath(), createFile)
    }

    @Test
    fun testLoadExistingFileWithoutInclude() {
        val testPath = getTestAbsolutePath(createFile = true)
        val settings = Settings()
        val loader = FileSourceCodeLoader(settings)
        try {
            val stream = loader.openStream(testPath.toString())
            stream.close()
        } finally {
            testPath.toFile().delete()
        }
    }

    @Test(expected = FileNotFoundException::class)
    fun testLoadNonExistFileWithoutInclude() {
        val testPath = getTestAbsolutePath(createFile = false)
        val settings = Settings()
        val loader = FileSourceCodeLoader(settings)
        loader.openStream(testPath.toString())
    }

    @Test(expected = FileNotFoundException::class)
    fun testThrowExceptionIfFileNotFoundInPath() {
        val homePath = getAbsoluteHomePath()
        val testPath = getTestAbsolutePath(homePath, createFile = false)
        val relativePath = homePath.relativize(testPath)
        val settings = Settings(includePaths = listOf(homePath.toString()))
        val loader = FileSourceCodeLoader(settings)
        loader.openStream(relativePath.toString())
    }

    @Test
    fun testReturnFileWhenIncludePathExists() {
        val homePath = getAbsoluteHomePath()
        val testPath = getTestAbsolutePath(homePath, createFile = true)
        val relativePath = homePath.relativize(testPath)
        val settings = Settings(includePaths = listOf(homePath.toString()))
        val loader = FileSourceCodeLoader(settings)
        try {
            val stream = loader.openStream(relativePath.toString())
            stream.close()
        } finally {
            testPath.toFile().delete()
        }
    }

    @Test
    fun testOpenFileUnderCurrentFolder() {
        val currentPath = Paths.get(".")
        val testPath = getTestAbsolutePath(currentPath, createFile = true)
        val relativePath = currentPath.relativize(testPath)
        val settings = Settings(includePaths = listOf())
        val loader = FileSourceCodeLoader(settings)
        try {
            val stream = loader.openStream(relativePath.toString())
            stream.close()
        } finally {
            testPath.toFile().delete()
        }
    }
}