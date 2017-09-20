// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

import net.dummydigit.qbranch.QBranchSerializable

class StructT<T : QBranchSerializable>(private val instanceCreator : () -> T) : QTypeArg<T> {
    constructor(targetCls : Class<T>) : this({ targetCls.newInstance() })

    private val refObj = instanceCreator()
    private val cls = refObj.javaClass

    override fun newInstance() = instanceCreator()
    fun getJClass() : Class<T> = cls
}