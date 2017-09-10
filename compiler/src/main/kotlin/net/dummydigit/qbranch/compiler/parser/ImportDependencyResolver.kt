// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.parser

import net.dummydigit.qbranch.compiler.ParsingUtil
import net.dummydigit.qbranch.compiler.grammar.BondIdlGrammarBaseListener
import net.dummydigit.qbranch.compiler.grammar.BondIdlGrammarParser
import java.util.*

/**
 * Compute list of source code files from import dependency chain.
 */
internal class ImportDependencyResolver : BondIdlGrammarBaseListener() {
    val referenceToIdlList = LinkedList<String>()

    override fun enterImportDecl(ctx: BondIdlGrammarParser.ImportDeclContext?) {
        val includedIdlPath = ctx?.QUOTED_STRING()
        val unquoted = ParsingUtil.unescapeQuotedString(includedIdlPath.toString())
        referenceToIdlList.add(unquoted)
    }
}