// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

class ListT<E : Any>(val elementT: QTypeArg<E>) : QTypeArg<MutableList<E>>, ContainerTypeArg<E> {
    private val refObj = newInstance()
    override fun newInstance(): MutableList<E> = mutableListOf()
    override fun getGenericType() : Class<MutableList<E>> = refObj.javaClass
    override fun newElement(): E = elementT.newInstance()
}
