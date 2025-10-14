package com.nevitonviana.generateflutter.generateflutter

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import java.awt.*
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class MyInputDialog : DialogWrapper(null) {
    private val textField = JTextField(20)
    var featureName = ""

    init {
        init()
        title = "Enter Feature Name"
    }

    override fun createCenterPanel(): JComponent {
        val panel = JPanel(BorderLayout())
        panel.add(JLabel("folder Name:"), BorderLayout.WEST)
        panel.add(textField, BorderLayout.CENTER)
        return panel
    }

    override fun getPreferredSize(): Dimension {
        return Dimension(300, 100)
    }

    override fun doOKAction() {
        featureName = textField.text.trim()

        val regex = Regex("^[a-z][a-z0-9_]*\$")

        if (featureName.isEmpty()) {
            Messages.showErrorDialog("Feature name cannot be empty.", "Error")
            return
        }

        if (!regex.matches(featureName)) {
            Messages.showErrorDialog(
                "Invalid feature name.\nUse only lowercase letters, digits, and underscores.\nExample: login_page",
                "Invalid Name"
            )
            return
        }

        super.doOKAction()
    }


    override fun getPreferredFocusedComponent(): JComponent {
        return textField
    }

}