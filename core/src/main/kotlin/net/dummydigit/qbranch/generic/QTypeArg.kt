// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

interface QTypeArg<T : Any> {
    fun newInstance() : T
    fun getGenericType() : Class<T>
}