// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.parser

import net.dummydigit.qbranch.compiler.ParsingUtil
import net.dummydigit.qbranch.compiler.exceptions.CompilationError
import net.dummydigit.qbranch.compiler.mocks.MockStringSourceCodeLoader
import net.dummydigit.qbranch.compiler.symbols.*
import org.junit.Assert
import org.junit.Test

class IntermediateConstructTest {
    @Test
    fun testNamespaceCreation() {
        val mockCode = hashMapOf(
                "test.bond" to """
                    import "bond.bond"
                    import "lib.bond"
                    namespace qbranch.ut
                    """,
                "bond.bond" to "namespace bond",
                "lib.bond" to "namespace qbranch.ut"
        )
        val loader = MockStringSourceCodeLoader(mockCode)
        val parser = SourceTreeParser(loader)
        val sortedSourceTree = parser.parse("test.bond")
        val construct = IntermediateConstruct(sortedSourceTree)
        Assert.assertEquals(3, construct.symbolTableBySource.size)
        Assert.assertTrue(construct.symbolTableBySource.containsKey("test.bond"))
        Assert.assertTrue(construct.symbolTableBySource.containsKey("bond.bond"))
        Assert.assertTrue(construct.symbolTableBySource.containsKey("lib.bond"))
        Assert.assertTrue(construct.symbolTableByNamespace.containsKey("bond"))
        // include <builtin>, bond and qbranch.ut
        Assert.assertEquals(3, construct.symbolTableByNamespace.size)
        Assert.assertTrue(construct.symbolTableByNamespace.containsKey("qbranch.ut"))
    }

    @Test
    fun testEnumCreation() {
        val mockCode = hashMapOf(
                "test.bond" to """
                    namespace qbranch.ut
                    enum TestEnum {
                        Value0,
                        Value2 = 2,
                        Value3
                    }
                    """
        )
        val loader = MockStringSourceCodeLoader(mockCode)
        val parser = SourceTreeParser(loader)
        val sortedSourceTree = parser.parse("test.bond")
        val construct = IntermediateConstruct(sortedSourceTree)
        Assert.assertTrue(construct.symbolTableByNamespace.containsKey("qbranch.ut"))
        val symbols : HashSet<Symbol> = construct.symbolTableByNamespace["qbranch.ut"]!!
        Assert.assertEquals(1, symbols.size)
        val idlDef = construct.symbolTableBySource["test.bond"]!!
        val enumSymbol = idlDef.symbolsInFile[1] as EnumDef
        Assert.assertEquals(SymbolType.Enum, enumSymbol.type)
        Assert.assertEquals("TestEnum", enumSymbol.name)
        Assert.assertEquals(3, enumSymbol.valueList.size)
        Assert.assertEquals("Value0", enumSymbol.valueList[0].first)
        Assert.assertEquals(0, enumSymbol.valueList[0].second)
        Assert.assertEquals("Value2", enumSymbol.valueList[1].first)
        Assert.assertEquals(2, enumSymbol.valueList[1].second)
        Assert.assertEquals("Value3", enumSymbol.valueList[2].first)
        Assert.assertEquals(3, enumSymbol.valueList[2].second)
    }

    @Test
    fun testNonGenericStructCreation() {
        val mockCode = hashMapOf(
                "test.bond" to """
                    namespace qbranch.ut
                    struct TestStruct {
                    }
                    """
        )
        val loader = MockStringSourceCodeLoader(mockCode)
        val parser = SourceTreeParser(loader)
        val sortedSourceTree = parser.parse("test.bond")
        val construct = IntermediateConstruct(sortedSourceTree)
        Assert.assertTrue(construct.symbolTableByNamespace.containsKey("qbranch.ut"))
        val symbols : HashSet<Symbol> = construct.symbolTableByNamespace["qbranch.ut"]!!
        Assert.assertEquals(1, symbols.size)
        val idlDef = construct.symbolTableBySource["test.bond"]!!
        val structSymbol = idlDef.symbolsInFile[1] as StructDef
        Assert.assertEquals(SymbolType.Struct, structSymbol.type)
        Assert.assertEquals("TestStruct", structSymbol.name)
        Assert.assertFalse(structSymbol.isGeneric)
        Assert.assertFalse(structSymbol.hasBaseClass)
        Assert.assertFalse(structSymbol.isViewOf)
    }

