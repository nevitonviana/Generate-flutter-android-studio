package com.nevitonviana.generateflutter.generateflutter.generateImplement.fileTemplates

import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.lang.dart.psi.DartClass
import com.jetbrains.lang.dart.psi.DartClassBody
import com.jetbrains.lang.dart.psi.DartMethodDeclaration
import com.jetbrains.lang.dart.util.DartUrlResolver
import com.nevitonviana.generateflutter.generateflutter.generateImplement.OverrideImplementMethod
import com.nevitonviana.generateflutter.generateflutter.generateImplement.PresentableUtil

object DartTemplateUtil {

    fun toUpperCamelCase(name: String): String {
        return name.split(Regex("[\\W_]+"))
            .joinToString("") { word ->
                word.takeIf { it.isNotEmpty() }
                    ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    ?: ""
            }
    }

    fun camelToSnake(name: String): String {
        val builder = StringBuilder()
        builder.append(name[0].lowercaseChar())
        for (i in 1 until name.length) {
            val ch = name[i]
            if (ch.isUpperCase()) {
                builder.append('_')
                builder.append(ch.lowercaseChar())
            } else {
                builder.append(ch)
            }
        }
        return builder.toString()
    }

    fun generateTemplateImplementationAbstractClass(
        dartClass: DartClass,
        dartUrlResolver: DartUrlResolver,
        imports: List<PsiElement>,
        templateManager: TemplateManager,
        className: String
    ): Template {
        val template = templateManager.createTemplate(dartClass.javaClass.name, "Dart")
        template.isToReformat = true

        val dartUrl = dartUrlResolver.getDartUrlForFile(dartClass.containingFile.virtualFile)
        template.addTextSegment("import '$dartUrl';")
        template.addTextSegment(PresentableUtil.buildImportText(imports))
        template.addTextSegment("class $className implements ${dartClass.name.orEmpty()} {\n")

        val classBody = PsiTreeUtil.getChildOfType(dartClass, DartClassBody::class.java)
        val dartComponents = PsiTreeUtil.collectElementsOfType(classBody, DartMethodDeclaration::class.java)

        dartComponents.forEach { dartComponent ->
            val method = OverrideImplementMethod(dartClass)
            template.addTextSegment(method.buildFunctionsText(templateManager, dartComponent).templateText)
        }

        template.addTextSegment("}")
        return template
    }
}
