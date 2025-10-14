package com.nevitonviana.generateflutter.generateflutter.generateImplement

import com.intellij.CommonBundle
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.impl.file.PsiDirectoryFactory
import com.intellij.util.IncorrectOperationException
import com.jetbrains.lang.dart.DartFileType
import com.nevitonviana.generateflutter.generateflutter.generateImplement.fileTemplates.DartTemplateUtil
import java.io.IOException

abstract class HelperCreateTestFile {

    @IntentionFamilyName
    fun getFamilyName(): String = "Create Dart test"

    fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        return file?.fileType?.defaultExtension == DartFileType.INSTANCE.defaultExtension
    }

    fun startInWriteAction(): Boolean = false

    @Throws(IncorrectOperationException::class)
    fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        if (editor == null || file == null) return

        try {
            val originalFile = file.originalFile.virtualFile ?: return
            val suggestedFileName = DartTemplateUtil.camelToSnake(getFilenameSuggestion(originalFile))
            val newDirectoryName = originalFile.parent.path.replace("lib", "test")
            val newDirectory = VfsUtil.createDirectories(newDirectoryName)

            newDirectory?.let { dir ->
                val newDirReal = PsiDirectoryFactory.getInstance(project).createDirectory(dir)
                val newFilePath = "${suggestedFileName}_test"

                val existingFile = newDirReal.virtualFile.findChild("$newFilePath.${DartFileType.INSTANCE.defaultExtension}")
                if (existingFile != null) {
                    FileEditorManager.getInstance(project).openFile(existingFile, true)
                } else {
                    createTestFile(project, newFilePath, newDirReal)
                }
            }

        } catch (e: IOException) {
            Messages.showMessageDialog(
                project,
                e.localizedMessage,
                CommonBundle.getErrorTitle(),
                Messages.getErrorIcon()
            )
        }
    }

    private fun getFilenameSuggestion(file: VirtualFile): String {
        return file.name.removeSuffix(".${DartFileType.INSTANCE.defaultExtension}")
    }

    protected abstract fun createTestFile(project: Project, filename: String, psiDirectory: PsiDirectory)

    private fun fileExists(psiDirectory: PsiDirectory, filename: String): Boolean {
        return psiDirectory.files.any {
            it.name == "$filename.${DartFileType.INSTANCE.defaultExtension}"
        }
    }
}
