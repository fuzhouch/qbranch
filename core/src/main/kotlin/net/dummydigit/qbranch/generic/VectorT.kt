// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

class VectorT<E : Any>(val elementT: QTypeArg<E>) : QTypeArg<ArrayList<E>>, ContainerTypeArg<E> {
    private val refObj = newInstance()
    override fun newInstance(): ArrayList<E> = arrayListOf()
    override fun getGenericType() : Class<ArrayList<E>> = refObj.javaClass
    override fun newElement(): E = elementT.newInstance()
}