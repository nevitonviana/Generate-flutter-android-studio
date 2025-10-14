package com.nevitonviana.generateflutter.generateflutter.generateImplement

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.util.text.StringUtil
import com.intellij.ui.DocumentAdapter
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.JBInsets
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.event.DocumentEvent


class CreateDartClassDialog(
    project: Project,
    title: String,
    private val myClassName: String,
    private val myCanonicalPath: String
) : DialogWrapper(project, true) {
    private val myInformationLabel = JLabel("#")
    private val myTfClassName: JTextField =
       com.nevitonviana.generateflutter.generateflutter.generateImplement.CreateDartClassDialog.MyTextField()

    private val myInputFile: TextFieldWithBrowseButton =
        TextFieldWithBrowseButton(com.nevitonviana.generateflutter.generateflutter.generateImplement.CreateDartClassDialog.MyTextField())


    init {
        init()
        setTitle(title)
        myInformationLabel.text = "Create class"
        myTfClassName.text = myClassName
        myInputFile.text = myCanonicalPath
        myInputFile.addBrowseFolderListener(
            "Choose Destination Directory",
            "",
            project,
            FileChooserDescriptorFactory.createSingleFolderDescriptor()
        )
    }

    override fun createCenterPanel(): JComponent? {
        return JPanel(BorderLayout())
    }

    override fun createNorthPanel(): JComponent? {
        val panel = JPanel(GridBagLayout())
        val gbConstraints = GridBagConstraints()

        gbConstraints.insets = JBInsets.create(4, 8)
        gbConstraints.fill = GridBagConstraints.HORIZONTAL
        gbConstraints.anchor = GridBagConstraints.WEST


        gbConstraints.weightx = 0.0
        gbConstraints.gridwidth = 1
        panel.add(myInformationLabel, gbConstraints)
        gbConstraints.insets = JBInsets.create(4, 8)
        gbConstraints.gridx = 1
        gbConstraints.weightx = 1.0
        gbConstraints.gridwidth = 1
        gbConstraints.fill = GridBagConstraints.HORIZONTAL
        gbConstraints.anchor = GridBagConstraints.WEST
        panel.add(myTfClassName, gbConstraints)

        myTfClassName.document.addDocumentListener(object : DocumentAdapter() {
            override fun textChanged(e: DocumentEvent) {
                okAction.isEnabled =
                    StringUtil.isNotEmpty(myTfClassName.text) && StringUtil.isNotEmpty(
                        myCanonicalPath
                    )
            }
        })


        gbConstraints.gridy = 3
        gbConstraints.gridx = 0
        gbConstraints.gridwidth = 2
        gbConstraints.insets.top = 12
        gbConstraints.anchor = GridBagConstraints.WEST
        gbConstraints.fill = GridBagConstraints.NONE
        val label = JBLabel("To directory: ")
        panel.add(label, gbConstraints)

        gbConstraints.gridy = 3
        gbConstraints.gridx = 1
        gbConstraints.fill = GridBagConstraints.HORIZONTAL
        gbConstraints.insets.top = 12
        panel.add(myInputFile, gbConstraints)
        myInputFile.textField.document.addDocumentListener(object : DocumentAdapter() {
            override fun textChanged(e: DocumentEvent) {
                okAction.isEnabled =
                    StringUtil.isNotEmpty(myInputFile.text) && StringUtil.isNotEmpty(myClassName)
            }
        })
        okAction.isEnabled =
            StringUtil.isNotEmpty(myClassName) && StringUtil.isNotEmpty(myCanonicalPath)
        return panel
    }

    private class MyTextField : JTextField() {
        override fun getPreferredSize(): Dimension {
            val size = super.getPreferredSize()
            val fontMetrics = getFontMetrics(font)
            size.width = fontMetrics.charWidth('a') * 80
            return size
        }
    }

    val baseDir: String
        get() = myInputFile.text

    val className: String
        get() = myTfClassName.text
}