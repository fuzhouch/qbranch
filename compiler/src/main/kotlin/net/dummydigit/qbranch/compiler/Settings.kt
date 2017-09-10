// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler

/**
 * A configuration object to provide settings for compiler.
 */
data class Settings(val includePaths : List<String> = listOf(),
                    val setOutputPathByNamespace : Boolean = false,
                    val outputPathRoot : String = "",
                    val translateImportedSources : Boolean = false,
                    val targetSourceGen : String = "kotlin",
                    val compilerName : String = "",
                    val compilerVersion : String = "",
                    val moreSettings : Map<String, String> = mapOf())