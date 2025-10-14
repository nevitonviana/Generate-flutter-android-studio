package  com.nevitonviana.generateflutter.generateflutter.generateImplement

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInspection.util.IntentionName
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

class GenerateTestFileIntention : HelperCreateTestFile(), IntentionAction {

    @IntentionName
    override fun getText(): String = "Create or open test file"

    override fun createTestFile(project: Project, filename: String, psiDirectory: PsiDirectory) {
        val templateManager = FileTemplateManager.getInstance(project)
        val template = templateManager.getInternalTemplate("Dart Test File")
        val properties = templateManager.defaultProperties

        try {
            FileTemplateUtil.createFromTemplate(template, filename, properties, psiDirectory)
        } catch (e: Exception) {
            println("Error creating the test file: ${e.message}")
        }
    }
}