    @Test
    fun testViewOfStructCreation() {
        val mockCode = hashMapOf(
                "test.bond" to """
                    namespace qbranch.ut
                    struct TestStructBase {
                        0: int32 value;
                    }
                    struct TestStruct view_of TestStructBase {
                        value
                    }
                    """
        )
        val loader = MockStringSourceCodeLoader(mockCode)
        val parser = SourceTreeParser(loader)
        val sortedSourceTree = parser.parse("test.bond")
        val construct = IntermediateConstruct(sortedSourceTree)
        Assert.assertTrue(construct.symbolTableByNamespace.containsKey("qbranch.ut"))
        val symbols : HashSet<Symbol> = construct.symbolTableByNamespace["qbranch.ut"]!!
        Assert.assertEquals(2, symbols.size)
        val idlDef = construct.symbolTableBySource["test.bond"]!!
        val structSymbol = idlDef.symbolsInFile[2] as StructDef
        Assert.assertEquals(SymbolType.Struct, structSymbol.type)
        Assert.assertEquals("TestStruct", structSymbol.name)
        Assert.assertFalse(structSymbol.isGeneric)
        Assert.assertTrue(structSymbol.isViewOf)
        Assert.assertFalse(structSymbol.hasBaseClass)
    }

    @Test
    fun testNonGenericStructParamListCreation() {
        val mockCode = hashMapOf(
                "test.bond" to """
                    namespace qbranch.ut
                    struct TestStructG<K,V> {
                    }
                    """
        )
        val loader = MockStringSourceCodeLoader(mockCode)
        val parser = SourceTreeParser(loader)
        val sortedSourceTree = parser.parse("test.bond")
        val construct = IntermediateConstruct(sortedSourceTree)
        Assert.assertTrue(construct.symbolTableByNamespace.containsKey("qbranch.ut"))
        val symbols : HashSet<Symbol> = construct.symbolTableByNamespace["qbranch.ut"]!!
        Assert.assertEquals(1, symbols.size)
        val idlDef = construct.symbolTableBySource["test.bond"]!!
        val structSymbol = idlDef.symbolsInFile[1] as StructDef
        Assert.assertEquals(SymbolType.Struct, structSymbol.type)
        Assert.assertEquals("TestStructG", structSymbol.name)
        Assert.assertTrue(structSymbol.isGeneric)
        Assert.assertEquals(2, structSymbol.genericTypeParamList.size)
        Assert.assertEquals("K", structSymbol.genericTypeParamList[0])
        Assert.assertEquals("V", structSymbol.genericTypeParamList[1])
        Assert.assertFalse(structSymbol.hasBaseClass)
        Assert.assertFalse(structSymbol.isViewOf)
    }

    @Test(expected = CompilationError::class)
    fun testRepeatedTypeParamsErrorInStructCreation() {
        val mockCode = hashMapOf(
                "test.bond" to """
                    namespace qbranch.ut
                    struct TestStructG<K,K> {
                    }
                    """
        )
        val loader = MockStringSourceCodeLoader(mockCode)
        val parser = SourceTreeParser(loader)
        val sortedSourceTree = parser.parse("test.bond")
        try {
            IntermediateConstruct(sortedSourceTree)
        } catch (e: CompilationError) {
            Assert.assertEquals("RepeatedTypeParam", e.hint)
            Assert.assertEquals("test.bond", e.sourceCodeInfo.path)
            Assert.assertEquals(3, e.sourceCodeInfo.lineNo)
            throw e
        }
    }

    @Test(expected = CompilationError::class)
    fun testKeywordAsTypeParamInStructdef() {
        ParsingUtil.bondIdlKeyword.forEach {
            val mockCode = hashMapOf(
                    "test.bond" to """
                    namespace qbranch.ut
                    struct TestStructG<K,$it> {
                    }
                    """
            )
            val loader = MockStringSourceCodeLoader(mockCode)
            val parser = SourceTreeParser(loader)
            val sortedSourceTree = parser.parse("test.bond")
            try {
                IntermediateConstruct(sortedSourceTree)
            } catch (e: CompilationError) {
                Assert.assertEquals("KeywordAsIdentifier", e.hint)
                Assert.assertEquals("test.bond", e.sourceCodeInfo.path)
                Assert.assertEquals(3, e.sourceCodeInfo.lineNo)
                throw e
            }
        }
    }

