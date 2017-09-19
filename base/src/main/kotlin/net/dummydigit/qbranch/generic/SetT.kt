// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

class SetT<E : Any>(private val elementT: TypeArg<E>) : TypeArg<MutableSet<E>>, ContainerTypeArg<E> {
    override fun newInstance(): MutableSet<E> = mutableSetOf()
    override fun newElement(): E = elementT.newInstance()
}
