// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.symbols

import net.dummydigit.qbranch.compiler.grammar.BondIdlGrammarParser
import java.util.LinkedList


internal class IdlDefinition(val sourceCodePath : String,
                             val syntaxTree : BondIdlGrammarParser.BondIdlContext,
                             val isInput : Boolean) {
    val referenceTo = LinkedList<IdlDefinition>()
    val symbolsInFile = LinkedList<Symbol>()
}