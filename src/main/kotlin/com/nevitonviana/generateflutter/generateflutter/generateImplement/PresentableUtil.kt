package com.nevitonviana.generateflutter.generateflutter.generateImplement

import com.intellij.psi.PsiElement
import java.util.function.Consumer



object PresentableUtil {
    fun buildImportText(imports: List<PsiElement>?): String {
        val result = StringBuilder()
        if (imports != null && !imports.isEmpty()) {
            imports.forEach(Consumer { _import: PsiElement ->
                result.append(_import.text)
            })
        }
        return result.toString()
    }
}