    @Test
    fun testStructWithBaseClassCreation() {
        val mockCode = hashMapOf(
                "test.bond" to """
                    namespace qbranch.ut
                    struct BaseClass {
                    }
                    struct TestStructB : BaseClass {
                    }
                    """
        )
        val loader = MockStringSourceCodeLoader(mockCode)
        val parser = SourceTreeParser(loader)
        val sortedSourceTree = parser.parse("test.bond")
        val construct = IntermediateConstruct(sortedSourceTree)

        val mainSymbolTable = construct.symbolTableBySource["test.bond"]!!
        Assert.assertEquals(3, mainSymbolTable.symbolsInFile.size)

        val testStructB = mainSymbolTable.symbolsInFile[2] as StructDef
        Assert.assertEquals("TestStructB", testStructB.name)
        Assert.assertEquals(true, testStructB.hasBaseClass)

        val baseClass = testStructB.baseClass!!.refType!!
        Assert.assertEquals("BaseClass", baseClass.name)
        Assert.assertEquals(baseClass, mainSymbolTable.symbolsInFile[1])
    }

    @Test
    fun testStructWithGenericBaseClassCreation() {
        val mockCode = hashMapOf(
                "test.bond" to """
                    namespace qbranch.ut
                    struct OtherType<S> {}
                    struct BaseClass<K, V> {}
                    struct TestStructGB<T>
                        : BaseClass<int32,
                                    T,
                                    OtherType<string>,
                                    list<T>> {}
                    """
        )
        val loader = MockStringSourceCodeLoader(mockCode)
        val parser = SourceTreeParser(loader)
        val sortedSourceTree = parser.parse("test.bond")
        val construct = IntermediateConstruct(sortedSourceTree)

        val mainSymbolTable = construct.symbolTableBySource["test.bond"]!!
        Assert.assertEquals(4, mainSymbolTable.symbolsInFile.size)

        val testStructB = mainSymbolTable.symbolsInFile[3] as StructDef
        Assert.assertEquals("TestStructGB", testStructB.name)
        Assert.assertEquals(true, testStructB.hasBaseClass)

        val baseClass = testStructB.baseClass!!.refType!!
        Assert.assertEquals("BaseClass", baseClass.name)
        Assert.assertEquals(baseClass, mainSymbolTable.symbolsInFile[2])

        Assert.assertEquals(4, testStructB.baseClass!!.genericTypeArgs.size)
        val typeArgs = testStructB.baseClass!!.genericTypeArgs
        Assert.assertEquals(SymbolType.TypeRef, typeArgs[0].type)
        val type0 = (typeArgs[0] as TypeRef).refType
        Assert.assertEquals(SymbolType.BuiltinType, type0?.type)
        Assert.assertEquals("int32", type0?.name)

        Assert.assertEquals(SymbolType.TypeRef, typeArgs[1].type)
        val type1 = (typeArgs[1] as TypeRef).refType
        Assert.assertEquals(SymbolType.GenericTypeParam, type1!!.type)
        Assert.assertEquals("T", type1.name)

        Assert.assertEquals(SymbolType.TypeRef, typeArgs[2].type)
        val type2 = (typeArgs[2] as TypeRef).refType
        println(type2!!.name)
        Assert.assertEquals(SymbolType.Struct, type2.type)
        Assert.assertEquals("OtherType", type2.name)
        // Verify type argument of OtherType<string>
        // element format:
        // TypeRef.genericTypeArgs
        //     [0] = TypeRef
        //         BuiltinType.name = string
        val typeArgsOfOtherType = (typeArgs[2] as TypeRef).genericTypeArgs
        Assert.assertEquals(1, typeArgsOfOtherType.size)
        Assert.assertEquals(SymbolType.TypeRef, typeArgsOfOtherType[0].type)
        val otherTypeArgsType = (typeArgsOfOtherType[0] as TypeRef).refType
        Assert.assertEquals(SymbolType.BuiltinType, otherTypeArgsType!!.type)
        Assert.assertEquals("string", otherTypeArgsType.name)
        Assert.assertEquals(testStructB, (typeArgsOfOtherType[0] as TypeRef).definedByType)

        Assert.assertEquals(SymbolType.TypeRef, typeArgs[3].type)
        val type3 = (typeArgs[3] as TypeRef).refType
        Assert.assertEquals(SymbolType.BuiltinContainer, type3!!.type)
        Assert.assertEquals("list", type3.name)
        // Verify type argument of list
        // element format:
        // TypeRef.genericTypeArgs
        //     [0] = TypeRef
        //         GenericTypeParam.name = T
        val typeArgsOfList = (typeArgs[3] as TypeRef).genericTypeArgs
        Assert.assertEquals(1, typeArgsOfList.size)
        Assert.assertEquals(SymbolType.TypeRef, typeArgsOfList[0].type)
        val listElementType = (typeArgsOfList[0] as TypeRef).refType
        Assert.assertEquals(SymbolType.GenericTypeParam, listElementType!!.type)
        Assert.assertEquals("T", listElementType.name)
        Assert.assertEquals(testStructB, (typeArgsOfList[0] as TypeRef).definedByType)
    }

