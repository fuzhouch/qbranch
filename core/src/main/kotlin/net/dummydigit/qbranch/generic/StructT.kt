// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

import net.dummydigit.qbranch.QBranchSerializable

/**
 * A type argument to represent a generated structure.
 */
abstract class StructT<T : QBranchSerializable>(private val typeParamMapping : Map<String, QTypeArg<*>>) : QTypeArg<T> {
    fun getFieldTypeArg(typeParam : String) : QTypeArg<*>? {
        return typeParamMapping[typeParam]
    }
}