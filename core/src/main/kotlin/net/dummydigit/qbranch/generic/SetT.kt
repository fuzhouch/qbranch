// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

class SetT<E : Any>(val elementT: QTypeArg<E>) : QTypeArg<MutableSet<E>>, ContainerTypeArg<E> {
    private val refObj = mutableSetOf<E>()
    private val refType = refObj.javaClass

    override fun newInstance(): MutableSet<E> = mutableSetOf()
    override fun newElement(): E = elementT.newInstance()
    override fun getGenericType() = refType
}
