// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

import java.lang.reflect.Type

interface QTypeArg<out T : Any> {
    fun newInstance() : T
    fun getGenericType() : Type
}