    @Test
    fun testStructFieldValueAssignmentCreation() {
        val mockCode = hashMapOf(
                "test.bond" to """
                    namespace qbranch.ut
                    struct TestStructWithField {
                        8: int8 int8Value = nothing;
                        16: nullable<int16> int16Value;
                        32: int32 nothing;
                        64: int64 int64Value = 100;
                        128: string strValue = "hello";
                        256: wstring wstrValue = "world";
                        512: float floatValue = 1.1f;
                        1024: double doubleValue = 1024;
                        2048: double doubleValue = 2.2;
                    }
                    """
        )

        val loader = MockStringSourceCodeLoader(mockCode)
        val parser = SourceTreeParser(loader)
        val sortedSourceTree = parser.parse("test.bond")
        val construct = IntermediateConstruct(sortedSourceTree)
        val mainSymbolTable = construct.symbolTableBySource["test.bond"]!!
        Assert.assertEquals(2, mainSymbolTable.symbolsInFile.size)
        val namespaceDef = mainSymbolTable.symbolsInFile[0]
        Assert.assertEquals(SymbolType.Namespace, namespaceDef.type)

        val structDef = mainSymbolTable.symbolsInFile[1] as StructDef
        val fieldList = structDef.fieldList
        Assert.assertEquals(9, fieldList.size)
        val firstField = structDef.fieldList[0]
        val secondField = structDef.fieldList[1]
        val thirdField = structDef.fieldList[2]
        val fourthField = structDef.fieldList[3]
        val fifthField = structDef.fieldList[4]
        val sixthField = structDef.fieldList[5]
        val seventhField = structDef.fieldList[6]
        val eighthField = structDef.fieldList[7]
        val ninthField = structDef.fieldList[8]

        Assert.assertEquals(true, firstField.isNothing)
        Assert.assertEquals(true, secondField.isNullable)
        Assert.assertEquals("nothing", thirdField.name)
        Assert.assertEquals(true, fourthField.isValueAssigned)
        Assert.assertEquals(true, seventhField.isValueAssigned)
        Assert.assertEquals("\"hello\"", fifthField.assignedValue)
        Assert.assertEquals(true, fifthField.isValueAssigned)
        Assert.assertEquals("\"world\"", sixthField.assignedValue)
        Assert.assertEquals(true, sixthField.isValueAssigned)
        Assert.assertEquals("1.1f", seventhField.assignedValue)
        Assert.assertEquals(true, eighthField.isValueAssigned)
        Assert.assertEquals("1024", eighthField.assignedValue)
        Assert.assertEquals(true, ninthField.isValueAssigned)
        Assert.assertEquals("2.2", ninthField.assignedValue)
    }

