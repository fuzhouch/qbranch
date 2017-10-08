// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package net.dummydigit.qbranch.compiler

import net.dummydigit.qbranch.compiler.parser.IntermediateConstruct
import net.dummydigit.qbranch.compiler.symbols.IdlDefinition
import net.dummydigit.qbranch.compiler.symbols.SymbolType

internal class OneClassPerFileCodeGen(private val translator : Translator,
                                      private val targetCodeWriter : TargetCodeWriter) {

    fun generateTargetSource(construct: IntermediateConstruct) {
        val userInputSources = construct.symbolTableBySource.filter { it.value.isInput }
        userInputSources.forEach { writeSymbolsToFile(it.value, construct) }
    }

    private fun writeSymbolsToFile(idlDef : IdlDefinition, construct : IntermediateConstruct) {
        val namespace = construct.importToNamespace[idlDef.sourceCodePath]!!
        val header = translator.generateHeader()
        val namespaceSymbol = idlDef.symbolsInFile.find { it.type == SymbolType.Namespace }!!
        idlDef.symbolsInFile.forEach {
            if (it.type == SymbolType.Struct || it.type == SymbolType.Enum) {
                val writtenText = it.toTargetCode(translator)
                val output = targetCodeWriter.openTargetCodeAsStream(namespace, it.name)
                output.use {
                    output.write(header.toByteArray())
                    output.write(namespaceSymbol.toTargetCode(translator).toByteArray())
                    output.write(writtenText.toByteArray())
                }
                targetCodeWriter.onSaveDone()
            }
        }
    }
}