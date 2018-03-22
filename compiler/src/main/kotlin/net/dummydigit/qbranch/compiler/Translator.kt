// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler

import net.dummydigit.qbranch.compiler.symbols.*

internal interface Translator {
    fun getGeneratedFileExt() : String
    fun generateHeader() : String
    fun generate(symbol : BuiltinTypeDef) : String
    fun generate(symbol : BuiltinContainerDef) : String
    fun generate(symbol : BuiltinKvpContainerDef) : String
    fun generate(symbol: GenericTypeParamDef) : String
    fun generate(symbol : AttributeDef) : String
    fun generate(symbol : EnumDef) : String
    fun generate(symbol : ImportDef) : String
    fun generate(symbol : NamespaceDef) : String
    fun generate(symbol : StructDef) : String
    fun generate(symbol: StructFieldDef) : String
}