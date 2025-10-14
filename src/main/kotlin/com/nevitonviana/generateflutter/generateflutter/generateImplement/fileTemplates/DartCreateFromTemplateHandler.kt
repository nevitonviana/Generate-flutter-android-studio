package com.nevitonviana.generateflutter.generateflutter.generateImplement.fileTemplates

import com.intellij.ide.fileTemplates.DefaultCreateFromTemplateHandler
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.JavaTemplateUtil
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx
import com.intellij.openapi.project.Project
import com.intellij.util.ArrayUtil
import com.jetbrains.lang.dart.DartFileType


class DartCreateFromTemplateHandler : DefaultCreateFromTemplateHandler() {
    override fun handlesTemplate(template: FileTemplate): Boolean {
        val fileType = FileTypeManagerEx.getInstanceEx().getFileTypeByExtension(template.extension)
        return fileType == DartFileType.INSTANCE && !ArrayUtil.contains(
            template.name,
            *JavaTemplateUtil.INTERNAL_FILE_TEMPLATES
        )
    }

    override fun prepareProperties(
        props: MutableMap<String, Any>,
        filename: String,
        template: FileTemplate,
        project: Project
    ) {
        props[ATTRIBUT_CLASS_NAME_DART_UPPER] =
            DartTemplateUtil.toUpperCamelCase(filename)
    }

    companion object {
        const val ATTRIBUT_CLASS_NAME_DART_UPPER: String = "CLASS_NAME_UPPER"
    }
}