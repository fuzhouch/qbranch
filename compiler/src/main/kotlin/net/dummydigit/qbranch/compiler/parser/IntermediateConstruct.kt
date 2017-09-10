package net.dummydigit.qbranch.compiler.parser

import net.dummydigit.qbranch.compiler.ParsingUtil
import net.dummydigit.qbranch.compiler.SourceCodeInfo
import net.dummydigit.qbranch.compiler.exceptions.*
import net.dummydigit.qbranch.compiler.grammar.BondIdlGrammarBaseListener
import net.dummydigit.qbranch.compiler.grammar.BondIdlGrammarParser
import net.dummydigit.qbranch.compiler.symbols.*
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.util.LinkedList

internal class IntermediateConstruct(sourceTreeList : List<IdlDefinition>) {
    companion object {
        val builtinNamespaceName = "<builtin>"
    }

    val symbolTableBySource = HashMap<String, IdlDefinition>()
    val symbolTableByNamespace = HashMap<String, HashSet<Symbol>>()
    private val importToNamespace = HashMap<String, String>()

    init {
        val walker = ParseTreeWalker()
        sourceTreeList.forEach {
            val visitor = SyntaxTreeVisitor(it)
            if (symbolTableBySource.containsKey(it.sourceCodePath)) {
                throw RepeatedParsingException(it.sourceCodePath)
            } else {
                symbolTableBySource[it.sourceCodePath] = it
            }
            walker.walk(visitor, it.syntaxTree)
        }
    }

