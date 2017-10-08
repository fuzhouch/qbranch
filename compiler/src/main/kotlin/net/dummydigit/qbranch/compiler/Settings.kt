// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler

import java.net.URLClassLoader
import java.util.jar.Manifest


/**
 * A configuration object to provide settings for compiler.
 */
data class Settings(val includePaths : List<String> = listOf(),
                    val outputRootPath: String = "",
                    val translateImportedSources : Boolean = false,
                    val targetCodeGen: String = "kotlin",
                    val compilerName : String = "qbranch",
                    val codeGenSyntaxVersion: String = "1",
                    val moreSettings : Map<String, String> = mapOf()) {

    companion object {
        @JvmStatic
        fun createSimpleSettings(includePaths : Array<String>,
                           outputRootPath: String,
                           targetCodeGen: String) : Settings {
            return Settings(includePaths = includePaths.toList(),
                    outputRootPath = outputRootPath,
                    targetCodeGen = targetCodeGen)
        }
    }
}