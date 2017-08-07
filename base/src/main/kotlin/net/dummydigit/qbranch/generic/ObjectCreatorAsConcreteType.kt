package net.dummydigit.qbranch.generic

interface ObjectCreatorAsConcreteType<T> : ObjectCreatorAsAny {
    fun newInstance() : T
}