// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.parser

import net.dummydigit.qbranch.compiler.ParsingUtil
import org.junit.Assert
import org.junit.Test

class ParsingUtilTest {
    @Test
    fun testQuotedString() {
        val unquoted1 = ParsingUtil.unescapeQuotedString("\"bond.bond\"")
        Assert.assertEquals("bond.bond", unquoted1)
        val unquoted2 = ParsingUtil.unescapeQuotedString("\"bond.\\bond\"")
        Assert.assertEquals("bond.bond", unquoted2)
        val unquoted3 = ParsingUtil.unescapeQuotedString("\"bond.\\\\bond\"")
        Assert.assertEquals("bond.\\bond", unquoted3)
    }
}