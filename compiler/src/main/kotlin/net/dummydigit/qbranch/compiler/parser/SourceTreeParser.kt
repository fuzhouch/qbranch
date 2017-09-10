// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler.parser

import java.io.InputStream
import java.util.LinkedList
import net.dummydigit.qbranch.compiler.SourceCodeLoader
import net.dummydigit.qbranch.compiler.symbols.IdlDefinition
import net.dummydigit.qbranch.compiler.exceptions.ImportDependencyCircleException
import net.dummydigit.qbranch.compiler.grammar.BondIdlGrammarBaseListener
import net.dummydigit.qbranch.compiler.grammar.BondIdlGrammarParser
import net.dummydigit.qbranch.compiler.grammar.BondIdlGrammarLexer
import net.dummydigit.qbranch.compiler.grammar.BondIdlGrammarParser.BondIdlContext
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker

internal class SourceTreeParser(private val idlSourceLoader : SourceCodeLoader)
    : BondIdlGrammarBaseListener() {

    fun parse(initialSource : String) : List<IdlDefinition> {
        val initialInputStream = idlSourceLoader.openStream(initialSource)
        initialInputStream.use {
            val syntaxTreeLookupTable = loadSyntaxTree(initialSource, it)
            return sortSyntaxTree(initialSource, syntaxTreeLookupTable)
        }
    }

    private fun parseSingleIdl(stream : InputStream) : BondIdlContext {
        val input = CharStreams.fromStream(stream)
        val lexer = BondIdlGrammarLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = BondIdlGrammarParser(tokens)
        return parser.bondIdl()
    }

    private fun buildLookupKey(path : String) : String {
        return idlSourceLoader.resolvePath(path).toLowerCase()
    }

    private fun loadSyntaxTree(initialSource : String, initialInputStream: InputStream)
            : HashMap<String, IdlDefinition> {
        val syntaxTreeLookupTable = HashMap<String, IdlDefinition>()
        val pendingIdlDefStack = LinkedList<IdlDefinition>()
        val treeWalker = ParseTreeWalker()
        val importDepResolver = ImportDependencyResolver()

        val initialTree = parseSingleIdl(initialInputStream)
        val initialIdlDef = IdlDefinition(initialSource, initialTree, true)

        pendingIdlDefStack.push(initialIdlDef)
        syntaxTreeLookupTable[initialIdlDef.sourceCodePath] = initialIdlDef

        while (pendingIdlDefStack.size != 0) {
            val parsingIdl = pendingIdlDefStack.pop()
            treeWalker.walk(importDepResolver, parsingIdl.syntaxTree)
            val referenceTo = importDepResolver.referenceToIdlList
            referenceTo.forEach {
                val lookupKey = buildLookupKey(it)
                if (!syntaxTreeLookupTable.containsKey(lookupKey)) {
                    val newContent = idlSourceLoader.openStream(it)
                    newContent.use {
                        val newSourceTree = parseSingleIdl(newContent)
                        val newIdlDef = IdlDefinition(lookupKey, newSourceTree, false)
                        parsingIdl.referenceTo.add(newIdlDef)
                        syntaxTreeLookupTable[newIdlDef.sourceCodePath] = newIdlDef
                        pendingIdlDefStack.push(newIdlDef)
                    }
                }
            }
            importDepResolver.referenceToIdlList.clear()
        }
        return syntaxTreeLookupTable
    }

    private fun sortSyntaxTree(initialSource : String,
                               syntaxTreeLookupTable : HashMap<String, IdlDefinition>)
            : List<IdlDefinition> {
        val sortedSyntaxTreeList = LinkedList<IdlDefinition>()
        val pendingCheckSyntaxTreeList = LinkedList<IdlDefinition>()
        val checkedSyntaxTree = HashSet<IdlDefinition>()

        val initialIdl = syntaxTreeLookupTable[initialSource]
        pendingCheckSyntaxTreeList.push(initialIdl)
        while (pendingCheckSyntaxTreeList.size != 0) {
            val nextNodeToRemoveList = pendingCheckSyntaxTreeList
                    .flatMap { it.referenceTo }
                    .distinct()

            nextNodeToRemoveList.forEach {
                val foundElement = it.referenceTo.find { checkedSyntaxTree.contains(it) }
                if (foundElement != null) {
                    throw ImportDependencyCircleException(foundElement.sourceCodePath)
                }
            }

            pendingCheckSyntaxTreeList.forEach {
                sortedSyntaxTreeList.push(it)
                checkedSyntaxTree.add(it)
            }
            pendingCheckSyntaxTreeList.clear()
            nextNodeToRemoveList.forEach {
                pendingCheckSyntaxTreeList.push(it)
            }
        }
        return sortedSyntaxTreeList
    }
}