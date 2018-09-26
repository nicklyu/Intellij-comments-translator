package com.nicklyu.translator.settings

import com.intellij.openapi.options.Configurable
import com.nicklyu.translator.settings.ui.CommentsTranslatorSettingsForm
import com.nicklyu.translator.translators.TranslatorType
import javax.swing.JComponent

class CommentsTranslatorSettings : Configurable {
    @Volatile
    private var settingsForm: CommentsTranslatorSettingsForm? = null

    @Synchronized
    private fun initializeSettings() {
        if (settingsForm == null)
            settingsForm = CommentsTranslatorSettingsForm()
    }


    override fun getDisplayName(): String = "Comments translator"

    override fun isModified(): Boolean {
        val state = CommentsTranslatorSettingsState.instance

        @Suppress("UnnecessaryVariable")
        val isModified =
                state.currentTranslator != settingsForm!!.translator()
                        || state.currentApiKey() != settingsForm!!.apiKey()


        return isModified
    }

    override fun apply() {
        val state = CommentsTranslatorSettingsState.instance

        //type
        state.currentTranslator = settingsForm!!.translator()

        //api key
        when (state.currentTranslator) {
            TranslatorType.YANDEX -> state.yandexApiKey = settingsForm!!.apiKey()
        }

    }

    override fun createComponent(): JComponent? {
        initializeSettings()
        return settingsForm!!.content
    }
}