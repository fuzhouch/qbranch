package net.dummydigit.qbranch.compiler

import net.dummydigit.qbranch.compiler.symbols.*

internal class JavaTranslator(private val settings : Settings) : Translator {

    private val builtinTypeMap = hashMapOf(
            "bool" to "java.lang.Boolean",
            "int8" to "byte",
            "int16" to "short",
            "int32" to "int",
            "int64" to "long",
            "uint8" to "UnsignedByte",
            "uint16" to "UnsignedShort",
            "uint32" to "UnsignedInt",
            "uint64" to "UnsignedLong",
            "string" to "ByteString",
            "wstring" to "String",
            "float" to "float",
            "double" to "double",
            "blob" to "net.dummydigit.qbranch.Blob"
    )

    private val builtinContainerTypeMap = hashMapOf(
            "vector" to "java.util.ArrayList",
            "list" to "java.util.LinkedList",
            "set" to "java.util.HashSet"
    )

    private val builtinKvpContainerTypeMap = hashMapOf(
            "map" to "java.util.HashMap"
    )

    private val structFieldModifierMap = hashMapOf(
            StructFieldModifier.Required to "@net.dummydigit.qbranch.annotations.Required",
            StructFieldModifier.RequiredOptional to "@net.dummydigit.qbranch.annotations.RequiredOptional",
            StructFieldModifier.Optional to ""
    )

    override fun getGeneratedFileExt() : String = "java"
    override fun generateHeader() : String = "// Generated by qbranch"

    override fun generate(symbol : BuiltinTypeDef) : String {
        return builtinTypeMap[symbol.name]!!
    }

    override fun generate(symbol : BuiltinContainerDef) : String {
        val elementType = symbol.elemetTypeRef!!.toTargetCode(this)
        val containerType = builtinContainerTypeMap[symbol.name]!!
        return "$containerType<$elementType>"
    }

    override fun generate(symbol : BuiltinKvpContainerDef) : String {
        val keyType = symbol.keyTypeRef!!.toTargetCode(this)
        val valueType = symbol.valueTypeRef!!.toTargetCode(this)
        val containerType = builtinKvpContainerTypeMap[symbol.name]!!
        return "$containerType<$keyType, $valueType>"
    }

    override fun generate(symbol: GenericTypeParamDef) : String {
        return symbol.name!!
    }

    override fun generate(symbol : AttributeDef) : String {
        return "@net.dummydigit.qbranch.annotations.Attribute(\"${symbol.key}\", \"${symbol.value}\")"
    }

    // TODO
    override fun generate(symbol : EnumDef) : String {
        throw NotImplementedError("EnumDef")
    }

    override fun generate(symbol : ImportDef) : String {
        return "import ${symbol.importPath}.*"
    }

    override fun generate(symbol : NamespaceDef) : String {
        return "package ${symbol.namespace}"
    }

    override fun generate(symbol: StructFieldDef) : String {
        val fieldId = "@net.dummydigit.qbranch.annotations.FieldId(id = ${symbol.fieldOrderId})"
        val nullable = if (symbol.isNullable) {
            "@net.dummydigit.qbranch.annotations.Nullable"
        } else {
            ""
        }

        val modifier = structFieldModifierMap[symbol.modifier]!!
        val fieldType = if (symbol.isNothing) {
            val type = symbol.typeRef!!.toTargetCode(this)
            when (type) {
                "byte" -> "java.lang.Byte"
                "short" -> "java.lang.Short"
                "int" -> "java.lang.Integer"
                "long" -> "java.lang.Long"
                "float" -> "java.lang.Float"
                "double" -> "java.lang.Double"
                else -> type
            }
        } else {
            symbol.typeRef!!.toTargetCode(this)
        }
        val field = symbol.name!!

        val defaultValue = if (symbol.isValueAssigned) {
            if (symbol.assignedValue.equals("empty", ignoreCase = false)) {
                " = null"
            } else {
                " = ${symbol.assignedValue!!}"
            }
        } else {
            ""
        }

        return "private $fieldId $nullable $modifier $fieldType $field$defaultValue"
    }

    override fun generate(symbol : StructDef) : String {
        val compilerName = settings.compilerName
        val compilerVersion = settings.codeGenSyntaxVersion
        val qbranchTag = "@QBranchGeneratedCode(compilerName = \"$compilerName\", version = \"$compilerVersion\")"
        return ""
    }
}