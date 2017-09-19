// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler

import net.dummydigit.qbranch.compiler.exceptions.InvalidInputPathError
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.Paths
import java.util.jar.Manifest

internal object ParsingUtil {
    private val removeQuotePattern = Regex("^\"|\"$")
    private val unescapePattern = Regex("\\\\(.)")

    val bondIdlKeyword = setOf(
            "int8", "int16", "int32", "int64",
            "uint8", "uint16", "uint32", "uint64",
            "string", "wstring",
            "float", "double",
            "vector", "list", "map",
            "nullable", "nothing",
            "bonded", "blob"
    )

    val bondIdlPrimitiveType = setOf(
            "int8", "int16", "int32", "int64",
            "uint8", "uint16", "uint32", "uint64",
            "string", "wstring",
            "float", "double",
            "blob")

    val bondIdlContainerType = setOf(
            "vector", "list", "map", "set", "nullable", "bonded"
    )

    val bondIdlKvpContainerType = setOf("map")


    fun unescapeQuotedString(str : String) : String {
        val removeQuote = str.replace(removeQuotePattern, "")
        return removeQuote.replace(unescapePattern, { result ->
            result.groups[0]!!.value.substring(1)
        })
    }

    fun readCompilerInfoFromManifest() : Pair<String, String> {
        val stream = javaClass.getResourceAsStream("/META-INF/MANIFEST.MF")
        stream.use {
            val manifest = Manifest(it)
            val attr = manifest.mainAttributes
            val compiler = attr.getValue("Implementation-Title")
            val version = attr.getValue("Implementation-Version")
            return Pair(compiler, version)
        }
    }

    fun buildOutputFileName(inputFileName: String, outputFolder: String) : Path {
        val inputPath = Paths.get(inputFileName)
        val inputBaseName = inputPath.fileName
        val matcher = FileSystems.getDefault().getPathMatcher("glob:*.bond")
        if (!matcher.matches(inputBaseName)) {
            throw InvalidInputPathError(inputPath.toString())
        }
        val inputBaseNameStr = inputBaseName.toString()
        return Paths.get(outputFolder,
                inputBaseNameStr.substring(0, inputBaseNameStr.length - 5) + "_types.kt")
    }

    fun isStringTypeName(typeName : String) : Boolean {
        return typeName == "string" || typeName == "wstring"
    }

    fun isFloatTypeName(typeName : String) : Boolean {
        return typeName == "float" || typeName == "double"
    }

    fun isIntegerTypeName(typeName : String) : Boolean {
        return typeName == "int8" ||
                typeName == "int16" ||
                typeName == "int32" ||
                typeName == "int64" ||
                typeName == "uint8" ||
                typeName == "uint16" ||
                typeName == "uint32" ||
                typeName == "uint64"
    }
}