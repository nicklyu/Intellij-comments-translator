package com.nicklyu.translator.settings.ui

import com.nicklyu.translator.settings.CommentsTranslatorSettingsState
import com.nicklyu.translator.translators.TranslatorType
import javax.swing.JComponent

class CommentsTranslatorSettingsForm : CommentsTranslatorSettingsFormTemplate() {
    init {
        val state = CommentsTranslatorSettingsState.instance
        TranslatorType.values().forEach { type ->
            translatorTypeComboBox.addItem(type.translatorName)
        }
        apiKeyTextField.text = state.currentApiKey()
        translatorDesctibtionPane.text = state.currentApiDescription()
        translatorTypeComboBox.addActionListener {
            apiKeyTextField.text = state.currentApiKey()
            translatorDesctibtionPane.text = state.currentApiDescription()
        }
    }

    val content: JComponent = rootPanel

    fun apiKey() = apiKeyTextField.text.orEmpty()

    fun translator() = TranslatorType.type(translatorTypeComboBox.selectedItem as String)
}