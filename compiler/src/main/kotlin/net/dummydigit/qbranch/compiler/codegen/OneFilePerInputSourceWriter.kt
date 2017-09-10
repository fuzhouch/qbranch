package net.dummydigit.qbranch.compiler.codegen

import net.dummydigit.qbranch.compiler.TargetCodeSaver
import net.dummydigit.qbranch.compiler.parser.IntermediateConstruct

internal class OneFilePerInputSourceWriter(private val translator : Translator,
                                           private val saver : TargetCodeSaver) {
    fun generateTargetSource(construct: IntermediateConstruct) {
        val userInputSources = construct.symbolTableBySource.filter { it.value.isInput }
        userInputSources.forEach {
            val idlDef = it.value
            val inputFileName = it.key
            val output = saver.openStream(inputFileName)
            output.write(translator.generateHeader().toByteArray())
            output.use {
                idlDef.symbolsInFile.forEach {
                    output.write(it.toTargetCode(translator).toByteArray())
                }
                saver.onSaveDone() // Have a chance to do clean-up
            }
        }
    }
}