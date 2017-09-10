// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.symbols

import net.dummydigit.qbranch.compiler.SourceCodeInfo
import net.dummydigit.qbranch.compiler.codegen.Translator
import java.util.*

internal class StructFieldDef(sourceCodeInfo : SourceCodeInfo,
                              var fieldOrderId : Int = -1,
                              var modifier : StructFieldModifier = StructFieldModifier.Optional,
                              var typeRef: TypeRef? = null,
                              var assignedValue: String? = null,
                              var isNothing : Boolean = false,
                              var isNullable : Boolean = false,
                              var isValueAssigned : Boolean = false,
                              val attributeList : LinkedList<AttributeDef> = LinkedList())
    : Symbol(sourceCodeInfo, SymbolType.StructField) {
    override fun toTargetCode(gen: Translator): String = gen.generate(this)
}