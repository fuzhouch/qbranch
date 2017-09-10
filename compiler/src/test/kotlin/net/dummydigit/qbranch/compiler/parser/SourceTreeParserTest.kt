// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.parser

import net.dummydigit.qbranch.compiler.mocks.MockStringSourceCodeLoader
import org.junit.Assert
import org.junit.Test

class SourceTreeParserTest {

    @Test
    fun testLoadSourceTree() {
        val mockCode = hashMapOf(
                "test.bond" to """
                    import "bond.bond"
                    namespace qbranch.ut
                    """,
                "bond.bond" to "namespace bond"
        )
        val loader = MockStringSourceCodeLoader(mockCode)
        val parser = SourceTreeParser(loader)
        val sortedSourceTree = parser.parse("test.bond")
        Assert.assertEquals(2, sortedSourceTree.size)
    }

    @Test
    fun testLinearDependency() {
        val mockCode = hashMapOf(
                "a.bond" to """
                    import "b.bond"
                    namespace qbranch.ut
                    """,
                "b.bond" to """
                    import "c.bond"
                    namespace qbranch.ut
                    """,
                "c.bond" to """
                    namespace qbranch.ut
                """
        )
        val loader = MockStringSourceCodeLoader(mockCode)
        val parser = SourceTreeParser(loader)
        val sortedSourceTree = parser.parse("a.bond")
        Assert.assertEquals(3, sortedSourceTree.size)
        Assert.assertEquals("c.bond", sortedSourceTree[0].sourceCodePath)
        Assert.assertEquals("b.bond", sortedSourceTree[1].sourceCodePath)
        Assert.assertEquals("a.bond", sortedSourceTree[2].sourceCodePath)
    }
}