package net.dummydigit.qbranch.compiler.symbols

import net.dummydigit.qbranch.compiler.SourceCodeInfo
import net.dummydigit.qbranch.compiler.codegen.Translator
import java.util.*

internal class StructDef(sourceCodeInfo : SourceCodeInfo,
                         val fieldList : LinkedList<StructFieldDef> = LinkedList(),
                         val attributeList : LinkedList<AttributeDef> = LinkedList(),
                         var isGeneric : Boolean = false,
                         val genericTypeParamList : LinkedList<String> = LinkedList(),
                         var hasBaseClass : Boolean = false,
                         var baseClass : TypeRef? = null,
                         var isViewOf : Boolean = false,
                         var isForwardDeclaration : Boolean = false)
    : Symbol(sourceCodeInfo, SymbolType.Struct) {
    override fun toTargetCode(gen: Translator): String = gen.generate(this)
}