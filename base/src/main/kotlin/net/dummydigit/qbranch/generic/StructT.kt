// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

import kotlin.reflect.KClass

class StructT<out T : Any>(private val instanceCreator : () -> T) : TypeArg<T> {
    constructor(cls : KClass<T>) : this({ cls.java.newInstance() })
    override fun newInstance() = instanceCreator()
}