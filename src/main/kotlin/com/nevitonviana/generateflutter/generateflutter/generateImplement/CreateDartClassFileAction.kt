package com.nevitonviana.generateflutter.generateflutter.generateImplement

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.util.PlatformIcons.ABSTRACT_CLASS_ICON
import com.intellij.util.PlatformIcons.CLASS_ICON
import com.intellij.util.PlatformIcons.INTERFACE_ICON
import com.jetbrains.lang.dart.DartBundle
import icons.DartIcons

class CreateDartClassFileAction : CreateFileFromTemplateAction(
    "Dart Class",
    "Create Dart class",
    DartIcons.Dart_file
) {

    override fun buildDialog(
        project: Project,
        directory: PsiDirectory,
        builder: CreateFileFromTemplateDialog.Builder
    ) {
        builder.setTitle("New Dart Class File")
            .addKind("Class", CLASS_ICON, "Dart Class File")
            .addKind("Base class", CLASS_ICON, "Dart Base Class File")
            .addKind("Interface class", INTERFACE_ICON, "Dart Interface Class File")
            .addKind("Final class", CLASS_ICON, "Dart Final Class File")
            .addKind("Sealed class", CLASS_ICON, "Dart Sealed Class File")
            .addKind("Abstract class", ABSTRACT_CLASS_ICON, "Dart Abstract Class File")
            .addKind("Abstract base class", ABSTRACT_CLASS_ICON, "Dart Abstract Base Class File")
            .addKind("Abstract interface class", ABSTRACT_CLASS_ICON, "Dart Abstract Interface Class File")
            .addKind("Abstract final class", ABSTRACT_CLASS_ICON, "Dart Abstract Final Class File")
    }

    override fun getActionName(directory: PsiDirectory, newName: String, templateName: String): String {
        return DartBundle.message("title.create.dart.file.0", newName)
    }

    companion object {
        @JvmStatic
        fun createCustomFileFromTemplate(
            name: String,
            template: FileTemplate,
            dir: PsiDirectory,
            templateProperty: String = "",
            interactive: Boolean = true
        ): PsiFile? {
            return CreateFileFromTemplateAction.createFileFromTemplate(
                name, template, dir, templateProperty, interactive, emptyMap()
            )
        }
    }



    override fun createFile(name: String, templateName: String, dir: PsiDirectory): PsiFile {
        return super.createFile(name, templateName, dir)!!
    }


}
