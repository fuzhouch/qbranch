// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

interface ContainerTypeArg<out E : Any> {
    fun newElement() : E
}