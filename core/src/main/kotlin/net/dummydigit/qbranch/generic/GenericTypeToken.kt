// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

import kotlin.text.Regex

class GenericTypeToken(val name : String, val typeArguments : Array<GenericTypeToken>?) {
    companion object {
        private val genericTypeNamePattern = Regex("([a-zA-Z][a-zA-Z0-9.]*)<(.*)>")
        private val nonGenericTypeNamePattern = Regex("([a-zA-Z][a-zA-Z0-9.]*)")

        @JvmStatic fun parseTypeName(typeName: String): GenericTypeToken {
            val matcher = genericTypeNamePattern.matchEntire(typeName)
            return if (matcher != null) {
                val genericTypeName = matcher.groups[1]!!.value
                val genericTypeArgs = matcher.groups[2]!!.value
                        .split(",".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .map { parseTypeName(it) }
                        .toTypedArray()
                GenericTypeToken(genericTypeName, genericTypeArgs)
            } else {
                val nonGenericMatcher = nonGenericTypeNamePattern.matchEntire(typeName)
                if (nonGenericMatcher != null) {
                    val nonGenericTypeName = nonGenericMatcher.groups[1]!!.value
                    GenericTypeToken(nonGenericTypeName, null)
                } else {
                    throw IllegalArgumentException(typeName)
                }
            }
        }
    }
}