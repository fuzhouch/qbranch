// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

class ListT<E : Any>(private val elementT: QTypeArg<E>) : QTypeArg<MutableList<E>>, ContainerTypeArg<E> {
    private val refObj = mutableListOf<E>()
    private val refType = refObj.javaClass

    override fun newInstance(): MutableList<E> = mutableListOf()
    override fun newElement(): E = elementT.newInstance()
    override fun getGenericType() = refType
}
