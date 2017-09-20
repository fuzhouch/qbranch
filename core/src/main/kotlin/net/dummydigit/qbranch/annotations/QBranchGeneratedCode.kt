// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.annotations

/**
 * Annotation to tag a class generated from a Bond IDL. 
 *
 * @param compilerName Compiler of Bond IDL. Typically "gbc".
 * @param version Build version of compiler of Bond IDL.
 */
@Target(AnnotationTarget.CLASS)
annotation class QBranchGeneratedCode(
        val compilerName : String,
        val version : String)
