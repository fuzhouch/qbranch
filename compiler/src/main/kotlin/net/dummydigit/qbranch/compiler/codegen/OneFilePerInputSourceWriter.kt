package net.dummydigit.qbranch.compiler.codegen

import net.dummydigit.qbranch.compiler.TargetCodeWriter
import net.dummydigit.qbranch.compiler.parser.IntermediateConstruct

internal class OneFilePerInputSourceWriter(private val translator : Translator,
                                           private val writer: TargetCodeWriter) {
    fun generateTargetSource(construct: IntermediateConstruct) {
        val userInputSources = construct.symbolTableBySource.filter { it.value.isInput }
        userInputSources.forEach {
            val idlDef = it.value
            val inputFileName = it.key
            val output = writer.openStream(inputFileName)
            output.write(translator.generateHeader().toByteArray())
            output.use {
                idlDef.symbolsInFile.forEach {
                    output.write(it.toTargetCode(translator).toByteArray())
                }
                writer.onSaveDone() // Have a chance to do clean-up
            }
        }
    }
}