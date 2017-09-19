package net.dummydigit.qbranch.generic

interface KvpContainerTypeArg<out K : Any, out V : Any> {
    fun newKey() : K
    fun newValue() : V
}