// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler

import net.dummydigit.qbranch.compiler.symbols.*

internal interface Translator {
    fun getGeneratedFileExt() : String
    fun generateHeader() : String
    fun generate(bulitinType : BuiltinTypeDef) : String
    fun generate(containerType : BuiltinContainerDef) : String
    fun generate(builtinKvpContainerType : BuiltinKvpContainerDef) : String
    fun generate(typeParamDef: GenericTypeParamDef) : String
    fun generate(attr : AttributeDef) : String
    fun generate(enumDef : EnumDef) : String
    fun generate(importDef : ImportDef) : String
    fun generate(namespaceDef : NamespaceDef) : String
    fun generate(structDef : StructDef) : String
    fun generate(structField: StructFieldDef) : String
}