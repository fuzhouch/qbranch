// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

@file:JvmName("ReflectionExtensions")

package net.dummydigit.qbranch

import net.dummydigit.qbranch.annotations.QBranchGeneratedCode
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass

/** A function to check if given class is a generated bond class.
 *  @return True if cls is Bond generated, or false if not.
 */
fun Class<*>.isQBranchGeneratedStruct() : Boolean {
    val isBondGenerated = this.getAnnotation(QBranchGeneratedCode::class.java) != null
    return isBondGenerated && !this.isEnum
}

/**
 * A function to check if given class is a generic type.
 * @return True if class is generic type, or false if not.
 */
fun Class<*>.isGenericClass() : Boolean {
    return this.typeParameters.size > 0
}

/**
 * A function to check if given Kotlin class is a generic type.
 * @return True if class is generic type, or false if not.
 */
fun KClass<*>.isGenericClass() : Boolean {
    return this.java.isGenericClass()
}

fun Class<*>.isQBranchBuiltinType() : Boolean = when (this) {
    String::class.java -> true
    Byte::class.java -> true
    Short::class.java -> true
    Int::class.java -> true
    Long::class.java -> true
    UnsignedByte::class.java -> true
    UnsignedShort::class.java -> true
    UnsignedInt::class.java -> true
    UnsignedLong::class.java -> true
    ByteString::class.java -> true
    // TODO: Add container types later.
    else -> false
}

fun KClass<*>.isQBranchBuiltinType() : Boolean {
    return this.java.isQBranchBuiltinType()
}

/**
 * Tell whether a given field has a generic type.
 */
fun Field.isGenericType() : Boolean {
    val declaredType = this.genericType
    val realType = this.type
    return (declaredType !is ParameterizedType) && declaredType != (realType)
}


/**
 * A helper class to retrieve type arguments of given type.
 */
abstract class TypeReference<T> : Comparable<TypeReference<T>> {
    val type: Type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
    override fun compareTo(other: TypeReference<T>) = 0
}

/**
 * Extract type arguments of generic types. Callable from Kotlin code only.
 * @return An array of type, which represents the list of type arguments.
 */
inline fun <reified T: Any> extractGenericTypeArguments() : Array<Type> {
    // Make use of generic type token to allow we
    val type = object : TypeReference<T>() {}.type
    if (type is ParameterizedType) {
        return type.actualTypeArguments
    } else {
        throw UnsupportedOperationException("NonParameterizedType:type=$type")
    }
}
