package com.nevitonviana.generateflutter.generateflutter

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.projectRoots.impl.jdkDownloader.RuntimeChooserMessages.showErrorMessage
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.findOrCreateFile

class GenerateFolderStructureActionInMVVM : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
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
                            libDir.findOrCreateFile("app_module.dart")
                                .setBinaryContent(getAppModulo().toByteArray())
                            libDir.findOrCreateFile("app_widget.dart")
                                .setBinaryContent(getAppWidget().toByteArray())
                            val appDir = findOrCreateChildDir(libDir, "app")
                            if (appDir != null) {
                                findOrCreateChildDir(appDir, "core")
                                findOrCreateChildDir(appDir, "models")
                                findOrCreateChildDir(appDir, "module")
                                findOrCreateChildDir(appDir, "services")
                                findOrCreateChildDir(appDir, "repositories")
                            }
                            showToastMessage("Generated Successfully!")
                        }
                    }
                }
            }
        } else {
            showErrorMessage("Project context is not available.")
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

    private fun getAppWidget(): String {
        val repositoryContent = """
            |import 'package:flutter/material.dart';
            |import 'package:flutter_modular/flutter_modular.dart';
            |
            |
            |class AppWidget extends StatelessWidget {
            |Widget build(BuildContext context){
            |return MaterialApp.router(
            |title: 'My Smart App',
            |theme: ThemeData(primarySwatch: Colors.blue),
            |routerConfig: Modular.routerConfig,
            | ); 
            | }
            | }
        """.trimMargin()
        return repositoryContent
    }

    private fun getAppModulo(): String {
        val repositoryContent = """
            |import 'package:flutter/material.dart';
            |import 'package:flutter_modular/flutter_modular.dart';
            |
            |
            |class AppModule extends Module {
            |@override
            |void binds(i) {}
            |@override
            |void routes(r) {}
            |
            | }
        """.trimMargin()
        return repositoryContent
    }


    private fun showToastMessage(message: String) {
        ApplicationManager.getApplication().invokeLater {
            Messages.showMessageDialog(message, "Success", Messages.getInformationIcon())
        }
    }
}





