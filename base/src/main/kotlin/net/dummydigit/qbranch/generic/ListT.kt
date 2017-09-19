// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

class ListT<E : Any>(private val elementT: TypeArg<E>) : TypeArg<MutableList<E>>, ContainerTypeArg<E> {
    override fun newInstance(): MutableList<E> = mutableListOf()
    override fun newElement(): E = elementT.newInstance()
}
