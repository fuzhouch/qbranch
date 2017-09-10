// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.symbols

internal data class StructFieldType(val baseName : String,
                           val fullQualifiedName : String,
                           val typeFlag : SymbolType,
                           val typeArgIndex : Int,
                           val isTypeArg : Boolean,
                           val isBuiltInType : Boolean,
                           val isBuiltInContainer : Boolean,
                           val isNullable : Boolean,
                           val isEnum : Boolean)