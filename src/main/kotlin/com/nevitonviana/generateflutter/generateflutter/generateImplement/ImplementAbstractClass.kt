package com.nevitonviana.generateflutter.generateflutter.generateImplement

import com.intellij.CommonBundle
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.*
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.IncorrectOperationException
import com.jetbrains.lang.dart.DartFileType
import com.jetbrains.lang.dart.DartTokenTypes
import com.jetbrains.lang.dart.psi.DartClass
import com.jetbrains.lang.dart.psi.DartImportStatement
import com.jetbrains.lang.dart.util.DartUrlResolver
import com.nevitonviana.generateflutter.generateflutter.generateImplement.fileTemplates.DartTemplateUtil
import com.nevitonviana.generateflutter.generateflutter.generateImplement.CreateDartClassFileAction

class ImplementAbstractClass : PsiElementBaseIntentionAction(), IntentionAction {

    override fun startInWriteAction(): Boolean = false

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val dartClass = PsiTreeUtil.getParentOfType(element, DartClass::class.java) ?: return

        val dialog = CreateDartClassDialog(
            project,
            "Implement Class",
            "${dartClass.name}Impl",
            dartClass.containingFile.virtualFile.parent.canonicalPath ?: return
        )

        if (dialog.showAndGet()) {
            try {
                val psiManager = PsiManager.getInstance(project)
                val dartUrlResolver =
                    DartUrlResolver.getInstance(project, dartClass.containingFile.virtualFile)
                val imports = ArrayList(
                    PsiTreeUtil.collectElementsOfType(
                        dartClass.parent, DartImportStatement::class.java
                    )
                )
                val templateManager = TemplateManager.getInstance(project)
                val className = dialog.className
                val dirPath = dialog.baseDir

                val virtualFile = VfsUtil.createDirectories(dirPath)
                val psiDirectory = psiManager.findDirectory(virtualFile)

                val template = DartTemplateUtil.generateTemplateImplementationAbstractClass(
                    dartClass, dartUrlResolver, imports, templateManager, className
                )

                val fileTemplate: FileTemplate = FileTemplateUtil.createTemplate(
                    "template", "dart", template.templateText, emptyArray()
                )

                psiDirectory?.let {
                    CreateDartClassFileAction.createCustomFileFromTemplate(
                        DartTemplateUtil.camelToSnake(className), fileTemplate, it, "", true
                    )
                }

            } catch (e: IncorrectOperationException) {
                Messages.showMessageDialog(
                    project,
                    e.localizedMessage,
                    CommonBundle.getErrorTitle(),
                    Messages.getErrorIcon()
                )
            } catch (e: java.io.IOException) {
                Messages.showMessageDialog(
                    project,
                    e.localizedMessage,
                    CommonBundle.getErrorTitle(),
                    Messages.getErrorIcon()
                )
            }
        }
    }

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        if (element.language.id.lowercase() != DartFileType.INSTANCE.defaultExtension.lowercase()) return false

        val id = element.parent ?: return false
        val parent = id.parent ?: return false
        val node = parent.node ?: return false
        val firstChildNode = node.treeParent?.firstChildNode ?: return false
        val firstPsi = firstChildNode.psi ?: return false

        return canBeImplemented(firstPsi)
    }

    override fun getText(): String = "Implement class"

    @IntentionFamilyName
    override fun getFamilyName(): String = "Implement class"

    private fun canBeImplemented(element: PsiElement): Boolean {
        if (element is LeafPsiElement) {
            val type: IElementType = element.elementType
            if (type == DartTokenTypes.BASE || type == DartTokenTypes.FINAL || type == DartTokenTypes.SEALED) {
                return false
            }
        }
        return element.nextSibling?.let { canBeImplemented(it) } ?: true
    }
}
