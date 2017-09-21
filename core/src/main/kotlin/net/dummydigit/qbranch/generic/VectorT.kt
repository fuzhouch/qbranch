// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

class VectorT<E : Any>(private val elementT: QTypeArg<E>) : QTypeArg<ArrayList<E>>, ContainerTypeArg<E> {

    private val refObj = arrayListOf<E>()
    private val refType = refObj.javaClass

    override fun newInstance(): ArrayList<E> = arrayListOf()
    override fun newElement(): E = elementT.newInstance()
    override fun getGenericType() = refType
}