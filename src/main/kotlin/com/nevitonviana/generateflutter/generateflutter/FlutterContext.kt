package com.nevitonviana.generateflutter.generateflutter

import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.codeInsight.template.TemplateActionContext

class FlutterContext private constructor() :
    TemplateContextType("FLUTTER_GENERATOR", "Flutter generator") {
    override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
        return templateActionContext.file.name.endsWith(".dart")
    }
}