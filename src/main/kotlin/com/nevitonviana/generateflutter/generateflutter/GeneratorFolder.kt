package com.nevitonviana.generateflutter.generateflutter

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.projectRoots.impl.jdkDownloader.RuntimeChooserMessages.showErrorMessage
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.findOrCreateFile
import com.intellij.psi.PsiManager
import org.jetbrains.debugger.VariableView
import org.yaml.snakeyaml.Yaml
import java.io.InputStreamReader
import java.util.Locale

class GenerateFolder : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val dialog = MyInputDialog()
        dialog.show()
        if (dialog.isOK) {
            val featureName = dialog.featureName
            val selectedDir = getSelectedDirectory(event)
            val project = event.project

            if (project != null) {
                ApplicationManager.getApplication().runWriteAction {
                    if (selectedDir != null) {
                        val projectDir = project.guessProjectDir()
                        if (projectDir == null) {
                            showErrorMessage("No directory selected.")
                        } else {
                            val libDir = findOrCreateChildDir(projectDir, "lib")
                            if (libDir != null) {
                                var appDir = findOrCreateChildDir(libDir, "app")
                                if (appDir != null) {
                                    var moduleDir = findOrCreateChildDir(appDir, "module")
                                    if (moduleDir != null) {
                                        var folderDir = createDir(moduleDir, featureName)
                                        findOrCreateChildDir(folderDir, "widget")
                                        folderDir.findOrCreateFile("${featureName}_controller.dart")
                                            .setBinaryContent(getController(featureName).toByteArray())
                                        folderDir.findOrCreateFile("${featureName}_module.dart")
                                            .setBinaryContent(getModule(featureName).toByteArray())
                                        folderDir.findOrCreateFile("${featureName}_page.dart")
                                            .setBinaryContent(getPage(featureName).toByteArray())

                                        if (isDartAvailable()) formatWithDart(folderDir.path)

                                        showToastMessage("Generated Successfully!")
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                showErrorMessage("Project context is not available.")
            }
        }
    }

    private fun findDir(parentDir: VirtualFile?, dirName: String): VirtualFile? {
        return parentDir?.findChild(dirName)
    }

    private fun createDir(parentDir: VirtualFile, dirName: String): VirtualFile {
        return parentDir.createChildDirectory(null, dirName)
    }

    private fun findOrCreateChildDir(parentDir: VirtualFile, childName: String): VirtualFile? {
        return parentDir.findChild(childName) ?: parentDir.createChildDirectory(null, childName)
    }

    private fun getSelectedDirectory(event: AnActionEvent): VirtualFile? {
        val selectedFiles =
            event.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE_ARRAY)
        return selectedFiles?.firstOrNull()
    }


    private fun getController(featureName: String): String {
        var nameFolder = featureName.toCamelCase()
        val repositoryContent = """
            |import 'package:mobx/mobx.dart';
            |
            |
            |part '${featureName}_controller.g.dart';
            |
            |
            |class ${nameFolder}Controller = ${nameFolder}ControllerBase with _$${nameFolder}Controller;
            |
            |abstract class ${nameFolder}ControllerBase with Store {
            |
            |
            | }
        """.trimMargin()
        return repositoryContent
    }

    private fun getModule(featureName: String): String {
        var nameFolder = featureName.toCamelCase()
        val repositoryContent = """
            |import 'package:flutter_modular/flutter_modular.dart';
            |
            |
            |import '${featureName}_controller.dart';
            |import '${featureName}_page.dart';
            |
            |
            |class ${featureName}Module extends Module {
            |@override
            |void binds(i) {
            |i.addLazySingleton(${nameFolder}Controller.new);
            |}
            |
            |@override
            |List<Module> get imports => [];
            |
            |@override
            |
            |void routes(r) {
            |r.child(Modular.initialRoute, child: (context) => const ${nameFolder}Page());
            |}
            |}
        """.trimMargin()
        return repositoryContent
    }


    private fun getPage(featureName: String): String {
        var nameFolder = featureName.toCamelCase()
        val repositoryContent = """
            |import 'package:flutter/material.dart';
            |
            |
            |class ${nameFolder}Page  extends StatefulWidget {
               |const ${nameFolder}Page({super.key});
               |@override
               |
               |State<${nameFolder}Page> createState() => _${nameFolder}PageState();
               |}
            |class _${nameFolder}PageState extends State<${nameFolder}Page> {
                |@override
                |Widget build(BuildContext context) {
                |return Scaffold (
                |appBar: AppBar(
                |title: Text("$nameFolder"),
                |),
                |body: Container(),
                |);
                |}
                |
            |}

        """.trimMargin()
        return repositoryContent
    }

    private fun String.toCamelCase(): String {
        return this.split("_").joinToString("") {
            it.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        }
    }

    private fun showToastMessage(message: String) {
        ApplicationManager.getApplication().invokeLater {
            Messages.showMessageDialog(message, "Success", Messages.getInformationIcon())
        }
    }

    private fun formatWithDart(path: String) {
        try {
            val processBuilder = ProcessBuilder("dart", "format", path)
            processBuilder.redirectErrorStream(true)
            val process = processBuilder.start()
            val output = process.inputStream.bufferedReader().readText()
            process.waitFor()

            println("dart format output:\n$output")
        } catch (e: Exception) {
            showToastMessage("Erro ao executar dart format: ${e.message}")
        }
    }

    private fun isDartAvailable(): Boolean {
        return try {
            val process = ProcessBuilder("dart", "--version").start()
            process.waitFor()
            process.exitValue() == 0
        } catch (e: Exception) {
            false
        }
    }


    private fun getPackageName(project: Project?): String? {
        val pubspecFile = project?.guessProjectDir()?.findChild("pubspec.yaml") ?: return null
        val psiFile = PsiManager.getInstance(project).findFile(pubspecFile) ?: return null
        val virtualFile = psiFile.virtualFile ?: return null

        val yaml = Yaml()
        val inputStream = virtualFile.inputStream
        val yamlObject = yaml.load<Map<*, *>>(InputStreamReader(inputStream))

        return yamlObject?.get("name") as? String
    }


}