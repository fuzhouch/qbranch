// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.
package net.dummydigit.qbranch

import net.dummydigit.qbranch.compiler.BondIdlCompiler
import net.dummydigit.qbranch.compiler.Settings
import org.gradle.api.Plugin
import org.gradle.api.Project

class QBranchCompilerGradlePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        String generatedPath = "${project.buildDir}/qbranch-generated"
        def extension = project.extensions.create('qbranch',
                QBranchCompilerGradlePluginConfig)
        project.task('compileBondIdl') {
            def settings = Settings.createSimpleSettings(
                    extension.includePaths,
                    generatedPath,
                    "kotlin")
            def compiler = new BondIdlCompiler(settings)
            extension.inputIdls.each {
                print("QBranch:Compiling:${it}...")
                compiler.generateTargetCode(it)
                println("Done.")
            }
        }
    }
}