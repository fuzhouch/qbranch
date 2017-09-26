// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

class SetT<E : Any>(val elementT: QTypeArg<E>) : QTypeArg<MutableSet<E>>, ContainerTypeArg<E> {
    private val refObj = newInstance()
    override fun newInstance(): MutableSet<E> = mutableSetOf()
    override fun getGenericType() : Class<MutableSet<E>> = refObj.javaClass
    override fun newElement(): E = elementT.newInstance()
}
