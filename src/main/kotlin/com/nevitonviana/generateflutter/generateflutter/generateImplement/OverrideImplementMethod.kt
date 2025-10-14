package com.nevitonviana.generateflutter.generateflutter.generateImplement

import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.lang.dart.ide.generation.BaseCreateMethodsFix
import com.jetbrains.lang.dart.psi.DartClass
import com.jetbrains.lang.dart.psi.DartComponent
import com.jetbrains.lang.dart.psi.DartReturnType
import com.jetbrains.lang.dart.psi.DartType
import com.jetbrains.lang.dart.psi.DartVarAccessDeclaration
import com.jetbrains.lang.dart.psi.DartVarDeclarationListPart
import com.jetbrains.lang.dart.psi.impl.DartMethodDeclarationImpl
import com.jetbrains.lang.dart.util.DartPresentableUtil


class OverrideImplementMethod(dartClass: DartClass) :
    BaseCreateMethodsFix<DartComponent>(dartClass) {
    override fun getNothingFoundMessage(): String {
        return "No methods were found to implement"
    }

    public override fun buildFunctionsText(
        templateManager: TemplateManager,
        element: DartComponent
    ): Template {
        val template = templateManager.createTemplate(javaClass.name, "Dart")
        if (element.firstChild != null && element.firstChild.parent != null && (element.firstChild.parent as DartMethodDeclarationImpl).isConstructor
        ) {
            return template
        }
        template.isToReformat = true
        template.addTextSegment("@override\n")
        val isField = element is DartVarAccessDeclaration || element is DartVarDeclarationListPart
        if (isField && element.isFinal) {
            template.addTextSegment("final")
            template.addTextSegment(" ")
        }

        val returnType = PsiTreeUtil.getChildOfType(
            element,
            DartReturnType::class.java
        )
        val dartType = PsiTreeUtil.getChildOfType(
            element,
            DartType::class.java
        )
        if (returnType != null) {
            template.addTextSegment(
                DartPresentableUtil.buildTypeText(
                    element,
                    returnType,
                    this.specializations
                )
            )
            template.addTextSegment(" ")
        } else if (dartType != null) {
            template.addTextSegment(
                DartPresentableUtil.buildTypeText(
                    element,
                    dartType,
                    this.specializations
                )
            )
            template.addTextSegment(" ")
        }

        if (isField) {
            if (returnType == null && dartType == null) {
                template.addTextSegment("var")
                template.addTextSegment(" ")
            }

            template.addTextSegment(element.name!!)
            if (element.isFinal) {
                template.addTextSegment(" ")
                template.addTextSegment("=")
                template.addTextSegment(" ")
                template.addTextSegment("null")
            }

            template.addTextSegment("; ")
        } else {
            if (element.isOperator) {
                template.addTextSegment("operator ")
            }

            if (element.isGetter || element.isSetter) {
                template.addTextSegment(if (element.isGetter) "get " else "set ")
            }

            template.addTextSegment(element.name!!)
            if (!element.isGetter) {
                template.addTextSegment("(")
                template.addTextSegment(
                    DartPresentableUtil.getPresentableParameterList(
                        element,
                        this.specializations, false, true, true
                    )
                )
                template.addTextSegment(")")
            }

            template.addTextSegment("{\n")
            template.addTextSegment(" // TODO: implement ")
            template.addTextSegment(element.name!!)
            template.addTextSegment("\n")
            template.addTextSegment("throw UnimplementedError();")
            template.addTextSegment("\n} ")
        }
        return template
    }
}