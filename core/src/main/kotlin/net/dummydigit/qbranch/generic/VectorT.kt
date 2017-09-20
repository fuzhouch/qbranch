// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

import net.dummydigit.qbranch.impl.DeserializerBase

class VectorT<E : Any>(private val elementT: QTypeArg<E>) : QTypeArg<ArrayList<E>>, ContainerTypeArg<E> {
    override fun newInstance(): ArrayList<E> = arrayListOf()
    override fun newElement(): E = elementT.newInstance()
}