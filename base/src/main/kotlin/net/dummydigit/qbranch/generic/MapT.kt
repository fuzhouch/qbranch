// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

import java.util.HashMap

class MapT<K : Any, V: Any>(private val kCls : TypeArg<K>,
                            private val vCls : TypeArg<V>) : TypeArg<MutableMap<K, V>>, KvpContainerTypeArg<K, V> {
    override fun newInstance(): MutableMap<K, V> = mutableMapOf()
    override fun newKey(): K = kCls.newInstance()
    override fun newValue() : V = vCls.newInstance()
}