    @Test
    fun testStructFieldFieldIdModifierCreation() {
        val mockCode = hashMapOf(
                "test.bond" to """
                    namespace qbranch.ut
                    struct TestStructWithField {
                        8: int8 int8Value;
                        16: optional int16 int16Value;
                        32: required int32 int32Value;
                        64: required_optional int64 int32Value;
                    }
                    """
        )
        val loader = MockStringSourceCodeLoader(mockCode)
        val parser = SourceTreeParser(loader)
        val sortedSourceTree = parser.parse("test.bond")
        val construct = IntermediateConstruct(sortedSourceTree)
        val mainSymbolTable = construct.symbolTableBySource["test.bond"]!!
        Assert.assertEquals(2, mainSymbolTable.symbolsInFile.size)
        val namespaceDef = mainSymbolTable.symbolsInFile[0]
        Assert.assertEquals(SymbolType.Namespace, namespaceDef.type)

        val structDef = mainSymbolTable.symbolsInFile[1] as StructDef
        val fieldList = structDef.fieldList
        Assert.assertEquals(4, fieldList.size)
        val firstField = structDef.fieldList[0]
        val secondField = structDef.fieldList[1]
        val thirdField = structDef.fieldList[2]
        val fourthField = structDef.fieldList[3]
        Assert.assertEquals("int8Value", firstField.name)
        Assert.assertEquals(8, firstField.fieldOrderId)
        Assert.assertEquals("int16Value", secondField.name)
        Assert.assertEquals(16, secondField.fieldOrderId)

        Assert.assertEquals("int8", firstField.typeRef!!.refType!!.name)
        Assert.assertEquals("int16", secondField.typeRef!!.refType!!.name)
        Assert.assertEquals(false, firstField.isValueAssigned)
        Assert.assertEquals(false, secondField.isValueAssigned)
        Assert.assertEquals(false, firstField.isNothing)
        Assert.assertEquals(false, secondField.isNothing)
        Assert.assertEquals(false, firstField.isNullable)
        Assert.assertEquals(false, secondField.isNullable)
        Assert.assertEquals(StructFieldModifier.Optional, firstField.modifier)
        Assert.assertEquals(StructFieldModifier.Optional, secondField.modifier)

        Assert.assertEquals(StructFieldModifier.Required, thirdField.modifier)
        Assert.assertEquals(StructFieldModifier.RequiredOptional, fourthField.modifier)
    }

    @Test
    fun testAttributeCreation() {
        val mockCode = hashMapOf(
                "test.bond" to """
                    namespace qbranch.ut
                    [StructAttr1("StructAttrValue1")]
                    [StructAttr2("StructAttrValue2")]
                    struct TestStructWithAttr {
                        [FieldAttr1("FieldAttrValue1")]
                        [FieldAttr2("FieldAttrValue2")]
                        0: int8 int8Value;
                        1: int16 int16Value;
                    }
                    """
        )
        val loader = MockStringSourceCodeLoader(mockCode)
        val parser = SourceTreeParser(loader)
        val sortedSourceTree = parser.parse("test.bond")
        val construct = IntermediateConstruct(sortedSourceTree)
        val mainSymbolTable = construct.symbolTableBySource["test.bond"]!!
        Assert.assertEquals(2, mainSymbolTable.symbolsInFile.size)
        val namespaceDef = mainSymbolTable.symbolsInFile[0]
        Assert.assertEquals(SymbolType.Namespace, namespaceDef.type)

        val structDef = mainSymbolTable.symbolsInFile[1] as StructDef
        val structAttrs = structDef.attributeList
        Assert.assertEquals(2, structAttrs.size)
        Assert.assertEquals("StructAttr1", structAttrs[0].key)
        Assert.assertEquals("StructAttrValue1", structAttrs[0].value)
        Assert.assertEquals("StructAttr2", structAttrs[1].key)
        Assert.assertEquals("StructAttrValue2", structAttrs[1].value)
        val firstField = structDef.fieldList[0]
        val secondField = structDef.fieldList[1]
        Assert.assertEquals(2, firstField.attributeList.size)
        Assert.assertEquals(0, secondField.attributeList.size)
        val fieldAttrs = firstField.attributeList
        Assert.assertEquals("FieldAttr1", fieldAttrs[0].key)
        Assert.assertEquals("FieldAttrValue1", fieldAttrs[0].value)
        Assert.assertEquals("FieldAttr2", fieldAttrs[1].key)
        Assert.assertEquals("FieldAttrValue2", fieldAttrs[1].value)
    }
}