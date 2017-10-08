// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler

import net.dummydigit.qbranch.compiler.mocks.MockStringSourceCodeLoader
import net.dummydigit.qbranch.compiler.mocks.MockStringTargetWriter
import net.dummydigit.qbranch.compiler.parser.IntermediateConstruct
import net.dummydigit.qbranch.compiler.parser.SourceTreeParser
import org.junit.Assert
import org.junit.Test

class KotlinTranslatorTest {
    @Test
    fun testEnumCodeGen() {
        val mockCode = hashMapOf(
                "test.bond" to """
                    namespace qbranch.ut
                    enum TestEnum {
                        Value0,
                        Value3 = 3,
                        Value4
                    }
                    """)
        val settings = Settings()
        val loader = MockStringSourceCodeLoader(mockCode)
        val parser = SourceTreeParser(loader)
        val sortedSourceTree = parser.parse("test.bond")
        val construct = IntermediateConstruct(sortedSourceTree)
        val translator = KotlinTranslator(settings)
        val saver = MockStringTargetWriter(65535)
        val writer = OneClassPerFileCodeGen(translator, saver)
        writer.generateTargetSource(construct)
        Assert.assertEquals(8, saver.savedContentArray.size)
        // NOTE: Don't test compiler version and name as
        // it does not work in unit test
        Assert.assertEquals("enum class TestEnum(val num : Int) {",
                saver.savedContentArray[3])
        Assert.assertEquals("    Value0(0),",
                saver.savedContentArray[4])
        Assert.assertEquals("    Value3(3),",
                saver.savedContentArray[5])
        Assert.assertEquals("    Value4(4),",
                saver.savedContentArray[6])
    }

    @Test
    fun testStructCodeGen() {
        val mockCode = hashMapOf(
                "test.bond" to """
                    namespace qbranch.ut
                    struct TestStruct {
                    }
                    """)
        val settings = Settings()
        val loader = MockStringSourceCodeLoader(mockCode)
        val parser = SourceTreeParser(loader)
        val sortedSourceTree = parser.parse("test.bond")
        val construct = IntermediateConstruct(sortedSourceTree)
        val translator = KotlinTranslator(settings)
        val saver = MockStringTargetWriter(65535)
        val writer = OneClassPerFileCodeGen(translator, saver)
        writer.generateTargetSource(construct)
    }
}