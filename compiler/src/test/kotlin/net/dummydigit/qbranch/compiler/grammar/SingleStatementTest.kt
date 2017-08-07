package net.dummydigit.qbranch.compiler.grammar

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.*
import org.junit.Assert
import org.junit.Test
import java.nio.charset.StandardCharsets

class SingleStatementTest {

    @Test
    fun namespaceDeclTest() {
        val statement = "namespace net.dummydigit.qbranch"
        val stream = statement.byteInputStream(StandardCharsets.UTF_8)
        val input = CharStreams.fromStream(stream)
        val lexer = BondGrammarLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = BondGrammarParser(tokens)
    }

}