    private inner class SyntaxTreeVisitor(val idlDef : IdlDefinition) : BondIdlGrammarBaseListener() {
        private val symbolStack = LinkedList<Symbol>()

        override fun enterBondIdl(ctx: BondIdlGrammarParser.BondIdlContext?) {
            super.enterBondIdl(ctx)
            buildBuiltinNamespace(ctx!!)
        }

        // ==================================================================
        // Handle namespace declaration
        // ==================================================================
        override fun enterNamespaceName(ctx: BondIdlGrammarParser.NamespaceNameContext?) {
            super.enterNamespaceName(ctx)
            val namespaceStr = ctx?.IDENTIFIER()
            val multiSecNamespaceStr = ctx?.MULTI_SECTION_IDENTIFIER()

            val namespaceName = when {
                namespaceStr != null -> namespaceStr.toString()
                multiSecNamespaceStr != null -> multiSecNamespaceStr.toString()
                else -> throw CompilationError("badNamespaceName", getSourceCodeInfo(ctx))
            }

            // Allow we know the declared namespace in given file
            importToNamespace[idlDef.sourceCodePath] = namespaceName

            // Allow we generate proper statement for namespace
            val namespaceDef = NamespaceDef(getSourceCodeInfo(ctx), namespaceName)
            idlDef.symbolsInFile.add(namespaceDef)

            // Allow we quickly reference the symbol in cross reference
            if (!symbolTableByNamespace.containsKey(namespaceName)) {
                symbolTableByNamespace[namespaceName] = HashSet()
            }
        }

        // ==================================================================
        // Handle import statement
        // ==================================================================
        override fun enterImportDecl(ctx: BondIdlGrammarParser.ImportDeclContext?) {
            super.enterImportDecl(ctx)
            val value = ctx?.QUOTED_STRING()!! // Impossible to be null
            val importPath = ParsingUtil.unescapeQuotedString(value.toString())
            val importSymbol = ImportDef(getSourceCodeInfo(ctx), importToNamespace[importPath]!!)
            idlDef.symbolsInFile.add(importSymbol)
        }

        override fun enterAttributeBody(ctx: BondIdlGrammarParser.AttributeBodyContext?) {
            super.enterAttributeBody(ctx)
            val attrKey = ctx?.IDENTIFIER().toString()
            val attrValue = ParsingUtil.unescapeQuotedString(ctx?.QUOTED_STRING().toString())
            val newAttr = AttributeDef(getSourceCodeInfo(ctx), attrKey, attrValue)
            val ongoingSymbol = symbolStack.first
            when (ongoingSymbol) {
                is StructDef -> ongoingSymbol.attributeList.addLast(newAttr)
                is StructFieldDef -> ongoingSymbol.attributeList.addLast(newAttr)
                else -> throw InternalCompilationStateError(
                        "ExpectStructOrFieldBeforeAttribute",
                        getSourceCodeInfo(ctx))
            }
        }

        // ==================================================================
        // Handle struct definitions
        // ==================================================================
        override fun enterStructDef(ctx: BondIdlGrammarParser.StructDefContext?) {
            super.enterStructDef(ctx)
            val newStructSymbol = StructDef(getSourceCodeInfo(ctx))
            symbolStack.push(newStructSymbol)
        }

        override fun exitStructDef(ctx: BondIdlGrammarParser.StructDefContext?) {
            super.exitStructDef(ctx)
            val completedStructSymbol = symbolStack.first as StructDef
            // We'd better avoid trying to verify struct definition here,
            // because it's difficult to get correct code line number.

            idlDef.symbolsInFile.add(completedStructSymbol)
            addSymbolToNamespace(completedStructSymbol)
        }

        override fun enterTypeDeclName(ctx: BondIdlGrammarParser.TypeDeclNameContext?) {
            super.enterTypeDeclName(ctx)
            val ongoingStructSymbol = symbolStack.first as StructDef
            ongoingStructSymbol.name = ctx?.IDENTIFIER().toString()
        }

        override fun enterTypeParamName(ctx: BondIdlGrammarParser.TypeParamNameContext?) {
            super.enterTypeParamName(ctx)
            val ongoingStructSymbol = symbolStack.first as StructDef

            // Given we see a type parameter, we can confirm this is
            // a generic struct.
            val typeDeclName = ctx?.IDENTIFIER().toString()
            if (ParsingUtil.bondIdlKeyword.contains(typeDeclName)) {
                throw CompilationError("KeywordAsIdentifier", getSourceCodeInfo(ctx))
            }

            if (ongoingStructSymbol.genericTypeParamList.toSet().contains(typeDeclName)) {
                throw CompilationError("RepeatedTypeParam", getSourceCodeInfo(ctx))
            }

            ongoingStructSymbol.isGeneric = true
            ongoingStructSymbol.genericTypeParamList.add(typeDeclName)
        }

        override fun enterStructOrViewDef(ctx: BondIdlGrammarParser.StructOrViewDefContext?) {
            super.enterStructOrViewDef(ctx)
            if (ctx?.VIEW_OF_KEYWORD() != null) {
                val ongoingStructSymbol = symbolStack.first as StructDef
                ongoingStructSymbol.isViewOf = true
            }
        }

        // ==================================================================
        // Handle type references in IDL code, including generic
        // ==================================================================
        override fun enterTypeNameWithGeneric(ctx: BondIdlGrammarParser.TypeNameWithGenericContext?) {
            super.enterTypeNameWithGeneric(ctx)
            val newTypeRefSymbol = TypeRef(getSourceCodeInfo(ctx))
            // TypeRef can be a recursive structure when parsing
            // generic argument list, like
            //     struct MyStruct<T> : Base<T> { }
            //
            // T will be represented as typeNameWithGeneric
            // when parsing Base<T>. In this case, the previous
            // symbol in stack is TypeRef with refType == Base.
            // In this case, we must trace to real MyStruct,
            // in order to get correct definition of T.
            var ongoingParentSymbol = symbolStack.first
            while (ongoingParentSymbol.type == SymbolType.TypeRef) {
                ongoingParentSymbol = (ongoingParentSymbol as TypeRef).definedByType
            }
            newTypeRefSymbol.definedByType = ongoingParentSymbol
            symbolStack.push(newTypeRefSymbol)
        }

        override fun exitTypeNameWithGeneric(ctx: BondIdlGrammarParser.TypeNameWithGenericContext?) {
            super.exitTypeNameWithGeneric(ctx)
            val completedTypeRefSymbol = symbolStack.pop() as TypeRef
            val ongoingParentSymbol = symbolStack.first
            when (ongoingParentSymbol) {
                is BuiltinContainerDef -> ongoingParentSymbol.elemetTypeRef = completedTypeRefSymbol
                is StructDef -> {
                    ongoingParentSymbol.baseClass = completedTypeRefSymbol
                    if (!ongoingParentSymbol.isViewOf) {
                        ongoingParentSymbol.hasBaseClass = true
                    }
                }
                is BuiltinKvpContainerDef -> when {
                    ongoingParentSymbol.keyTypeRef == null ->
                        ongoingParentSymbol.keyTypeRef = completedTypeRefSymbol
                    ongoingParentSymbol.valueTypeRef == null ->
                        ongoingParentSymbol.valueTypeRef = completedTypeRefSymbol
                    else -> throw CompilationError("MoreOnKvp", getSourceCodeInfo(ctx))
                }

                is TypeRef -> {
                    ongoingParentSymbol.genericTypeArgs.add(completedTypeRefSymbol)
                }

                is StructFieldDef -> {
                    ongoingParentSymbol.typeRef = completedTypeRefSymbol
                    ongoingParentSymbol.isNullable = completedTypeRefSymbol.refType?.name == "nullable"
                }

                else -> throw NotImplementedError(ongoingParentSymbol.name) // TODO: CustomType
            }
        }

        override fun enterBuiltinPrimitiveType(ctx: BondIdlGrammarParser.BuiltinPrimitiveTypeContext?) {
            super.enterBuiltinPrimitiveType(ctx)
            val builtinTypeName = ctx?.SIGNED_INTEGER_TYPE_KEYWORD()
                    ?: ctx?.UNSIGNED_INTEGER_TYPE_KEYWORD()
                    ?: ctx?.FLOAT_POINT_TYPE_KEYWORD()
                    ?: ctx?.BYTESTRING_TYPE_KEYWORD()
                    ?: ctx?.WSTRING_TYPE_KEYWORD()
                    ?: throw InternalCompilationStateError("enterBuiltinPrimitiveType:UnknownPrimitive",
                    getSourceCodeInfo(ctx))

            val builtinTypeSymbol = BuiltinTypeDef(getSourceCodeInfo(ctx), builtinTypeName.toString())
            val ongoingTypeRef = symbolStack.first as TypeRef
            ongoingTypeRef.refType = builtinTypeSymbol
        }

        override fun enterBuiltinContainerType(ctx: BondIdlGrammarParser.BuiltinContainerTypeContext?) {
            super.enterBuiltinContainerType(ctx)
            val containerNameTag = ctx?.CONTAINER_TYPE_KEYWORD() ?:
                    throw InternalCompilationStateError("enterBuiltinContainerType:noContainerName",
                            getSourceCodeInfo(ctx))
            val containerName = containerNameTag.toString()
            val newContainerSymbol : Symbol = when {
                ParsingUtil.bondIdlContainerType.contains(containerName) ->
                        BuiltinContainerDef(getSourceCodeInfo(ctx), containerName)
                ParsingUtil.bondIdlKvpContainerType.contains(containerName) ->
                        BuiltinKvpContainerDef(getSourceCodeInfo(ctx), containerName)
                else -> throw InternalCompilationStateError(
                        "enterBuiltinContainerType:unknownContainer",
                        getSourceCodeInfo(ctx))
            }
            val ongoingTypeRef = symbolStack.first as TypeRef
            ongoingTypeRef.refType = newContainerSymbol // We will parse type args later.
            symbolStack.push(newContainerSymbol)
        }

        override fun exitBuiltinContainerType(ctx: BondIdlGrammarParser.BuiltinContainerTypeContext?) {
            super.exitBuiltinContainerType(ctx)
            symbolStack.pop()
        }

        override fun enterCustomType(ctx: BondIdlGrammarParser.CustomTypeContext?) {
            super.enterCustomType(ctx)
            val typeName = ctx?.IDENTIFIER()
                    ?: ctx?.MULTI_SECTION_IDENTIFIER()
                    ?: throw SymbolNotFoundError("UnknownSymbol", getSourceCodeInfo(ctx))
            val ongoingTypeRefSymbol = symbolStack.first as TypeRef

            val refToTypeDef = lookupSymbolByName(typeName.toString(),
                    ctx!!,
                    ongoingTypeRefSymbol,
                    SymbolType.Struct)
            val ongoingTypeRef = symbolStack.first as TypeRef
            ongoingTypeRef.refType = refToTypeDef
        }

        // ==================================================================
        // Handling struct field definition
        // ==================================================================
        override fun enterSingleStructFieldDef(ctx: BondIdlGrammarParser.SingleStructFieldDefContext?) {
            super.enterSingleStructFieldDef(ctx)
            val newStructFieldDef = StructFieldDef(getSourceCodeInfo(ctx))
            symbolStack.push(newStructFieldDef)
        }

        override fun exitSingleStructFieldDef(ctx: BondIdlGrammarParser.SingleStructFieldDefContext?) {
            super.exitSingleStructFieldDef(ctx)
            val completedStructFieldDef = symbolStack.pop() as StructFieldDef
            val ongoingStructDef = symbolStack.first as StructDef
            ongoingStructDef.fieldList.addLast(completedStructFieldDef)
        }

        override fun enterFieldName(ctx: BondIdlGrammarParser.FieldNameContext?) {
            super.enterFieldName(ctx)
            val ongoingStructFieldDef = symbolStack.first as StructFieldDef
            val fieldName = when {
                ctx?.NOTHING_KEYWORD() != null -> ctx.NOTHING_KEYWORD()
                ctx?.IDENTIFIER() != null -> ctx.IDENTIFIER()
                else -> throw InternalCompilationStateError(
                        "unexpectedFieldNameType",
                        getSourceCodeInfo(ctx))
            }
            ongoingStructFieldDef.name = fieldName.toString()
        }

        override fun enterFieldModifier(ctx: BondIdlGrammarParser.FieldModifierContext?) {
            super.enterFieldModifier(ctx)
            val modifier = when {
                ctx?.OPTIONAL_KEYWORD() != null -> StructFieldModifier.Optional
                ctx?.REQUIRED_KEYWORD() != null -> StructFieldModifier.Required
                ctx?.REQUIRED_OPTIONAL_KEYWORD() != null -> StructFieldModifier.RequiredOptional
                else -> throw InternalCompilationStateError(
                        "UnexpectedFieldModifier",
                        getSourceCodeInfo(ctx))
            }

            val ongoingStructFieldDef = symbolStack.first as StructFieldDef
            ongoingStructFieldDef.modifier = modifier
        }

        override fun enterDefaultValues(ctx: BondIdlGrammarParser.DefaultValuesContext?) {
            super.enterDefaultValues(ctx)
            val ongoingStructFieldDef = symbolStack.first as StructFieldDef
            when {
                ctx?.IDENTIFIER() != null -> {
                    val defaultValueAsString = ctx.IDENTIFIER().toString()
                    ongoingStructFieldDef.isValueAssigned = true
                    ongoingStructFieldDef.assignedValue = defaultValueAsString
                    // TODO Shall we check enum values?
                }
                ctx?.NOTHING_KEYWORD() != null -> {
                    ongoingStructFieldDef.isNothing = true
                    ongoingStructFieldDef.isValueAssigned = false
                }

                ctx?.QUOTED_STRING() != null -> {
                    val stringValue = ctx.QUOTED_STRING().toString()
                    val fieldTypeDef = ongoingStructFieldDef.typeRef!!.refType!!
                    if (ParsingUtil.isStringTypeName(fieldTypeDef.name)) {
                        ongoingStructFieldDef.isValueAssigned = true
                        ongoingStructFieldDef.assignedValue = stringValue
                    } else {
                        throw BadValueError(stringValue, getSourceCodeInfo(ctx))
                    }
                }

                ctx?.DEC_NUMBER() != null -> {
                    val fieldTypeDef = ongoingStructFieldDef.typeRef!!.refType!!
                    val value = ctx.DEC_NUMBER().toString()
                    if (ParsingUtil.isIntegerTypeName(fieldTypeDef.name) ||
                            ParsingUtil.isFloatTypeName(fieldTypeDef.name)) {
                        ongoingStructFieldDef.isValueAssigned = true
                        ongoingStructFieldDef.assignedValue = value
                    } else {
                        throw BadValueError(value, getSourceCodeInfo(ctx))
                    }
                }

                ctx?.HEX_NUMBER() != null -> {
                    val fieldTypeDef = ongoingStructFieldDef.typeRef!!.refType!!
                    val value = ctx.DEC_NUMBER().toString()
                    if (ParsingUtil.isIntegerTypeName(fieldTypeDef.name)) {
                        ongoingStructFieldDef.isValueAssigned = true
                        ongoingStructFieldDef.assignedValue = value
                    } else {
                        throw BadValueError(value, getSourceCodeInfo(ctx))
                    }
                }

                ctx?.FLOAT_NUMBER() != null -> {
                    val fieldTypeDef = ongoingStructFieldDef.typeRef!!.refType!!
                    val value = ctx.FLOAT_NUMBER().toString()
                    if (ParsingUtil.isFloatTypeName(fieldTypeDef.name)) {
                        ongoingStructFieldDef.isValueAssigned = true
                        ongoingStructFieldDef.assignedValue = value
                    } else {
                        throw BadValueError(value, getSourceCodeInfo(ctx))
                    }
                }
            }
        }

        // ==================================================================
        // Handling enum types
        // ==================================================================
        override fun enterEnumName(ctx: BondIdlGrammarParser.EnumNameContext?) {
            super.enterEnumName(ctx)
            val enumTypeName = ctx?.IDENTIFIER()?.toString()!!
            val attrListPlaceholder = LinkedList<Pair<String, Int>>()
            val enumDef = EnumDef(getSourceCodeInfo(ctx), enumTypeName, attrListPlaceholder)
            symbolStack.push(enumDef)
        }

        override fun exitEnumDef(ctx: BondIdlGrammarParser.EnumDefContext?) {
            super.exitEnumDef(ctx)
            val completedEnumDef = symbolStack.pop() as EnumDef

            idlDef.symbolsInFile.add(completedEnumDef)
            addSymbolToNamespace(completedEnumDef)
        }

        override fun enterEnumSymbol(ctx: BondIdlGrammarParser.EnumSymbolContext?) {
            super.enterEnumSymbol(ctx)
            val enumSymbolName = ctx?.IDENTIFIER()?.toString()!!
            val parsingEnumDef = symbolStack.first as EnumDef

            // Check duplicated symbol definition.
            parsingEnumDef.valueList.forEach {
                if (it.first == enumSymbolName) {
                    throw DuplicatedEnumSymbolError(enumSymbolName, getSourceCodeInfo(ctx))
                }
            }

            val enumValue = parsingEnumDef.currentDefaultValue
            val newEnumFieldSymbol = EnumFieldDef(getSourceCodeInfo(ctx),
                    parsingEnumDef,
                    enumSymbolName,
                    enumValue)
            parsingEnumDef.currentDefaultValue += 1
            symbolStack.push(newEnumFieldSymbol)
        }

        override fun exitSingleEnumSymbolDef(ctx: BondIdlGrammarParser.SingleEnumSymbolDefContext?) {
            super.exitSingleEnumSymbolDef(ctx)
            val completeEnumField = symbolStack.pop() as EnumFieldDef
            val parsingEnumDef = symbolStack.first as EnumDef
            val symbolNameValue = Pair(completeEnumField.fieldName, completeEnumField.enumValue)
            parsingEnumDef.valueList.add(symbolNameValue)
        }

        // ==================================================================
        // Handling common callbacks, shared by all structs
        // ==================================================================
        override fun enterIntegerLiteral(ctx: BondIdlGrammarParser.IntegerLiteralContext?) {
            super.enterIntegerLiteral(ctx)

            val numStr10 = ctx?.DEC_NUMBER()
            val numStr16 = ctx?.HEX_NUMBER()
            if (numStr10 == null && numStr16 == null) {
                throw InternalCompilationStateError(
                        "enterIntegerLiteral:NoValue",
                        getSourceCodeInfo(ctx))
            }

            val num = numStr10?.toString()?.toInt() ?:
                    numStr16?.toString()?.substring(2)?.toInt(radix = 16)

            val lastSymbolInStack = symbolStack.first
            when (lastSymbolInStack) {
                is EnumFieldDef -> {
                    lastSymbolInStack.enumValue = num!!
                    lastSymbolInStack.enumDef.currentDefaultValue = lastSymbolInStack.enumValue + 1
                }

                is StructFieldDef -> {
                    lastSymbolInStack.fieldOrderId = num!!
                }

                else -> throw NotImplementedError("IntegerLiteral") // TODO Handle other types
            }
        }

        // ==================================================================
        // Helper functions
        // ==================================================================
        private fun getSourceCodeInfo(ctx : ParserRuleContext?) = SourceCodeInfo(
                path = idlDef.sourceCodePath,
                lineNo = ctx?.start?.line ?: 0
        )

        private fun getNamespaceOfIdl() : String {
            // namespace can be null when and only when
            // passed Idl definition structure does not follow
            // dependency order, which should be handled by
            // SourceTreeParser.
            return importToNamespace[idlDef.sourceCodePath]!!
        }

        private fun addSymbolToNamespace(symbol : Symbol) {
            // TODO : Add additional check
            symbolTableByNamespace[getNamespaceOfIdl()]!!.add(symbol)
        }

        private fun lookupSymbolByName(symbolName : String,
                                       ctx: ParserRuleContext,
                                       belongToTypeRef : TypeRef,
                                       expectedType : SymbolType) : Symbol {
            // There are three different scenarios, which should be
            // checked with order below:

            // 1. If it's a built-in type.
            val builtinNamespaceSymbols = symbolTableByNamespace[builtinNamespaceName]!!
            val foundBuiltinSymbol = builtinNamespaceSymbols.find { symbolName == it.name }
            if (foundBuiltinSymbol != null) {
                return foundBuiltinSymbol
            }

            // 2. If it's a type argument from belongToTypeRef
            val definedByType = belongToTypeRef.definedByType
            when (definedByType) {
                is StructDef -> {
                    if (definedByType.genericTypeParamList.toSet().contains(symbolName)) {
                        return GenericTypeParamDef(getSourceCodeInfo(ctx), symbolName)
                    }
                }
                is BuiltinContainerDef -> {
                    if (definedByType.elemetTypeRef!!.name == symbolName) {
                        return GenericTypeParamDef(getSourceCodeInfo(ctx), symbolName)
                    }
                }

                is BuiltinKvpContainerDef -> {
                    if (definedByType.keyTypeRef!!.name == symbolName) {
                        return GenericTypeParamDef(getSourceCodeInfo(ctx), symbolName)
                    }

                    if (definedByType.valueTypeRef!!.name == symbolName) {
                        return GenericTypeParamDef(getSourceCodeInfo(ctx), symbolName)
                    }
                }
                else -> {} // Simply do nothing. Let's go next step.
            }

            // 3. It can be a symbol defined in defined namespaces:
            val splitNames = symbolName.split('.')
            val baseName = splitNames[splitNames.size - 1]
            val namespace = when (splitNames.size) {
                // 1 == implicit namespace. Assume symbol from current namespace.
                1 -> importToNamespace[idlDef.sourceCodePath]
                        ?: throw CompilationError("NamespaceNotDefinedBeforeContent",
                                                  getSourceCodeInfo(ctx))
                else -> splitNames.slice(IntRange(0, splitNames.size - 2)).joinToString(".")
            }

            if (!symbolTableByNamespace.containsKey(namespace)) {
                throw UndefinedNamespaceError(namespace, getSourceCodeInfo(ctx))
            }
            val symbolSet = symbolTableByNamespace[namespace]!!
            val foundSymbol = symbolSet.find { it.name == baseName }

            return when {
                foundSymbol == null -> throw SymbolNotFoundError(symbolName, getSourceCodeInfo(ctx))
                expectedType == SymbolType.Any -> foundSymbol
                foundSymbol.type != expectedType -> throw TypeMismatchError(
                        symbolName,
                        expectedType,
                        foundSymbol.type,
                        getSourceCodeInfo(ctx))
                else -> foundSymbol
            }
        }

        private fun buildBuiltinNamespace(ctx: ParserRuleContext) {
            val defaultNamespaceSymbols = HashSet<Symbol>()
            ParsingUtil.bondIdlPrimitiveType
                    .map { BuiltinTypeDef(getSourceCodeInfo(ctx), it) }
                    .forEach {
                        defaultNamespaceSymbols.add(it)
                    }
            ParsingUtil.bondIdlContainerType
                    .map { BuiltinContainerDef(getSourceCodeInfo(ctx), it) }
                    .forEach {
                        defaultNamespaceSymbols.add(it)
                    }
            ParsingUtil.bondIdlKvpContainerType
                    .map { BuiltinKvpContainerDef(getSourceCodeInfo(ctx), it) }
                    .forEach {
                        defaultNamespaceSymbols.add(it)
                    }
            symbolTableByNamespace[builtinNamespaceName] = defaultNamespaceSymbols
        }
    }
}