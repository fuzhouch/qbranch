// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.generic

class MapT<K : Any, V: Any>(private val kCls : QTypeArg<K>,
                            private val vCls : QTypeArg<V>) : QTypeArg<MutableMap<K, V>>, KvpContainerTypeArg<K, V> {
    override fun newInstance(): MutableMap<K, V> = mutableMapOf()
    override fun newKey(): K = kCls.newInstance()
    override fun newValue() : V = vCls.newInstance()
}